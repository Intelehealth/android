package org.intelehealth.app.ui.rosterquestionnaire.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentHealthServiceRosterBinding
import org.intelehealth.app.ui.rosterquestionnaire.model.HealthServiceModel
import org.intelehealth.app.ui.rosterquestionnaire.model.RoasterViewQuestion
import org.intelehealth.app.ui.rosterquestionnaire.ui.adapter.HealthServiceAdapter
import org.intelehealth.app.ui.rosterquestionnaire.ui.listeners.HealthServiceClickListener
import org.intelehealth.app.ui.rosterquestionnaire.utilities.RosterQuestionnaireStage
import org.intelehealth.app.ui.rosterquestionnaire.viewmodel.RosterViewModel
import org.intelehealth.app.utilities.SpacingItemDecoration

@AndroidEntryPoint
class HealthServiceRosterFragment : BaseRosterFragment(R.layout.fragment_health_service_roster),
    HealthServiceClickListener {
    private var healthServiceAdapter: HealthServiceAdapter? = null
    private lateinit var binding: FragmentHealthServiceRosterBinding
    private var patientUuid: String? = null

    private var healthServiceList = ArrayList<HealthServiceModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHealthServiceRosterBinding.bind(view)
        rosterViewModel = ViewModelProvider.create(requireActivity())[RosterViewModel::class]
        rosterViewModel.updateRosterStage(RosterQuestionnaireStage.HEALTH_SERVICE)

        initViews()
        setHealthServiceAdapter()
        setObserver()
        clickListeners()
    }

    private fun clickListeners() {
        // val activityBinding = (requireActivity() as RosterQuestionnaireMainActivity).binding

        binding.frag2BtnNext.setOnClickListener {
            //for now only UI is there hence navigated directly
            navigateToDetails()
        }
        binding.frag2BtnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.tvAddHealthService.setOnClickListener {
            val dialog = AddHealthServiceDialog()
            dialog.show(childFragmentManager, AddHealthServiceDialog::class.simpleName)
        }
    }
    private fun setObserver() {
        rosterViewModel.healthServiceLiveList.observe(viewLifecycleOwner) {
            healthServiceList.clear()
            healthServiceList.addAll(it)
            healthServiceAdapter?.notifyDataSetChanged()
        }
    }

    private fun setHealthServiceAdapter() {
        binding.rvHealthService.apply {
            layoutManager = LinearLayoutManager(requireContext())
            healthServiceAdapter =
                HealthServiceAdapter(healthServiceList, this@HealthServiceRosterFragment)
            addItemDecoration(SpacingItemDecoration(16))
            adapter = healthServiceAdapter
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

    override fun onClickDelete(view: View, position: Int, item: HealthServiceModel) {
        rosterViewModel.deletePregnancyOutcome(position)
        healthServiceAdapter?.notifyItemRemoved(position)
    }

    override fun onClickEdit(view: View, position: Int, item: HealthServiceModel) {
        rosterViewModel.existPregnancyOutComePosition = position
        rosterViewModel.existingRoasterQuestionList =
            item.roasterViewQuestion as ArrayList<RoasterViewQuestion>
        val dialog = AddOutcomeDialog()
        dialog.show(childFragmentManager, AddOutcomeDialog::class.simpleName)
    }

    override fun onClickOpen(view: View, position: Int, item: HealthServiceModel) {
        item.isOpen = !item.isOpen
        healthServiceAdapter?.notifyItemChanged(position)
    }
}
