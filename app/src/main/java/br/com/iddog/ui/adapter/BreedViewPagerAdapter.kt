package br.com.iddog.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import br.com.iddog.ui.fragment.HoundFragment
import br.com.iddog.ui.fragment.HuskyFragment
import br.com.iddog.ui.fragment.LabradorFragment
import br.com.iddog.ui.fragment.PugFragment

const val HUSKY_PAGE_INDEX = 0
const val LABRADOR_PAGE_INDEX = 1
const val HOUND_PAGE_INDEX = 2
const val PUG_PAGE_INDEX = 3

class BreedViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private val tabFragmentsCreator: Map<Int, () -> Fragment> = mapOf(
        HUSKY_PAGE_INDEX to { HuskyFragment() },
        LABRADOR_PAGE_INDEX to { LabradorFragment() },
        HOUND_PAGE_INDEX to { HoundFragment() },
        PUG_PAGE_INDEX to { PugFragment() }
    )

    override fun getItemCount() = tabFragmentsCreator.size

    override fun createFragment(position: Int): Fragment {
        return tabFragmentsCreator[position]?.invoke() ?: throw IndexOutOfBoundsException()
    }

}