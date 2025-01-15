package org.intelehealth.app.ui.rosterquestionnaire.ui

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentAddPregnancyOutcomeBinding
import org.intelehealth.app.ui.dialog.CalendarDialog
import org.intelehealth.app.ui.rosterquestionnaire.model.RoasterViewQuestion
import org.intelehealth.app.ui.rosterquestionnaire.ui.adapter.MultiViewAdapter
import org.intelehealth.app.ui.rosterquestionnaire.ui.listeners.MultiViewListener
import org.intelehealth.app.ui.rosterquestionnaire.viewmodel.RosterViewModel
import org.intelehealth.app.utilities.SpacingItemDecoration


class AddOutcomeDialog : DialogFragment(), MultiViewListener {

    private lateinit var _binding: FragmentAddPregnancyOutcomeBinding
    private lateinit var rosterViewModel: RosterViewModel
    private lateinit var pregnancyOutcomeList: ArrayList<RoasterViewQuestion>
    private var editPosition: Int = -1
    private val pregnancyAdapter: MultiViewAdapter by lazy {
        MultiViewAdapter(listener = this@AddOutcomeDialog)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Inflate the binding
        rosterViewModel = ViewModelProvider.create(requireActivity())[RosterViewModel::class]
        val dialog = Dialog(requireContext())
        _binding = FragmentAddPregnancyOutcomeBinding.inflate(layoutInflater)
        dialog.setContentView(_binding.root)

        dialog.setCancelable(true)

        dialog.window?.apply {
            setBackgroundDrawableResource(R.drawable.ui2_rounded_corners_dialog_bg) // Show rounded corners

            // Set the dialog width and height with margins
            val metrics = requireContext().resources.displayMetrics
            val horizontalMargin =
                requireContext().resources.getDimensionPixelSize(R.dimen.dialog_margin)
            val verticalMargin =
                requireContext().resources.getDimensionPixelSize(R.dimen.dialog_margin_vertical)
            val dialogWidth =
                metrics.widthPixels - (horizontalMargin * 2) // Screen width minus horizontal margins
            val dialogHeight =
                metrics.heightPixels - (verticalMargin * 2) // Screen height minus vertical margins
            clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
            setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
            setLayout(dialogWidth, dialogHeight)
        }
        return dialog
    }


    override fun onResume() {
        super.onResume()
        setAdapter()
        setClickListeners()
    }

    private fun setClickListeners() {
        _binding.btnSave.setOnClickListener {
            rosterViewModel.validatePregnancyOutcomeList(pregnancyOutcomeList)?.let {
                _binding.rvOutcomeQuestions.smoothScrollToPosition(it)
                pregnancyAdapter.updateErrorMessage(it)
            } ?: run {
                rosterViewModel.addPregnancyOutcome(pregnancyOutcomeList,editPosition)
                dismiss()
            }

        }
        _binding.btnCancel.setOnClickListener { dismiss() }
    }


    private fun setAdapter() {
        _binding.rvOutcomeQuestions.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = pregnancyAdapter
            addItemDecoration(SpacingItemDecoration(16))
        }
        pregnancyAdapter.notifyList(pregnancyOutcomeList)
    }


    override fun onItemClick(item: RoasterViewQuestion, position: Int, view: View) {
        CalendarDialog.showDatePickerDialog(object : CalendarDialog.OnDatePickListener {
            override fun onDatePick(day: Int, month: Int, year: Int, value: String?) {
                item.answer = value
                pregnancyAdapter.notifyItemChanged(position)

            }
        }, childFragmentManager)
    }

    fun setPregnancyOutcomeList(list: List<RoasterViewQuestion>, editPosition: Int = -1) {
        pregnancyOutcomeList = list as ArrayList<RoasterViewQuestion>
        this.editPosition = editPosition
    }
}
