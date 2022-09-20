package com.gscapin.chattapp.ui.contacts.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gscapin.chattapp.core.BaseViewHolder
import com.gscapin.chattapp.data.model.User
import com.gscapin.chattapp.databinding.UserBinding

class UsersAdapter(
    private val usersList: List<User>,
    private val onUserClickListener: OnUserClickListener
) : RecyclerView.Adapter<BaseViewHolder<*>>() {
    private var userClickListener: OnUserClickListener? = null

    init {
        userClickListener = onUserClickListener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val itemBinding = UserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(itemBinding, parent.context)
    }

    private inner class UserViewHolder(val itemBinding: UserBinding, val context: Context): BaseViewHolder<User>(itemBinding.root) {
        override fun bind(item: User) {
            setName(item)
            clickUser(item)
        }

        private fun clickUser(item: User) {
            itemBinding.userModal.setOnClickListener {
                userClickListener?.onUserBtnClick(item)
            }
        }

        private fun setName(item: User) {
            itemBinding.user.text = item.username
        }

    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when(holder){
            is UserViewHolder -> holder.bind(usersList[position])
        }
    }

    override fun getItemCount(): Int = usersList.size

}

interface OnUserClickListener {
    fun onUserBtnClick(user: User)
}