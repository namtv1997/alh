package com.example.alohaandroid.ui.ae_phone.infor_contact

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.alohaandroid.R
import com.example.alohaandroid.domain.remote.pojo.response.Contact
import com.example.alohaandroid.domain.remote.pojo.response.InforContact
import com.example.alohaandroid.ui.a_adapter.AdapterHistoryCall
import com.example.alohaandroid.ui.a_base.BaseActivity
import com.example.alohaandroid.ui.a_viewmodel.GetallHistoryCallViewModel
import com.example.alohaandroid.utils.linphone.LinphoneManager
import kotlinx.android.synthetic.main.activity_infor_contact.*
import kotlinx.android.synthetic.main.toolbar_infor_contact.*

class InforContactActivity : BaseActivity(), View.OnClickListener {

    private lateinit var getContactByIdViewModel: GetContactByIdViewModel
    private lateinit var getallHistoryCallViewModel: GetallHistoryCallViewModel

    private lateinit var datacontact: Contact
    private lateinit var adapterHistoryCall: AdapterHistoryCall

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_infor_contact)
        initgetContactByIdViewModel()
        initgetallHistoryCallViewModel()
        if (intent != null) {
            if (intent.hasExtra("CONTACT")) {
                datacontact = intent.getParcelableExtra("CONTACT")
                tvName.text = datacontact.fullName
                tvNumber.text = datacontact.phone
                tvEmail.text = datacontact.email
                Glide.with(this).load(datacontact.avatar).centerCrop().placeholder(R.drawable.iv_call).into(ivAvatar)
                datacontact.id?.let { getContactByIdViewModel.getContactInfor(it) }
            }

        }
        getallHistoryCallViewModel.getAllHistoryCall()
        initAdapter()
        ivBack.setOnClickListener(this)
        ivCall.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            ivBack.id -> finish()

            ivCall.id -> {
                LinphoneManager.getCallManager().newOutgoingCall(tvNumber.text.toString(), null)
            }

        }
    }

    private fun initgetContactByIdViewModel() {
        getContactByIdViewModel =
            ViewModelProviders.of(this).get(GetContactByIdViewModel::class.java).apply {
                contactInfor.observe(this@InforContactActivity, Observer {
                    it?.let {
                        setDataConTact(it)
                    }
                })

            }

    }

    private fun initgetallHistoryCallViewModel() {
        getallHistoryCallViewModel =
            ViewModelProviders.of(this).get(GetallHistoryCallViewModel::class.java).apply {
                listHistoryCall.observe(this@InforContactActivity, Observer {
                    it?.let {
                        adapterHistoryCall.setData(it)
                    }
                })

            }

    }

    private fun setDataConTact(inforContact: InforContact) {
        tvName.text = inforContact.fullName
        tvNumber.text = inforContact.phone
        tvEmail.text = inforContact.email
        Glide.with(this).load(inforContact.avatar).centerCrop().placeholder(R.drawable.iv_call)
            .into(ivAvatar)
    }

    private fun initAdapter() {
        adapterHistoryCall =
            AdapterHistoryCall(
                mutableListOf()
            )
        rvHistorycall.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = adapterHistoryCall
        }
    }


}
