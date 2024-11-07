package org.intelehealth.core.network.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

/**
 * Created by Vaghela Mithun R. on 25-10-2024 - 16:27.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class DataSyncUpWorker(
    private val ctx: Context, private val params: WorkerParameters
) : CoroutineWorker(ctx, params) {
    override suspend fun doWork(): Result {
        TODO("Not yet implemented")
    }
}