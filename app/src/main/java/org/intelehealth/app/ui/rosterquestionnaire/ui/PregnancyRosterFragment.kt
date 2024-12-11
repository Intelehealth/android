package org.intelehealth.app.ui.rosterquestionnaire.ui

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentPregnancyRosterBinding

import org.intelehealth.app.ui.rosterquestionnaire.utilities.RosterQuestionnaireStage
import org.intelehealth.app.ui.rosterquestionnaire.viewmodel.RosterViewModel

@AndroidEntryPoint
class PregnancyRosterFragment : BaseRosterFragment(R.layout.fragment_pregnancy_roster) {
    private lateinit var binding: FragmentPregnancyRosterBinding
    private var patientUuid: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPregnancyRosterBinding.bind(view)
        rosterViewModel = ViewModelProvider.create(requireActivity())[RosterViewModel::class]
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
        //val activityBinding = (requireActivity() as RosterQuestionnaireMainActivity).binding

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
