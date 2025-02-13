package org.intelehealth.app.triagingengine.model

/**
 * Created by Lincon Pradhan on  03-02-2025.
 **/
data class TriageCalculatedResultModel(
    val popupResult: String,
    val resultFacilityType: String,
    val resultFacilityCategory: String,
    val resultHealthProviderDesignations: List<String>,
    val riskName: String
)
