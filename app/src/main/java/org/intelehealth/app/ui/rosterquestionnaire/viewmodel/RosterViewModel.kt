package org.intelehealth.app.ui.rosterquestionnaire.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.intelehealth.app.R
import org.intelehealth.app.app.IntelehealthApplication
import org.intelehealth.app.ui.rosterquestionnaire.model.PregnancyOutComeModel
import org.intelehealth.app.ui.rosterquestionnaire.model.PregnancyOutComeViewQuestion
import org.intelehealth.app.ui.rosterquestionnaire.repository.RosterRepositoryImp
import org.intelehealth.app.ui.rosterquestionnaire.model.RosterModel
import org.intelehealth.app.ui.rosterquestionnaire.utilities.RosterQuestionnaireStage
import javax.inject.Inject

@HiltViewModel
class RosterViewModel @Inject constructor(
    private val rosterRepository: RosterRepositoryImp,
) : ViewModel() {


    private var mutableLivePatient = MutableLiveData<RosterModel>()
    val rosterAttributesData: LiveData<RosterModel> get() = mutableLivePatient

    private var mutableLiveRosterStage = MutableLiveData(RosterQuestionnaireStage.GENERAL_ROSTER)
    val rosterStageData: LiveData<RosterQuestionnaireStage> get() = mutableLiveRosterStage

    var isEditMode: Boolean = false

    private val _outComeLiveList = MutableLiveData<ArrayList<PregnancyOutComeModel>>(arrayListOf())
    val outComeLiveList: LiveData<ArrayList<PregnancyOutComeModel>> = _outComeLiveList

    var existingPregnancyOutComeList: ArrayList<PregnancyOutComeViewQuestion>? = null
    var existPregnancyOutComePosition = 0

    fun updateRosterStage(stage: RosterQuestionnaireStage) {
        mutableLiveRosterStage.postValue(stage)
    }

    fun addPregnancyOutcome(questionList: List<PregnancyOutComeViewQuestion>) {
        val pregnancyOutComeModel = PregnancyOutComeModel(
            title = questionList[0].answer ?: "",
            pregnancyOutComeViewQuestion = questionList
        )
        val list = _outComeLiveList.value ?: mutableListOf()
        if (existingPregnancyOutComeList == null) {
            list.add(pregnancyOutComeModel)
        } else {
            list[existPregnancyOutComePosition] = pregnancyOutComeModel
            existingPregnancyOutComeList = null
        }
        _outComeLiveList.postValue(list as ArrayList<PregnancyOutComeModel>?)
    }


    fun deletePregnancyOutcome(position: Int) {
        val list = _outComeLiveList.value ?: mutableListOf()
        list.removeAt(position)
        _outComeLiveList.postValue(list as ArrayList<PregnancyOutComeModel>?)

    }

    fun getOutcomeQuestionList(): ArrayList<PregnancyOutComeViewQuestion> {
        return if (existingPregnancyOutComeList == null) {
            rosterRepository.getOutcomeQuestionList()
        } else {
            existingPregnancyOutComeList!!
        }

    }
}