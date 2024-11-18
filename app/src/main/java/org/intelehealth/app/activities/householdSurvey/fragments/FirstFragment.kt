package org.intelehealth.app.activities.householdSurvey.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentOneHouseholdSurveyBinding
import org.intelehealth.app.ui.patient.fragment.PatientAddressInfoFragmentDirections
import org.intelehealth.app.utilities.HouseholdSurveyStage
import org.intelehealth.app.utilities.PatientRegStage
import org.intelehealth.core.registry.PermissionRegistry
import java.util.Calendar

class FirstFragment : BaseHouseholdSurveyFragment(R.layout.fragment_one_household_survey) {

    private lateinit var binding: FragmentOneHouseholdSurveyBinding
    var selectedDate = Calendar.getInstance().timeInMillis
    private val permissionRegistry by lazy {
        PermissionRegistry(requireContext(), requireActivity().activityResultRegistry)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentOneHouseholdSurveyBinding.bind(view)
        houseHoldViewModel.updatePatientStage(HouseholdSurveyStage.FIRST_SCREEN)
        setClickListener()

    }

    private fun setClickListener() {
        binding.btnFirstFragNext.setOnClickListener {
            FirstFragmentDirections.actionOneToTwo().apply {
                findNavController().navigate(this)
            }
        }
    }

}