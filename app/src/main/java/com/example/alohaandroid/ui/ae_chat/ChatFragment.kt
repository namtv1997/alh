package com.example.alohaandroid.ui.ae_chat


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.alohaandroid.R
import com.example.alohaandroid.ui.a_base.BaseFragment
import com.example.alohaandroid.ui.ad_main.MainActivity
import kotlinx.android.synthetic.main.fragment_chat.*


class ChatFragment : BaseFragment(),View.OnClickListener {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        (activity as MainActivity).checkFragment(this)
        fabConTact.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            fabConTact.id->{
                startActivity(Intent(activity,ChatActivity::class.java))
            }
        }
    }
}
