package org.intelehealth.app.ui.baseline_survey.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.github.ajalt.timberkt.Timber
import com.google.gson.Gson
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentBaselineSurveyOtherBinding
import org.intelehealth.app.models.dto.PatientDTO
import org.intelehealth.app.utilities.BaselineSurveyStage

/**
 * Created by Shazzad H Kanon on 06-12-2024 - 11:00.
 * Email : shazzad@intelehealth.org
 * Mob   : +8801647040520
 **/
class BaselineOtherFragment : BaseFragmentBaselineSurvey(R.layout.fragment_baseline_survey_other) {

    private lateinit var binding: FragmentBaselineSurveyOtherBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentBaselineSurveyOtherBinding.bind(view)
        baselineSurveyViewModel.updateBaselineStage(BaselineSurveyStage.OTHER)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onPatientDataLoaded(patient: PatientDTO) {
        super.onPatientDataLoaded(patient)
        Timber.d { "onPatientDataLoaded" }
        Timber.d { Gson().toJson(patient) }
        setClickListener()
        binding.patient = patient
        binding.baselineEditMode = baselineSurveyViewModel.baselineEditMode
    }

    private fun setClickListener() {
        binding.frag3BtnBack.setOnClickListener {
            BaselineOtherFragmentDirections.navigationOtherToMedical().apply {
                findNavController().navigate(this)
            }
        }
        binding.frag3BtnNext.setOnClickListener {
            BaselineOtherFragmentDirections.navigationOtherToPatientDetails(
                patient.uuid, "searchPatient", "false"
            ).also {
                findNavController().navigate(it)
                requireActivity().finish()
            }
        }
    }

}