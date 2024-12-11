package org.intelehealth.coreroomdb.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import org.intelehealth.coreroomdb.entity.ProviderAttribute

@Dao
interface ProviderAttributeDao : CoreDao<ProviderAttribute> {
    @Query("SELECT * FROM tbl_provider_attribute WHERE uuid = :providerUuid")
    fun getAttributesByProviderUuid(providerUuid: String): LiveData<List<ProviderAttribute>>

    @Query("SELECT * FROM tbl_provider_attribute WHERE provider_uuid = :providerUuid AND provider_attribute_type_uuid = :personAttributeTypeUuid")
    fun getAttributesByProviderUuidAndPersonAttributeTypeUuid(
        providerUuid: String, personAttributeTypeUuid: String
    ): LiveData<List<ProviderAttribute>>

    @Query("SELECT DISTINCT provider_uuid FROM tbl_provider_attribute WHERE value = :value")
    fun getProvidersUuidsByValue(value: String): LiveData<List<String>>
}