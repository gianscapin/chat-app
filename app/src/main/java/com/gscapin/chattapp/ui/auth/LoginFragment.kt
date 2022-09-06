package com.gscapin.chattapp.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.gscapin.chattapp.R
import com.gscapin.chattapp.databinding.FragmentLoginBinding

class LoginFragment : Fragment(R.layout.fragment_login) {
    private lateinit var binding: FragmentLoginBinding

    var emailInputFill: Boolean = false
    var passwordInputFill: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)

        goToWelcomeScreen()

        emailInputConfig()

        passwordsInputConfig()

        enableBtn()

        binding.loginBtn.setOnClickListener {
            Log.d("login", "$emailInputFill, $passwordInputFill")
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