package com.gscapin.chattapp.ui.chat

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.gscapin.chattapp.R
import com.gscapin.chattapp.core.Result
import com.gscapin.chattapp.databinding.FragmentChatBinding
import com.gscapin.chattapp.presentation.chat.ChatViewModel
import com.gscapin.chattapp.ui.chat.adapter.ChatAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatFragment : Fragment(R.layout.fragment_chat) {

    private lateinit var binding: FragmentChatBinding
    private val args by navArgs<ChatFragmentArgs>()
    val viewModel: ChatViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentChatBinding.bind(view)

        setupToolbar()

        getLatestMessages()

        binding.inputMessage.addTextChangedListener {
            binding.sendMessageButton.isEnabled = binding.inputMessage.text!!.isNotEmpty()
        }

        binding.sendMessageButton.setOnClickListener {
            val text = binding.inputMessage.text.toString().trim()
            viewModel.sendMessage(text = text, idChat = args.idChat).observe(viewLifecycleOwner, Observer { result ->
                when(result){
                    is Result.Loading -> {}
                    is Result.Success -> {
                        //binding.inputMessage.clearComposingText()
                        binding.inputMessage.setText("")
                        binding.inputMessage.clearFocus()
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

    private fun getLatestMessages() {
        viewModel.getMessages(args.idChat)
        lifecycleScope.launchWhenCreated {
            viewModel.getListMessages().collect { result ->
                when (result) {
                    is Result.Loading -> {}
                    is Result.Success -> {
                        if (result.data.isEmpty()) {
                            return@collect
                        }
                        val chatAdapter: ChatAdapter = ChatAdapter(result.data)
                        binding.rvChat.adapter = chatAdapter
                        binding.rvChat.scrollToPosition(chatAdapter.itemCount - 1)
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
            }
        }
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        binding.toolbar.title = args.nameUser
        Glide.with(requireContext()).load(args.photoUser).centerCrop().into(binding.imageContact)
    }

}