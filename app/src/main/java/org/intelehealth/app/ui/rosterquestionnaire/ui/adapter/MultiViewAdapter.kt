package org.intelehealth.app.ui.rosterquestionnaire.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import org.intelehealth.app.R
import org.intelehealth.app.databinding.ItemDatePickerViewBinding
import org.intelehealth.app.databinding.ItemSpinnerViewBinding
import org.intelehealth.app.ui.rosterquestionnaire.model.PregnancyOutComeViewQuestion
import org.intelehealth.app.ui.rosterquestionnaire.ui.listeners.MultiViewListener
import org.intelehealth.app.utilities.ArrayAdapterUtils

class MultiViewAdapter(
    private val items: List<PregnancyOutComeViewQuestion>,
    private val listener: MultiViewListener,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return items[position].layoutId
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = when (viewType) {
            R.layout.item_spinner_view -> ItemSpinnerViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

            R.layout.item_date_picker_view -> ItemDatePickerViewBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
            // Add other layouts here
            else -> throw IllegalArgumentException("Unknown view type: $viewType")
        }

        return GenericViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        (holder as GenericViewHolder).bind(item)
    }

    override fun getItemCount(): Int = items.size

    inner class GenericViewHolder(private val binding: ViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: PregnancyOutComeViewQuestion) {
            when (binding) {
                is ItemSpinnerViewBinding -> {

                    binding.tvSpinnerHeader.text = data.question
                    if (!data.answer.isNullOrEmpty()) {
                        binding.spinner.setText(data.answer)
                    } else {
                        binding.spinner.setText(binding.root.context.getText(R.string.select))
                    }
                    val adapter = ArrayAdapterUtils.getObjectArrayAdapter(
                        binding.root.context,
                        data.spinnerItem!!
                    )

                    binding.spinner.setAdapter(adapter)
                    binding.spinner.setOnItemClickListener { _, _, _, id ->
                        data.answer = adapter.getItem(id.toInt())
                    }

                }

                is ItemDatePickerViewBinding -> {
                    binding.tvDatePickerQuestion.text = data.question
                    binding.textInputETDob.setText(data.answer ?: "")
                    binding.textInputETDob.setOnClickListener {
                        listener.onItemClick(data, bindingAdapterPosition, it)
                    }
                }

            }
        }
    }

}
