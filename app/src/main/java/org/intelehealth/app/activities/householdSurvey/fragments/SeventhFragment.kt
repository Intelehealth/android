package org.intelehealth.app.activities.householdSurvey.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.datatransport.runtime.firebase.transport.LogEventDropped
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentSeventhHouseholdSurveyBinding
import org.intelehealth.app.ui.patient.fragment.PatientAddressInfoFragmentDirections
import org.intelehealth.app.utilities.HouseholdSurveyStage
import org.intelehealth.app.utilities.PatientRegStage
import org.intelehealth.core.registry.PermissionRegistry
import java.util.Calendar

class SeventhFragment : BaseHouseholdSurveyFragment(R.layout.fragment_seventh_household_survey) {

    private lateinit var binding: FragmentSeventhHouseholdSurveyBinding
    var selectedDate = Calendar.getInstance().timeInMillis
    private val permissionRegistry by lazy {
        PermissionRegistry(requireContext(), requireActivity().activityResultRegistry)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSeventhHouseholdSurveyBinding.bind(view)
        houseHoldViewModel.updatePatientStage(HouseholdSurveyStage.SEVENTH_SCREEN)
        setClickListener()
    }

    private fun setClickListener() {
        binding.frag2BtnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.frag2BtnNext.setOnClickListener {
            Log.d("householdnewflow", "setClickListener: kaveri last 7 frag")
        }
    }
}