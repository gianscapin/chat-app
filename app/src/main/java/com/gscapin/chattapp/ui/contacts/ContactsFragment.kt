package com.gscapin.chattapp.ui.contacts

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.gscapin.chattapp.R
import com.gscapin.chattapp.core.Result
import com.gscapin.chattapp.core.UsersModal
import com.gscapin.chattapp.core.hide
import com.gscapin.chattapp.core.show
import com.gscapin.chattapp.data.model.ContactMessage
import com.gscapin.chattapp.data.model.User
import com.gscapin.chattapp.databinding.FragmentContactsBinding
import com.gscapin.chattapp.presentation.chat.ChatViewModel
import com.gscapin.chattapp.presentation.contact.ContactViewModel
import com.gscapin.chattapp.ui.contacts.adapter.ContactsAdapter
import com.gscapin.chattapp.ui.contacts.adapter.OnContactClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ContactsFragment : Fragment(R.layout.fragment_contacts), OnContactClickListener {
    private lateinit var binding: FragmentContactsBinding

    val viewModel: ContactViewModel by viewModels()

    val chatViewModel: ChatViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentContactsBinding.bind(view)

        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        val defaultValue = 0

        if(sharedPref?.getInt("nightMode", defaultValue) == 1){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        logOut()

        settingsBtn()

        fabAction()

        getContacts()

    }

    private fun fabAction() {
        binding.fabAdd.setOnClickListener {
            showModal()
        }
    }

    private fun settingsBtn() {
        binding.settingBtn.setOnClickListener {
            findNavController().navigate(R.id.action_contactsFragment_to_settingsFragment)
        }
    }

    private fun logOut() {
        binding.logOut.setOnClickListener {
            Log.d("log out", "btn log out action")
            context?.let { it1 ->
                MaterialAlertDialogBuilder(it1).setTitle("Desea cerrar sesión?")
                    .setPositiveButton("Si", DialogInterface.OnClickListener { dialogInterface, i ->
                        FirebaseAuth.getInstance().signOut()
                        findNavController().navigate(R.id.action_contactsFragment_to_welcomeFragment)
                    })
                    .setNegativeButton("No", null).show()
            }

        }
    }

    private fun getContacts() {
        viewModel.getContactList()
        lifecycleScope.launchWhenCreated {
            viewModel.getListContacts().collect { result ->
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
                            return@collect
                        } else {
                            binding.emptyContacts.hide()
                            binding.rvContacts.show()
                            binding.rvContacts.adapter = ContactsAdapter(
                                result.data,
                                this@ContactsFragment,
                                chatViewModel
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
            }
        }
    }

    fun showModal() {
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

    override fun onLongPressBtnClick(contact: ContactMessage) {
        context?.let { it1 ->
            MaterialAlertDialogBuilder(it1).setMessage("Se eliminarán todos los mensajes.").setTitle("Desea eliminar el contacto?")
                .setPositiveButton("Si", DialogInterface.OnClickListener { dialogInterface, i ->
                    deleteContact(contact)
                })
                .setNegativeButton("No", null).show()
        }

    }

    private fun deleteContact(contact: ContactMessage) {
        lifecycleScope.launch {
            viewModel.deleteChat(contact).observe(viewLifecycleOwner, Observer { result ->
                when (result) {
                    is Result.Loading -> {}
                    is Result.Success -> {}
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
}