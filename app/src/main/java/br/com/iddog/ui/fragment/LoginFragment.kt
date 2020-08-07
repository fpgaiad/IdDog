package br.com.iddog.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import br.com.iddog.R
import br.com.iddog.data.store.Store
import br.com.iddog.util.Resource
import br.com.iddog.data.store.UserStore
import br.com.iddog.util.ValidationHelper
import br.com.iddog.viewmodel.DogsViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_login.*

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private var isLoggedUi = true
    private val viewModel: DogsViewModel by viewModels()
    private lateinit var storage: Store

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        context?.apply {
            storage = UserStore(getSharedPreferences("User", Context.MODE_PRIVATE))
        }

        setUserInterface()

        btnLogin.setOnClickListener {
            if (isLoggedUi) {
                navigateToDogsList()
            } else {
                val email = etEmailInput.text.toString()
                if (ValidationHelper.isValidEmail(email)) {
                    viewModel.login(email)
                } else {
                    etEmailInput.text = null
                    showError(view, getString(R.string.insert_valid_email))
                }
            }
        }

        viewModel.user.observe(viewLifecycleOwner, Observer { loginResponse ->
            when (loginResponse) {
                is Resource.Success -> navigateToDogsList()
                is Resource.Error -> {
                    hideProgressBar()
                    loginResponse.message?.let { message ->
                        showError(view, message)
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun navigateToDogsList() {
        hideProgressBar()
        findNavController().navigate(R.id.action_loginFragment_to_dogsFragment)
    }

    private fun setUserInterface() {
        storage.getUser()?.let {
            setUiToLoggedUser(it.email)
        } ?: setUiToUnloggedUser()
    }

    private fun setUiToLoggedUser(email: String) {
        isLoggedUi = true
        val loggedEmailText = "${getString(R.string.logged_as)}\n$email"
        tvLoggedEmail.text = loggedEmailText
        tvLoggedEmail.visibility = View.VISIBLE
        etEmailInput.visibility = View.GONE
        btnChangeEmail.visibility = View.VISIBLE
        btnChangeEmail.setOnClickListener { setUiToUnloggedUser() }
    }

    private fun setUiToUnloggedUser() {
        isLoggedUi = false
        tvLoggedEmail.visibility = View.GONE
        etEmailInput.visibility = View.VISIBLE
        btnChangeEmail.visibility = View.GONE
    }

    private fun showError(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            .setTextColor(resources.getColor(R.color.primaryTextColor))
            .setBackgroundTint(resources.getColor(R.color.secondaryDarkColor))
            .show()
    }

    private fun hideProgressBar() {
        pbLogin.visibility = View.GONE
    }

    private fun showProgressBar() {
        pbLogin.visibility = View.VISIBLE
    }
}