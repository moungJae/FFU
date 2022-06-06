package com.example.ffu.chatting


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ffu.databinding.ItemChattingArticleBinding
import com.example.ffu.utils.ChattingArticle


class ArticleAdapter(val onItemClicked: (ChattingArticle) -> Unit) : ListAdapter<ChattingArticle, ArticleAdapter.ViewHolder>(diffUtil) {
    inner class ViewHolder(private val binding: ItemChattingArticleBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(chattingArticleModel: ChattingArticle){
            binding.itemChattingArticleName.text=chattingArticleModel.Name
            binding.itemChattingArticleGender.text=chattingArticleModel.gender
            binding.itemChattingArticleMbti.text=chattingArticleModel.mbti


            Glide.with(binding.root)
                .load(chattingArticleModel.imageUri)
                .into(binding.itemChattingArticleThumbnailImageView)
            binding.root.setOnClickListener {
                onItemClicked(chattingArticleModel)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(ItemChattingArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }


    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ChattingArticle>() {
            override fun areItemsTheSame(oldItem: ChattingArticle, newItem: ChattingArticle): Boolean {
                return oldItem.Id == newItem.Id
            }

            override fun areContentsTheSame(oldItem: ChattingArticle, newItem: ChattingArticle): Boolean {
                return oldItem == newItem
            }
        }
    }

}