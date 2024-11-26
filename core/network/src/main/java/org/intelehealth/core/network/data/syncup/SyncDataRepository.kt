package org.intelehealth.core.network.data.syncup

import org.intelehealth.core.network.model.PullResponse
import org.intelehealth.coreroomdb.IHDatabase

/**
 * Created by Vaghela Mithun R. on 25-11-2024 - 14:05.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class SyncDataRepository(private val ihDb: IHDatabase, private val dataSource: SyncDataSource) {
    suspend fun pullData(pageNo: Int, pageLimit: Int = 50) = dataSource.pullData(pageNo, pageLimit)

    suspend fun saveData(pullResponse: PullResponse) {

    }
}