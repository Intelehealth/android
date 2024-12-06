package org.intelehealth.app.ui.rosterquestionnaire.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import org.intelehealth.app.app.IntelehealthApplication
import org.intelehealth.app.database.dao.PatientsDAO
import org.intelehealth.app.ui.rosterquestionnaire.data.RosterRepository
import org.intelehealth.app.ui.rosterquestionnaire.viewmodel.RosterViewModel

class RosterViewModelFactory (
    private val rosterRepository: RosterRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RosterViewModel(rosterRepository) as T
    }

    companion object {
        fun create(
            context: Context,
            owner: ViewModelStoreOwner
        ): RosterViewModel {
            val patientDao = PatientsDAO()
            val sqlHelper = IntelehealthApplication.inteleHealthDatabaseHelper
            val repository = RosterRepository(patientDao, sqlHelper)
            val factory = RosterViewModelFactory(repository)
            return ViewModelProvider(owner, factory)[RosterViewModel::class]
        }
    }
}