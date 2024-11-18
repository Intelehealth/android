package org.intelehealth.app.activities.householdSurvey.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentFifthHouseholdSurveyBinding
import org.intelehealth.app.ui.patient.fragment.PatientAddressInfoFragmentDirections
import org.intelehealth.app.utilities.HouseholdSurveyStage
import org.intelehealth.app.utilities.PatientRegStage
import org.intelehealth.core.registry.PermissionRegistry
import java.util.Calendar

class FifthFragment : BaseHouseholdSurveyFragment(R.layout.fragment_fifth_household_survey) {

    private lateinit var binding: FragmentFifthHouseholdSurveyBinding
    var selectedDate = Calendar.getInstance().timeInMillis
    private val permissionRegistry by lazy {
        PermissionRegistry(requireContext(), requireActivity().activityResultRegistry)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFifthHouseholdSurveyBinding.bind(view)
        houseHoldViewModel.updatePatientStage(HouseholdSurveyStage.FIFTH_SCREEN)
        setClickListener()
    }
    private fun setClickListener() {
        binding.frag2BtnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.frag2BtnNext.setOnClickListener {
            FifthFragmentDirections.actionFiveToSix().apply {
                findNavController().navigate(this)
            }
        }
    }
}