package br.com.iddog.ui.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import br.com.iddog.R
import br.com.iddog.data.model.feed.FeedResponse
import br.com.iddog.ui.adapter.DogListAdapter
import br.com.iddog.util.HomeViewPagerFragmentDirections
import br.com.iddog.util.Resource
import br.com.iddog.viewmodel.DogsViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_pug.*

@AndroidEntryPoint
class PugFragment : Fragment(R.layout.fragment_pug) {

    private val viewModel: DogsViewModel by viewModels()

    companion object {
        private const val PUG = "pug"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getDogsByCategory(PUG)

        viewModel.dogs.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { feedResponse ->
                        setupRecyclerView(view, feedResponse)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        showError(view, message)
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun hideProgressBar() {
        pbPug.visibility = View.GONE
    }

    private fun showProgressBar() {
        pbPug.visibility = View.VISIBLE
    }

    private fun showError(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun setupRecyclerView(view: View, feedResponse: FeedResponse) {
        val orientation = activity?.resources?.configuration?.orientation
        val dogListAdapter = DogListAdapter(feedResponse.list, ::handleCharacterClick)
        rvPug.apply {
            adapter = dogListAdapter
            layoutManager = GridLayoutManager(
                view.context,
                getSpanCount(orientation),
                GridLayoutManager.VERTICAL,
                false
            )
        }
    }

    private fun getSpanCount(orientation: Int?): Int {
        return when (orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> 3
            else -> 2
        }
    }

    private fun handleCharacterClick(urlDogImage: String) {
        val direction = HomeViewPagerFragmentDirections
            .actionViewPagerFragmentToDetailFragment(urlDogImage)
        findNavController().navigate(direction)
    }
}