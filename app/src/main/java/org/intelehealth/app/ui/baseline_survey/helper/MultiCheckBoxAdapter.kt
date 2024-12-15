/*
package org.intelehealth.app.ui.baseline_survey.helper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.intelehealth.app.R

class MultiCheckBoxAdapter(
    private val items: List<Item>,
    private val onOptionSelected: (List<Item.Option>, String) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_SECTION = 0
        private const val TYPE_OPTION = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is Item.Section -> TYPE_SECTION
            is Item.Option -> TYPE_OPTION
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_SECTION -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(android.R.layout.simple_list_item_1, parent, false)
                SectionViewHolder(view)
            }
            TYPE_OPTION -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.multi_check_boxItem, parent, false)
                OptionViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is Item.Section -> (holder as SectionViewHolder).bind(item)
            is Item.Option -> (holder as OptionViewHolder).bind(item)
        }
    }

    override fun getItemCount(): Int = items.size

    inner class SectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(android.R.id.text1)

        fun bind(section: Item.Section) {
            textView.text = section.title
        }
    }

    inner class OptionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val checkBox: CheckBox = itemView.findViewById(R.id.checkboxFuelOption)

        fun bind(option: Item.Option) {
            checkBox.text = option.name
            checkBox.isChecked = option.isSelected

            checkBox.setOnCheckedChangeListener { _, isChecked ->
                option.isSelected = isChecked

                val sectionTitle = items.filterIndexed { index, _ -> index < adapterPosition }
                    .lastOrNull { it is Item.Section } as? Item.Section

                onOptionSelected(
                    items.filterIsInstance<Item.Option>().filter { it.isSelected },
                    sectionTitle?.title.orEmpty()
                )
            }
        }
    }
}


sealed class Item {
    data class Section(val title: String) : Item()
    data class Option(val name: String, var isSelected: Boolean = false) : Item()
}
*/
