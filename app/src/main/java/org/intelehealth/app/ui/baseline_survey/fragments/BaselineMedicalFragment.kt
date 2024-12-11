package org.intelehealth.app.ui.baseline_survey.fragments

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.github.ajalt.timberkt.Timber
import com.google.gson.Gson
import org.intelehealth.app.R
import org.intelehealth.app.activities.patientDetailActivity.StaticPatientRegistrationEnabledFieldsHelper
import org.intelehealth.app.databinding.FragmentBaselineSurveyMedicalBinding
import org.intelehealth.app.models.dto.PatientDTO
import org.intelehealth.app.utilities.ArrayAdapterUtils
import org.intelehealth.app.utilities.BaselineSurveyStage
import org.intelehealth.app.utilities.LanguageUtils
import org.intelehealth.app.utilities.PatientRegFieldsUtils
import org.intelehealth.app.utilities.extensions.hideError

/**
 * Created by Shazzad H Kanon on 06-12-2024 - 11:00.
 * Email : shazzad@intelehealth.org
 * Mob   : +8801647040520
 **/


class BaselineMedicalFragment : BaseFragmentBaselineSurvey(R.layout.fragment_baseline_survey_medical) {

    private lateinit var binding: FragmentBaselineSurveyMedicalBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentBaselineSurveyMedicalBinding.bind(view)
        baselineSurveyViewModel.updateBaselineStage(BaselineSurveyStage.MEDICAL)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onPatientDataLoaded(patient: PatientDTO) {
        super.onPatientDataLoaded(patient)
        Timber.d { "onPatientDataLoaded" }
        Timber.d { Gson().toJson(patient) }
        fetchMedicalBaselineConfig()
        binding.patient = patient
        binding.baselineEditMode = baselineSurveyViewModel.baselineEditMode
    }

    private fun fetchMedicalBaselineConfig() {
        val it = getStaticPatientRegistrationFields()
        binding.medicalConfig = PatientRegFieldsUtils.buildMedicalBaselineConfig(it)
        setValues()
        setClickListener()
    }

    private fun getStaticPatientRegistrationFields() =
        StaticPatientRegistrationEnabledFieldsHelper.getEnabledMedicalBaselineFields()

    private fun setValues() {
        setupHbCheck()
        setupBpCheck()
        setupSugarCheck()
        setupBP()
        setupDiabetes()
        setupArthritis()
        setupAnemia()
        setupSurgeries()
        setupSmokingHistory()
        showSmokingHistory()
        setupTobaccoChew()
        setupAlcoholHistory()
        showAlcoholHistory()
    }

    private fun showAlcoholHistory(){
        setupAlcoholRate()
        setupAlcoholDuration()
        setupAlcoholFrequency()
    }

    private fun showSmokingHistory(){
        setupSmokingRate()
        setupSmokingDuration()
        setupSmokingFrequency()
    }

    private fun setupHbCheck() {
        val adapter = ArrayAdapterUtils.getArrayAdapter(requireContext(), R.array.hb_check)
        binding.acHbCheck.setAdapter(adapter)
        binding.acHbCheck.setText("Select your choice", false)

        binding.acHbCheck.setOnItemClickListener { _, _, i, _ ->
            binding.tilHbCheckOption.hideError()
            LanguageUtils.getSpecificLocalResource(requireContext(), "en").apply {
                binding.acHbCheck.setText(this.getStringArray(R.array.hb_check)[i], false)
            }
        }
    }

    private fun setupBpCheck() {
        val adapter = ArrayAdapterUtils.getArrayAdapter(requireContext(), R.array.bp_check)
        binding.acBpCheck.setAdapter(adapter)
        binding.acBpCheck.setText("Select your choice", false)

        binding.acBpCheck.setOnItemClickListener { _, _, i, _ ->
            binding.tilBpCheckOption.hideError()
            LanguageUtils.getSpecificLocalResource(requireContext(), "en").apply {
                binding.acBpCheck.setText(this.getStringArray(R.array.bp_check)[i], false)
            }
        }
    }

    private fun setupSugarCheck() {
        val adapter = ArrayAdapterUtils.getArrayAdapter(requireContext(), R.array.sugar_check)
        binding.acSugarCheck.setAdapter(adapter)
        binding.acSugarCheck.setText("Select your choice", false)

        binding.acSugarCheck.setOnItemClickListener { _, _, i, _ ->
            binding.tilSugarCheckOption.hideError()
            LanguageUtils.getSpecificLocalResource(requireContext(), "en").apply {
                binding.acSugarCheck.setText(this.getStringArray(R.array.sugar_check)[i], false)
            }
        }
    }

