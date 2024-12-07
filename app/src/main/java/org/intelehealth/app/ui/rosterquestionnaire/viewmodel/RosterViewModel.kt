package org.intelehealth.app.ui.rosterquestionnaire.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.intelehealth.app.ui.rosterquestionnaire.repository.RosterRepositoryImp
import org.intelehealth.app.ui.rosterquestionnaire.model.RosterModel
import org.intelehealth.app.ui.rosterquestionnaire.utilities.RosterQuestionnaireStage
import javax.inject.Inject

@HiltViewModel
class RosterViewModel @Inject constructor(
    private val rosterRepository: RosterRepositoryImp
) : ViewModel() {

    private var mutableLivePatient = MutableLiveData<RosterModel>()
    val rosterAttributesData: LiveData<RosterModel> get() = mutableLivePatient
    private var mutableLiveRosterStage = MutableLiveData(RosterQuestionnaireStage.GENERAL_ROSTER)
    val rosterStageData: LiveData<RosterQuestionnaireStage> get() = mutableLiveRosterStage
    var isEditMode: Boolean = false

    fun updateRosterStage(stage: RosterQuestionnaireStage) {
        mutableLiveRosterStage.postValue(stage)
    }
}