package org.intelehealth.app.ui.rosterquestionnaire.usecase

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.intelehealth.app.models.dto.PatientAttributesDTO
import org.intelehealth.app.ui.rosterquestionnaire.model.HealthIssues
import org.intelehealth.app.ui.rosterquestionnaire.model.HealthServiceModel
import org.intelehealth.app.ui.rosterquestionnaire.model.PregnancyOutComeModel
import org.intelehealth.app.ui.rosterquestionnaire.model.PregnancyRosterData
import org.intelehealth.app.ui.rosterquestionnaire.model.RoasterViewQuestion
import org.intelehealth.app.ui.rosterquestionnaire.repository.RosterRepository
import org.intelehealth.app.ui.rosterquestionnaire.utilities.HEALTH_ISSUE_REPORTED
import org.intelehealth.app.ui.rosterquestionnaire.utilities.PREGNANCY_OUTCOME_REPORTED
import javax.inject.Inject

class GetAllRoasterDataUseCase @Inject constructor(private val repository: RosterRepository) {
    fun fetchAllData(patientId: String): ArrayList<PatientAttributesDTO> {
        return repository.getAllRoasterData(patientId)
    }

    fun getGeneralData(
        attributeList: ArrayList<PatientAttributesDTO>,
        generalQuestion: ArrayList<RoasterViewQuestion>,
    ): ArrayList<RoasterViewQuestion> {
        // Iterate through each item in generalQuestion
        generalQuestion.forEach { question ->
            // Find a matching attribute in the attributeList based on the attribute and personAttributeTypeUuid
            val matchingAttribute = attributeList.find {
                it.personAttributeTypeUuid == question.attribute
            }

            // If a match is found, update the answer in the generalQuestion
            if (matchingAttribute != null) {
                question.answer = matchingAttribute.value
            }
        }
        return generalQuestion
    }

    fun getPregnancyData(
        allAttributeData: List<PatientAttributesDTO>,
        outcomeQuestionList: List<RoasterViewQuestion>
    ): List<PregnancyOutComeModel> {
        // Find matching pregnancy outcome data
        val pregnancyDataJson = allAttributeData.find { it.personAttributeTypeUuid == PREGNANCY_OUTCOME_REPORTED }?.value ?: ""
        val pregnancyList = parseJsonToPregnancyRosterData(pregnancyDataJson)

        // Build the PregnancyOutComeModel list
        return pregnancyList.map { pregnancy ->
            val pregnancyOutComeQuestion = outcomeQuestionList.map { it.copy() }.apply {
                if (size >= 7) {
                    this[0].answer = pregnancy.pregnancyOutcome
                    this[1].answer = pregnancy.yearOfPregnancyOutcome
                    this[2].answer = pregnancy.monthsOfPregnancy
                    this[3].answer = pregnancy.placeOfDelivery
                    this[4].answer = pregnancy.typeOfDelivery
                    this[5].answer = pregnancy.pregnancyPlanned
                    this[6].answer = pregnancy.highRiskPregnancy
                }
            }

            PregnancyOutComeModel(
                title = pregnancy.pregnancyOutcome,
                roasterViewQuestion = pregnancyOutComeQuestion
            )
        }
    }

    private fun parseJsonToPregnancyRosterData(jsonString: String): List<PregnancyRosterData> {
        val gson = Gson()
        val listType = object : TypeToken<List<PregnancyRosterData>>() {}.type
        return gson.fromJson(jsonString, listType)
    }

    fun getHealthServiceData(
        allAttributeData: List<PatientAttributesDTO>,
        healthServiceList: List<RoasterViewQuestion>
    ): List<HealthServiceModel> {
        // Find the matching health issues data
        val healthIssuesJson = allAttributeData.find { it.personAttributeTypeUuid == HEALTH_ISSUE_REPORTED }?.value ?: ""
        val healthIssuesList = parseJsonToHealthRosterData(healthIssuesJson)

        // Build the HealthServiceModel list
        return healthIssuesList.map { healthIssue ->
            val healthServiceQuestionList = healthServiceList.map { it.copy() }.apply {
                if (size >= 10) {
                    this[0].answer = healthIssue.healthIssueReported
                    this[1].answer = healthIssue.numberOfEpisodesInTheLastYear
                    this[2].answer = healthIssue.primaryHealthcareProviderValue
                    this[3].answer = healthIssue.firstLocationOfVisit
                    this[4].answer = healthIssue.referredTo
                    this[5].answer = healthIssue.modeOfTransportation
                    this[6].answer = healthIssue.averageCostOfTravelAndStayPerEpisode
                    this[7].answer = healthIssue.averageCostOfConsultation
                    this[8].answer = healthIssue.averageCostOfMedicine
                    this[9].answer = healthIssue.scoreForExperienceOfTreatment
                }
            }

            HealthServiceModel(
                title = healthIssue.healthIssueReported,
                roasterViewQuestion = healthServiceQuestionList
            )
        }
    }

    private fun parseJsonToHealthRosterData(jsonString: String): List<HealthIssues> {
        val gson = Gson()
        val listType = object : TypeToken<List<HealthIssues>>() {}.type
        return gson.fromJson(jsonString, listType)
    }
}