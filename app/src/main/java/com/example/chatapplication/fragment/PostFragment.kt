package com.example.chatapplication.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.chatapplication.databinding.FragmentPostBinding
import com.example.chatapplication.databinding.FragmentProfileBinding

class PostFragment:Fragment() {

    private val binding:FragmentPostBinding by lazy {
        FragmentPostBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initView()
        bindView()
    }


    private fun initView(){

    }

    private fun bindView(){

    }

}