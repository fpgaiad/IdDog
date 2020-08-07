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
    var adapter: BreedViewPagerAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = BreedViewPagerAdapter(this)
        viewPagerDogs.adapter = adapter

        TabLayoutMediator(tabLayoutDogs, viewPagerDogs) { tab, position ->
            tab.text = adapter?.getTitle(position)
        }.attach()
    }
}