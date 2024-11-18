package org.intelehealth.app.ui.patient.fragment

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.github.ajalt.timberkt.Timber
import com.google.gson.Gson
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentPatientOtherInfoBinding
import org.intelehealth.app.models.dto.PatientDTO
import org.intelehealth.app.utilities.ArrayAdapterUtils
import org.intelehealth.app.utilities.LanguageUtils
import org.intelehealth.app.utilities.PatientRegConfigKeys
import org.intelehealth.app.utilities.PatientRegFieldsUtils
import org.intelehealth.app.utilities.PatientRegStage
import org.intelehealth.app.utilities.extensions.hideError
import org.intelehealth.app.utilities.extensions.hideErrorOnTextChang
import org.intelehealth.app.utilities.extensions.validate
import org.intelehealth.app.utilities.extensions.validateDropDowb
import org.intelehealth.config.room.entity.PatientRegistrationFields

/**
 * Created by Vaghela Mithun R. on 27-06-2024 - 13:42.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 **/
class PatientOtherInfoFragment : BasePatientFragment(R.layout.fragment_patient_other_info) {
    private lateinit var binding: FragmentPatientOtherInfoBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentPatientOtherInfoBinding.bind(view)
        patientViewModel.updatePatientStage(PatientRegStage.OTHER)
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupSocialCategory() {
        val adapter = ArrayAdapterUtils.getArrayAdapter(requireContext(), R.array.caste)
        binding.autoCompleteSocialCategory.setAdapter(adapter)
        if (patient.caste != null && patient.caste.isNotEmpty()) {
            binding.autoCompleteSocialCategory.setText(patient.caste, false)
        }
        binding.autoCompleteSocialCategory.setOnItemClickListener { _, _, i, _ ->
            binding.textInputLaySocialCategory.hideError()
            LanguageUtils.getSpecificLocalResource(requireContext(), "en").apply {
                patient.caste = this.getStringArray(R.array.caste)[i]
            }
        }
    }

    private fun setupOccupations() {
        val adapter = ArrayAdapterUtils.getArrayAdapter(requireContext(), R.array.occupation)
        binding.autoCompleteOccupation.setAdapter(adapter)
        if (patient.occupation != null && patient.occupation.isNotEmpty()) {
            binding.autoCompleteOccupation.setText(patient.occupation, false)
        }
        binding.autoCompleteOccupation.setOnItemClickListener { _, _, i, _ ->
            binding.textInputLayOccupation.hideError()
            LanguageUtils.getSpecificLocalResource(requireContext(), "en").apply {
                patient.occupation = this.getStringArray(R.array.occupation)[i]
            }
        }
    }

    override fun onPatientDataLoaded(patient: PatientDTO) {
        super.onPatientDataLoaded(patient)
        Timber.d { "onPatientDataLoaded" }
        Timber.d { Gson().toJson(patient) }
        binding.patient = patient
        binding.isEditMode = patientViewModel.isEditMode
        fetchOtherInfoConfig()
    }

    private fun fetchOtherInfoConfig() {
//        patientViewModel.fetchOtherRegFields().observe(viewLifecycleOwner) {
        val it = getStaticPatientRegistrationFields()
        binding.otherInfoConfig = PatientRegFieldsUtils.buildPatientOtherInfoConfig(it)
        setupSocialCategory()
        setupEducations()
        setupOccupations()
        setupEconomicCategory()
        applyFilter()
        setInputTextChangListener()
        setClickListener()
//        }
    }

    private fun getStaticPatientRegistrationFields(): List<PatientRegistrationFields> {
        val fields: MutableList<PatientRegistrationFields> = mutableListOf()

        // National Id
        var currentField = PatientRegistrationFields(
            id = 0,
            groupId = "",
            name = "",
            idKey = PatientRegConfigKeys.NATIONAL_ID,
            isMandatory = false,
            isEditable = true,
            isEnabled = true
        )

        fields.add(currentField)

        // Occupations
        currentField = PatientRegistrationFields(
            id = 0,
            groupId = "",
            name = "",
            idKey = PatientRegConfigKeys.OCCUPATION,
            isMandatory = true,
            isEditable = true,
            isEnabled = true
        )

        fields.add(currentField)

        // Social Category
        currentField = PatientRegistrationFields(
            id = 0,
            groupId = "",
            name = "",
            idKey = PatientRegConfigKeys.SOCIAL_CATEGORY,
            isMandatory = false,
            isEditable = false,
            isEnabled = false
        )

        fields.add(currentField)

        // Education
        currentField = PatientRegistrationFields(
            id = 0,
            groupId = "",
            name = "",
            idKey = PatientRegConfigKeys.EDUCATION,
            isMandatory = true,
            isEditable = true,
            isEnabled = true
        )

        fields.add(currentField)

        // Economic Category
        currentField = PatientRegistrationFields(
            id = 0,
            groupId = "",
            name = "",
            idKey = PatientRegConfigKeys.ECONOMIC_CATEGORY,
            isMandatory = true,
            isEditable = true,
            isEnabled = true
        )

        fields.add(currentField)

        return fields
    }

