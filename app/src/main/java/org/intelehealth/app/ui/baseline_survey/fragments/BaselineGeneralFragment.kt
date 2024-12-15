package org.intelehealth.app.ui.baseline_survey.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import org.intelehealth.app.R
import org.intelehealth.app.activities.patientDetailActivity.StaticPatientRegistrationEnabledFieldsHelper
import org.intelehealth.app.databinding.FragmentBaselineSurveyGeneralBinding
import org.intelehealth.app.utilities.BaselineSurveyStage
import org.intelehealth.app.utilities.PatientRegFieldsUtils
import androidx.navigation.fragment.findNavController
import org.intelehealth.app.models.dto.PatientAttributesDTO
import org.intelehealth.app.ui.baseline_survey.model.Baseline
import org.intelehealth.app.utilities.ArrayAdapterUtils
import org.intelehealth.app.utilities.LanguageUtils
import org.intelehealth.app.utilities.extensions.getSelectedData
import org.intelehealth.app.utilities.extensions.hideError
import org.intelehealth.app.utilities.extensions.validate
import org.intelehealth.app.utilities.extensions.validateDropDowb

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

    override fun onBaselineDataLoaded(baselineData: Baseline) {
        super.onBaselineDataLoaded(baselineData)
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
    }

    private fun setupOccupationCheck() {
        val adapter = ArrayAdapterUtils.getArrayAdapter(requireContext(), R.array.occupation)
        binding.acOccupation.setAdapter(adapter)

        binding.acOccupation.setOnItemClickListener { _, _, i, _ ->
            binding.tilOccupationOption.hideError()
            LanguageUtils.getSpecificLocalResource(requireContext(), "en").apply {
                binding.acOccupation.setText(this.getStringArray(R.array.occupation)[i], false)
            }
        }
    }

    private fun setupCasteCheck() {
        val adapter = ArrayAdapterUtils.getArrayAdapter(requireContext(), R.array.caste)
        binding.acCaste.setAdapter(adapter)

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
        val adapter = ArrayAdapterUtils.getArrayAdapter(requireContext(), R.array.economic)
        binding.acEconomicStatus.setAdapter(adapter)

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
        val adapter = ArrayAdapterUtils.getArrayAdapter(requireContext(), R.array.phone_ownership)
        binding.acPhoneOwnership.setAdapter(adapter)

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
            validateForm { saveSurveyData() }
        }
    }

    private fun saveSurveyData() {
        baselineSurveyData.apply {

            occupation = binding.acOccupation.text.toString()
            caste = binding.acCaste.text.toString()
            education = binding.acEducation.text.toString()
            economicStatus = binding.acEconomicStatus.text.toString()
            ayushmanCard = binding.rgACOptions.getSelectedData()
            mgnregaCard = binding.rgMCOptions.getSelectedData()
            bankAccount = binding.rgBAOptions.getSelectedData()
            phoneOwnership = binding.acPhoneOwnership.text.toString()
            familyWhatsApp = binding.rgFamilyWhatsappOptions.getSelectedData()
            martialStatus = binding.rgMaritalStatusOptions.getSelectedData()

            baselineSurveyViewModel.updateBaselineData(this)

            BaselineGeneralFragmentDirections.navigationGeneralToMedical().apply {
                findNavController().navigate(this)
            }
        }
    }

    private fun validateForm(block: () -> Unit) {
        val error = R.string.this_field_is_mandatory

        binding.generalConfig?.let {
            val bOccupation = if (it.occupation!!.isEnabled && it.occupation!!.isMandatory) {
                binding.tilOccupationOption.validateDropDowb(binding.acOccupation, error)
            } else true

            val bCaste = if (it.caste!!.isEnabled && it.caste!!.isMandatory) {
                binding.tilCasteOption.validateDropDowb(binding.acCaste, error)
            } else true

            val bEducation = if (it.education!!.isEnabled && it.education!!.isMandatory) {
                binding.tilEducationOption.validateDropDowb(binding.acEducation, error)
            } else true

            val bEconomicStatus =
                if (it.economicStatus!!.isEnabled && it.economicStatus!!.isMandatory) {
                    binding.tilEconomicStatusOption.validateDropDowb(
                        binding.acEconomicStatus,
                        error
                    )
                } else true

            val bAyushmanCard = if (it.ayushmanCard!!.isEnabled && it.ayushmanCard!!.isMandatory) {
                binding.rgACOptions.validate()
            } else true

            val bMgnrega = if (it.mgnrega!!.isEnabled && it.mgnrega!!.isMandatory) {
                binding.rgMCOptions.validate()
            } else true

            val bBankAc = if (it.bankAccount!!.isEnabled && it.bankAccount!!.isMandatory) {
                binding.rgBAOptions.validate()
            } else true

            val phoneOwnership =
                if (it.phoneOwnership!!.isEnabled && it.phoneOwnership!!.isMandatory) {
                    binding.tilPhoneOwnershipOption.validateDropDowb(
                        binding.acPhoneOwnership,
                        error
                    )
                } else true

            val familyWhatsApp =
                if (it.familyWhatsapp!!.isEnabled && it.familyWhatsapp!!.isMandatory) {
                    binding.rgFamilyWhatsappOptions.validate()
                } else true

            val maritalStatus =
                if (it.maritalStatus!!.isEnabled && it.maritalStatus!!.isMandatory) {
                    binding.rgMaritalStatusOptions.validate()
                } else true

            if (bOccupation.and(bCaste).and(bEducation).and(bEconomicStatus)
                    .and(bAyushmanCard).and(bMgnrega).and(bBankAc).and(phoneOwnership)
                    .and(familyWhatsApp).and(maritalStatus)
            ) {
                block.invoke()
            } else {
                Toast.makeText(
                    requireActivity(),
                    getString(R.string.please_select_all_the_required_fields),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}