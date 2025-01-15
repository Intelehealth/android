package org.intelehealth.app.ui.rosterquestionnaire.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentGeneralRosterBinding
import org.intelehealth.app.ui.rosterquestionnaire.model.RoasterViewQuestion
import org.intelehealth.app.ui.rosterquestionnaire.ui.RosterQuestionnaireMainActivity.Companion.handleBackEventFromRosterToPatientReg
import org.intelehealth.app.ui.rosterquestionnaire.ui.adapter.MultiViewAdapter
import org.intelehealth.app.ui.rosterquestionnaire.ui.listeners.MultiViewListener
import org.intelehealth.app.ui.rosterquestionnaire.utilities.RosterQuestionnaireStage
import org.intelehealth.app.ui.rosterquestionnaire.viewmodel.RosterViewModel
import org.intelehealth.app.utilities.PatientRegStage
import org.intelehealth.app.utilities.SpacingItemDecoration

@AndroidEntryPoint
class GeneralRosterFragment : BaseRosterFragment(R.layout.fragment_general_roster),
    MultiViewListener {
    private lateinit var _binding: FragmentGeneralRosterBinding
    private val generalQuestionAdapter: MultiViewAdapter by lazy {
        MultiViewAdapter(listener = this@GeneralRosterFragment)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentGeneralRosterBinding.bind(view)
        rosterViewModel = ViewModelProvider.create(requireActivity())[RosterViewModel::class]
        rosterViewModel.updateRosterStage(RosterQuestionnaireStage.GENERAL_ROSTER)
        setAdapter()
        clickListeners()
        observeLiveData()
    }

    /**
     * Method to observe LiveData and update the adapter with new data when available
     */
    private fun observeLiveData() {
        rosterViewModel.generalLiveList.observe(viewLifecycleOwner) { generalList ->
            // If the list is not null or empty, notify the adapter to refresh the list
            if (!generalList.isNullOrEmpty()) generalQuestionAdapter.notifyList(generalList)
        }
    }

    private fun setAdapter() {
        _binding.rvGeneralQuestion.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = generalQuestionAdapter
            addItemDecoration(SpacingItemDecoration(16))
        }
    }


    private fun clickListeners() {

        _binding.frag2BtnBack.setOnClickListener {
            // Handle back button click
            handleBackEventFromRosterToPatientReg(
                requireActivity(),
                rosterViewModel.patientUuid,
                PatientRegStage.OTHER
            )
        }

        _binding.frag2BtnNext.setOnClickListener {
            rosterViewModel.validateGeneralList()?.let {
                _binding.rvGeneralQuestion.smoothScrollToPosition(it)
                generalQuestionAdapter.updateErrorMessage(it)
            } ?: run { navigateToPregnancy() }

        }

    }

    /**
     * Navigate to Pregnancy Roster Fragment
     */
    private fun navigateToPregnancy() {
        GeneralRosterFragmentDirections.navigationGeneralToPregnancyRoster().apply {
            findNavController().navigate(this)
        }
    }

    /**
     * Method for handling item clicks on the RecyclerView (currently not implemented)
     */
    override fun onItemClick(item: RoasterViewQuestion, position: Int, view: View) {
        // Handle item click
    }
}
