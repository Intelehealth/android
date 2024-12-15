package org.intelehealth.app.ui.baseline_survey.fragments

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import org.intelehealth.app.models.dto.PatientAttributesDTO
import org.intelehealth.app.models.dto.PatientDTO
import org.intelehealth.app.ui.baseline_survey.factory.BaselineSurveyViewModelFactory
import org.intelehealth.app.ui.baseline_survey.model.Baseline

/**
 * Created by Shazzad H Kanon on 06-12-2024 - 11:00.
 * Email : shazzad@intelehealth.org
 * Mob   : +8801647040520
 **/
abstract class BaseFragmentBaselineSurvey(@LayoutRes layoutResId: Int) : Fragment(layoutResId) {
    protected var patient: PatientDTO = PatientDTO()
    protected var baselineSurveyData: Baseline = Baseline()

    protected val baselineSurveyViewModel by lazy {
        return@lazy BaselineSurveyViewModelFactory.create(requireActivity(), requireActivity())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        baselineSurveyViewModel.patientData.observe(viewLifecycleOwner) {
            onPatientDataLoaded(it)
        }
        baselineSurveyViewModel.baselineData.observe(viewLifecycleOwner) {
            onBaselineDataLoaded(it)
        }
    }

    open fun onPatientDataLoaded(patient: PatientDTO) {
        this.patient = patient
    }

    open fun onBaselineDataLoaded(baselineData: Baseline) {
        this.baselineSurveyData = baselineData
    }
}