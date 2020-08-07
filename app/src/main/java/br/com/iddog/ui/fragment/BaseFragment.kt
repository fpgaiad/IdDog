package br.com.iddog.ui.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import br.com.iddog.R
import br.com.iddog.ui.adapter.DogListAdapter
import br.com.iddog.util.HomeViewPagerFragmentDirections
import br.com.iddog.util.Resource
import br.com.iddog.viewmodel.DogsViewModel
import com.google.android.material.snackbar.Snackbar

abstract class BaseFragment(
    layout: Int,
    private var dogCategory: String
) : Fragment(layout) {

    private val viewModel: DogsViewModel by viewModels()

    lateinit var dogListAdapter: DogListAdapter

    companion object {
        const val HUSKY = "husky"
        const val LABRADOR = "labrador"
        const val HOUND = "hound"
        const val PUG = "pug"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getDogsByCategory(dogCategory)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.dogs.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { feedResponse ->
                        setupRecyclerView(view)
                        dogListAdapter.differ.submitList(feedResponse.list)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        showError(view, message)
                        findNavController().navigate(R.id.action_dogsFragment_to_loginFragment)
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    open fun hideProgressBar() {}

    open fun showProgressBar() {}

    private fun showError(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            .setTextColor(resources.getColor(R.color.primaryTextColor))
            .setBackgroundTint(resources.getColor(R.color.secondaryDarkColor))
            .show()
    }

    open fun setupRecyclerView(view: View) {}

    protected fun getSpanCount(orientation: Int?): Int {
        return when (orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> 3
            else -> 2
        }
    }

    protected fun handleCharacterClick(urlDogImage: String) {
        val direction = HomeViewPagerFragmentDirections
            .actionViewPagerFragmentToDetailFragment(urlDogImage)
        findNavController().navigate(direction)
    }
}