    private fun setupEconomicCategory() {
        val adapter = ArrayAdapterUtils.getArrayAdapter(requireContext(), R.array.economic)
        binding.autoCompleteEconomicCategory.setAdapter(adapter)
        if (patient.economic != null && patient.economic.isNotEmpty()) {
            binding.autoCompleteEconomicCategory.setText(patient.economic, false)
        }
        binding.autoCompleteEconomicCategory.setOnItemClickListener { _, _, i, _ ->
            binding.textInputLayEducation.hideError()
            LanguageUtils.getSpecificLocalResource(requireContext(), "en").apply {
                patient.economic = this.getStringArray(R.array.economic)[i]
            }
        }
    }

    private fun setClickListener() {
        binding.frag2BtnBack.setOnClickListener { findNavController().popBackStack() }
        binding.frag2BtnNext.setOnClickListener {
            validateForm { savePatient() }
        }
    }

    private fun savePatient() {
        patient.apply {
            nationalID = binding.textInputNationalId.text?.toString()
            patientViewModel.updatedPatient(this)
            patientViewModel.savePatient().observe(viewLifecycleOwner) {
                it ?: return@observe
                patientViewModel.handleResponse(it) { result -> if (result) navigateToDetails() }
            }
        }
    }

    private fun navigateToDetails() {
        PatientOtherInfoFragmentDirections.navigationOtherToDetails(
            patient.uuid, "searchPatient", "false"
        ).also {
            findNavController().navigate(it)
            requireActivity().finish()
        }
    }

    private fun applyFilter() {
//        binding.textInputNationalId.addFilter(FirstLetterUpperCaseInputFilter())
//        binding.textInputOccupation.addFilter(FirstLetterUpperCaseInputFilter())
    }

    private fun setInputTextChangListener() {
        binding.textInputLayNationalId.hideErrorOnTextChang(binding.textInputNationalId)
    }

    private fun setupEducations() {
        val adapter = ArrayAdapterUtils.getArrayAdapter(requireContext(), R.array.education)
        binding.autoCompleteEducation.setAdapter(adapter)
        if (patient.education != null && patient.education.isNotEmpty()) {
            binding.autoCompleteEducation.setText(patient.education, false)
        }
        binding.autoCompleteEducation.setOnItemClickListener { _, _, i, _ ->
            binding.textInputLayEducation.hideError()
            LanguageUtils.getSpecificLocalResource(requireContext(), "en").apply {
                patient.education = this.getStringArray(R.array.education)[i]
            }
        }
    }

    private fun validateForm(block: () -> Unit) {
        Timber.d { "Final patient =>${Gson().toJson(patient)}" }
        val error = R.string.this_field_is_mandatory
        binding.otherInfoConfig?.let {
            val bNationalId = if (it.nationalId!!.isEnabled && it.nationalId!!.isMandatory) {
                binding.textInputLayNationalId.validate(binding.textInputNationalId, error)
            } else true

            val bOccupations = if (it.occuptions!!.isEnabled && it.occuptions!!.isMandatory) {
                binding.textInputLayOccupation.validateDropDowb(
                    binding.autoCompleteOccupation,
                    error
                )
            } else true


            val bSocialCategory =
                if (it.socialCategory!!.isEnabled && it.socialCategory!!.isMandatory) {
                    binding.textInputLaySocialCategory.validateDropDowb(
                        binding.autoCompleteSocialCategory,
                        error
                    )
                } else true

            val bEducation = if (it.education!!.isEnabled && it.education!!.isMandatory) {
                binding.textInputLayEducation.validateDropDowb(
                    binding.autoCompleteEducation,
                    error
                )
            } else true

            val bEconomic =
                if (it.economicCategory!!.isEnabled && it.economicCategory!!.isMandatory) {
                    binding.textInputLayEconomicCategory.validateDropDowb(
                        binding.autoCompleteEconomicCategory,
                        error
                    )
                } else true


            if (bOccupations.and(bSocialCategory).and(bEducation)
                    .and(bEconomic).and(bNationalId).and(bOccupations)
            ) block.invoke()
        }
    }
}