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

class GetRoasterDataUseCase @Inject constructor(private val repository: RosterRepository) {
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
        allAttributeData: ArrayList<PatientAttributesDTO>,
        outcomeQuestionList: ArrayList<RoasterViewQuestion>,
    ): ArrayList<PregnancyOutComeModel> {
        val matchingPregnancyOutcomeReported = allAttributeData.find {
            it.personAttributeTypeUuid == PREGNANCY_OUTCOME_REPORTED
        }
        val pregnancyOutComeModelList = ArrayList<PregnancyOutComeModel>()
        val pregnancyList =
            parseJsonToPregnancyRosterData(matchingPregnancyOutcomeReported?.value ?: "")
        pregnancyList.forEach {
            val pregnancyOutComeQuestion = ArrayList<RoasterViewQuestion>(outcomeQuestionList)
            val pregnancyOutComeModel = PregnancyOutComeModel(
                title = it.pregnancyOutcome,
                roasterViewQuestion = pregnancyOutComeQuestion
            )

            pregnancyOutComeQuestion[0].answer = it.pregnancyOutcome
            pregnancyOutComeQuestion[1].answer = it.yearOfPregnancyOutcome
            pregnancyOutComeQuestion[2].answer = it.monthsOfPregnancy
            pregnancyOutComeQuestion[3].answer = it.placeOfDelivery
            pregnancyOutComeQuestion[4].answer = it.typeOfDelivery
            pregnancyOutComeQuestion[5].answer = it.pregnancyPlanned
            pregnancyOutComeQuestion[6].answer = it.highRiskPregnancy

            pregnancyOutComeModelList.add(pregnancyOutComeModel)

        }
        return pregnancyOutComeModelList
    }

    private fun parseJsonToPregnancyRosterData(jsonString: String): List<PregnancyRosterData> {
        val gson = Gson()
        val listType = object : TypeToken<List<PregnancyRosterData>>() {}.type
        return gson.fromJson(jsonString, listType)
    }

    fun getHealthServiceData(
        allAttributeData: ArrayList<PatientAttributesDTO>,
        healthServiceList: ArrayList<RoasterViewQuestion>,
    ): ArrayList<HealthServiceModel> {
        val healthServiceModelList = ArrayList<HealthServiceModel>()
        val matchingHealthIssues = allAttributeData.find {
            it.personAttributeTypeUuid == HEALTH_ISSUE_REPORTED
        }
        val healthIssuesList = parseJsonToHealthRosterData(matchingHealthIssues?.value ?: "")
        healthIssuesList.forEach {
            val healthServiceQuestionList = ArrayList<RoasterViewQuestion>(healthServiceList)
            val healthServiceModel = HealthServiceModel(
                title = it.healthIssueReported,
                roasterViewQuestion = healthServiceQuestionList
            )

            healthServiceQuestionList[0].answer = it.healthIssueReported
            healthServiceQuestionList[1].answer = it.numberOfEpisodesInTheLastYear
            healthServiceQuestionList[2].answer = it.primaryHealthcareProviderValue
            healthServiceQuestionList[3].answer = it.firstLocationOfVisit
            healthServiceQuestionList[4].answer = it.referredTo
            healthServiceQuestionList[5].answer = it.modeOfTransportation
            healthServiceQuestionList[6].answer = it.averageCostOfTravelAndStayPerEpisode
            healthServiceQuestionList[7].answer = it.averageCostOfConsultation
            healthServiceQuestionList[8].answer = it.averageCostOfMedicine
            healthServiceQuestionList[9].answer = it.scoreForExperienceOfTreatment

            healthServiceModelList.add(healthServiceModel)
        }
        return healthServiceModelList
    }

    private fun parseJsonToHealthRosterData(jsonString: String): List<HealthIssues> {
        val gson = Gson()
        val listType = object : TypeToken<List<HealthIssues>>() {}.type
        return gson.fromJson(jsonString, listType)
    }
}