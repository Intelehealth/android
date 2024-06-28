package org.intelehealth.app.ui.patient.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import org.intelehealth.app.ui.patient.fragment.PatientPersonalInfoFragment

/**
 * Created by Vaghela Mithun R. on 04-06-2023 - 00:01.
 * Email : mithun@intelehealth.org
 * Mob   : +919727206702
 */
class PatientInfoPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun createFragment(position: Int): Fragment {
        return PatientPersonalInfoFragment()
    }

    override fun getItemCount(): Int {
        return 1
    }

}