package com.gscapin.chattapp.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.gscapin.chattapp.R
import com.gscapin.chattapp.databinding.FragmentWelcomeBinding

class WelcomeFragment : Fragment(R.layout.fragment_welcome) {

    private lateinit var binding: FragmentWelcomeBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWelcomeBinding.bind(view)

        isUserLogged()

        goToCreateAccount()

        goToLogin()
    }

    private fun isUserLogged() {
        val user = FirebaseAuth.getInstance().currentUser

        if (user != null) {
            findNavController().navigate(R.id.action_welcomeFragment_to_contactsFragment)
        }
    }

    private fun goToLogin() {
        binding.login.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_loginFragment)
        }
    }

    private fun goToCreateAccount() {
        binding.createAccount.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_createAccountFragment)
        }
    }

}