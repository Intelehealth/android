package org.intelehealth.app.ui.baseline_survey.fragments

import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import org.intelehealth.app.R
import org.intelehealth.app.activities.patientDetailActivity.StaticPatientRegistrationEnabledFieldsHelper
import org.intelehealth.app.databinding.FragmentBaselineSurveyOtherBinding
import org.intelehealth.app.ui.baseline_survey.model.Baseline
import org.intelehealth.app.utilities.ArrayAdapterUtils
import org.intelehealth.app.utilities.BaselineSurveyStage
import org.intelehealth.app.utilities.LanguageUtils
import org.intelehealth.app.utilities.PatientRegFieldsUtils
import org.intelehealth.app.utilities.extensions.getSelectedData
import org.intelehealth.app.utilities.extensions.hideError
import org.intelehealth.app.utilities.extensions.validate
import org.intelehealth.app.utilities.extensions.validateCheckboxes
import org.intelehealth.app.utilities.extensions.validateDropDowb

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

    override fun onBaselineDataLoaded(baselineData: Baseline) {
        super.onBaselineDataLoaded(baselineData)
        fetchOtherBaselineConfig()
        binding.baseline = baselineData
        binding.baselineEditMode = baselineSurveyViewModel.baselineEditMode
    }

    private fun fetchOtherBaselineConfig() {
        val it = getStaticPatientRegistrationFields()
        binding.otherConfig = PatientRegFieldsUtils.buildOtherBaselineConfig(it)
        setValues()
        setClickListener()
    }

    private fun getStaticPatientRegistrationFields() =
        StaticPatientRegistrationEnabledFieldsHelper.getEnabledOtherBaselineFields()

    private fun setValues() {
        setupHohCheck()
        setupSourceOfWater()
        setupSourceOfLight()
        setupFuelType()
        setupHandWashPractice()
        setupWaterSafeguarding()
        setupEconomicStatus()
        setupReligion()
        setupNumberOfToiletFacilities()
        setupHouseStructure()
        setupCultivableLand()
    }

    private fun setupHohCheck() {
        binding.rgHOHOptions.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                binding.radioHOHYes.id -> {
                    binding.llHohYes.visibility = View.VISIBLE
                    binding.llRelationWithHOH.visibility = View.GONE
                }

                else -> {
                    binding.llHohYes.visibility = View.GONE
                    binding.llRelationWithHOH.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setupSourceOfWater() {
        val mItems = resources.getStringArray(R.array.source_of_water).toList()
        createCheckboxes(binding.cgSourceOfWater, mItems)
    }

    private fun setupSourceOfLight() {
        val mItems = resources.getStringArray(R.array.source_of_light).toList()
        createCheckboxes(binding.cgSourceOfLight, mItems)
    }

    private fun setupFuelType() {
        val mItems = resources.getStringArray(R.array.fuel_type).toList()
        createCheckboxes(binding.cgFuelType, mItems)
    }

    private fun setupHandWashPractice() {
        val mItems = resources.getStringArray(R.array.hand_wash_practice).toList()
        createCheckboxes(binding.cgHandWashPractices, mItems)
    }

    private fun setupWaterSafeguarding() {
        val mItems = resources.getStringArray(R.array.safeguard_water).toList()
        createCheckboxes(binding.cgSafeguardWater, mItems)
    }

    private fun createCheckboxes(container: LinearLayout, items: List<String>) {
        for (item in items) {
            val checkBox = CheckBox(requireContext())
            checkBox.text = item
            container.addView(checkBox)
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                println("$item is checked: $isChecked")
            }
        }
    }

    private fun setupEconomicStatus() {
        val adapter = ArrayAdapterUtils.getArrayAdapter(requireContext(), R.array.economic)
        binding.acEconomicStatusCheck.setAdapter(adapter)

        binding.acEconomicStatusCheck.setOnItemClickListener { _, _, i, _ ->
            binding.tilEconomicStatusOption.hideError()
            LanguageUtils.getSpecificLocalResource(requireContext(), "en").apply {
                binding.acEconomicStatusCheck.setText(
                    this.getStringArray(R.array.hb_check)[i],
                    false
                )
            }
        }
    }

    private fun setupReligion() {
        val adapter = ArrayAdapterUtils.getArrayAdapter(requireContext(), R.array.baseline_religion)
        binding.acReligion.setAdapter(adapter)

        binding.acReligion.setOnItemClickListener { _, _, i, _ ->
            binding.tilReligionOption.hideError()
            LanguageUtils.getSpecificLocalResource(requireContext(), "en").apply {
                binding.acReligion.setText(this.getStringArray(R.array.hb_check)[i], false)
            }
        }
    }

    private fun setupNumberOfToiletFacilities() {
        val adapter = ArrayAdapterUtils.getArrayAdapter(
            requireContext(),
            R.array.baseline_toilet_facilities
        )

        binding.acToiletFacility.setAdapter(adapter)

        binding.acToiletFacility.setOnItemClickListener { _, _, i, _ ->
            binding.tilToiletFacilityOption.hideError()
            LanguageUtils.getSpecificLocalResource(requireContext(), "en").apply {
                binding.acToiletFacility.setText(this.getStringArray(R.array.hb_check)[i], false)
            }
        }
    }

    private fun setupHouseStructure() {
        val adapter =
            ArrayAdapterUtils.getArrayAdapter(requireContext(), R.array.baseline_house_structure)
        binding.acHouseStructure.setAdapter(adapter)

        binding.acHouseStructure.setOnItemClickListener { _, _, i, _ ->
            binding.tilHouseStructureOption.hideError()
            LanguageUtils.getSpecificLocalResource(requireContext(), "en").apply {
                binding.acHouseStructure.setText(this.getStringArray(R.array.hb_check)[i], false)
            }
        }
    }

    private fun setupCultivableLand() {
        val adapter = ArrayAdapterUtils.getArrayAdapter(
            requireContext(),
            R.array.baseline_cultivable_land
        )
        
        binding.acCultivableLand.setAdapter(adapter)

        binding.acCultivableLand.setOnItemClickListener { _, _, i, _ ->
            binding.tilCultivableLandOption.hideError()
            LanguageUtils.getSpecificLocalResource(requireContext(), "en").apply {
                binding.acCultivableLand.setText(this.getStringArray(R.array.hb_check)[i], false)
            }
        }
    }

    private fun setClickListener() {
        binding.frag3BtnBack.setOnClickListener {
            BaselineOtherFragmentDirections.navigationOtherToMedical().apply {
                findNavController().navigate(this)
            }
        }
        binding.frag3BtnNext.setOnClickListener {
            validateFields { saveSurveyData() }
        }
    }

    private fun validateFields(block: () -> Unit) {
        val error = R.string.this_field_is_mandatory
        val isHeadOfHousehold = binding.llHohYes.isVisible

        binding.otherConfig?.let {
            val headOfHousehold =
                if (it.headOfHousehold!!.isEnabled && it.headOfHousehold!!.isMandatory) {
                    binding.rgHOHOptions.validate()
                } else true

            val rationCardCheck =
                if (it.rationCardCheck!!.isEnabled && it.rationCardCheck!!.isMandatory && isHeadOfHousehold) {
                    binding.rgRationOptions.validate()
                } else true

            val economicStatus =
                if (it.economicStatus!!.isEnabled && it.economicStatus!!.isMandatory && isHeadOfHousehold) {
                    binding.tilEconomicStatusOption.validateDropDowb(
                        binding.acEconomicStatusCheck,
                        error
                    )
                } else true

            val religion =
                if (it.religion!!.isEnabled && it.religion!!.isMandatory && isHeadOfHousehold) {
                    binding.tilReligionOption.validateDropDowb(binding.acReligion, error)
                } else true

            val totalHouseholdMembers =
                if (it.totalHouseholdMembers!!.isEnabled && it.totalHouseholdMembers!!.isMandatory && isHeadOfHousehold) {
                    binding.tilTotalMemberOption.validate(binding.textInputTotalHHMembers, error)
                } else true

            val usualHouseholdMembers =
                if (it.usualHouseholdMembers!!.isEnabled && it.usualHouseholdMembers!!.isMandatory && isHeadOfHousehold) {
                    binding.tilUsualMemberOption.validate(binding.textInputUsualHHMembers, error)
                } else true

            val numberOfSmartphones =
                if (it.numberOfSmartphones!!.isEnabled && it.numberOfSmartphones!!.isMandatory && isHeadOfHousehold) {
                    binding.tilNumberOfSmartphonesOption.validate(
                        binding.textInputNoOfSmartPhones,
                        error
                    )
                } else true

            val numberOfFeaturePhones =
                if (it.numberOfFeaturePhones!!.isEnabled && it.numberOfFeaturePhones!!.isMandatory && isHeadOfHousehold) {
                    binding.tilNumberOfFeaturePhoneOption.validate(
                        binding.textInputNoOfFeaturePhones,
                        error
                    )
                } else true

            val numberOfEarningMembers =
                if (it.numberOfEarningMembers!!.isEnabled && it.numberOfEarningMembers!!.isMandatory && isHeadOfHousehold) {
                    binding.tilNumberOfEarningMembersOption.validate(
                        binding.textInputEarningMembers,
                        error
                    )
                } else true

            val electricityCheck =
                if (it.electricityCheck!!.isEnabled && it.electricityCheck!!.isMandatory && isHeadOfHousehold) {
                    binding.rgElectricityOptions.validate()
                } else true

            val waterCheck =
                if (it.waterCheck!!.isEnabled && it.waterCheck!!.isMandatory && isHeadOfHousehold) {
                    binding.rgWaterCheckOptions.validate()
                } else true

            val sourceOfWater =
                if (it.sourceOfWater!!.isEnabled && it.sourceOfWater!!.isMandatory && isHeadOfHousehold) {
                    binding.cgSourceOfWater.validateCheckboxes()
                } else true

            val safeguardWater =
                if (it.safeguardWater!!.isEnabled && it.sourceOfWater!!.isMandatory && isHeadOfHousehold) {
                    binding.cgSafeguardWater.validateCheckboxes()
                } else true

            val distanceFromWater =
                if (it.distanceFromWater!!.isEnabled && it.distanceFromWater!!.isMandatory && isHeadOfHousehold) {
                    binding.rgDistanceFromWaterOptions.validate()
                } else true

            val toiletFacility =
                if (it.toiletFacility!!.isEnabled && it.toiletFacility!!.isMandatory && isHeadOfHousehold) {
                    binding.tilToiletFacilityOption.validateDropDowb(
                        binding.acToiletFacility,
                        error
                    )
                } else true

            val houseStructure =
                if (it.houseStructure!!.isEnabled && it.houseStructure!!.isMandatory && isHeadOfHousehold) {
                    binding.tilHouseStructureOption.validateDropDowb(
                        binding.acHouseStructure,
                        error
                    )
                } else true

            val cultivableLand =
                if (it.cultivableLand!!.isEnabled && it.cultivableLand!!.isMandatory && isHeadOfHousehold) {
                    binding.tilCultivableLandOption.validateDropDowb(
                        binding.acHouseStructure,
                        error
                    )
                } else true

            val averageIncome =
                if (it.averageIncome!!.isEnabled && it.averageIncome!!.isMandatory && isHeadOfHousehold) {
                    binding.rgAverageIncomeOptions.validate()
                } else true

            val fuelType =
                if (it.fuelType!!.isEnabled && it.fuelType!!.isMandatory && isHeadOfHousehold) {
                    binding.cgFuelType.validateCheckboxes()
                } else true

            val sourceOfLight =
                if (it.sourceOfLight!!.isEnabled && it.sourceOfLight!!.isMandatory && isHeadOfHousehold) {
                    binding.cgSourceOfLight.validateCheckboxes()
                } else true

            val handWashPractices =
                if (it.handWashPractices!!.isEnabled && it.handWashPractices!!.isMandatory && isHeadOfHousehold) {
                    binding.cgHandWashPractices.validateCheckboxes()
                } else true

            val ekalServiceCheck =
                if (it.ekalServiceCheck!!.isEnabled && it.ekalServiceCheck!!.isMandatory && isHeadOfHousehold) {
                    binding.rgEkalServiceCheckOptions.validate()
                } else true

            val relationWithHousehold =
                if (it.relationWithHousehold!!.isEnabled && it.relationWithHousehold!!.isMandatory && !isHeadOfHousehold) {
                    binding.rgRelationWithHohOptions.validate()
                } else true


            if (headOfHousehold.and(rationCardCheck).and(rationCardCheck).and(economicStatus)
                    .and(religion).and(totalHouseholdMembers).and(usualHouseholdMembers)
                    .and(numberOfSmartphones).and(numberOfFeaturePhones).and(numberOfEarningMembers)
                    .and(electricityCheck).and(waterCheck).and(sourceOfWater).and(safeguardWater)
                    .and(distanceFromWater).and(toiletFacility).and(houseStructure)
                    .and(cultivableLand).and(averageIncome).and(fuelType).and(sourceOfLight)
                    .and(handWashPractices).and(ekalServiceCheck).and(relationWithHousehold)
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

    private fun saveSurveyData() {
        val isHeadOfHousehold = binding.llHohYes.isVisible

        baselineSurveyData.apply {
            headOfHousehold = binding.rgHOHOptions.getSelectedData()

            if (!isHeadOfHousehold) {
                relationWithHousehold = binding.rgRelationWithHohOptions.getSelectedData()
            } else {
                rationCardCheck = binding.rgRationOptions.getSelectedData()
                economicStatus = binding.acEconomicStatusCheck.text.toString()
                religion = binding.acReligion.text.toString()
                totalHouseholdMembers = binding.textInputTotalHHMembers.text.toString()
                usualHouseholdMembers = binding.textInputUsualHHMembers.text.toString()
                numberOfSmartphones = binding.textInputNoOfSmartPhones.text.toString()
                numberOfFeaturePhones = binding.textInputNoOfFeaturePhones.text.toString()
                numberOfEarningMembers = binding.textInputEarningMembers.text.toString()
                electricityCheck = binding.rgElectricityOptions.getSelectedData()
                waterCheck = binding.rgWaterCheckOptions.getSelectedData()
//                sourceOfWater = to be done
//                safeguardWater = to be done
                distanceFromWater = binding.rgDistanceFromWaterOptions.getSelectedData()
                toiletFacility = binding.acToiletFacility.text.toString()
                houseStructure = binding.acHouseStructure.text.toString()
                cultivableLand = binding.acCultivableLand.text.toString()
                averageIncome = binding.rgAverageIncomeOptions.getSelectedData()
//                fuelType = binding.cgFuelType - to be done
//                sourceOfLight = binding.cgSourceOfLight - to be done
//                handWashPractices = binding.cgHandWashPractices - to be done
                ekalServiceCheck = binding.rgEkalServiceCheckOptions.getSelectedData()

                baselineSurveyViewModel.updateBaselineData(this)
            }
        }
    }

    private fun navigateToPatientDetailsScreen() {
        BaselineOtherFragmentDirections.navigationOtherToPatientDetails(
            patient.uuid, "searchPatient", "false"
        ).also {
            findNavController().navigate(it)
            requireActivity().finish()
        }
    }

}