package br.com.iddog.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import br.com.iddog.ui.fragment.*

const val HUSKY = "husky"
const val LABRADOR = "labrador"
const val HOUND = "hound"
const val PUG = "pug"

class BreedViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

//    private val tabFragmentsCreator: Map<Int, () -> Fragment> = mapOf(
//        HUSKY_PAGE_INDEX to { HuskyFragment() },
//        LABRADOR_PAGE_INDEX to { LabradorFragment() },
//        HOUND_PAGE_INDEX to { HoundFragment() },
//        PUG_PAGE_INDEX to { PugFragment() }
//    )

    private val dogCategories = listOf(HUSKY, LABRADOR, HOUND, PUG)

    override fun getItemCount() = dogCategories.size

    override fun createFragment(position: Int): Fragment {
        return BaseFragment.newInstance(dogCategories[position])
    }

    fun getTitle(position: Int): String {
        return dogCategories[position].capitalize()
    }



}