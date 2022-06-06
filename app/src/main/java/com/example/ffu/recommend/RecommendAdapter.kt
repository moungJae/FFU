package com.example.ffu.recommend

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ffu.databinding.ItemLikeinfoArticleBinding
import com.example.ffu.utils.RecommendArticle

class RecommendAdapter(val onItemClicked: (RecommendArticle) -> Unit) : ListAdapter<RecommendArticle, RecommendAdapter.ViewHolder>(diffUtil) {
    inner class ViewHolder(private val binding: ItemLikeinfoArticleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(recommendArticleModel: RecommendArticle) {

            binding.itemLikeinfoArticleName.text = recommendArticleModel.nickName
            binding.itemLikeinfoArticleAgeJob.text = recommendArticleModel.ageJob
            binding.itemLikeinfoArticleIntroMe.text = recommendArticleModel.introMe

            if (recommendArticleModel.imageUrl.isNotEmpty()) {
                Glide.with(binding.thumbnailImageView)
                    .load(recommendArticleModel.imageUrl)
                    .into(binding.thumbnailImageView)
            }
            binding.root.setOnClickListener {
                onItemClicked(recommendArticleModel)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendAdapter.ViewHolder {
        return ViewHolder(ItemLikeinfoArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: RecommendAdapter.ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<RecommendArticle>() {
            override fun areItemsTheSame(oldItem: RecommendArticle, newItem: RecommendArticle): Boolean {
                return oldItem.nickName == newItem.nickName
            }

            override fun areContentsTheSame(oldItem: RecommendArticle, newItem: RecommendArticle): Boolean {
                return oldItem == newItem
            }

        }
    }
}