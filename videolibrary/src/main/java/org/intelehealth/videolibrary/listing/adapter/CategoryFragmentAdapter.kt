package org.intelehealth.videolibrary.listing.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import org.intelehealth.videolibrary.listing.fragment.VideoFragment
import org.intelehealth.videolibrary.model.Category

class CategoryFragmentAdapter(
    private val categoryList: List<Category>,
    lifecycle: Lifecycle,
    manager: FragmentManager,
) : FragmentStateAdapter(manager, lifecycle) {

    override fun getItemCount(): Int = categoryList.size

    override fun createFragment(position: Int): Fragment {
        val category = categoryList[position]
        return VideoFragment()
    }
}