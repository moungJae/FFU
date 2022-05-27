package com.example.ffu.chatdetail

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ffu.UserInformation
import com.example.ffu.R
import com.example.ffu.UserInformation.Companion.URI
import com.example.ffu.chatdetail.ChatItem
import com.example.ffu.databinding.ItemChatLeftBinding
import com.example.ffu.databinding.ItemChatRightBinding
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class ChatItemAdapter : ListAdapter<ChatItem, RecyclerView.ViewHolder>(diffUtil) {

    inner class leftViewHolder(private var binding: ItemChatLeftBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(chatItem: ChatItem) {
            binding.itemChatLeftName.text = chatItem.senderName
            binding.itemChatLeftDate.text = chatItem.sendDate
            binding.itemChatLeftMessage.text = chatItem.message

            Glide.with(binding.root)
                .load(URI[chatItem.senderId])
                .into(binding.itemChatLeftImage)
        }

    }


    inner class rightViewHolder(private var binding: ItemChatRightBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(chatItem: ChatItem) {
            binding.itemChatRightDate.text = chatItem.sendDate
            binding.itemChatRightMessage.text = chatItem.message
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  RecyclerView.ViewHolder {
        when (viewType) {
            ChatItem.LEFT_TYPE -> {
                //view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_left, parent, false)
                return leftViewHolder(ItemChatLeftBinding.inflate(LayoutInflater.from(parent.context), parent, false))
            }
        }
        return rightViewHolder(ItemChatRightBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val obj = currentList[position]
        when (obj.type) {
            ChatItem.LEFT_TYPE ->  (holder as leftViewHolder).bind(obj)
            ChatItem.RIGHT_TYPE ->  (holder as rightViewHolder).bind(obj)
        }

    }

    // onCreateViewHolder의 viewType 반환
    override fun getItemViewType(position: Int): Int {
        return currentList[position].type
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ChatItem>() {
            override fun areItemsTheSame(oldItem: ChatItem, newItem: ChatItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: ChatItem, newItem: ChatItem): Boolean {
                return oldItem == newItem
            }

        }
    }

}