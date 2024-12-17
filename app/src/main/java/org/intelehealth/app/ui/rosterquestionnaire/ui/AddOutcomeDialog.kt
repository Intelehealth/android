package org.intelehealth.app.ui.rosterquestionnaire.ui

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.intelehealth.app.R
import org.intelehealth.app.databinding.FragmentAddPregnancyOutcomeBinding
import org.intelehealth.app.ui.dialog.CalendarDialog
import org.intelehealth.app.ui.rosterquestionnaire.model.RoasterViewQuestion
import org.intelehealth.app.ui.rosterquestionnaire.ui.adapter.MultiViewAdapter
import org.intelehealth.app.ui.rosterquestionnaire.ui.listeners.MultiViewListener
import org.intelehealth.app.ui.rosterquestionnaire.viewmodel.RosterViewModel
import org.intelehealth.app.utilities.SpacingItemDecoration


class AddOutcomeDialog : DialogFragment(), MultiViewListener {

    private lateinit var pregnancyAdapter: MultiViewAdapter
    private lateinit var _binding: FragmentAddPregnancyOutcomeBinding
    private lateinit var rosterViewModel: RosterViewModel
    private var pregnancyOutcomeList: ArrayList<RoasterViewQuestion> = arrayListOf()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Inflate the binding
        _binding = FragmentAddPregnancyOutcomeBinding.inflate(LayoutInflater.from(context))
        rosterViewModel = ViewModelProvider.create(requireActivity())[RosterViewModel::class]
        val builder = MaterialAlertDialogBuilder(requireContext())
        builder.setView(_binding.root)

        val alertDialog: androidx.appcompat.app.AlertDialog = builder.create()
        alertDialog.setCancelable(true)

        alertDialog.window?.apply {
            setBackgroundDrawableResource(R.drawable.ui2_rounded_corners_dialog_bg) // Show rounded corners
            addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND) // Dim background

            // Set the dialog width and height with margins
            val metrics = requireContext().resources.displayMetrics
            val horizontalMargin = requireContext().resources.getDimensionPixelSize(R.dimen.dialog_margin)
            val verticalMargin = requireContext().resources.getDimensionPixelSize(R.dimen.dialog_margin_vertical)
            val dialogWidth = metrics.widthPixels - (horizontalMargin * 2) // Screen width minus horizontal margins
            val dialogHeight = metrics.heightPixels - (verticalMargin * 2) // Screen height minus vertical margins
            setLayout(dialogWidth, dialogHeight)
        }
        return alertDialog
    }


    override fun onResume() {
        super.onResume()
        setAdapter()
        setClickListeners()
    }

    private fun setClickListeners() {
        _binding.btnSave.setOnClickListener {
            if (isValidList(pregnancyOutcomeList)) {
                rosterViewModel.addPregnancyOutcome(pregnancyOutcomeList)
                dismiss()
            }
        }
        _binding.btnCancel.setOnClickListener { dismiss() }
    }

    private fun isValidList(pregnancyOutcomeList: ArrayList<RoasterViewQuestion>): Boolean {
        pregnancyOutcomeList.forEach {
            if (it.answer.isNullOrEmpty()) {
                pregnancyAdapter.updateErrorMessage(true)
                return false
            }
        }
        return true
    }


    private fun setAdapter() {
        pregnancyOutcomeList.addAll(rosterViewModel.getOutcomeQuestionList())

        _binding.rvOutcomeQuestions.apply {
            layoutManager = LinearLayoutManager(requireContext())
            pregnancyAdapter = MultiViewAdapter(
                pregnancyOutcomeList,
                this@AddOutcomeDialog
            )
            adapter = pregnancyAdapter
            addItemDecoration(SpacingItemDecoration(16))
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        rosterViewModel.existingRoasterQuestionList = null
    }
    override fun onItemClick(item: RoasterViewQuestion, position: Int, view: View) {
        CalendarDialog.showDatePickerDialog(object : CalendarDialog.OnDatePickListener {
            override fun onDatePick(day: Int, month: Int, year: Int, value: String?) {
                item.answer = value
                pregnancyAdapter.notifyItemChanged(position)

            }
        }, childFragmentManager)
    }


}
