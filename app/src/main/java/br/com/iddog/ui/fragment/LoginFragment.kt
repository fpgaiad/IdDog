package br.com.iddog.ui.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import br.com.iddog.R
import br.com.iddog.util.Resource
import br.com.iddog.util.UserHelper
import br.com.iddog.util.ValidationHelper
import br.com.iddog.viewmodel.DogsViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_login.*

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private var isLoggedUi = true
    private val viewModel: DogsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val storage = UserHelper.getStorage()
        setUserInterface(storage)

        btnLogin.setOnClickListener {
            if (isLoggedUi) {
                viewModel.emailToLogin = storage?.getString("email", "").toString()
                viewModel.login()
            } else {
                val insertedEmail = etEmailInput.text.toString()
                when {
                    ValidationHelper.isValidEmail(insertedEmail) -> {
                        viewModel.emailToLogin = insertedEmail
                        viewModel.login()
                    }
                    else -> {
                        etEmailInput.text = null
                        showError(view, getString(R.string.insert_valid_email))
                    }
                }
            }
        }

        viewModel.user.observe(viewLifecycleOwner, Observer { loginResponse ->
            when (loginResponse) {
                is Resource.Success -> {
                    hideProgressBar()

                    loginResponse.data?.let {
                        val email = loginResponse.data.user.email
                        val token = loginResponse.data.user.token
                        UserHelper.saveUser(email, token)
                    }
                    findNavController().navigate(R.id.action_loginFragment_to_dogsFragment)
                }
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

    private fun setUserInterface(storage: SharedPreferences?) {
        if (!storage?.getString("email", "").isNullOrEmpty()) {
            val storedEmail = storage?.getString("email", null)
            storedEmail?.let { setUiToLoggedUser(it) }
        } else {
            setUiToUnloggedUser()
        }
    }

    private fun setUiToLoggedUser(email: String) {
        isLoggedUi = true
        val loggedEmailText = "Você já está logado como\n$email"
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