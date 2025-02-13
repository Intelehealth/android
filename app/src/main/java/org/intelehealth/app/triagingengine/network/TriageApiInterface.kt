package org.intelehealth.app.triagingengine.network

import org.intelehealth.common.triagingrule.model.rules.TriagingReferralRule
import retrofit2.Response
import retrofit2.http.GET

/**
 * Created by Lincon Pradhan on  03-02-2025.
 **/
interface TriageApiInterface {
    @GET("/triage")
    suspend fun loadTriagingRuleData(): Response<TriagingReferralRule>
}