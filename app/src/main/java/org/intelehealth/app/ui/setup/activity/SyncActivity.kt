package org.intelehealth.app.ui.setup.activity

import androidx.work.WorkInfo
import org.intelehealth.app.syncModule.SyncWorkManager
import org.intelehealth.core.network.worker.SyncDataWorker
import org.intelehealth.core.ui.activity.CircularProgressActivity

/**
 * Created by Vaghela Mithun R. on 11-12-2024 - 11:50.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class SyncActivity : CircularProgressActivity() {
    override fun onViewCreated() {
        progressTitle("Initialisation")
        progressTask("Sync...")
        SyncDataWorker.startSyncWorker(this) {
            if (it.state == WorkInfo.State.SUCCEEDED) {
                onProgress(100)
                runOnUiThread { progressTask("Sync Completed") }
            } else {
                val progress = it.progress.getInt(SyncDataWorker.WORK_PROGRESS, 0)
                onProgress(progress)
            }
        }
    }

    override fun onRetry() {
        TODO("Not yet implemented")
    }

    override fun onClose() {
        TODO("Not yet implemented")
    }
}