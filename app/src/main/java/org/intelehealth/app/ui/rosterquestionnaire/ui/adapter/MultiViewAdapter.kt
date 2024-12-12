package org.intelehealth.app.ui.rosterquestionnaire.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import org.intelehealth.app.R
import org.intelehealth.app.databinding.ItemDatePickerViewBinding
import org.intelehealth.app.databinding.ItemSpinnerViewBinding
import org.intelehealth.app.ui.rosterquestionnaire.model.RoasterViewQuestion
import org.intelehealth.app.ui.rosterquestionnaire.ui.listeners.MultiViewListener
import org.intelehealth.app.utilities.ArrayAdapterUtils
import org.intelehealth.app.utilities.extensions.hideError
import org.intelehealth.app.utilities.extensions.showDropDownError
import org.intelehealth.app.utilities.extensions.validate

class MultiViewAdapter(
    private val items: List<RoasterViewQuestion>,
    private val listener: MultiViewListener,
    private var isResulCheck: Boolean = false,
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
        fun bind(data: RoasterViewQuestion) {
            when (binding) {
                is ItemSpinnerViewBinding -> {

                    binding.tvSpinnerHeader.text = data.question
                    binding.spinner.apply {
                        val adapter = ArrayAdapterUtils.getObjectArrayAdapter(
                            binding.root.context,
                            data.spinnerItem!!
                        )

                        if (adapter != this.adapter) {
                            setAdapter(adapter)
                        }

                        if (text.toString() != data.answer) {
                            setText(data.answer ?: binding.root.context.getString(R.string.select), false)
                        }

                        if (isResulCheck && data.answer.isNullOrEmpty()) {
                            binding.textInputLayRelation.showDropDownError(data.answer, data.errorMessage)
                        } else {
                            binding.textInputLayRelation.hideError()
                        }

                        setOnItemClickListener { _, _, _, id ->
                            data.answer = adapter.getItem(id.toInt())
                            binding.textInputLayRelation.hideError()
                        }
                    }
                }

                is ItemDatePickerViewBinding -> {
                    binding.tvDatePickerQuestion.text = data.question
                    binding.textInputETDob.setText(data.answer ?: "")
                    if (isResulCheck && data.answer.isNullOrEmpty()) {
                        binding.textInputLayDob.validate(binding.textInputETDob, data.errorMessage)
                    } else {
                        binding.textInputLayDob.hideError()
                    }
                    binding.textInputETDob.setOnClickListener {
                        listener.onItemClick(data, bindingAdapterPosition, it)
                    }
                }

            }
        }
    }

    fun updateErrorMessage(status: Boolean) {
        isResulCheck = status
        notifyDataSetChanged()
    }


}
