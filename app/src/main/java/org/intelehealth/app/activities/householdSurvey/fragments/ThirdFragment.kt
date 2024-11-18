package org.intelehealth.app.activities.householdSurvey.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentThirdHouseholdSurveyBinding
import org.intelehealth.app.ui.patient.fragment.PatientAddressInfoFragmentDirections
import org.intelehealth.app.utilities.HouseholdSurveyStage
import org.intelehealth.app.utilities.PatientRegStage
import org.intelehealth.core.registry.PermissionRegistry
import java.util.Calendar

class ThirdFragment : BaseHouseholdSurveyFragment(R.layout.fragment_third_household_survey) {

    private lateinit var binding: FragmentThirdHouseholdSurveyBinding
    var selectedDate = Calendar.getInstance().timeInMillis
    private val permissionRegistry by lazy {
        PermissionRegistry(requireContext(), requireActivity().activityResultRegistry)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentThirdHouseholdSurveyBinding.bind(view)
        houseHoldViewModel.updatePatientStage(HouseholdSurveyStage.THIRD_SCREEN)
        setClickListener()
    }
    private fun setClickListener() {
        binding.frag2BtnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.frag2BtnNext.setOnClickListener {
            ThirdFragmentDirections.actionThreeToFour().apply {
                findNavController().navigate(this)
            }
        }
    }
}