package com.gscapin.chattapp.ui.contacts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.gscapin.chattapp.R
import com.gscapin.chattapp.core.Result
import com.gscapin.chattapp.core.UsersModal
import com.gscapin.chattapp.core.hide
import com.gscapin.chattapp.core.show
import com.gscapin.chattapp.data.model.ContactMessage
import com.gscapin.chattapp.data.model.User
import com.gscapin.chattapp.databinding.FragmentContactsBinding
import com.gscapin.chattapp.presentation.contact.ContactViewModel
import com.gscapin.chattapp.ui.contacts.adapter.ContactsAdapter
import com.gscapin.chattapp.ui.contacts.adapter.OnContactClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactsFragment : Fragment(R.layout.fragment_contacts), OnContactClickListener {
    private lateinit var binding: FragmentContactsBinding

    val viewModel: ContactViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentContactsBinding.bind(view)

        binding.logoutBtn.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            findNavController().navigate(R.id.action_contactsFragment_to_welcomeFragment)
        }

        binding.fabAdd.setOnClickListener {
            showModal()
        }

        viewModel.getContactList().observe(viewLifecycleOwner, Observer { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBarContact.show()
                }
                is Result.Success -> {
                    binding.progressBarContact.hide()
                    binding.contacts.show()
                    if (result.data.isEmpty()) {
                        binding.emptyContacts.show()
                        binding.rvContacts.hide()
                        return@Observer
                    } else {
                        binding.emptyContacts.hide()
                        binding.rvContacts.show()
                        binding.rvContacts.adapter = ContactsAdapter(
                            result.data,
                            this@ContactsFragment
                        )
                    }
                }
                is Result.Failure -> {
                    binding.progressBarContact.hide()
                    Toast.makeText(
                        requireContext(),
                        "Error ${result.exception}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })

    }

    fun showModal(){
        val modalFragment = UsersModal()
        activity?.let { modalFragment.show(it.supportFragmentManager, "MY_BOTTOM_SHEET") }
    }

    override fun onContactBtnClick(contact: ContactMessage) {
        val action = ContactsFragmentDirections.actionContactsFragmentToChatFragment(
            photoUser = contact.user!!.userPhotoUrl,
            nameUser = contact.user.username,
            idChat = contact.idMessage!!
        )

        findNavController().navigate(action)
    }
}