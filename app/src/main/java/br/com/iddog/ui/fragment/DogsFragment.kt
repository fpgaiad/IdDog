package br.com.iddog.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import br.com.iddog.R
import br.com.iddog.ui.adapter.*
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_dogs.*

@AndroidEntryPoint
class DogsFragment : Fragment(R.layout.fragment_dogs) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPagerDogs.adapter = BreedViewPagerAdapter(this)

        TabLayoutMediator(tabLayoutDogs, viewPagerDogs) { tab, position ->
            tab.text = getTabTitle(position)
        }.attach()
    }

    private fun getTabTitle(position: Int): String? {
        return when (position) {
            HUSKY_PAGE_INDEX -> "Husk"
            LABRADOR_PAGE_INDEX -> "Labrador"
            HOUND_PAGE_INDEX -> "Hound"
            PUG_PAGE_INDEX -> "Pug"
            else -> null
        }
    }
}