    private fun setupSurgeryReasonCheck() {
        val adapter = ArrayAdapterUtils.getArrayAdapter(requireContext(), R.array.surgery_reason_check)
        binding.acSurgeryReasonCheck.setAdapter(adapter)
        binding.acSurgeryReasonCheck.setText("Select your choice", false)

        binding.acSurgeryReasonCheck.setOnItemClickListener { _, _, i, _ ->
            binding.tilSurgeryReasonOption.hideError()
            LanguageUtils.getSpecificLocalResource(requireContext(), "en").apply {
                binding.acSurgeryReasonCheck.setText(this.getStringArray(R.array.surgery_reason_check)[i], false)
            }
        }
    }

    private fun setupBP() {
        binding.rgBpOptions.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioBpYes -> {
                    binding.rgBpOptions.check(R.id.radioBpYes)
                }
                R.id.radioBpNo -> {
                    binding.rgBpOptions.check(R.id.radioBpNo)
                }
            }
        }
    }

    private fun setupDiabetes() {
        binding.rgDiabetesOptions.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioDiabetesYes -> {
                    binding.rgDiabetesOptions.check(R.id.radioDiabetesYes)
                }
                R.id.radioDiabetesNo -> {
                    binding.rgDiabetesOptions.check(R.id.radioDiabetesNo)
                }
            }
        }
    }

    private fun setupArthritis() {
        binding.rgArthritisOptions.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioArthritisYes -> {
                    binding.rgArthritisOptions.check(R.id.radioArthritisYes)
                }
                R.id.radioArthritisNo -> {
                    binding.rgArthritisOptions.check(R.id.radioArthritisNo)
                }
            }
        }
    }

    private fun setupAnemia() {
        binding.rgAnemiaOptions.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioAnemiaYes -> {
                    binding.rgAnemiaOptions.check(R.id.radioAnemiaYes)
                }
                R.id.radioAnemiaNo -> {
                    binding.rgAnemiaOptions.check(R.id.radioAnemiaNo)
                }
            }
        }
    }

    private fun setupSurgeries() {
        binding.rgSurgeryOptions.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioSurgeryYes -> {
                    binding.rgSurgeryOptions.check(R.id.radioSurgeryYes)
                    binding.medicalConfig?.surgeryReason?.isEnabled = true
                    setupSurgeryReasonCheck()
                }
                R.id.radioSurgeryNo -> {
                    binding.rgSurgeryOptions.check(R.id.radioSurgeryNo)
                    binding.medicalConfig?.surgeryReason?.isEnabled = false
                }
            }
        }
    }

    private fun setupSmokingHistory() {
        binding.rgSmokingHistoryOptions.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioSmoker -> {
                    binding.rgSmokingHistoryOptions.check(R.id.radioSmoker)
                }
                R.id.radioNonSmoker -> {
                    binding.rgSmokingHistoryOptions.check(R.id.radioNonSmoker)
                }
                R.id.radioAlcoholHistoryDeclined -> {
                    binding.rgSmokingHistoryOptions.check(R.id.radioAlcoholHistoryDeclined)
                }
            }
        }
    }

    private fun setupTobaccoChew() {
        binding.rgChewTobaccoOptions.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioChewYes -> {
                    binding.rgChewTobaccoOptions.check(R.id.radioChewYes)
                }
                R.id.radioChewNo -> {
                    binding.rgChewTobaccoOptions.check(R.id.radioChewNo)
                }
                R.id.radioChewDeclined -> {
                    binding.rgChewTobaccoOptions.check(R.id.radioChewDeclined)
                }
            }
        }
    }

    private fun setupAlcoholHistory() {
        binding.rgAlcoholHistoryOptions.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioAlcoholHistoryYes -> {
                    binding.rgAlcoholHistoryOptions.check(R.id.radioAlcoholHistoryYes)
                }
                R.id.radioAlcoholHistoryNo -> {
                    binding.rgAlcoholHistoryOptions.check(R.id.radioAlcoholHistoryNo)
                }
                R.id.radioAlcoholHistoryDeclined -> {
                    binding.rgAlcoholHistoryOptions.check(R.id.radioAlcoholHistoryDeclined)
                }
            }
        }
    }

    private fun setupAlcoholRate() {
        binding.rgAlcoholRateOptions.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioAlcoholRate1 -> {
                    binding.rgAlcoholRateOptions.check(R.id.radioAlcoholRate1)
                }
                R.id.radioAlcoholRate2 -> {
                    binding.rgAlcoholRateOptions.check(R.id.radioAlcoholRate2)
                }
                R.id.radioAlcoholRate3 -> {
                    binding.rgAlcoholRateOptions.check(R.id.radioAlcoholRate3)
                }
                R.id.radioAlcoholRate4 -> {
                    binding.rgAlcoholRateOptions.check(R.id.radioAlcoholRate4)
                }
                R.id.radioAlcoholRate5 -> {
                    binding.rgAlcoholRateOptions.check(R.id.radioAlcoholRate5)
                }
            }
        }
    }

    private fun setupAlcoholDuration() {
        binding.rgAlcoholDurationOptions.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioAlcoholDuration1 -> {
                    binding.rgAlcoholDurationOptions.check(R.id.radioAlcoholDuration1)
                }
                R.id.radioAlcoholDuration2 -> {
                    binding.rgAlcoholDurationOptions.check(R.id.radioAlcoholDuration2)
                }
                R.id.radioAlcoholDuration3 -> {
                    binding.rgAlcoholDurationOptions.check(R.id.radioAlcoholDuration3)
                }
                R.id.radioAlcoholDuration4 -> {
                    binding.rgAlcoholDurationOptions.check(R.id.radioAlcoholDuration4)
                }
            }
        }
    }

    private fun setupAlcoholFrequency() {
        binding.rgAlcoholFrequencyOptions.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioAlcoholFrequency1 -> {
                    binding.rgAlcoholFrequencyOptions.check(R.id.radioAlcoholFrequency1)
                }
                R.id.radioAlcoholFrequency2 -> {
                    binding.rgAlcoholFrequencyOptions.check(R.id.radioAlcoholFrequency2)
                }
                R.id.radioAlcoholFrequency3 -> {
                    binding.rgAlcoholFrequencyOptions.check(R.id.radioAlcoholFrequency3)
                }
                R.id.radioAlcoholFrequency4 -> {
                    binding.rgAlcoholFrequencyOptions.check(R.id.radioAlcoholFrequency4)
                }
            }
        }
    }



    private fun setupSmokingRate() {
        binding.rgAlcoholRateOptions.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioAlcoholRate1 -> {
                    binding.rgAlcoholRateOptions.check(R.id.radioAlcoholRate1)
                }
                R.id.radioAlcoholRate2 -> {
                    binding.rgAlcoholRateOptions.check(R.id.radioAlcoholRate2)
                }
                R.id.radioAlcoholRate3 -> {
                    binding.rgAlcoholRateOptions.check(R.id.radioAlcoholRate3)
                }
                R.id.radioAlcoholRate4 -> {
                    binding.rgAlcoholRateOptions.check(R.id.radioAlcoholRate4)
                }
                R.id.radioAlcoholRate5 -> {
                    binding.rgAlcoholRateOptions.check(R.id.radioAlcoholRate5)
                }
            }
        }
    }

    private fun setupSmokingDuration() {
        binding.rgAlcoholDurationOptions.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioSmokingDuration1 -> {
                    binding.rgAlcoholDurationOptions.check(R.id.radioSmokingDuration1)
                }
                R.id.radioSmokingDuration2 -> {
                    binding.rgAlcoholDurationOptions.check(R.id.radioSmokingDuration2)
                }
                R.id.radioSmokingDuration3 -> {
                    binding.rgAlcoholDurationOptions.check(R.id.radioSmokingDuration3)
                }
                R.id.radioSmokingDuration4 -> {
                    binding.rgAlcoholDurationOptions.check(R.id.radioSmokingDuration4)
                }
            }
        }
    }

    private fun setupSmokingFrequency() {
        binding.rgSmokingFrequencyOptions.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioSmokingFrequency1 -> {
                    binding.rgSmokingFrequencyOptions.check(R.id.radioSmokingFrequency1)
                }
                R.id.radioSmokingFrequency2 -> {
                    binding.rgSmokingFrequencyOptions.check(R.id.radioSmokingFrequency2)
                }
                R.id.radioSmokingFrequency3 -> {
                    binding.rgSmokingFrequencyOptions.check(R.id.radioSmokingFrequency3)
                }
                R.id.radioSmokingFrequency4 -> {
                    binding.rgSmokingFrequencyOptions.check(R.id.radioSmokingFrequency4)
                }
            }
        }
    }


    private fun setClickListener() {
        binding.frag2BtnBack.setOnClickListener {
            BaselineMedicalFragmentDirections.navigationMedicalToGeneral().apply {
                findNavController().navigate(this)
            }
        }
        binding.frag2BtnNext.setOnClickListener {
            BaselineMedicalFragmentDirections.navigationMedicalToOther().apply {
                findNavController().navigate(this)
            }
        }
    }

}