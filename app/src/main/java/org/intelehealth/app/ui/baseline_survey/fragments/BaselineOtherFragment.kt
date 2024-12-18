package org.intelehealth.app.ui.baseline_survey.fragments

import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import org.intelehealth.app.R
import org.intelehealth.app.activities.patientDetailActivity.StaticPatientRegistrationEnabledFieldsHelper
import org.intelehealth.app.databinding.FragmentBaselineSurveyOtherBinding
import org.intelehealth.app.ui.baseline_survey.constants.Constants
import org.intelehealth.app.ui.baseline_survey.model.Baseline
import org.intelehealth.app.utilities.ArrayAdapterUtils
import org.intelehealth.app.utilities.BaselineSurveyStage
import org.intelehealth.app.utilities.LanguageUtils
import org.intelehealth.app.utilities.PatientRegFieldsUtils
import org.intelehealth.app.utilities.extensions.getSelectedCheckboxes
import org.intelehealth.app.utilities.extensions.getSelectedData
import org.intelehealth.app.utilities.extensions.getTextIfVisible
import org.intelehealth.app.utilities.extensions.hideError
import org.intelehealth.app.utilities.extensions.hideErrorOnTextChang
import org.intelehealth.app.utilities.extensions.validate
import org.intelehealth.app.utilities.extensions.validateCheckboxes
import org.intelehealth.app.utilities.extensions.validateDropDowb
import org.intelehealth.app.utilities.extensions.validateIntegerDataLimits
import org.intelehealth.app.utilities.extensions.validateNumberOfUsualMembers

/**
 * Created by Shazzad H Kanon on 06-12-2024 - 11:00.
 * Email : shazzad@intelehealth.org
 * Mob   : +8801647040520
 **/
class BaselineOtherFragment : BaseFragmentBaselineSurvey(R.layout.fragment_baseline_survey_other) {

    private lateinit var binding: FragmentBaselineSurveyOtherBinding

