package org.intelehealth.app.ui.rosterquestionnaire.ui

import android.app.Dialog
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
import org.intelehealth.app.ui.rosterquestionnaire.model.PregnancyOutComeViewQuestion
import org.intelehealth.app.ui.rosterquestionnaire.ui.adapter.MultiViewAdapter
import org.intelehealth.app.ui.rosterquestionnaire.ui.listeners.MultiViewListener
import org.intelehealth.app.ui.rosterquestionnaire.viewmodel.RosterViewModel
import org.intelehealth.app.utilities.SpacingItemDecoration


class AddOutcomeDialog : DialogFragment(), MultiViewListener {

    private lateinit var pregnancyAdapter: MultiViewAdapter
    private lateinit var _binding: FragmentAddPregnancyOutcomeBinding
    private lateinit var rosterViewModel: RosterViewModel
    private var pregnancyOutcomeList: ArrayList<PregnancyOutComeViewQuestion> = arrayListOf()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Inflate the binding
        _binding = FragmentAddPregnancyOutcomeBinding.inflate(LayoutInflater.from(context))
        rosterViewModel = ViewModelProvider.create(requireActivity())[RosterViewModel::class]
        val builder = MaterialAlertDialogBuilder(requireContext())
        builder.setView(_binding.root)

        val dialog = builder.create()
        dialog.setCancelable(true)

        val alertDialog: androidx.appcompat.app.AlertDialog = builder.create()
        alertDialog.window!!.setBackgroundDrawableResource(R.drawable.ui2_rounded_corners_dialog_bg) // show rounded corner for the dialog
        alertDialog.window!!.addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND) // dim background
        val width =
            context!!.resources.getDimensionPixelSize(R.dimen.internet_dialog_width) // set width to your dialog.
        alertDialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT)

        return alertDialog
    }


    override fun onResume() {
        super.onResume()
        setAdapter()
        setClickListeners()
    }

    private fun setClickListeners() {
        _binding.btnSave.setOnClickListener {
            rosterViewModel.addPregnancyOutcome(pregnancyOutcomeList)
            dismiss()
        }
        _binding.btnCancel.setOnClickListener { dismiss() }
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


    override fun onItemClick(item: PregnancyOutComeViewQuestion, position: Int, view: View) {
        if (item.layoutId == R.layout.item_spinner_view) {
            TODO()
        } else if (item.layoutId == R.layout.item_date_picker_view) {

            CalendarDialog.showDatePickerDialog(object : CalendarDialog.OnDatePickListener {
                override fun onDatePick(day: Int, month: Int, year: Int, value: String?) {
                    item.answer = value
                    pregnancyAdapter.notifyItemChanged(position)

                }
            }, childFragmentManager)
        }
    }


}
