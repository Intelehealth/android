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
import org.intelehealth.app.utilities.ArrayAdapterUtils
import org.intelehealth.app.utilities.LanguageUtils
import org.intelehealth.app.utilities.extensions.hideError

/**
 * Created by Shazzad H Kanon on 06-12-2024 - 11:00.
 * Email : shazzad@intelehealth.org
 * Mob   : +8801647040520
 **/


class BaselineGeneralFragment :
    BaseFragmentBaselineSurvey(R.layout.fragment_baseline_survey_general) {

    private lateinit var binding: FragmentBaselineSurveyGeneralBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentBaselineSurveyGeneralBinding.bind(view)
        baselineSurveyViewModel.updateBaselineStage(BaselineSurveyStage.GENERAL)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onPatientDataLoaded(patient: PatientDTO) {
        super.onPatientDataLoaded(patient)
        Timber.d { "onPatientDataLoaded" }
        Timber.d { Gson().toJson(patient) }
        fetchGeneralBaselineConfig()
        binding.patient = patient
        binding.baselineEditMode = baselineSurveyViewModel.baselineEditMode
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
        setupOccupationCheck()
        setupCasteCheck()
        setupEducationCheck()
        setupEconomicStatusCheck()
        setupPhoneOwnershipCheck()
        setupAyushmanCard()
        setupMgnregaCard()
        setupBankAccount()
        setupFamilyWhatsapp()
        setupMaritalStatus()
    }

    private fun setupAyushmanCard() {
        binding.rgACOptions.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioACYes -> {
                    binding.rgACOptions.check(R.id.radioACYes)
                }

                R.id.radioACNo -> {
                    binding.rgACOptions.check(R.id.radioACNo)
                }

                R.id.radioACNotSure -> {
                    binding.rgACOptions.check(R.id.radioACNotSure)
                }
            }
        }
    }

    private fun setupMgnregaCard() {
        binding.rgMCOptions.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioMCYes -> {
                    binding.rgMCOptions.check(R.id.radioMCYes)
                }

                R.id.radioMCNo -> {
                    binding.rgMCOptions.check(R.id.radioMCNo)
                }

                R.id.radioMCNotSure -> {
                    binding.rgMCOptions.check(R.id.radioMCNotSure)
                }
            }
        }
    }

    private fun setupBankAccount() {
        binding.rgBAOptions.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioBAYes -> {
                    binding.rgBAOptions.check(R.id.radioBAYes)
                }

                R.id.radioBANo -> {
                    binding.rgBAOptions.check(R.id.radioBANo)
                }

                R.id.radioBANotSure -> {
                    binding.rgBAOptions.check(R.id.radioBANotSure)
                }
            }
        }
    }

    private fun setupFamilyWhatsapp() {
        binding.rgBankAccountOptions.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioPersonal -> {
                    binding.rgBankAccountOptions.check(R.id.radioPersonal)
                }

                R.id.radioFamilyMember -> {
                    binding.rgBankAccountOptions.check(R.id.radioFamilyMember)
                }

                R.id.radioFamilyWhatsappNo -> {
                    binding.rgBankAccountOptions.check(R.id.radioFamilyWhatsappNo)
                }
            }
        }
    }

    private fun setupMaritalStatus() {
        binding.rgMaritalStatusOptions.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioMarried -> {
                    binding.rgMaritalStatusOptions.check(R.id.radioMarried)
                }

                R.id.radioUnmarried -> {
                    binding.rgMaritalStatusOptions.check(R.id.radioUnmarried)
                }

                R.id.radioWidowed -> {
                    binding.rgMaritalStatusOptions.check(R.id.radioWidowed)
                }
            }
        }
    }

    private fun setupOccupationCheck() {
        val adapter =
            ArrayAdapterUtils.getArrayAdapter(requireContext(), R.array.occupation)
        binding.acOccupation.setAdapter(adapter)
        binding.acOccupation.setText(getString(R.string.select_occupation_txt), false)

        binding.acOccupation.setOnItemClickListener { _, _, i, _ ->
            binding.tilOccupationOption.hideError()
            LanguageUtils.getSpecificLocalResource(requireContext(), "en").apply {
                binding.acOccupation.setText(
                    this.getStringArray(R.array.occupation)[i],
                    false
                )
            }
        }
    }

    private fun setupCasteCheck() {
        val adapter = ArrayAdapterUtils.getArrayAdapter(requireContext(), R.array.caste)
        binding.acCaste.setAdapter(adapter)
        binding.acCaste.setText(getString(R.string.select_caste), false)

        binding.acCaste.setOnItemClickListener { _, _, i, _ ->
            binding.tilCasteOption.hideError()
            LanguageUtils.getSpecificLocalResource(requireContext(), "en").apply {
                binding.acCaste.setText(this.getStringArray(R.array.caste)[i], false)
            }
        }
    }

    private fun setupEducationCheck() {
        val adapter = ArrayAdapterUtils.getArrayAdapter(requireContext(), R.array.education)
        binding.acEducation.setAdapter(adapter)
        binding.acEducation.setText(getString(R.string.select_education), false)

        binding.acEducation.setOnItemClickListener { _, _, i, _ ->
            binding.tilEducationOption.hideError()
            LanguageUtils.getSpecificLocalResource(requireContext(), "en").apply {
                binding.acEducation.setText(
                    this.getStringArray(R.array.education)[i],
                    false
                )
            }
        }
    }

    private fun setupEconomicStatusCheck() {
        val adapter =
            ArrayAdapterUtils.getArrayAdapter(requireContext(), R.array.economic)
        binding.acEconomicStatus.setAdapter(adapter)
        binding.acEconomicStatus.setText(getString(R.string.select_economic_category), false)

        binding.acEconomicStatus.setOnItemClickListener { _, _, i, _ ->
            binding.tilEconomicStatusOption.hideError()
            LanguageUtils.getSpecificLocalResource(requireContext(), "en").apply {
                binding.acEconomicStatus.setText(
                    this.getStringArray(R.array.economic)[i],
                    false
                )
            }
        }
    }

    private fun setupPhoneOwnershipCheck() {
        val adapter =
            ArrayAdapterUtils.getArrayAdapter(requireContext(), R.array.phone_ownership)
        binding.acPhoneOwnership.setAdapter(adapter)
        binding.acPhoneOwnership.setText(getString(R.string.select), false)

        binding.acPhoneOwnership.setOnItemClickListener { _, _, i, _ ->
            binding.tilPhoneOwnershipOption.hideError()
            LanguageUtils.getSpecificLocalResource(requireContext(), "en").apply {
                binding.acPhoneOwnership.setText(
                    this.getStringArray(R.array.phone_ownership)[i],
                    false
                )
            }
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