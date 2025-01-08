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
    private var patientUuid: String? = null
    private lateinit var generalQuestionAdapter: MultiViewAdapter
    private lateinit var generalQuestionList: ArrayList<RoasterViewQuestion>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentGeneralRosterBinding.bind(view)
        rosterViewModel = ViewModelProvider.create(requireActivity())[RosterViewModel::class]
        rosterViewModel.updateRosterStage(RosterQuestionnaireStage.GENERAL_ROSTER)
        rosterViewModel.getGeneralQuestionList()
        clickListeners()
        observeLiveData()
    }

    private fun observeLiveData() {
        rosterViewModel.generalLiveList.observe(viewLifecycleOwner) { generalList ->
            generalQuestionList = generalList
            setAdapter()
        }
    }

    private fun setAdapter() {
        _binding.rvGeneralQuestion.apply {
            layoutManager = LinearLayoutManager(requireContext())
            generalQuestionAdapter = MultiViewAdapter(
                generalQuestionList,
                this@GeneralRosterFragment
            )
            adapter = generalQuestionAdapter
            addItemDecoration(SpacingItemDecoration(16))
        }
    }

    private fun isValidList(): Boolean {
        generalQuestionList.forEach {
            if (it.answer.isNullOrEmpty()) {
                generalQuestionAdapter.updateErrorMessage(true)
                return false
            }
        }
        return true
    }


    private fun clickListeners() {

        _binding.frag2BtnBack.setOnClickListener {
            // Handle back button click
            handleBackEventFromRosterToPatientReg(
                requireActivity(),
                patientUuid,

                PatientRegStage.OTHER
            )
        }

        _binding.frag2BtnNext.setOnClickListener {
            if (isValidList()) {
                navigateToDetails()
            }
        }

    }

    private fun navigateToDetails() {
        /*  if (rosterViewModel.isEditMode) {
              GeneralRosterFragmentDirections.navigationGeneralToDetails(
                  patientUuid, "roster", "false"
              ).also {
                  findNavController().navigate(it)
                  requireActivity().finish()
              }
          } else {*/
        GeneralRosterFragmentDirections.navigationGeneralToPregnancyRoster().apply {
            findNavController().navigate(this)
        }
        // }
    }

    override fun onItemClick(item: RoasterViewQuestion, position: Int, view: View) {

    }


}
