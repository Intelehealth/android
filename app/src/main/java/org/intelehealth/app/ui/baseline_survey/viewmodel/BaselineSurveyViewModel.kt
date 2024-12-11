package org.intelehealth.app.ui.baseline_survey.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.github.ajalt.timberkt.Timber
import com.google.gson.Gson
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.intelehealth.app.models.dto.PatientDTO
import org.intelehealth.app.ui.patient.data.PatientRepository
import org.intelehealth.app.utilities.BaselineSurveyStage
import org.intelehealth.app.utilities.PatientRegStage
import org.intelehealth.config.presenter.fields.data.RegFieldRepository
import org.intelehealth.config.presenter.fields.viewmodel.RegFieldViewModel
import org.intelehealth.core.shared.ui.viewmodel.BaseViewModel

/**
 * Created by Shazzad H Kanon on 10-12-2024 - 11:00.
 * Email : shazzad@intelehealth.org
 * Mob   : +8801647040520
 **/
class BaselineSurveyViewModel(
    private val repository: PatientRepository
) : RegFieldViewModel(repository) {

    private var mutableLivePatient = MutableLiveData<PatientDTO>()
    val patientData: LiveData<PatientDTO> get() = mutableLivePatient

    private var mutableBaselineSurveyStage = MutableLiveData(BaselineSurveyStage.GENERAL)
    val mutableBaselineSurveyStageData: LiveData<BaselineSurveyStage> get() = mutableBaselineSurveyStage

    var baselineEditMode: Boolean = false


    fun loadPatientDetails(
        patientId: String
    ) = executeLocalQuery {
        repository.fetchPatient(patientId)
    }.asLiveData()

    fun updatedPatient(patient: PatientDTO) {
        Timber.d { "Saved patient => ${Gson().toJson(patient)}" }
        mutableLivePatient.postValue(patient)
    }

    fun updateBaselineStage(stage: BaselineSurveyStage) {
        mutableBaselineSurveyStage.postValue(stage)
    }

}