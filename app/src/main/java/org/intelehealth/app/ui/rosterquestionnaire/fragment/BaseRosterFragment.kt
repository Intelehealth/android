package org.intelehealth.app.ui.rosterquestionnaire.fragment

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import org.intelehealth.app.models.dto.PatientDTO
import org.intelehealth.app.ui.rosterquestionnaire.factory.RosterViewModelFactory

open class BaseRosterFragment (@LayoutRes layoutResId: Int) : Fragment(layoutResId) {
    protected var patient: PatientDTO = PatientDTO()
    protected val rosterViewModel by lazy {
        return@lazy RosterViewModelFactory.create(requireActivity(), requireActivity())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       /* rosterViewModel.rosterAttributesData.observe(viewLifecycleOwner) {
        onPatientDataLoaded(it)
        }*/
    }

 /*   open fun onPatientDataLoaded(patient: PatientDTO) {
        this.patient = patient
    }*/
}