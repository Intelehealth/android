package org.intelehealth.coreroomdb.dao

import androidx.lifecycle.LiveData
import org.intelehealth.coreroomdb.entity.PatientAttributeTypeMaster

interface PatientAttributeTypeMasterDao : CoreDao<PatientAttributeTypeMaster> {

    fun getAttributeNameByUuid(uuid: String): LiveData<List<PatientAttributeTypeMaster>>

    fun getAttributeUuidByName(name: String): LiveData<List<PatientAttributeTypeMaster>>

}