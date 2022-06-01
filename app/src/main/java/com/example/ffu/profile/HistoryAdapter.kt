package com.example.ffu.chatting


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ffu.databinding.ItemArticleBinding
import com.example.ffu.databinding.ItemHistoryBinding
import com.example.ffu.profile.HistoryModel
import com.example.ffu.profile.HistoryModel.Companion.MATCH_TYPE
import com.example.ffu.profile.HistoryModel.Companion.RECEIVE_TYPE
import com.example.ffu.profile.HistoryModel.Companion.SEND_TYPE


class HistoryAdapter() : ListAdapter<HistoryModel, HistoryAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val binding: ItemHistoryBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(historyModel: HistoryModel){

            when(historyModel.type){
                SEND_TYPE->{
                    binding.itemHistoryMessage.text=historyModel.name+"님에게 like를 보냈습니다."
                }
                RECEIVE_TYPE->{
                    binding.itemHistoryMessage.text=historyModel.name+"님이 like를 보냈습니다."
                }
                MATCH_TYPE->{
                    binding.itemHistoryMessage.text=historyModel.name+"님과 match되었습니다."
                }
            }
            binding.itemHistoryDate.text=historyModel.time

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<HistoryModel>() {
            override fun areItemsTheSame(oldItem: HistoryModel, newItem: HistoryModel): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: HistoryModel, newItem: HistoryModel): Boolean {
                return oldItem == newItem
            }
        }
    }

}