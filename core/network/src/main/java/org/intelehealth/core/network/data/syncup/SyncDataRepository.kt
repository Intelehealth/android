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

    suspend fun saveData(pullResponse: PullResponse, onSaved: suspend (Int, Int) -> Unit) {
        if (pullResponse.patientlist.isNotEmpty()) ihDb.patientDao().insert(pullResponse.patientlist)
        if (pullResponse.patientAttributeTypeListMaster.isNotEmpty()) ihDb.patientAttrMasterDao()
            .insert(pullResponse.patientAttributeTypeListMaster)
        if (pullResponse.patientAttributesList.isNotEmpty()) ihDb.patientAttrDao()
            .insert(pullResponse.patientAttributesList)
        if (pullResponse.visitlist.isNotEmpty()) ihDb.visitDao().insert(pullResponse.visitlist)
        if (pullResponse.encounterlist.isNotEmpty()) ihDb.encounterDao().insert(pullResponse.encounterlist)
        if (pullResponse.obslist.isNotEmpty()) ihDb.observationDao().insert(pullResponse.obslist)
        if (pullResponse.locationlist.isNotEmpty()) ihDb.patientLocationDao().insert(pullResponse.locationlist)
        if (pullResponse.providerlist.isNotEmpty()) ihDb.providerDao().insert(pullResponse.providerlist)
        if (pullResponse.providerAttributeTypeList.isNotEmpty()) ihDb.providerAttributeDao()
            .insert(pullResponse.providerAttributeTypeList)
        if (pullResponse.providerAttributeList.isNotEmpty()) ihDb.providerAttributeDao()
            .insert(pullResponse.providerAttributeList)
        if (pullResponse.visitAttributeTypeList.isNotEmpty()) ihDb.visitAttributeDao()
            .insert(pullResponse.visitAttributeTypeList)
        if (pullResponse.visitAttributeList.isNotEmpty()) ihDb.visitAttributeDao()
            .insert(pullResponse.visitAttributeList)
        onSaved(pullResponse.totalCount, pullResponse.pageNo)
    }
}