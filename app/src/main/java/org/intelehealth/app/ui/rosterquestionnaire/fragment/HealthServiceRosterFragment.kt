package org.intelehealth.app.ui.rosterquestionnaire.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentHealthServiceRosterBinding
import org.intelehealth.app.ui.rosterquestionnaire.utilities.RosterQuestionnaireStage

class HealthServiceRosterFragment : BaseRosterFragment(R.layout.fragment_health_service_roster) {
    private lateinit var binding: FragmentHealthServiceRosterBinding
    private var patientUuid: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHealthServiceRosterBinding.bind(view)
        rosterViewModel.updateRosterStage(RosterQuestionnaireStage.HEALTH_SERVICE)

        initViews()
        clickListeners()
    }

    private fun clickListeners() {
        binding.frag2BtnNext.setOnClickListener {
            //for now only UI is there hence navigated directly
            navigateToDetails()
        }
        binding.frag2BtnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun navigateToDetails() {
        //patient.uuid - for now its hardcoded
        HealthServiceRosterFragmentDirections.navigationHealthServiceToDetails(
            patientUuid,
            "reg",
            "false"
        ).apply {
            findNavController().navigate(this)
            requireActivity().finish()
        }
    }

    private fun initViews() {
        val intent = requireActivity().intent
        if (intent != null) {
            patientUuid = intent.getStringExtra("patientUuid")
        }
    }
}
