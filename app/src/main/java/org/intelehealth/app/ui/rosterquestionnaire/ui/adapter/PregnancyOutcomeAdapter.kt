package org.intelehealth.app.ui.rosterquestionnaire.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.intelehealth.app.databinding.ItemPregnancyOutcomeBinding
import org.intelehealth.app.ui.rosterquestionnaire.model.PregnancyOutComeModel
import org.intelehealth.app.utilities.SpacingItemDecoration

class PregnancyOutcomeAdapter(
    private val items: ArrayList<PregnancyOutComeModel>
) : RecyclerView.Adapter<PregnancyOutcomeAdapter.ParentViewHolder>() {

    inner class ParentViewHolder(private val binding: ItemPregnancyOutcomeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PregnancyOutComeModel) {
            binding.tvTitle.text = item.title

            // Setup Child RecyclerView
            binding.rvOutComeItem.apply {
                layoutManager = LinearLayoutManager(
                    binding.root.context,
                    LinearLayoutManager.VERTICAL,
                    false
                )
                adapter = PregnancyOutComeChildAdapter(item.pregnancyOutComeViewQuestion)
                addItemDecoration(SpacingItemDecoration(12))
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParentViewHolder {
        val binding = ItemPregnancyOutcomeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ParentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ParentViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
