package com.gscapin.chattapp.ui.contacts.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gscapin.chattapp.core.BaseViewHolder
import com.gscapin.chattapp.data.model.ContactMessage
import com.gscapin.chattapp.data.model.User
import com.gscapin.chattapp.databinding.ContactBinding
import com.gscapin.chattapp.presentation.chat.ChatViewModel

class ContactsAdapter(
    private val contactsList: List<ContactMessage>,
    private val onContactClickListener: OnContactClickListener,
    private val viewModel: ChatViewModel
) : RecyclerView.Adapter<BaseViewHolder<*>>() {

    private var contactClickListener: OnContactClickListener? = null

    init {
        contactClickListener = onContactClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val itemBinding = ContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContactViewHolder(itemBinding, parent.context)
    }

    private inner class ContactViewHolder(
        val itemBinding: ContactBinding,
        val context: Context
    ) : BaseViewHolder<ContactMessage>(itemBinding.root) {
        override fun bind(item: ContactMessage) {
            setImageContact(item)
            setNameContact(item)
            clickContactAction(item)
            setMessage(item)
        }

        private fun setMessage(item: ContactMessage) {


        }

        private fun clickContactAction(item: ContactMessage) {
            itemBinding.contactMessage.setOnClickListener {
                contactClickListener?.onContactBtnClick(item)
            }
            itemBinding.contactMessage.setOnLongClickListener {
                contactClickListener?.onLongPressBtnClick(item)
                true
            }
        }

        private fun setNameContact(item: ContactMessage) {
            itemBinding.nameContact.text = item.user?.username
        }

        private fun setImageContact(item: ContactMessage) {
            Glide.with(context).load(item.user?.userPhotoUrl).centerCrop().into(itemBinding.imageContact)
        }

    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when(holder){
            is ContactViewHolder -> holder.bind(contactsList[position])
        }
    }

    override fun getItemCount(): Int = contactsList.size
}

interface OnContactClickListener {
    fun onContactBtnClick(contact: ContactMessage)
    fun onLongPressBtnClick(contact: ContactMessage)
}
