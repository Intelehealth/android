package org.intelehealth.app.ui.baseline_survey.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import org.intelehealth.app.app.IntelehealthApplication
import org.intelehealth.app.database.dao.PatientsDAO
import org.intelehealth.app.ui.baseline_survey.viewmodel.BaselineSurveyViewModel
import org.intelehealth.app.ui.patient.data.PatientRepository
import org.intelehealth.config.room.ConfigDatabase

/**
 * Created by Shazzad H Kanon on 10-12-2024 - 11:00.
 * Email : shazzad@intelehealth.org
 * Mob   : +8801647040520
 **/
class BaselineSurveyViewModelFactory(
    private val patientRepository: PatientRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return BaselineSurveyViewModel(patientRepository) as T
    }

    companion object {
        fun create(
            context: Context,
            owner: ViewModelStoreOwner
        ): BaselineSurveyViewModel {
            val configDb = ConfigDatabase.getInstance(context)
            val patientDao = PatientsDAO()
            val sqlHelper = IntelehealthApplication.inteleHealthDatabaseHelper
            val repository = PatientRepository(patientDao, sqlHelper, configDb.patientRegFieldDao())
            val factory = BaselineSurveyViewModelFactory(repository)
            return ViewModelProvider(owner, factory)[BaselineSurveyViewModel::class]
        }
    }
}