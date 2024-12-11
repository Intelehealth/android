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
        if (pullResponse.patientAttributeTypeListMaster.isNotEmpty() && pullResponse.pageNo == 1) {
            pullResponse.patientAttributeTypeListMaster.map { it.synced = true }.apply {
                ihDb.patientAttrMasterDao().insert(pullResponse.patientAttributeTypeListMaster)
            }
        }
        if (pullResponse.patientAttributesList.isNotEmpty()) {
            pullResponse.patientAttributesList.map { it.synced = true }.apply {
                ihDb.patientAttrDao().insert(pullResponse.patientAttributesList)
            }
        }
        if (pullResponse.visitlist.isNotEmpty()) ihDb.visitDao().insert(pullResponse.visitlist)
        if (pullResponse.encounterlist.isNotEmpty()) ihDb.encounterDao().insert(pullResponse.encounterlist)
        if (pullResponse.obslist.isNotEmpty()) {
            pullResponse.obslist.map { it.synced = true }.apply {
                ihDb.observationDao().insert(pullResponse.obslist)
            }
        }
        if (pullResponse.locationlist.isNotEmpty()) {
            pullResponse.locationlist.map { it.synced = true }.apply {
                ihDb.patientLocationDao().insert(pullResponse.locationlist)
            }
        }
        if (pullResponse.providerlist.isNotEmpty()) {
            pullResponse.providerlist.map { it.synced = true }.apply {
                ihDb.providerDao().insert(pullResponse.providerlist)
            }
        }
//        if (pullResponse.providerAttributeTypeList.isNotEmpty()) ihDb.providerAttributeDao()
//            .insert(pullResponse.providerAttributeTypeList)
        if (pullResponse.providerAttributeList.isNotEmpty()){
            pullResponse.providerAttributeList.map { it.synced = true }.apply {
                ihDb.providerAttributeDao().insert(pullResponse.providerAttributeList)
            }
        }
//        if (pullResponse.visitAttributeTypeList.isNotEmpty()) ihDb.visitAttributeDao()
//            .insert(pullResponse.visitAttributeTypeList)
        if (pullResponse.visitAttributeList.isNotEmpty()) {
            pullResponse.visitAttributeList.map { it.synced = true }.apply {
                ihDb.visitAttributeDao().insert(pullResponse.visitAttributeList)
            }
        }
        onSaved(pullResponse.totalCount, pullResponse.pageNo)
    }
}