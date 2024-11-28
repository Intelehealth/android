package org.intelehealth.app.activities.householdSurvey.repository

object HouseholdSurveyFragmentMap {
    private val fragmentFieldsMap = mapOf(
        "firstScreen" to listOf(
            "houseStructure",
            "resultOfVisit",
            "namePrimaryRespondent",
            "ReportDate of survey started",
            "HouseholdNumber"
        ),
        "secondScreen" to listOf(
            "numberOfSmartPhones",
            "numberOfFeaturePhones",
            "numberOfEarningMembers",
            "primarySourceOfIncome"
        )
    )

    fun getFieldsForFragment(fragmentIdentifier: String): List<String> {
        return fragmentFieldsMap[fragmentIdentifier] ?: emptyList()
    }
}