package org.intelehealth.app.activities.householdSurvey.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import org.intelehealth.app.activities.householdSurvey.repository.HouseholdRepository
import org.intelehealth.app.activities.householdSurvey.viewmodels.HouseHoldViewModel
import org.intelehealth.app.app.IntelehealthApplication
import org.intelehealth.app.database.dao.PatientsDAO
import org.intelehealth.app.ui.patient.data.PatientRepository
import org.intelehealth.app.ui.patient.viewmodel.PatientViewModel
import org.intelehealth.config.presenter.fields.factory.PatientViewModelFactory
import org.intelehealth.config.room.ConfigDatabase

class HouseHoldViewModelFactory(
    private val householdRepository: HouseholdRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HouseHoldViewModel(householdRepository) as T
    }

    companion object {
        fun create(
            context: Context,
            owner: ViewModelStoreOwner
        ): HouseHoldViewModel {
            val patientDao = PatientsDAO()
            val sqlHelper = IntelehealthApplication.inteleHealthDatabaseHelper
            val repository = HouseholdRepository(patientDao, sqlHelper)
            val factory = HouseHoldViewModelFactory(repository)
            return ViewModelProvider(owner, factory)[HouseHoldViewModel::class]
        }
    }
}