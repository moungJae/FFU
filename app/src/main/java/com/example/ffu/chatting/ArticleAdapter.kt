package com.example.ffu.chatting


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ffu.databinding.ItemArticleBinding
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class ArticleAdapter(val onItemClicked: (ArticleModel) -> Unit) : ListAdapter<ArticleModel, ArticleAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val binding: ItemArticleBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(articleModel: ArticleModel){
            binding.itemArticleName.text=articleModel.Name
            binding.itemArticleGender.text=articleModel.Gender
            binding.itemArticleBirth.text=articleModel.Birth

            Glide.with(binding.root)
                .load(articleModel.imageUri)
                .into(binding.thumbnailImageView)

            /*
            storage = FirebaseStorage.getInstance()
            pathReference = storage.reference
            pathReference.child("photo/${articleModel.Id}/real.jpg").downloadUrl.addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    Glide.with(binding.root)
                        .load(task.result)
                        .into(binding.thumbnailImageView)
                }
            }*/
            /*
            Glide.with(binding.thumbnailImageView)
                .load(articleModel.imageUrl.toString())
                .into(binding.thumbnailImageView)
            */
            /*
            Glide.with(binding.thumbnailImageView)
                .load(articleModel.imageUrl)
                .into(binding.thumbnailImageView)
            */
            binding.root.setOnClickListener {
                onItemClicked(articleModel)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ArticleModel>() {
            override fun areItemsTheSame(oldItem: ArticleModel, newItem: ArticleModel): Boolean {
                return oldItem.Id == newItem.Id
            }

            override fun areContentsTheSame(oldItem: ArticleModel, newItem: ArticleModel): Boolean {
                return oldItem == newItem
            }
        }
    }

}