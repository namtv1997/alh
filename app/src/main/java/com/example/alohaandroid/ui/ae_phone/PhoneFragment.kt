package com.example.alohaandroid.ui.ae_phone


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.alohaandroid.R
import com.example.alohaandroid.ui.a_adapter.AdapterContactPager
import com.example.alohaandroid.ui.a_base.BaseFragment
import kotlinx.android.synthetic.main.fragment_phone.*

class PhoneFragment : BaseFragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_phone, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

//        (activity as MainActivity).checkFragment(this)

        val adapter = AdapterContactPager(
            childFragmentManager
        )
        adapter.addFragment(DialFragment(), getString(R.string.quay_so))
        adapter.addFragment(CallHistoryFragment(), getString(R.string.lich_su_cuoc_goi))
        adapter.addFragment(PhonebookFragment(),  getString(R.string.danh_ba))
        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)


    }



}
