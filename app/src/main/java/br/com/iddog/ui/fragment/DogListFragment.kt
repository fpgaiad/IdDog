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
import br.com.iddog.ui.adapter.DogListAdapter
import br.com.iddog.util.HomeViewPagerFragmentDirections
import br.com.iddog.util.Resource
import br.com.iddog.viewmodel.DogsViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_dog_list.*

@AndroidEntryPoint
class DogListFragment : Fragment(R.layout.fragment_dog_list) {

    companion object {
        const val CATEGORY = "dogCategory"

        fun newInstance(dogCategory: String): Fragment {
            val fragment = DogListFragment()
            val args = Bundle().apply {
                putString(CATEGORY, dogCategory)
            }
            fragment.arguments = args
            return fragment
        }
    }

    private val viewModel: DogsViewModel by viewModels()
    private var dogCategory: String? = null

    lateinit var dogListAdapter: DogListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dogCategory = arguments?.getString(CATEGORY)
        dogCategory?.let { viewModel.getDogsByCategory(it) }
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

    private fun setupRecyclerView(view: View) {
        val orientation = activity?.resources?.configuration?.orientation
        dogListAdapter = DogListAdapter(::handleCharacterClick)
        rvDogs.apply {
            adapter = dogListAdapter
            layoutManager = GridLayoutManager(
                view.context,
                getSpanCount(orientation),
                GridLayoutManager.VERTICAL,
                false
            )
        }
    }

    private fun hideProgressBar() {
        pbDogs.visibility = View.GONE
    }

    private fun showProgressBar() {
        pbDogs.visibility = View.VISIBLE
    }

    private fun showError(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            .setTextColor(resources.getColor(R.color.primaryTextColor))
            .setBackgroundTint(resources.getColor(R.color.secondaryDarkColor))
            .show()
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