    private var isLandlessOptionChosen: Boolean = false

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
        setupElectricityCheck()
        setUpWaterCheck()
        setupNumberOfToiletFacilities()
        setupHouseStructure()
        setupCultivableLand()
        setOnTextChangeListener()
    }

    private fun setOnTextChangeListener() {
        binding.tilTotalMemberOption.hideErrorOnTextChang(binding.textInputTotalHHMembers)
        binding.tilUsualMemberOption.hideErrorOnTextChang(binding.textInputUsualHHMembers)
        binding.tilNumberOfSmartphonesOption.hideErrorOnTextChang(binding.textInputNoOfSmartPhones)
        binding.tilNumberOfFeaturePhoneOption.hideErrorOnTextChang(binding.textInputNoOfFeaturePhones)
        binding.tilNumberOfEarningMembersOption.hideErrorOnTextChang(binding.textInputEarningMembers)
        binding.tilLoadShedingHoursOption.hideErrorOnTextChang(binding.textInputloadSheddingHours)
        binding.tilLoadShedingDaysOption.hideErrorOnTextChang(binding.textInputloadSheddingDays)
        binding.tilWaterAvailabilityHoursOption.hideErrorOnTextChang(binding.textInputWaterAvailabilityHours)
        binding.tilWaterAvailabilityDaysOption.hideErrorOnTextChang(binding.textInputWaterAvailabilityDays)
        binding.tilCultivableLandValue.hideErrorOnTextChang(binding.textInputCultivableLandValue)
    }

    private fun setUpWaterCheck() {
        binding.rgWaterCheckOptions.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                binding.radioWaterCheckYes.id -> {
                    binding.llWaterAvailability.visibility = View.VISIBLE
                }

                else -> {
                    binding.llWaterAvailability.visibility = View.GONE
                }
            }
        }
    }

    private fun setupElectricityCheck() {
        binding.rgElectricityOptions.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                binding.radioElectricityYes.id -> {
                    binding.llLoadShedding.visibility = View.VISIBLE
                }

                else -> {
                    binding.llLoadShedding.visibility = View.GONE
                }
            }
        }
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
                    this.getStringArray(R.array.economic)[i],
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
                binding.acReligion.setText(this.getStringArray(R.array.baseline_religion)[i], false)
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
                binding.acToiletFacility.setText(
                    this.getStringArray(R.array.baseline_toilet_facilities)[i],
                    false
                )
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
                binding.acHouseStructure.setText(
                    this.getStringArray(R.array.baseline_house_structure)[i],
                    false
                )
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
                binding.acCultivableLand.setText(
                    this.getStringArray(R.array.baseline_cultivable_land)[i],
                    false
                )
            }
        }

        binding.acCultivableLand.doOnTextChanged { text, start, before, count ->
            val value = getString(R.string.cultivable_land_landless)
            if (text?.isEmpty() == true || value == text.toString()) {
                binding.tilCultivableLandValue.visibility = View.GONE
                isLandlessOptionChosen = true
            } else {
                binding.tilCultivableLandValue.visibility = View.VISIBLE
                isLandlessOptionChosen = false
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
        val isHeadOfHousehold = binding.llHohYes.isVisible
        val isElectricityAvailable = binding.radioElectricityYes.isChecked
        val isRunningWaterAvailable = binding.radioWaterCheckYes.isChecked

        val error = R.string.this_field_is_mandatory
        val usualMembersError =
            R.string.error_number_of_people_living_cannot_be_greater_than_the_total_number_of_members_in_the_household
        val hoursError = R.string.load_shedding_hours_error
        val daysError = R.string.load_shedding_days_error

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
                    binding.tilUsualMemberOption.validateNumberOfUsualMembers(
                        binding.textInputUsualHHMembers,
                        binding.textInputTotalHHMembers,
                        usualMembersError
                    )
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

            val loadSheddingHours =
                if (it.loadSheddingHours!!.isEnabled && it.loadSheddingHours!!.isMandatory && isHeadOfHousehold && isElectricityAvailable) {
                    binding.tilLoadShedingHoursOption.validate(
                        binding.textInputloadSheddingHours,
                        error
                    )
                    binding.tilLoadShedingHoursOption.validateIntegerDataLimits(
                        binding.textInputloadSheddingHours,
                        Constants.LIMIT_START_HOURS,
                        Constants.LIMIT_END_HOURS,
                        hoursError
                    )
                } else true

            val loadSheddingDays =
                if (it.loadSheddingDays!!.isEnabled && it.loadSheddingDays!!.isMandatory && isHeadOfHousehold && isElectricityAvailable) {
                    binding.tilLoadShedingDaysOption.validate(
                        binding.textInputloadSheddingDays,
                        error
                    )

                    binding.tilLoadShedingDaysOption.validateIntegerDataLimits(
                        binding.textInputloadSheddingDays,
                        Constants.LIMIT_START_DAY,
                        Constants.LIMIT_END_DAY,
                        daysError
                    )
                } else true

            val waterCheck =
                if (it.waterCheck!!.isEnabled && it.waterCheck!!.isMandatory && isHeadOfHousehold) {
                    binding.rgWaterCheckOptions.validate()
                } else true

            val waterAvailabilityHours =
                if (it.waterAvailabilityHours!!.isEnabled && it.waterAvailabilityHours!!.isMandatory && isHeadOfHousehold && isRunningWaterAvailable) {
                    binding.tilWaterAvailabilityHoursOption.validate(
                        binding.textInputWaterAvailabilityHours,
                        error
                    )
                    binding.tilWaterAvailabilityHoursOption.validateIntegerDataLimits(
                        binding.textInputWaterAvailabilityHours,
                        Constants.LIMIT_START_HOURS,
                        Constants.LIMIT_END_HOURS,
                        hoursError
                    )
                } else true

            val waterAvailabilityDays =
                if (it.waterAvailabilityDays!!.isEnabled && it.waterAvailabilityDays!!.isMandatory && isHeadOfHousehold && isRunningWaterAvailable) {
                    binding.tilWaterAvailabilityDaysOption.validate(
                        binding.textInputWaterAvailabilityDays,
                        error
                    )

                    binding.tilWaterAvailabilityDaysOption.validateIntegerDataLimits(
                        binding.textInputWaterAvailabilityDays,
                        Constants.LIMIT_START_DAY,
                        Constants.LIMIT_END_DAY,
                        daysError
                    )
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
                        binding.acCultivableLand,
                        error
                    )
                } else true

            val cultivableLandValue =
                if (it.cultivableLandValue!!.isEnabled && it.cultivableLandValue!!.isMandatory && isHeadOfHousehold && !isLandlessOptionChosen) {
                    binding.tilCultivableLandValue.validate(
                        binding.textInputCultivableLandValue,
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
                    .and(electricityCheck).and(loadSheddingHours).and(loadSheddingDays)
                    .and(waterCheck).and(waterAvailabilityHours).and(waterAvailabilityDays)
                    .and(sourceOfWater).and(safeguardWater).and(distanceFromWater)
                    .and(toiletFacility).and(houseStructure).and(cultivableLand)
                    .and(cultivableLandValue).and(averageIncome).and(fuelType).and(sourceOfLight)
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
                loadSheddingHours =
                    binding.llLoadShedding.getTextIfVisible(binding.textInputloadSheddingHours)
                loadSheddingDays =
                    binding.llLoadShedding.getTextIfVisible(binding.textInputloadSheddingDays)

                waterCheck = binding.rgWaterCheckOptions.getSelectedData()
                waterAvailabilityHours =
                    binding.llWaterAvailability.getTextIfVisible(binding.textInputWaterAvailabilityHours)
                waterAvailabilityDays =
                    binding.llWaterAvailability.getTextIfVisible(binding.textInputWaterAvailabilityDays)

                sourceOfWater = binding.cgSourceOfWater.getSelectedCheckboxes()
                safeguardWater = binding.cgSafeguardWater.getSelectedCheckboxes()

                distanceFromWater = binding.rgDistanceFromWaterOptions.getSelectedData()
                toiletFacility = binding.acToiletFacility.text.toString()
                houseStructure = binding.acHouseStructure.text.toString()

                cultivableLand = binding.acCultivableLand.text.toString()
                cultivableLandValue =
                    binding.tilCultivableLandValue.getTextIfVisible(binding.textInputCultivableLandValue)

                averageIncome = binding.rgAverageIncomeOptions.getSelectedData()
                fuelType = binding.cgFuelType.getSelectedCheckboxes()
                sourceOfLight = binding.cgSourceOfLight.getSelectedCheckboxes()
                handWashPractices = binding.cgHandWashPractices.getSelectedCheckboxes()
                ekalServiceCheck = binding.rgEkalServiceCheckOptions.getSelectedData()
            }

            baselineSurveyViewModel.updateBaselineData(this)
            baselineSurveyViewModel.savePatient().observe(viewLifecycleOwner) {
                it ?: return@observe
                baselineSurveyViewModel.handleResponse(it) { result -> if (result) navigateToPatientDetailsScreen() }
            }
        }
    }

    private fun navigateToPatientDetailsScreen() {
        BaselineOtherFragmentDirections.navigationOtherToPatientDetails(
            baselineSurveyViewModel.patientId, "searchPatient", "false"
        ).also {
            findNavController().navigate(it)
            requireActivity().finish()
        }
    }
}