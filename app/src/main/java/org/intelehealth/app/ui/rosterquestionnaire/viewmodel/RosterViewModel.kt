package org.intelehealth.app.ui.rosterquestionnaire.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.intelehealth.app.ui.rosterquestionnaire.data.RosterRepository
import org.intelehealth.app.ui.rosterquestionnaire.model.RosterModel
import org.intelehealth.app.ui.rosterquestionnaire.utilities.RosterQuestionnaireStage
import org.intelehealth.app.utilities.PatientRegStage
import org.intelehealth.core.shared.ui.viewmodel.BaseViewModel

class RosterViewModel (
    private val repository: RosterRepository
) : BaseViewModel() {

    private var mutableLivePatient = MutableLiveData<RosterModel>()
    val rosterAttributesData: LiveData<RosterModel> get() = mutableLivePatient
    private var mutableLiveRosterStage = MutableLiveData(RosterQuestionnaireStage.GENERAL_ROSTER)
    val rosterStageData: LiveData<RosterQuestionnaireStage> get() = mutableLiveRosterStage
    var isEditMode: Boolean = false

    fun updateRosterStage(stage: RosterQuestionnaireStage) {
        mutableLiveRosterStage.postValue(stage)
    }
}