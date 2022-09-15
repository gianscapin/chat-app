package com.gscapin.chattapp.ui.chat.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.gscapin.chattapp.core.BaseViewHolder
import com.gscapin.chattapp.core.show
import com.gscapin.chattapp.data.model.Message
import com.gscapin.chattapp.databinding.MessagesBinding

class ChatAdapter(
    private val messageList: List<Message>
) : RecyclerView.Adapter<BaseViewHolder<*>>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val itemBinding =
            MessagesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MessageViewHolder(itemBinding, parent.context)
    }

    private inner class MessageViewHolder(
        val itemBinding: MessagesBinding,
        val context: Context
    ) : BaseViewHolder<Message>(itemBinding.root) {
        override fun bind(item: Message) {
            setMessage(item)
        }

        private fun setMessage(item: Message) {
            val idUserMessage = item.idUser
            val idCurrentUser = FirebaseAuth.getInstance().currentUser!!.uid
            if (idUserMessage == idCurrentUser) {
                itemBinding.layoutMessagesCurrent.show()
                itemBinding.chatText.text = item.text
                itemBinding.chatTime.text = "${item.date?.hours.toString()}:${item.date?.minutes}"
            } else {
                itemBinding.layoutMessagesUser.show()
                itemBinding.chatTextUser.text = item.text
                itemBinding.chatTimeUser.text =
                    "${item.date?.hours.toString()}:${item.date?.minutes}"
            }
        }

    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when(holder){
            is MessageViewHolder -> holder.bind(messageList[position])
        }
    }

    override fun getItemCount(): Int = messageList.size
}