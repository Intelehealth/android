package org.intelehealth.app.ui.rosterquestionnaire.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentPregnancyRosterBinding
import org.intelehealth.app.ui.rosterquestionnaire.model.PregnancyOutComeModel
import org.intelehealth.app.ui.rosterquestionnaire.model.RoasterViewQuestion
import org.intelehealth.app.ui.rosterquestionnaire.ui.adapter.PregnancyOutcomeAdapter
import org.intelehealth.app.ui.rosterquestionnaire.ui.listeners.PregnancyOutcomeClickListener
import org.intelehealth.app.ui.rosterquestionnaire.utilities.RosterQuestionnaireStage
import org.intelehealth.app.ui.rosterquestionnaire.viewmodel.RosterViewModel
import org.intelehealth.app.utilities.SpacingItemDecoration

@AndroidEntryPoint
class PregnancyRosterFragment : BaseRosterFragment(R.layout.fragment_pregnancy_roster),
    PregnancyOutcomeClickListener {
    private var pregnancyAdapter: PregnancyOutcomeAdapter? = null
    private lateinit var binding: FragmentPregnancyRosterBinding
    private var patientUuid: String? = null
    private var pregnancyOutComeList = ArrayList<PregnancyOutComeModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPregnancyRosterBinding.bind(view)
        rosterViewModel = ViewModelProvider.create(requireActivity())[RosterViewModel::class]
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = rosterViewModel
        rosterViewModel.updateRosterStage(RosterQuestionnaireStage.PREGNANCY_ROSTER)
        initViews()
        setOutcomeAdapter()
        clickListeners()
        setObserver()
    }

    private fun setObserver() {
        rosterViewModel.outComeLiveList.observe(viewLifecycleOwner) {
            pregnancyOutComeList.clear()
            pregnancyOutComeList.addAll(it)
            pregnancyAdapter?.notifyDataSetChanged()
        }
    }

    private fun initViews() {
        val intent = requireActivity().intent
        if (intent != null) {
            patientUuid = intent.getStringExtra("patientUuid")
        }
    }

    private fun setOutcomeAdapter() {
        binding.rvPregnancyOutcome.apply {
            layoutManager = LinearLayoutManager(requireContext())
            pregnancyAdapter =
                PregnancyOutcomeAdapter(pregnancyOutComeList, this@PregnancyRosterFragment)
            addItemDecoration(SpacingItemDecoration(16))
            adapter = pregnancyAdapter
        }

    }

    private fun clickListeners() {
        binding.tvAddPregnancyOutcome.setOnClickListener {
            val dialog = AddOutcomeDialog()
            dialog.show(childFragmentManager, AddOutcomeDialog::class.simpleName)
        }

        binding.frag2BtnNext.setOnClickListener {
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

    override fun onClickDelete(view: View, position: Int, item: PregnancyOutComeModel) {
        rosterViewModel.deletePregnancyOutcome(position)
        pregnancyAdapter?.notifyItemRemoved(position)
    }

    override fun onClickEdit(view: View, position: Int, item: PregnancyOutComeModel) {
        rosterViewModel.existPregnancyOutComePosition = position
        rosterViewModel.existingRoasterQuestionList =
            item.roasterViewQuestion as ArrayList<RoasterViewQuestion>
        val dialog = AddOutcomeDialog()
        dialog.show(childFragmentManager, AddOutcomeDialog::class.simpleName)
    }

    override fun onClickOpen(view: View, position: Int, item: PregnancyOutComeModel) {
        item.isOpen = !item.isOpen
        pregnancyAdapter?.notifyItemChanged(position)
    }
}
