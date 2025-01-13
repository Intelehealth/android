package org.intelehealth.app.ui.rosterquestionnaire.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import org.intelehealth.app.models.dto.PatientAttributesDTO
import org.intelehealth.app.ui.rosterquestionnaire.di.IoDispatcher
import org.intelehealth.app.ui.rosterquestionnaire.model.HealthServiceModel
import org.intelehealth.app.ui.rosterquestionnaire.model.PregnancyOutComeModel
import org.intelehealth.app.ui.rosterquestionnaire.model.PregnancyRosterData
import org.intelehealth.app.ui.rosterquestionnaire.model.RoasterViewQuestion
import org.intelehealth.app.ui.rosterquestionnaire.usecase.GetGeneralQuestionUseCase
import org.intelehealth.app.ui.rosterquestionnaire.usecase.GetHealthServiceQuestionUseCase
import org.intelehealth.app.ui.rosterquestionnaire.usecase.GetOutComeQuestionUseCase
import org.intelehealth.app.ui.rosterquestionnaire.usecase.GetRoasterDataUseCase
import org.intelehealth.app.ui.rosterquestionnaire.usecase.InsertRoasterUseCase
import org.intelehealth.app.ui.rosterquestionnaire.utilities.NO_OF_PREGNANCY_OUTCOME_TWO_YEARS
import org.intelehealth.app.ui.rosterquestionnaire.utilities.NO_OF_TIME_PREGNANT
import org.intelehealth.app.ui.rosterquestionnaire.utilities.PREGNANCY_OUTCOME_REPORTED
import org.intelehealth.app.ui.rosterquestionnaire.utilities.PREGNANCY_PAST_TWO_YEARS
import org.intelehealth.app.ui.rosterquestionnaire.utilities.RosterQuestionnaireStage
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class RosterViewModel @Inject constructor(
    private val getHealthServiceQuestionUseCase: GetHealthServiceQuestionUseCase,
    private val getOutComeQuestionUseCase: GetOutComeQuestionUseCase,
    private val getGeneralQuestionUseCase: GetGeneralQuestionUseCase,
    private val insertRoasterUseCase: InsertRoasterUseCase,
    private val getRoasterDataUseCase: GetRoasterDataUseCase,
    @IoDispatcher
    private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {

    var pregnancyOutcomeCount: String = ""
    var pregnancyOutcome: String = ""
    var pregnancyCount: String = ""
    var patientUuid: String = ""
    private var mutableLiveRosterStage = MutableLiveData(RosterQuestionnaireStage.GENERAL_ROSTER)
    val rosterStageData: LiveData<RosterQuestionnaireStage> get() = mutableLiveRosterStage

    var isEditMode: Boolean = false

    private val _generalLiveList = MutableLiveData<ArrayList<RoasterViewQuestion>>(arrayListOf())
    val generalLiveList: LiveData<ArrayList<RoasterViewQuestion>> = _generalLiveList

    private val _outComeLiveList = MutableLiveData<ArrayList<PregnancyOutComeModel>>(arrayListOf())
    val outComeLiveList: LiveData<ArrayList<PregnancyOutComeModel>> = _outComeLiveList

    private val _healthServiceLiveList =
        MutableLiveData<ArrayList<HealthServiceModel>>(arrayListOf())
    val healthServiceLiveList: LiveData<ArrayList<HealthServiceModel>> = _healthServiceLiveList

    var existingRoasterQuestionList: ArrayList<RoasterViewQuestion>? = null
    var existPregnancyOutComePosition = 0

    private val _isDataInserted = MutableLiveData(false)
    val isDataInserted: LiveData<Boolean> = _isDataInserted

    fun updateRosterStage(stage: RosterQuestionnaireStage) {
        mutableLiveRosterStage.postValue(stage)
    }

    fun addPregnancyOutcome(questionList: List<RoasterViewQuestion>) {
        val pregnancyOutComeModel = PregnancyOutComeModel(
            title = questionList[0].answer ?: "",
            roasterViewQuestion = questionList
        )
        val list = _outComeLiveList.value ?: mutableListOf()
        if (existingRoasterQuestionList == null) {
            list.add(pregnancyOutComeModel)
        } else {
            list[existPregnancyOutComePosition] = pregnancyOutComeModel
            existingRoasterQuestionList = null
        }
        _outComeLiveList.postValue(list as ArrayList<PregnancyOutComeModel>?)
    }

    fun addHealthService(questionList: List<RoasterViewQuestion>) {
        val healthServiceModel = HealthServiceModel(
            title = questionList[0].answer ?: "",
            roasterViewQuestion = questionList
        )
        val list = _healthServiceLiveList.value ?: mutableListOf()
        if (existingRoasterQuestionList == null) {
            list.add(healthServiceModel)
        } else {
            list[existPregnancyOutComePosition] = healthServiceModel
            existingRoasterQuestionList = null
        }
        _healthServiceLiveList.postValue(list as ArrayList<HealthServiceModel>?)
    }


    fun deletePregnancyOutcome(position: Int) {
        val list = _outComeLiveList.value ?: mutableListOf()
        list.removeAt(position)
        _outComeLiveList.postValue(list as ArrayList<PregnancyOutComeModel>?)

    }

    fun deleteHealthService(position: Int) {
        val list = _healthServiceLiveList.value ?: mutableListOf()
        list.removeAt(position)
        _healthServiceLiveList.postValue(list as ArrayList<HealthServiceModel>?)

    }

    fun getOutcomeQuestionList(): ArrayList<RoasterViewQuestion> =
        getOutComeQuestionUseCase(existingRoasterQuestionList)

    fun getHealthServiceList(): ArrayList<RoasterViewQuestion> =
        getHealthServiceQuestionUseCase(existingRoasterQuestionList)

    fun getGeneralQuestionList() {
        _generalLiveList.postValue(getGeneralQuestionUseCase())
    }

    fun insertRoster() {
        viewModelScope.launch(ioDispatcher) {
            insertRoasterUseCase(
                patientUuid,
                generalLiveList.value,
                outComeLiveList.value,
                _healthServiceLiveList.value,
                pregnancyOutcome,
                pregnancyCount,
                pregnancyOutcomeCount,
            )
            _isDataInserted.postValue(true)
        }
    }

    fun getRoasterData() {
        viewModelScope.launch(ioDispatcher) {
            val allAttributeData = getRoasterDataUseCase.fetchAllData(patientUuid)

            // Fetch and post general data
            _generalLiveList.postValue(
                getRoasterDataUseCase.getGeneralData(allAttributeData, getGeneralQuestionUseCase())
            )

            // Extract pregnancy-related data
            val pregnancyDataMap = mapOf(
                NO_OF_TIME_PREGNANT to { value: String -> pregnancyCount = value },
                PREGNANCY_PAST_TWO_YEARS to { value: String -> pregnancyOutcome = value },
                NO_OF_PREGNANCY_OUTCOME_TWO_YEARS to { value: String -> pregnancyOutcomeCount = value }
            )

            pregnancyDataMap.forEach { (key, setter) ->
                allAttributeData.find { it.personAttributeTypeUuid == key }?.value?.let(setter)
            }

            // Fetch and post pregnancy outcome models
            _outComeLiveList.postValue(
                getRoasterDataUseCase.getPregnancyData(allAttributeData, getOutcomeQuestionList())
                        as ArrayList<PregnancyOutComeModel>?
            )

            // Fetch and post health service models
            _healthServiceLiveList.postValue(
                getRoasterDataUseCase.getHealthServiceData(allAttributeData, getHealthServiceList())
                        as ArrayList<HealthServiceModel>?
            )
        }
    }



}