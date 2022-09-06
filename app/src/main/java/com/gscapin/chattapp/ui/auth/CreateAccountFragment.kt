package com.gscapin.chattapp.ui.auth

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import com.gscapin.chattapp.R
import com.gscapin.chattapp.databinding.FragmentCreateAccountBinding

class CreateAccountFragment : Fragment(R.layout.fragment_create_account) {
    private lateinit var binding: FragmentCreateAccountBinding
    var nickInputFill: Boolean = false
    var emailInputFill: Boolean = false
    var passwordInputFill: Boolean = false
    var verifyPasswordFill: Boolean = false
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCreateAccountBinding.bind(view)



        goToWelcomeScreen()

        nickNameInputConfig()

        emailInputConfig()

        passwordsInputConfig()

        enableBtn()

        createAccount()



    }

    private fun createAccount() {
        binding.createAccountBtn.setOnClickListener {
            val password = binding.passwordInput.text.toString().trim()
            val verifyPassword = binding.verifyPasswordInput.text.toString().trim()
            val nickname = binding.nickInput.text.toString().trim()
            val email = binding.emailInput.text.toString().trim()
            if (password != verifyPassword) {
                binding.passwordInput.error = "The password must be equals."
            } else {
                Log.d("account", "nick: $nickname, email: $email, password: $password")
            }
        }
    }

    private fun enableBtn() {
        binding.createAccountBtn.isEnabled = nickInputFill && emailInputFill && passwordInputFill && verifyPasswordFill
    }

    private fun nickNameInputConfig() {
        binding.nickInput.addTextChangedListener {
            val textInput = binding.nickInput.text.toString()
            if (textInput.isEmpty()) {
                binding.nickInput.error = "Nickname can't be empty."
                if(nickInputFill){
                    nickInputFill = false
                }
            } else {
                nickInputFill = true
            }
            enableBtn()
        }
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
        binding.verifyPasswordInput.addTextChangedListener {
            val textInput = binding.verifyPasswordInput.text.toString()
            if (textInput.isEmpty()) {
                binding.verifyPasswordInput.error = "This field can't be empty."
                if(verifyPasswordFill){
                    verifyPasswordFill = false
                }
            } else {
                verifyPasswordFill = true
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