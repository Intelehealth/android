package org.intelehealth.core.network.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.intelehealth.core.network.data.syncup.SyncDataRepository
import org.intelehealth.core.network.data.syncup.SyncDataSource
import org.intelehealth.core.network.provider.CoreApiClientProvider
import org.intelehealth.core.network.state.Result.State
import org.intelehealth.core.utils.helper.PreferenceHelper
import org.intelehealth.coreroomdb.IHDatabase

/**
 * Created by Vaghela Mithun R. on 25-10-2024 - 16:27.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class SyncDataWorker(
    private val ctx: Context, private val params: WorkerParameters
) : CoroutineWorker(ctx, params) {
    private var workerResult = Result.failure()
    private var progress = 0
    private val repository: SyncDataRepository by lazy {
        val db = IHDatabase.getInstance(ctx)
        val preferenceHelper = PreferenceHelper(ctx)
        val dataSource = SyncDataSource(CoreApiClientProvider.getCoreApiClient(), preferenceHelper)
        SyncDataRepository(db, dataSource)
    }

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        setProgress(workDataOf(WORK_PROGRESS to progress))
        pullData(0)
        workerResult
    }

    private suspend fun pullData(pageNo: Int) {
        repository.pullData(pageNo).collect {
            when (it.status) {
                State.SUCCESS -> it.data?.data?.let { data ->
                    repository.saveData(data) { totalCount, page ->
                        val percentage = (data.patientlist.size * 100) / totalCount
                        progress += percentage
                        setProgress(workDataOf(WORK_PROGRESS to progress))
                        if (page > 0) withContext(Dispatchers.IO) { pullData(page) }
                        else workerResult = Result.success()
                    }
                }

                State.FAIL -> workerResult = Result.failure()
                State.ERROR -> workerResult = Result.failure()
                State.LOADING -> workerResult = Result.failure()
            }
        }
    }

    companion object {
        const val WORK_PROGRESS = "work_progress"
        fun startSyncWorker(context: Context, onResult: (WorkInfo) -> Unit) {
            val configWorkRequest = OneTimeWorkRequestBuilder<SyncDataWorker>().build()
            val workManager = WorkManager.getInstance(context.applicationContext)
            workManager.enqueue(configWorkRequest)
            val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
            scope.launch {
                workManager.getWorkInfoByIdFlow(configWorkRequest.id).collect {
                    onResult(it)
                }
            }
        }
    }
}