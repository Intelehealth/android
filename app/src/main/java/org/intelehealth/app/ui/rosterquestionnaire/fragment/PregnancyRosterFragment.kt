package org.intelehealth.app.ui.rosterquestionnaire.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentPregnancyRosterBinding
import org.intelehealth.app.ui.rosterquestionnaire.utilities.RosterQuestionnaireStage

class PregnancyRosterFragment : BaseRosterFragment(R.layout.fragment_pregnancy_roster) {
    private lateinit var binding: FragmentPregnancyRosterBinding
    private var patientUuid: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPregnancyRosterBinding.bind(view)
        rosterViewModel.updateRosterStage(RosterQuestionnaireStage.PREGNANCY_ROSTER)

        initViews()
        clickListeners()
    }

    private fun initViews() {
        val intent = requireActivity().intent
        if (intent != null) {
            patientUuid = intent.getStringExtra("patientUuid")
        }    }

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
        PregnancyRosterFragmentDirections.navigationPregnancyToHealthService().apply {
            findNavController().navigate(this)
        }
    }
}
