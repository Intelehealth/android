package org.intelehealth.app.activities.householdSurvey.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentSixthHouseholdSurveyBinding
import org.intelehealth.app.ui.patient.fragment.PatientAddressInfoFragmentDirections
import org.intelehealth.app.utilities.HouseholdSurveyStage
import org.intelehealth.app.utilities.PatientRegStage
import org.intelehealth.core.registry.PermissionRegistry
import java.util.Calendar

class SixthFragment : BaseHouseholdSurveyFragment(R.layout.fragment_sixth_household_survey) {

    private lateinit var binding: FragmentSixthHouseholdSurveyBinding
    var selectedDate = Calendar.getInstance().timeInMillis
    private val permissionRegistry by lazy {
        PermissionRegistry(requireContext(), requireActivity().activityResultRegistry)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSixthHouseholdSurveyBinding.bind(view)
        houseHoldViewModel.updatePatientStage(HouseholdSurveyStage.SIXTH_SCREEN)
        setClickListener()
    }
    private fun setClickListener() {
        binding.frag2BtnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.frag2BtnNext.setOnClickListener {
            SixthFragmentDirections.actionSixToSeven().apply {
                findNavController().navigate(this)
            }
        }
    }
}