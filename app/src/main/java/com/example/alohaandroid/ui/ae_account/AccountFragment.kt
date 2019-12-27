package com.example.alohaandroid.ui.ae_account


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide

import com.example.alohaandroid.R
import com.example.alohaandroid.domain.remote.pojo.response.Hotline
import com.example.alohaandroid.domain.remote.pojo.response.InforUser
import com.example.alohaandroid.ui.a_adapter.AdapterHotline1
import com.example.alohaandroid.ui.a_viewmodel.DeleteHotlineViewModel
import com.example.alohaandroid.ui.a_viewmodel.GetAllHotlineByProjectViewModel
import com.example.alohaandroid.ui.a_viewmodel.GetUserViewModel
import com.example.alohaandroid.ui.ac_project.ProjectActivity
import com.example.alohaandroid.ui.a_base.BaseFragment
import com.example.alohaandroid.ui.ac_project.ChooseHotlineNumberFragment
import com.example.alohaandroid.utils.Common
import com.example.alohaandroid.utils.extension.SharePrefs
import com.example.alohaandroid.utils.extension.showToast
import com.example.alohaandroid.utils.extension.switchFragment
import kotlinx.android.synthetic.main.fragment_account.*

/**
 * A simple [Fragment] subclass.
 */
class AccountFragment : BaseFragment(), View.OnClickListener,
    AdapterHotline1.AdapterContactListener {

    private var click = 0
    private var click1 = 0
    private var click2 = 0
    private var click3 = 0
    var dialog: AlertDialog? = null

    private lateinit var adapterHotline1: AdapterHotline1
    private lateinit var getUserViewModel: GetUserViewModel
    private lateinit var getAllHotlineByProjectViewModel: GetAllHotlineByProjectViewModel
    private lateinit var deleteHotlineViewModel: DeleteHotlineViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initUserViewModel()
        initGetAllByProjectViewModel()
        initAdapter()
        initViewModelAddHotline()

        getUserViewModel.getuserInfor()

        tvSetting.setOnClickListener(this)
        tvListProject.setOnClickListener(this)
        tvUpdateEditInformation.setOnClickListener(this)
        tvListHotline.setOnClickListener(this)
        tvService.setOnClickListener(this)
        btnUpdate.setOnClickListener(this)
        btnAddHotline.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ll -> {
                closeKeyboard()
            }
            R.id.tvUpdateEditInformation -> {
                if (click == 0) {
                    tvUpdateEditInformation.setText(R.string.thong_tin_tai_khoan)
                    ivDownUpdateEditInformation.visibility = View.GONE
                    ivUpUpdateEditInformation.visibility = View.VISIBLE
                    llViewUpdateEditInformation.visibility = View.VISIBLE
                    llUpdateEditInformation.setBackgroundResource(R.drawable.bg_2)
                    click = 1
                } else {
                    click = 0
                    tvUpdateEditInformation.setText(R.string.cap_nhap_chinh_sua_thong_tin)
                    ivDownUpdateEditInformation.visibility = View.VISIBLE
                    ivUpUpdateEditInformation.visibility = View.GONE
                    llViewUpdateEditInformation.visibility = View.GONE
                    llUpdateEditInformation.setBackgroundResource(R.drawable.bg_3)
                }
            }
            R.id.tvListHotline -> {
                if (click1 == 0) {
                    getAllHotlineByProjectViewModel.getAllByProject(SharePrefs().getInstance()[Common.CODE, String::class.java]!!)
                    ivDownListHotline.visibility = View.GONE
                    ivUpListHotline.visibility = View.VISIBLE
                    llViewListHotline.visibility = View.VISIBLE
                    llListHotline.setBackgroundResource(R.drawable.bg_2)
                    click1 = 1
                } else {
                    click1 = 0
                    ivDownListHotline.visibility = View.VISIBLE
                    ivUpListHotline.visibility = View.GONE
                    llViewListHotline.visibility = View.GONE
                    llListHotline.setBackgroundResource(R.drawable.bg_3)
                }
            }
            R.id.tvService -> {
                if (click2 == 0) {
                    ivDownService.visibility = View.GONE
                    ivUpService.visibility = View.VISIBLE
                    llViewService.visibility = View.VISIBLE
                    llService.setBackgroundResource(R.drawable.bg_2)
                    click2 = 1
                } else {
                    click2 = 0
                    ivDownService.visibility = View.VISIBLE
                    ivUpService.visibility = View.GONE
                    llViewService.visibility = View.GONE
                    llService.setBackgroundResource(R.drawable.bg_3)
                }
            }
            R.id.tvSetting -> {
                startActivity(Intent(activity, SettingActivity::class.java))
            }
            R.id.tvListProject -> {
                startActivity(Intent(activity, ProjectActivity::class.java))
            }
            R.id.btnUpdate -> {
                if (click3 == 0) {
                    click3 = 1
                    etFullName.isEnabled = true
                    etAddress.isEnabled = true
                    etContactPhoneNumber.isEnabled = true
                    etContactEmail.isEnabled = true
                    btnUpdate.setText(R.string.luu_thay_doi)
                } else {
                    click3 = 0
                    btnUpdate.setText(R.string.cap_nhap_thong_tin)
                    showToast("đang hoàn thiện")
                }
            }
            R.id.btnAddHotline -> {
                val chooseHotlineNumberFragment = ChooseHotlineNumberFragment()
//                            chooseHotlineNumberFragment.arguments = bundle
                switchFragment(chooseHotlineNumberFragment, R.id.fl)
            }
        }
    }

    fun initUserViewModel() {
        activity?.let {
            getUserViewModel =
                ViewModelProviders.of(this).get(GetUserViewModel::class.java).apply {
                    userInfor.observe(this@AccountFragment, Observer {
                        it?.let {
                            getDataUser(it)
                        }
                    })

                    loadingStatus.observe(this@AccountFragment, Observer {
                        showOrHideProgressDialog(it)
                    })
                }
        }
    }

    @SuppressLint("SetTextI18n")
    fun getDataUser(data: InforUser) {
        tvName.text = data.fullName
        tvPhone.text = data.phone
        tvMoney.text = data.totalMoney.toString() + getString(R.string.vnd)
        tvEmail.text = "Chưa trả về"
        tvPackOfData.text = "Chưa trả về"
        Glide.with(this).load(data.avatar).placeholder(R.drawable.iv_call).into(civAvatar)
        etFullName.setText(data.fullName)
        etAddress.setText("Chưa trả về")
        etContactPhoneNumber.setText(data.phone)
        etContactEmail.setText("Chưa trả về")
        tvServiceUse.text = "Chưa trả về"
    }

    private fun closeKeyboard() {
        val view = activity!!.currentFocus
        if (view != null) {
            val imm =
                activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun initGetAllByProjectViewModel() {
        getAllHotlineByProjectViewModel =
            ViewModelProviders.of(this).get(GetAllHotlineByProjectViewModel::class.java).apply {
                list.observe(this@AccountFragment, Observer {
                    it?.let {
                        adapterHotline1.setData(it)
                    }
                })
                loadingStatus.observe(this@AccountFragment, Observer {
                    it.let {
                        showOrHideProgressDialog(it)
                    }
                })
            }
    }

    private fun initAdapter() {
        adapterHotline1 = AdapterHotline1(mutableListOf())
        adapterHotline1.setListener(this)
        rvNumberHotline.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = adapterHotline1
        }
    }

    override fun onclickDelete(hotline: Hotline) {
        val builder: AlertDialog.Builder =
            AlertDialog.Builder(activity!!)
        val inflater =
            activity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view1: View = inflater.inflate(R.layout.dialog_delete, null)
        builder.setView(view1)
        builder.setCancelable(false)
        dialog = builder.create()
        dialog!!.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        var btnYes: Button = view1.findViewById(R.id.btnYes)
        var btnNo: Button = view1.findViewById(R.id.btnNo)

        btnYes.setOnClickListener {
            deleteHotlineViewModel.DeleteHotline(
                SharePrefs().getInstance()[Common.ID_PROJECT, Int::class.java]!!,
                hotline.id.toString()
            )
            dialog?.dismiss()
        }

        btnNo.setOnClickListener {
            dialog!!.dismiss()
        }
        val layoutManager =
            LinearLayoutManager(activity?.applicationContext)
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL)
        dialog!!.show()
    }

    private fun initViewModelAddHotline() {
        activity?.let {
            deleteHotlineViewModel =
                ViewModelProviders.of(this).get(DeleteHotlineViewModel::class.java).apply {
                    Success.observe(this@AccountFragment, Observer {
                        it?.let {
                            if (it) {
                                getAllHotlineByProjectViewModel.getAllByProject(SharePrefs().getInstance()[Common.CODE, String::class.java]!!)
                                showToast(R.string.thanh_cong)
                            }
                        }
                    })
                    message.observe(this@AccountFragment, Observer {
                        it?.let {
                            //                        showToast(it)
                        }
                    })
                    loadingStatus.observe(this@AccountFragment, Observer {
                        showOrHideProgressDialog(it)
                    })
                }
        }
    }


}
