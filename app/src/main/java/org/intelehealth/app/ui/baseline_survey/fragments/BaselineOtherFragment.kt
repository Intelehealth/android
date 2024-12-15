package org.intelehealth.app.ui.baseline_survey.fragments

import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.LinearLayout
import androidx.navigation.fragment.findNavController
import com.github.ajalt.timberkt.Timber
import com.google.gson.Gson
import org.intelehealth.app.R
import org.intelehealth.app.activities.patientDetailActivity.StaticPatientRegistrationEnabledFieldsHelper
import org.intelehealth.app.databinding.FragmentBaselineSurveyOtherBinding
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
        fetchOtherBaselineConfig()
        binding.patient = patient
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
        setupHouseHead()
        setupRationCard()
        setupRelationWithHOH()
        setupElectricityCheck()
        setupWaterCheck()
        setupDistanceFromWater()
        setupAverageIncome()
        setupEkalServiceCheck()
        setupEkalServiceCheck()
        setupEconomicStatus()
        setupReligion()
        setupNumberOfToiletFacilities()
        setupHouseStructure()
        setupCultivableLand()
    }

    private fun setupHohCheck() {
        binding.rgHOHOptions.setOnCheckedChangeListener { _, checkedId ->
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
        val adapter = ArrayAdapterUtils.getArrayAdapter(requireContext(), R.array.hb_check)
        binding.acEconomicStatusCheck.setAdapter(adapter)
        binding.acEconomicStatusCheck.setText("Select your choice", false)

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
        val adapter = ArrayAdapterUtils.getArrayAdapter(requireContext(), R.array.hb_check)
        binding.acReligion.setAdapter(adapter)
        binding.acReligion.setText("Select your choice", false)

        binding.acReligion.setOnItemClickListener { _, _, i, _ ->
            binding.tilReligionOption.hideError()
            LanguageUtils.getSpecificLocalResource(requireContext(), "en").apply {
                binding.acReligion.setText(this.getStringArray(R.array.hb_check)[i], false)
            }
        }
    }

    private fun setupNumberOfToiletFacilities() {
        val adapter = ArrayAdapterUtils.getArrayAdapter(requireContext(), R.array.hb_check)
        binding.acToiletFacility.setAdapter(adapter)
        binding.acToiletFacility.setText("Select your choice", false)

        binding.acToiletFacility.setOnItemClickListener { _, _, i, _ ->
            binding.tilToiletFacilityOption.hideError()
            LanguageUtils.getSpecificLocalResource(requireContext(), "en").apply {
                binding.acToiletFacility.setText(this.getStringArray(R.array.hb_check)[i], false)
            }
        }
    }


    private fun setupHouseStructure() {
        val adapter = ArrayAdapterUtils.getArrayAdapter(requireContext(), R.array.hb_check)
        binding.acHouseStructure.setAdapter(adapter)
        binding.acHouseStructure.setText("Select your choice", false)

        binding.acHouseStructure.setOnItemClickListener { _, _, i, _ ->
            binding.tilHouseStructureOption.hideError()
            LanguageUtils.getSpecificLocalResource(requireContext(), "en").apply {
                binding.acHouseStructure.setText(this.getStringArray(R.array.hb_check)[i], false)
            }
        }
    }

    private fun setupCultivableLand() {
        val adapter = ArrayAdapterUtils.getArrayAdapter(requireContext(), R.array.hb_check)
        binding.acCultivableLand.setAdapter(adapter)
        binding.acCultivableLand.setText("Select your choice", false)

        binding.acCultivableLand.setOnItemClickListener { _, _, i, _ ->
            binding.tilCultivableLandOption.hideError()
            LanguageUtils.getSpecificLocalResource(requireContext(), "en").apply {
                binding.acCultivableLand.setText(this.getStringArray(R.array.hb_check)[i], false)
            }
        }
    }


    private fun setupHouseHead() {
        binding.rgHOHOptions.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioHOHYes -> {
                    binding.rgHOHOptions.check(R.id.radioHOHYes)
                }

                R.id.radioHOHNo -> {
                    binding.rgHOHOptions.check(R.id.radioHOHNo)
                }
            }
        }
    }

    private fun setupRationCard() {
        binding.rgRationOptions.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioRationYes -> {
                    binding.rgHOHOptions.check(R.id.radioRationYes)
                }

                R.id.radioRationNo -> {
                    binding.rgHOHOptions.check(R.id.radioRationNo)
                }

                R.id.radioRationNotSure -> {
                    binding.rgHOHOptions.check(R.id.radioRationNotSure)
                }
            }
        }
    }

    private fun setupRelationWithHOH() {
        binding.rgRelationWithHohOptions.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioSpouse -> {
                    binding.rgHOHOptions.check(R.id.radioSpouse)
                }

                R.id.radioGrandchild -> {
                    binding.rgHOHOptions.check(R.id.radioGrandchild)
                }

                R.id.radioFatherMother -> {
                    binding.rgHOHOptions.check(R.id.radioFatherMother)
                }

                R.id.radioFatherMotherInLaw -> {
                    binding.rgHOHOptions.check(R.id.radioFatherMotherInLaw)
                }

                R.id.radioBrotherSister -> {
                    binding.rgHOHOptions.check(R.id.radioBrotherSister)
                }

                R.id.radioBrotherSisterInLaw -> {
                    binding.rgHOHOptions.check(R.id.radioBrotherSisterInLaw)
                }

                R.id.radioNieceNephew -> {
                    binding.rgHOHOptions.check(R.id.radioNieceNephew)
                }

                R.id.radioGrandparent -> {
                    binding.rgHOHOptions.check(R.id.radioGrandparent)
                }

                R.id.radioOther -> {
                    binding.rgHOHOptions.check(R.id.radioOther)
                }

                R.id.radioAdopted -> {
                    binding.rgHOHOptions.check(R.id.radioAdopted)
                }

                R.id.radioServant -> {
                    binding.rgHOHOptions.check(R.id.radioServant)
                }

                R.id.radioOtherNotRelated -> {
                    binding.rgHOHOptions.check(R.id.radioOtherNotRelated)
                }

                R.id.radioOtherSpecify -> {
                    binding.rgHOHOptions.check(R.id.radioOtherSpecify)
                }

                R.id.radioNotStated -> {
                    binding.rgHOHOptions.check(R.id.radioNotStated)
                }


            }
        }
    }

    private fun setupElectricityCheck() {
        binding.rgElectricityOptions.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioElectricityYes -> {
                    binding.rgHOHOptions.check(R.id.radioElectricityYes)
                }

                R.id.radioElectricityNo -> {
                    binding.rgHOHOptions.check(R.id.radioElectricityNo)
                }
            }
        }
    }

    private fun setupWaterCheck() {
        binding.rgWaterCheckOptions.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioWaterCheckYes -> {
                    binding.rgHOHOptions.check(R.id.radioWaterCheckYes)
                }

                R.id.radioWaterCheckNo -> {
                    binding.rgHOHOptions.check(R.id.radioWaterCheckNo)
                }
            }
        }
    }

    private fun setupDistanceFromWater() {
        binding.rgDistanceFromWaterOptions.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioDistanceFromWaterYes -> {
                    binding.rgHOHOptions.check(R.id.radioDistanceFromWaterYes)
                }

                R.id.radioDistanceFromWaterNo -> {
                    binding.rgHOHOptions.check(R.id.radioDistanceFromWaterNo)
                }
            }
        }
    }

    private fun setupAverageIncome() {
        binding.rgAverageIncomeOptions.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioAverageIncome1 -> {
                    binding.rgHOHOptions.check(R.id.radioAverageIncome1)
                }

                R.id.radioAverageIncome2 -> {
                    binding.rgHOHOptions.check(R.id.radioAverageIncome2)
                }

                R.id.radioAverageIncome3 -> {
                    binding.rgHOHOptions.check(R.id.radioAverageIncome3)
                }

                R.id.radioAverageIncome4 -> {
                    binding.rgHOHOptions.check(R.id.radioAverageIncome4)
                }

                R.id.radioAverageIncome5 -> {
                    binding.rgHOHOptions.check(R.id.radioAverageIncome5)
                }
            }
        }
    }

    private fun setupEkalServiceCheck() {
        binding.rgEkalServiceCheckOptions.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioEkalServiceYes -> {
                    binding.rgHOHOptions.check(R.id.radioEkalServiceYes)
                }

                R.id.radioEkalServiceNo -> {
                    binding.rgHOHOptions.check(R.id.radioEkalServiceNo)
                }
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
            BaselineOtherFragmentDirections.navigationOtherToPatientDetails(
                patient.uuid, "searchPatient", "false"
            ).also {
                findNavController().navigate(it)
                requireActivity().finish()
            }
        }
    }

}