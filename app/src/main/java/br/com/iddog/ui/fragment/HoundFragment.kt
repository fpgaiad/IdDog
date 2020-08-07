package br.com.iddog.ui.fragment

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import br.com.iddog.R
import br.com.iddog.ui.adapter.DogListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_hound.*

@AndroidEntryPoint
class HoundFragment : BaseFragment(R.layout.fragment_hound, HOUND) {

    override fun setupRecyclerView(view: View) {
        val orientation = activity?.resources?.configuration?.orientation
        dogListAdapter = DogListAdapter(::handleCharacterClick)
        rvHound.apply {
            adapter = dogListAdapter
            layoutManager = GridLayoutManager(
                view.context,
                getSpanCount(orientation),
                GridLayoutManager.VERTICAL,
                false
            )
        }
    }

    override fun hideProgressBar() {
        pbHound.visibility = View.GONE
    }

    override fun showProgressBar() {
        pbHound.visibility = View.VISIBLE
    }
}