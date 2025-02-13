package org.intelehealth.app.triagingengine


import org.intelehealth.app.triagingengine.network.Resource
import org.intelehealth.app.triagingengine.network.TriageApiInterface
import org.intelehealth.app.utilities.SessionManager
import org.intelehealth.common.triagingrule.model.rules.TriagingReferralRule
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Lincon Pradhan on  03-02-2025.
 **/
@Singleton
class TriagingRepository @Inject constructor(
    private val apiService: TriageApiInterface,
    private val sessionManager: SessionManager
) {
    suspend fun loadTriagingRuleData(): Resource<TriagingReferralRule> {
        return try {
            val response = apiService.loadTriagingRuleData()
            if (response.isSuccessful) {
                response.body()?.let {
                    Resource.Success(it)
                } ?: Resource.Error("No data found")
            } else {
                Resource.Error("API Error: ${response.code()} - ${response.message()}")
            }
        } catch (e: IOException) {
            Resource.Error("Network Error: ${e.message}")
        } catch (e: Exception) {
            Resource.Error("Unexpected Error: ${e.message}")
        }
    }

}