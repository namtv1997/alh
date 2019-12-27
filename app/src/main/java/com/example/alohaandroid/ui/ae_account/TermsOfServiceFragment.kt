package com.example.alohaandroid.ui.ae_account


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.alohaandroid.R
import com.example.alohaandroid.ui.a_base.BaseFragment
import kotlinx.android.synthetic.main.fragment_terms_of.*

/**
 * A simple [Fragment] subclass.
 */
class TermsOfServiceFragment : BaseFragment(), View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_terms_of, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        imBack.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.imBack -> {
                activity?.onBackPressed()
            }
        }
    }


}
