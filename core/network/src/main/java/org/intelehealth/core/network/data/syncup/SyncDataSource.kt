package org.intelehealth.core.network.data.syncup

import org.intelehealth.core.network.CoreApiClient
import org.intelehealth.core.network.data.BaseDataSource
import org.intelehealth.core.utils.helper.PreferenceHelper
import org.intelehealth.core.utils.helper.PreferenceHelper.Companion.AUTH_BASIC
import org.intelehealth.core.utils.helper.PreferenceHelper.Companion.KEY_PREF_LOCATION_UUID
import org.intelehealth.core.utils.helper.PreferenceHelper.Companion.PULL_EXECUTED_TIME

/**
 * Created by Vaghela Mithun R. on 25-11-2024 - 14:05.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class SyncDataSource(
    private val apiClient: CoreApiClient, private val preferenceHelper: PreferenceHelper
) : BaseDataSource() {
    suspend fun pullData(pageNo: Int, pageLimit: Int) = getResult {
        preferenceHelper.get<String>(AUTH_BASIC)
        apiClient.pullData(
            preferenceHelper.get(AUTH_BASIC),
            preferenceHelper.get(KEY_PREF_LOCATION_UUID),
            preferenceHelper.get(PULL_EXECUTED_TIME),
            pageNo,
            pageLimit
        )
    }
}