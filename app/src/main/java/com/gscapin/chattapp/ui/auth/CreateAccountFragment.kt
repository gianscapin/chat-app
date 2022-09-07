package com.gscapin.chattapp.ui.auth

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.gscapin.chattapp.R
import com.gscapin.chattapp.core.Result
import com.gscapin.chattapp.core.hide
import com.gscapin.chattapp.core.show
import com.gscapin.chattapp.databinding.FragmentCreateAccountBinding
import com.gscapin.chattapp.presentation.auth.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateAccountFragment : Fragment(R.layout.fragment_create_account) {
    private lateinit var binding: FragmentCreateAccountBinding
    var nickInputFill: Boolean = false
    var emailInputFill: Boolean = false
    var passwordInputFill: Boolean = false
    var verifyPasswordFill: Boolean = false

    val viewModel: AuthViewModel by viewModels()
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
                viewModel.signUp(email, password, nickname).observe(viewLifecycleOwner, Observer { result ->
                    when(result){
                        is Result.Loading -> {
                            binding.progressBarCreateAccount.show()
                            binding.createAccountBtn.isEnabled = false
                        }
                        is Result.Success -> {
                            binding.progressBarCreateAccount.hide()
                            findNavController().navigate(R.id.action_createAccountFragment_to_contactsFragment)
                        }
                        is Result.Failure -> {
                            binding.progressBarCreateAccount.show()
                            Toast.makeText(requireContext(), "Error ${result.exception}", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
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