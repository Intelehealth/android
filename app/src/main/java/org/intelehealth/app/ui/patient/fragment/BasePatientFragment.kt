package org.intelehealth.app.ui.patient.fragment

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import org.intelehealth.app.models.dto.PatientDTO
import org.intelehealth.config.presenter.fields.factory.PatientViewModelFactory

/**
 * Created by Vaghela Mithun R. on 10-07-2024 - 10:56.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
abstract class BasePatientFragment(@LayoutRes layoutResId: Int) : Fragment(layoutResId) {
    protected var patient: PatientDTO = PatientDTO()
    protected val patientViewModel by lazy {
        return@lazy PatientViewModelFactory.create(requireActivity(), requireActivity())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        patientViewModel.patientData.observe(viewLifecycleOwner) {
            if (it.cityvillage.isNullOrEmpty().not() && it.cityvillage.contains(":")) {
                patient.district = it.cityvillage.split(":")[0].trim()
            }
            onPatientDataLoaded(it)
        }
    }

    open fun onPatientDataLoaded(patient: PatientDTO) {
        this.patient = patient
    }
}