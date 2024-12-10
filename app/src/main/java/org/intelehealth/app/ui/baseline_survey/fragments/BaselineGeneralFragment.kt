package org.intelehealth.app.ui.baseline_survey.fragments

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.github.ajalt.timberkt.Timber
import com.google.gson.Gson
import org.intelehealth.app.R
import org.intelehealth.app.activities.patientDetailActivity.StaticPatientRegistrationEnabledFieldsHelper
import org.intelehealth.app.databinding.FragmentBaselineSurveyGeneralBinding
import org.intelehealth.app.models.dto.PatientDTO
import org.intelehealth.app.utilities.BaselineSurveyStage
import org.intelehealth.app.utilities.PatientRegFieldsUtils
import androidx.navigation.fragment.findNavController

/**
 * Created by Shazzad H Kanon on 06-12-2024 - 11:00.
 * Email : shazzad@intelehealth.org
 * Mob   : +8801647040520
 **/


class BaselineGeneralFragment : BaseFragmentBaselineSurvey(R.layout.fragment_baseline_survey_general) {

    private lateinit var binding: FragmentBaselineSurveyGeneralBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentBaselineSurveyGeneralBinding.bind(view)
        patientViewModel.updateBaselineStage(BaselineSurveyStage.GENERAL)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onPatientDataLoaded(patient: PatientDTO) {
        super.onPatientDataLoaded(patient)
        Timber.d { "onPatientDataLoaded" }
        Timber.d { Gson().toJson(patient) }
        fetchGeneralBaselineConfig()
        binding.patient = patient
        binding.baselineEditMode = patientViewModel.baselineEditMode
    }

    private fun fetchGeneralBaselineConfig() {
        val it = getStaticPatientRegistrationFields()
        binding.generalConfig = PatientRegFieldsUtils.buildGeneralBaselineConfig(it)
        setValues()
        setClickListener()
    }

    private fun getStaticPatientRegistrationFields() =
        StaticPatientRegistrationEnabledFieldsHelper.getEnabledGeneralBaselineFields()

    private fun setValues() {
        binding.toggleAyushmanCard.addOnButtonCheckedListener { group, checkedId, isChecked ->
            binding.tvAyushmanError.isVisible = false
        }
        binding.toggleMgnregaCard.addOnButtonCheckedListener { group, checkedId, isChecked ->
            binding.tvMgnregaError.isVisible = false
        }
        binding.toggleBankAccount.addOnButtonCheckedListener { group, checkedId, isChecked ->
            binding.tvBankError.isVisible = false
        }
        binding.toggleFamilyWhatsapp.addOnButtonCheckedListener { group, checkedId, isChecked ->
            binding.tvFamilyWhatsappError.isVisible = false
        }
        binding.toggleMaritalStatus.addOnButtonCheckedListener { group, checkedId, isChecked ->
            binding.tvMaritalStatusError.isVisible = false
        }
    }

    private fun setClickListener() {
        binding.btnGeneralBaselineNext.setOnClickListener {
            BaselineGeneralFragmentDirections.navigationGeneralToMedical().apply {
                findNavController().navigate(this)
            }
        }
    }

}