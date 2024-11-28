package org.intelehealth.app.activities.householdSurvey.fragments

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import com.github.ajalt.timberkt.Timber
import com.google.gson.Gson
import org.intelehealth.app.R
import org.intelehealth.app.activities.householdSurvey.models.HouseholdSurveyModel
import org.intelehealth.app.databinding.FragmentFifthHouseholdSurveyBinding
import org.intelehealth.app.models.dto.PatientDTO
import org.intelehealth.app.utilities.HouseholdSurveyStage
import org.intelehealth.app.utilities.SessionManager
import org.intelehealth.app.utilities.StringUtils
import java.util.Calendar
import java.util.Locale

class FifthFragment : BaseHouseholdSurveyFragment(R.layout.fragment_fifth_household_survey) {

    private lateinit var binding: FragmentFifthHouseholdSurveyBinding
    var selectedDate = Calendar.getInstance().timeInMillis
    private var patientUuid: String? = null
    private lateinit var updatedContext: Context


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFifthHouseholdSurveyBinding.bind(view)
        houseHoldViewModel.updatePatientStage(HouseholdSurveyStage.FIFTH_SCREEN)
        initViews()
        setClickListener()    }
    private fun setClickListener() {
        binding.frag2BtnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.frag2BtnNext.setOnClickListener {
            savePatient()
        }
    }

    private fun initViews() {
        val intent = requireActivity().intent
        if (intent != null) {
            patientUuid = intent.getStringExtra("patientUuid")
        }
    }
    private fun savePatient() {
        householdSurveyModel.apply {
            Log.d("devchdbsave4", "savePatient: householdSurveyModel : " + householdSurveyModel)
            houseHoldViewModel.updatedPatient(this)
            val patient = PatientDTO()
            patient.uuid = patientUuid
            saveAndNavigateToDetails(patient, householdSurveyModel)
        }
    }
    private fun saveAndNavigateToDetails(
        patient: PatientDTO,
        householdSurveyModel: HouseholdSurveyModel
    ) {
        houseHoldViewModel.savePatient(
            "fifthScreen", patient,
            householdSurveyModel
        ).observe(viewLifecycleOwner) {
            it ?: return@observe
            houseHoldViewModel.handleResponse(it) { result -> if (result) navigateToDetails() }
        }
    }
    private fun navigateToDetails() {
        FifthFragmentDirections.actionFiveToSix().apply {
            findNavController().navigate(this)
        }
    }
    private fun setupForFuelForCooking(){
        var otherCookingFuel: String = if (binding.otherCheckbox.isChecked) {
            StringUtils.getValue(binding.textInputOtherSourcesOfFuelLayout.text.toString())
        } else {
            "-"
        }
        householdSurveyModel.cookingFuelType = StringUtils.getSelectedCheckboxes(
            binding.householdCookingFuelCheckboxLinearLayout,
            SessionManager(requireActivity()).appLanguage,
            context,
            otherCookingFuel
        )

    }
    private fun setDataForFuelForCookingUI(){

    }
    private fun setupForMainLightingSource(){
        var otherMainLightingSource: String = if (binding.otherSourceOfLightingCheckbox.isChecked) {
            StringUtils.getValue(binding.textInputOtherSourcesOfLightingLayout.text.toString())
        } else {
            "-"
        }

       householdSurveyModel.mainLightingSource = StringUtils.getSelectedCheckboxes(
            binding.mainSourceOfLightingCheckboxLinearLayout,
           SessionManager(requireActivity()).appLanguage,
            context,
           otherMainLightingSource
        )
    }
    private fun setDataForMainLightingSourceUI(){

    }

    private fun setupForDrinkingWaterSource(){
        var otherDrinkingWaterSource: String = if (binding.otherSourceOfWaterCheckbox.isChecked) {
            StringUtils.getValue(binding.textInputOtherSourcesDrinkingWater.text.toString())
        } else {
            "-"
        }

        householdSurveyModel.mainDrinkingWaterSource = StringUtils.getSelectedCheckboxes(
            binding.mainSourceOfDrinkingWaterCheckboxLinearLayout,
            SessionManager(requireActivity()).appLanguage,
            context,
            otherDrinkingWaterSource
        )
    }
    private fun setDataForDrinkingWaterSourceUI(){

    }
    private fun setupForWaterPurification(){
        var otherWaterPurificationSource: String = if (binding.otherWaysOfPurifyingWaterCheckbox.isChecked) {
            StringUtils.getValue(binding.inputOtherPurificationMethod.text.toString())
        } else {
            "-"
        }

        householdSurveyModel.saferWaterProcess = StringUtils.getSelectedCheckboxes(
            binding.llSaferWaterPurificationCheckBox,
            SessionManager(requireActivity()).appLanguage,
            context,
            otherWaterPurificationSource
        )
    }
    private fun setDataForWaterPurification(){

    }
    private fun setupForHouseholdToiletFacility(){
       householdSurveyModel.householdToiletFacility =  StringUtils.getSelectedCheckboxes(
            binding.familyToiletFacilityCheckboxLinearLayout, SessionManager(requireActivity()).appLanguage,
            context, ""
        )
    }
    private fun setDataForHouseholdToiletFacility(){

    }
    private fun setDataToUI() {
        updatedContext =
            if (SessionManager(requireActivity()).appLanguage.equals("mr", ignoreCase = true)) {
                val configuration = Configuration(requireContext().resources.configuration)
                configuration.setLocale(Locale("en"))
                requireContext().createConfigurationContext(configuration)
            } else {
                requireContext()
            }

    }
    override fun onPatientDataLoaded(householdSurveyModel: HouseholdSurveyModel) {
        super.onPatientDataLoaded(householdSurveyModel)
        Timber.d { Gson().toJson(householdSurveyModel) }
        setDataToUI();

        binding.patientSurveyAttributes = householdSurveyModel
        binding.isEditMode = houseHoldViewModel.isEditMode
    }
}