package com.example.alohaandroid.ui.ae_phone


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.alohaandroid.R
import com.example.alohaandroid.ui.a_adapter.AdapterHistoryCall
import com.example.alohaandroid.ui.a_base.BaseFragment
import com.example.alohaandroid.ui.a_viewmodel.GetallHistoryCallViewModel
import kotlinx.android.synthetic.main.fragment_call_history.*

/**
 * A simple [Fragment] subclass.
 */
class CallHistoryFragment : BaseFragment() {

    private lateinit var getallHistoryCallViewModel: GetallHistoryCallViewModel

    private lateinit var adapterHistoryCall: AdapterHistoryCall

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_call_history, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initgetallHistoryCallViewModel()
        initAdapter()

        getallHistoryCallViewModel.getAllHistoryCall()
    }

    private fun initgetallHistoryCallViewModel() {
        getallHistoryCallViewModel =
            ViewModelProviders.of(this).get(GetallHistoryCallViewModel::class.java).apply {
                listHistoryCall.observe(this@CallHistoryFragment, Observer {
                    it?.let {
                        if (it.isEmpty()){
                            tvEmpty.visibility =View.VISIBLE
                        }else{
                            tvEmpty.visibility =View.GONE
                        }
                        adapterHistoryCall.setData(it)
                    }
                })

            }

    }

    private fun initAdapter() {
        adapterHistoryCall =
            AdapterHistoryCall(
                mutableListOf()
            )
        rvCallHistory.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = adapterHistoryCall
        }
    }

}
