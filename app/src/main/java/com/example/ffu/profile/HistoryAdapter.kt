package com.example.ffu.chatting


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.ffu.databinding.ItemHistoryBinding
import com.example.ffu.utils.History
import com.example.ffu.utils.History.Companion.MATCH_TYPE
import com.example.ffu.utils.History.Companion.RECEIVE_TYPE
import com.example.ffu.utils.History.Companion.SEND_TYPE

class HistoryAdapter() : ListAdapter<History, HistoryAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val binding: ItemHistoryBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(historyModel: History){

            when(historyModel.type){
                SEND_TYPE->{
                    binding.itemHistoryMessage.text=historyModel.name+"님에게 like 를 보냈습니다."
                }
                RECEIVE_TYPE->{
                    binding.itemHistoryMessage.text=historyModel.name+"님이 like 를 보냈습니다."
                }
                MATCH_TYPE->{
                    binding.itemHistoryMessage.text=historyModel.name+"님과 match 되었습니다."
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
        val diffUtil = object : DiffUtil.ItemCallback<History>() {
            override fun areItemsTheSame(oldItem: History, newItem: History): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: History, newItem: History): Boolean {
                return oldItem == newItem
            }
        }
    }

}