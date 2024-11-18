package org.intelehealth.app.activities.householdSurvey.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.intelehealth.app.activities.householdSurvey.repository.HouseholdRepository
import org.intelehealth.app.models.dto.PatientDTO
import org.intelehealth.app.utilities.HouseholdSurveyStage
import org.intelehealth.app.utilities.PatientRegStage
import org.intelehealth.core.shared.ui.viewmodel.BaseViewModel

class HouseHoldViewModel (
    private val repository: HouseholdRepository
) : BaseViewModel() {

    private var mutableLivePatient = MutableLiveData<PatientDTO>()
    val patientData: LiveData<PatientDTO> get() = mutableLivePatient
    private var mutableLivePatientStage = MutableLiveData(HouseholdSurveyStage.FIRST_SCREEN)
    val patientStageData: LiveData<HouseholdSurveyStage> get() = mutableLivePatientStage
    var isEditMode: Boolean = false

   /* fun loadPatientDetails(
        patientId: String
    ) = executeLocalQuery {
        repository.fetchPatient(patientId)
    }.asLiveData()*/

  /*  fun updatedPatient(patient: PatientDTO) {
        Timber.d { "Saved patient => ${Gson().toJson(patient)}" }
        mutableLivePatient.postValue(patient)
    }
*/
    fun updatePatientStage(stage: HouseholdSurveyStage) {
        mutableLivePatientStage.postValue(stage)
    }

  /*  fun savePatient() = executeLocalInsertUpdateQuery {
        return@executeLocalInsertUpdateQuery patientData.value?.let {
            return@let if (isEditMode) repository.updatePatient(it)
            else repository.createNewPatient(it)
        } ?: false
    }.asLiveData()*/

}