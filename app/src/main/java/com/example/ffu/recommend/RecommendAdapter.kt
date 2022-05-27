package com.example.ffu.recommend

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ffu.UserInformation

import com.example.ffu.chatting.ArticleModel


import com.example.ffu.databinding.UsersArticleBinding

class RecommendAdapter: ListAdapter<RecommendArticleModel, RecommendAdapter.ViewHolder>(diffUtil) {
    inner class ViewHolder(private val binding: UsersArticleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(recommendArticleModel: RecommendArticleModel) {

            binding.recommendNickname.text = recommendArticleModel.nickName
            binding.recommendAge.text = recommendArticleModel.age
            binding.recommendMbti.text = recommendArticleModel.mbti

            if (recommendArticleModel.imageUrl.isNotEmpty()) {
                Glide.with(binding.recommendUserImage)
                    .load(recommendArticleModel.imageUrl)
                    .into(binding.recommendUserImage)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendAdapter.ViewHolder {
        return ViewHolder(UsersArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecommendAdapter.ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<RecommendArticleModel>() {
            override fun areItemsTheSame(oldItem: RecommendArticleModel, newItem: RecommendArticleModel): Boolean {
                return oldItem.nickName == newItem.nickName
            }

            override fun areContentsTheSame(oldItem: RecommendArticleModel, newItem: RecommendArticleModel): Boolean {
                return oldItem == newItem
            }

        }
    }
}