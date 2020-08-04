package br.com.iddog.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import br.com.iddog.R
import br.com.iddog.util.Resource
import br.com.iddog.util.ValidationHelper
import br.com.iddog.viewmodel.DogsViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_login.*

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private val viewModel: DogsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnLogin.setOnClickListener {
            val insertedEmail = etEmailInput.text.toString()
            if (ValidationHelper.isValidEmail(insertedEmail)) {
                viewModel.emailToLogin = insertedEmail
                viewModel.login()
            } else {
                showError(view, getString(R.string.insert_valid_email))
            }
        }

        viewModel.user.observe(viewLifecycleOwner, Observer { loginResponse ->
            when (loginResponse) {
                is Resource.Success -> {
                    hideProgressBar()
                    findNavController().navigate(R.id.action_loginFragment_to_dogsFragment)
                }
                is Resource.Error -> {
                    hideProgressBar()
                    showError(view, getString(R.string.error_occurred))
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun showError(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun hideProgressBar() {
        pbLogin.visibility = View.GONE
    }

    private fun showProgressBar() {
        pbLogin.visibility = View.VISIBLE
    }
}