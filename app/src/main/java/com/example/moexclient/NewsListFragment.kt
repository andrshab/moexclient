package com.example.moexclient

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController


class NewsListFragment : Fragment() {

    lateinit var button: Button
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_news_list, container, false)
        button = root.findViewById(R.id.go_button)
        button.setOnClickListener { findNavController().navigate(R.id.action_newsListFragment_to_newsFragment) }
        return root
    }


}