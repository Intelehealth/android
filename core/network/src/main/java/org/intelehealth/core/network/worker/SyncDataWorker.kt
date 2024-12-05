package org.intelehealth.core.network.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
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

    private val repository: SyncDataRepository by lazy {
        val db = IHDatabase.getInstance(ctx)
        val preferenceHelper = PreferenceHelper(ctx)
        val dataSource = SyncDataSource(CoreApiClientProvider.getCoreApiClient(), preferenceHelper)
        SyncDataRepository(db, dataSource)
    }

    override suspend fun doWork(): Result {
        withContext(Dispatchers.IO) { pullData(1) }
        return Result.success()
    }

    private suspend fun pullData(pageNo: Int) {
        repository.pullData(pageNo).collect {
            when (it.status) {
                State.SUCCESS -> it.data?.data?.let { data ->
                    repository.saveData(data) { totalCount, page ->
                        if (totalCount > page) withContext(Dispatchers.IO) { pullData(page + 1) }
                        else Result.success()
                    }
                }

                State.FAIL -> Result.failure()
                State.ERROR -> Result.failure()
                State.LOADING -> Result.failure()
            }
        }
    }
}