package com.example.ffu.chatting

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ffu.R
import com.example.ffu.databinding.FragmentChattingBinding
import com.google.firebase.database.DatabaseReference

class ChattingFragment: Fragment(R.layout.fragment_chatting) {
    private lateinit var articleDB: DatabaseReference
    private var binding : FragmentChattingBinding? = null
    private lateinit var articleAdapter: ArticleAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentHomelistBinding = FragmentChattingBinding.bind(view)
        binding = fragmentHomelistBinding

        articleAdapter = ArticleAdapter()
        articleAdapter.submitList(mutableListOf<ArticleModel>().apply {
            add(ArticleModel("0","aaaa",1000000,"100",""))
        })

        fragmentHomelistBinding.articleRecyclerView.layoutManager = LinearLayoutManager(context)
        fragmentHomelistBinding.articleRecyclerView.adapter = articleAdapter

    }
}