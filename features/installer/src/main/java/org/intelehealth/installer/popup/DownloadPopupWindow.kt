package org.intelehealth.installer.popup

import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.PopupMenu.OnDismissListener
import androidx.core.view.isVisible
import org.intelehealth.installer.R
import org.intelehealth.installer.databinding.PopupWindowBinding
import org.intelehealth.installer.downloader.DynamicDeliveryCallback
import org.intelehealth.installer.downloader.DynamicModuleDownloadManager

/**
 * Created by Vaghela Mithun R. on 18-11-2024 - 11:17.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
abstract class DownloadPopupWindow(
    private val context: Context, private val anchor: View, private val listener: OnDownloadCompleteListener
) : PopupWindow(context), DynamicDeliveryCallback, OnDismissListener {
    val downloadManager = DynamicModuleDownloadManager.getInstance(context)
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private val binding: PopupWindowBinding = PopupWindowBinding.inflate(inflater)

    interface OnDownloadCompleteListener {
        fun onDownloadCompleted()
    }

    init {
        contentView = binding.root
//        elevation = 20F
        height = LinearLayout.LayoutParams.WRAP_CONTENT
        width = LinearLayout.LayoutParams.WRAP_CONTENT
        isOutsideTouchable = true
    }

    fun startDownloading(module: String) {
//        if (downloadManager.isModuleDownloaded(module)) {
//            listener.onDownloadCompleted()
//        } else {
//            //
//        }
        binding.btnClosePopup.setOnClickListener { dismiss() }
        showAtLocation(anchor, Gravity.CENTER, anchor.pivotX.toInt(), anchor.pivotY.toInt())
        Log.e("DownloadPopupWindow", "startDownloading: ")
    }

    override fun onDismiss(menu: PopupMenu?) {
        downloadManager.unregisterListener()
    }

    override fun onDownloading(percentage: Int) {
        binding.progressDownloading.progress = percentage
        binding.txtDownloadStatus.text = context.getString(R.string.module_downloading, percentage.toString())
    }

    override fun onDownloadCompleted() {
        binding.txtDownloadStatus.text = context.getString(R.string.module_downloaded)
    }

    override fun onInstalling() {
        binding.txtDownloadStatus.text = context.getString(R.string.module_installing)
        binding.progressDownloading.isVisible = false
    }

    override fun onInstallSuccess() {
        binding.txtDownloadStatus.text = context.getString(R.string.module_installed)
        dismiss()
        listener.onDownloadCompleted()
    }

    override fun onFailed(errorMessage: String) {
        binding.downloadErrorGroup.isVisible = false
        binding.downloadProgressGroup.isVisible = false
        binding.txtErrorMsg.text = errorMessage
    }
}