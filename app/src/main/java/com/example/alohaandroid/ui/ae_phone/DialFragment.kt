package com.example.alohaandroid.ui.ae_phone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alohaandroid.R
import com.example.alohaandroid.domain.remote.pojo.response.Hotline
import com.example.alohaandroid.ui.a_adapter.AdapterHotline2
import com.example.alohaandroid.ui.a_base.BaseFragment
import com.example.alohaandroid.ui.a_viewmodel.GetAllHotlineByProjectViewModel
import com.example.alohaandroid.utils.Common
import com.example.alohaandroid.utils.extension.SharePrefs
import com.example.alohaandroid.utils.linphone.AddressText
import com.example.alohaandroid.utils.linphone.Digit
import com.example.alohaandroid.utils.linphone.LinphoneManager
import kotlinx.android.synthetic.main.fragment_dial.*
import java.util.*


class DialFragment : BaseFragment(), View.OnClickListener, View.OnLongClickListener,
    AdapterHotline2.AdapterContactListener, AddressText.AddressChangedListener {

    var number: String = ""
    private var numberHotline: String? = null
    var dialog: AlertDialog? = null
    private lateinit var adapterHotline2: AdapterHotline2

    private lateinit var getAllHotlineByProjectViewModel: GetAllHotlineByProjectViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dial, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initGetAllByProjectViewModel()
        initAdapter()

        numberHotline = SharePrefs().getInstance()[Common.NUMBER_HOTLINE, String::class.java]
        if (numberHotline.isNullOrEmpty()) {
            getAllHotlineByProjectViewModel.getAllByProject(SharePrefs().getInstance()[Common.CODE, String::class.java]!!)
        } else {
            tvNumberHotLine.text = numberHotline
        }


        btnNumber0.setOnClickListener(this)
        btnNumber1.setOnClickListener(this)
        btnNumber2.setOnClickListener(this)
        btnNumber3.setOnClickListener(this)
        btnNumber4.setOnClickListener(this)
        btnNumber5.setOnClickListener(this)
        btnNumber6.setOnClickListener(this)
        btnNumber7.setOnClickListener(this)
        btnNumber8.setOnClickListener(this)
        btnNumber9.setOnClickListener(this)
        btnNumberSharp.setOnClickListener(this)
        btnNumberStar.setOnClickListener(this)
//        btnCall.setOnClickListener(this)
        ivDelete.setOnClickListener(this)
        llHotline.setOnClickListener(this)
        vElevation.setOnClickListener(this)

        ivDelete.setOnLongClickListener(this)

        view?.let {
            initUI(it)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnNumber0 -> {
                number = etNumber.text.toString()
                number += 0
                etNumber.setText(number)
            }
            R.id.btnNumber1 -> {
                number = etNumber.text.toString()
                number += 1
                etNumber.setText(number)
            }
            R.id.btnNumber2 -> {
                number = etNumber.text.toString()
                number += 2
                etNumber.setText(number)
            }
            R.id.btnNumber3 -> {
                number = etNumber.text.toString()
                number += 3
                etNumber.setText(number)
            }
            R.id.btnNumber4 -> {
                number = etNumber.text.toString()
                number += 4
                etNumber.setText(number)
            }
            R.id.btnNumber5 -> {
                number = etNumber.text.toString()
                number += 5
                etNumber.setText(number)
            }
            R.id.btnNumber6 -> {
                number = etNumber.text.toString()
                number += 6
                etNumber.setText(number)
            }
            R.id.btnNumber7 -> {
                number = etNumber.text.toString()
                number += 7
                etNumber.setText(number)
            }
            R.id.btnNumber8 -> {
                number = etNumber.text.toString()
                number += 8
                etNumber.setText(number)
            }
            R.id.btnNumber9 -> {
                number = etNumber.text.toString()
                number += 9
                etNumber.setText(number)
            }
            R.id.btnNumberStar -> {
                number = etNumber.text.toString()
                number += "*"
                etNumber.setText(number)
            }
            R.id.btnNumberSharp -> {
                number = etNumber.text.toString()
                number += "#"
                etNumber.setText(number)
            }
//            R.id.btnCall -> {
//
//            }
            R.id.ivDelete -> {
                number = etNumber.text.toString()
                if (number.isNotEmpty()) {
                    number = number.substring(0, number.length - 1)
                    etNumber.setText(number)
                }
            }
            R.id.llHotline -> {
                getAllHotlineByProjectViewModel.getAllByProject(SharePrefs().getInstance()[Common.CODE, String::class.java]!!)
                rvChooseNumberHotline.visibility = View.VISIBLE
                vElevation.visibility = View.VISIBLE
            }
            R.id.vElevation -> {

            }
        }
    }

    override fun onLongClick(v: View?): Boolean {
        when (v?.id) {
            R.id.ivDelete -> {
                etNumber.setText("")
            }
        }
        return true
    }

    private fun initGetAllByProjectViewModel() {
        getAllHotlineByProjectViewModel =
            ViewModelProviders.of(this).get(GetAllHotlineByProjectViewModel::class.java).apply {
                list.observe(this@DialFragment, Observer {
                    it?.let {
                        adapterHotline2.setData(it)
                        tvNumberHotLine.text = it[0].phoneNumber
                        SharePrefs().getInstance().put(Common.NUMBER_HOTLINE, it[0].phoneNumber)
                    }
                })
                loadingStatus.observe(this@DialFragment, Observer {
                    it.let {
                        showOrHideProgressDialog(it)
                    }
                })
            }
    }

    private fun initAdapter() {
        adapterHotline2 = AdapterHotline2(mutableListOf())
        adapterHotline2.setListener(this)
        rvChooseNumberHotline.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = adapterHotline2
        }
    }

    override fun onclickHotline(hotline: Hotline) {
        SharePrefs().getInstance().put(Common.NUMBER_HOTLINE, hotline.phoneNumber)
        tvNumberHotLine.text = hotline.phoneNumber
        rvChooseNumberHotline.visibility = View.GONE
        vElevation.visibility = View.GONE
    }

    private fun initUI(view: View) {
        etNumber.setAddressListener(this)
//
//        btnDeleteNumber.setAddressWidget(edtInputNumber)

        btnCall.setAddressWidget(etNumber)


//        ivAddNumber.setEnabled(false)
//        ivAddNumber.setOnClickListener(
//            View.OnClickListener {
//                val intent = Intent(activity, ContactsActivity::class.java)
//                intent.putExtra("EditOnClick", true)
//                intent.putExtra("SipAddress", edtInputNumber.getText().toString())
//                startActivity(intent)
//            })

        setUpNumpad(view)
    }

    fun setUpNumpad(view: View?) {
        if (view == null) return
        for (v in retrieveChildren((view as ViewGroup?)!!, Digit::class.java)) {
            v.setAddressWidget(etNumber)
        }
    }

    private fun <T> retrieveChildren(viewGroup: ViewGroup, clazz: Class<T>): Collection<T> {
        val views = ArrayList<T>()
        for (i in 0 until viewGroup.childCount) {
            val v = viewGroup.getChildAt(i)
            if (v is ViewGroup) {
                views.addAll(retrieveChildren(v, clazz))
            } else {
                if (clazz.isInstance(v)) views.add(clazz.cast(v))
            }
        }
        return views
    }

    override fun onAddressChanged() {
        val core = LinphoneManager.getCore()
//        ivAddNumber.setEnabled(
//            core != null && core.callsNb > 0 || !edtInputNumber.getText().toString().equals("")
//        )
    }


}
