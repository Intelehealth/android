package org.intelehealth.app.ui.rosterquestionnaire.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
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

    private val _outComeLiveList  = MutableLiveData<ArrayList<PregnancyOutComeModel>>(arrayListOf())
    val outComeLiveList: LiveData<ArrayList<PregnancyOutComeModel>> = _outComeLiveList

    fun updateRosterStage(stage: RosterQuestionnaireStage) {
        mutableLiveRosterStage.postValue(stage)
    }

    fun addPregnancyOutcome(questionList: List<PregnancyOutComeViewQuestion>,) {
        val pregnancyOutComeModel = PregnancyOutComeModel(
            title = questionList[0].answer ?: "",
            pregnancyOutComeViewQuestion = questionList
        )
        val list = _outComeLiveList.value ?: mutableListOf()
        list.add(pregnancyOutComeModel)
        _outComeLiveList.postValue(list as ArrayList<PregnancyOutComeModel>?)

    }
}