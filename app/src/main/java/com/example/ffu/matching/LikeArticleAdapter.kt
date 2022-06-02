package com.example.ffu.matching

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ffu.databinding.ItemArticleBinding
import com.example.ffu.utils.LikeArticle


class LikeArticleAdapter(val onItemClicked: (LikeArticle) -> Unit) : ListAdapter<LikeArticle, LikeArticleAdapter.ViewHolder>(diffUtil) {
    inner class ViewHolder(private val binding: ItemArticleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(likeArticleModel: LikeArticle) {
            binding.itemArticleName.text=likeArticleModel.nickName
            binding.itemArticleGender.text=likeArticleModel.age
            binding.itemArticleBirth.text=likeArticleModel.mbti

            if (likeArticleModel.imageUrl.isNotEmpty()) {
                Glide.with(binding.root)
                    .load(likeArticleModel.imageUrl)
                    .into(binding.thumbnailImageView)
            }

            binding.root.setOnClickListener {
                onItemClicked(likeArticleModel)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LikeArticleAdapter.ViewHolder {
        return ViewHolder(ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: LikeArticleAdapter.ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<LikeArticle>() {
            override fun areItemsTheSame(oldItem:LikeArticle, newItem: LikeArticle): Boolean {
                return oldItem.nickName == newItem.nickName
            }

            override fun areContentsTheSame(oldItem: LikeArticle, newItem:LikeArticle): Boolean {
                return oldItem == newItem
            }

        }
    }
}