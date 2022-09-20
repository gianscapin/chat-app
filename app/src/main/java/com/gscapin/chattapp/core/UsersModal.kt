package com.gscapin.chattapp.core

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.gscapin.chattapp.R
import com.gscapin.chattapp.data.model.User
import com.gscapin.chattapp.databinding.AddUserModalBinding
import com.gscapin.chattapp.presentation.contact.ContactViewModel
import com.gscapin.chattapp.ui.contacts.adapter.OnUserClickListener
import com.gscapin.chattapp.ui.contacts.adapter.UsersAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.math.log

@AndroidEntryPoint
open class UsersModal: DialogFragment(R.layout.add_user_modal), OnUserClickListener {
    private lateinit var binding: AddUserModalBinding
    val viewModel: ContactViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.add_user_modal, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = AddUserModalBinding.bind(view)
        viewModel.getUsers().observe(viewLifecycleOwner, Observer { result ->
            when(result){
                is Result.Loading -> {}
                is Result.Success -> {
                    if(result.data.isEmpty()){
                        return@Observer
                    }

                    binding.rvUser.adapter = UsersAdapter(result.data, this)

                }
                is Result.Failure -> {}
            }
        })
    }

    override fun onUserBtnClick(user: User) {

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                addChat(user)
            }
        }

    }

    private suspend fun addChat(user: User) {
        viewModel.addChat(user).observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Result.Loading -> {
                    //TODO
                }
                is Result.Success -> {
                    onDestroyView()
                }
                is Result.Failure -> {
                    Toast.makeText(
                        requireContext(),
                        "Error ${result.exception}",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.d("chat", "${result.exception}")
                }
            }
        })
    }

}