package com.gscapin.chattapp.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.gscapin.chattapp.R
import com.gscapin.chattapp.core.Result
import com.gscapin.chattapp.core.hide
import com.gscapin.chattapp.core.show
import com.gscapin.chattapp.databinding.FragmentLoginBinding
import com.gscapin.chattapp.presentation.auth.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {
    private lateinit var binding: FragmentLoginBinding

    var emailInputFill: Boolean = false
    var passwordInputFill: Boolean = false

    val viewModel: AuthViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)

        goToWelcomeScreen()

        emailInputConfig()

        passwordsInputConfig()

        enableBtn()

        login()



    }

    private fun login() {
        binding.loginBtn.setOnClickListener {
            Log.d("login", "$emailInputFill, $passwordInputFill")

            val email = binding.emailInput.text.toString().trim()
            val password = binding.passwordInput.text.toString().trim()

            viewModel.signIn(email, password).observe(viewLifecycleOwner, Observer { result ->
                when (result) {
                    is Result.Loading -> {
                        binding.progressBarLogin.show()
                        binding.loginBtn.isEnabled = false
                        binding.errorMessage.hide()
                    }
                    is Result.Success -> {
                        binding.progressBarLogin.hide()
                        findNavController().navigate(R.id.action_loginFragment_to_contactsFragment)
                    }
                    is Result.Failure -> {
                        binding.progressBarLogin.hide()
                        binding.loginBtn.isEnabled = true
                        binding.errorMessage.show()
                    }
                }
            })

        }
    }

    private fun enableBtn() {
        binding.loginBtn.isEnabled = emailInputFill && passwordInputFill
    }

    private fun emailInputConfig() {
        binding.emailInput.addTextChangedListener {
            val textInput = binding.emailInput.text.toString()
            if (textInput.isEmpty()) {
                binding.emailInput.error = "Email can't be empty."
                if(emailInputFill){
                    emailInputFill = false
                }
            } else {
                emailInputFill = true
            }
            enableBtn()
        }
    }

    private fun passwordsInputConfig() {
        binding.passwordInput.addTextChangedListener {
            val textInput = binding.passwordInput.text.toString()
            if (textInput.isEmpty()) {
                binding.passwordInput.error = "Password can't be empty."
                if(passwordInputFill){
                    passwordInputFill = false
                }
            } else {
                passwordInputFill = true
            }
            enableBtn()
        }

    }
    private fun goToWelcomeScreen() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }
}