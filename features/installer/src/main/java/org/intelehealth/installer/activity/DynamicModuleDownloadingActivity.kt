package org.intelehealth.installer.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.IntentCompat
import androidx.core.view.isVisible
import org.intelehealth.installer.R
import org.intelehealth.installer.databinding.ActivityDynamicModuleDownloadingBinding
import org.intelehealth.installer.downloader.DynamicDeliveryCallback
import org.intelehealth.installer.downloader.DynamicModuleDownloadManager

/**
 * Created by Vaghela Mithun R. on 09-10-2024 - 12:09.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class DynamicModuleDownloadingActivity : AppCompatActivity(), DynamicDeliveryCallback {

    private val binding: ActivityDynamicModuleDownloadingBinding by lazy {
        ActivityDynamicModuleDownloadingBinding.inflate(layoutInflater)
    }

    private val downloadManager: DynamicModuleDownloadManager by lazy {
        DynamicModuleDownloadManager.getInstance(this);
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.progressDownloading.max = 100
        extractIntent()
        setButtonClickListener()
    }

    private fun setButtonClickListener() {
        binding.btnClosePopup.setOnClickListener { finishWithResult(false) }
        binding.btnRetryDownload.setOnClickListener {
            binding.downloadErrorGroup.isVisible = false
            onDownloading(0)
            extractIntent()
        }
    }

    private fun extractIntent() {
        intent?.let {
            if (it.hasExtra(EXT_MODULES)) {
                val modules = IntentCompat.getSerializableExtra(it, EXT_MODULES, ArrayList::class.java)
//                downloadManager.downloadDynamicModules(modules)
            } else if (it.hasExtra(EXT_MODULE)) {
                val module = it.getStringExtra(EXT_MODULE)
                module?.let { it1 ->
                    binding.txtModuleNames.text = module
                    downloadManager.downloadDynamicModule(it1)
                }
            } else finishWithResult(false)
        } ?: finishWithResult(false)
    }

    private fun finishWithResult(status: Boolean) {
        val data = Intent()
        data.putExtra(MODULE_DOWNLOAD_STATUS, status)
        setResult(RESULT_OK, data)
        finish()
    }

    override fun onResume() {
        super.onResume()
        downloadManager.registerListener(this)
    }

    override fun onPause() {
        super.onPause()
        downloadManager.unregisterListener()
    }

    override fun onDownloading(percentage: Int) {
        println("DynamicModuleDownloadingActivity => DOWNLOADING percentage => $percentage")
        binding.progressDownloading.progress = percentage
        binding.txtDownloadStatus.text = getString(R.string.module_downloading, "${percentage}%")
    }

    override fun onDownloadCompleted() {
        binding.txtDownloadStatus.text = getString(R.string.module_downloaded)
    }

    override fun onInstalling() {
        binding.txtDownloadStatus.text = getString(R.string.module_installing)
        binding.progressDownloading.isVisible = true
    }

    override fun onInstallSuccess() {
        binding.txtDownloadStatus.text = getString(R.string.module_installed)
        finishWithResult(true)
    }

    override fun onFailed(errorMessage: String) {
        binding.downloadErrorGroup.isVisible = true
        binding.txtDownloadStatus.text = getString(R.string.module_failed)
        binding.txtErrorMsg.text = errorMessage
    }

    companion object {
        const val TAG = "DynamicModuleDownloadingActivity"
        const val MODULE_DOWNLOAD_STATUS = "download_status"
        const val MODULE_DOWNLOAD_RESULT = "download_result"
        const val EXT_MODULES = "ext_modules"
        const val EXT_MODULE = "ext_module"

        @JvmStatic
        fun getDownloadActivityIntent(context: Context, modules: ArrayList<String>): Intent {
            return Intent(context, DynamicModuleDownloadingActivity::class.java).apply {
                putExtra(EXT_MODULES, modules)
            }
        }

        @JvmStatic
        fun getDownloadActivityIntent(context: Context, module: String): Intent {
            return Intent(context, DynamicModuleDownloadingActivity::class.java).apply {
                putExtra(EXT_MODULE, module)
            }
        }
    }
}