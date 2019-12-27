package com.example.alohaandroid.ui.ac_project


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alohaandroid.R
import com.example.alohaandroid.domain.remote.pojo.response.Hotline
import com.example.alohaandroid.ui.a_adapter.AdapterHotline
import com.example.alohaandroid.ui.a_viewmodel.AddHotlineViewModel
import com.example.alohaandroid.ui.a_viewmodel.GetAllHotlineViewModel
import com.example.alohaandroid.ui.a_base.BaseFragment
import com.example.alohaandroid.ui.ad_main.MainActivity
import com.example.alohaandroid.utils.Common
import com.example.alohaandroid.utils.extension.SharePrefs
import com.example.alohaandroid.utils.extension.showToast
import kotlinx.android.synthetic.main.fragment_choose_hotline_number.*
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class ChooseHotlineNumberFragment : BaseFragment(), View.OnClickListener,
    AdapterHotline.AdapterContactListener {

    private lateinit var adapterHotline: AdapterHotline
    private lateinit var getAllHotlineViewModel: GetAllHotlineViewModel
    private lateinit var addHotlineViewModel: AddHotlineViewModel
    var idHotline: String? = null
    private var idProject: Int? = null
    var hotlineArrayList: ArrayList<Hotline> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_choose_hotline_number, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        idProject = SharePrefs().getInstance()[Common.ID_PROJECT, Int::class.java]

        initProjectViewModel()
        initAdapter()
        initViewModelAddHotline()

        btnStart.setOnClickListener(this)

        getAllHotlineViewModel.getAllHotline()
    }

    private fun initAdapter() {
        adapterHotline = AdapterHotline(mutableListOf())
        adapterHotline.setListener(this)
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = adapterHotline
        }
    }

    private fun initProjectViewModel() {
        getAllHotlineViewModel =
            ViewModelProviders.of(this).get(GetAllHotlineViewModel::class.java).apply {
                list.observe(this@ChooseHotlineNumberFragment, Observer {
                    it?.let {
                        //                        swipeRefresh.isRefreshing = false
                        adapterHotline.setData(it)
                    }
                })
                loadingStatus.observe(this@ChooseHotlineNumberFragment, Observer {
                    it.let {
                        //                        if (!swipeRefresh.isRefreshing) {
                        showOrHideProgressDialog(it)
//                        }
                    }
                })

            }
    }

    override fun onclickItem(hotline: Hotline) {
        if (hotlineArrayList.contains(hotline)) {
            hotlineArrayList.remove(hotline)
        } else {
            hotlineArrayList.add(hotline)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnStart -> {
                if (hotlineArrayList.size == 0) {
                    showToast(R.string.ban_chua_chon_hotline)
                } else {
                    var index = 0
                    idHotline = ""
                    for (item in hotlineArrayList) {
                        index++
                        if (index == hotlineArrayList.size) {
                            idHotline += item.id
                        } else {
                            idHotline += item.id.toString() + ","
                        }
                    }
                    hotlineArrayList.clear()
                    addHotlineViewModel.AddHotline(idProject!!, idHotline!!)
                }

            }
        }
    }

    private fun initViewModelAddHotline() {
        activity?.let {
            addHotlineViewModel =
                ViewModelProviders.of(this).get(AddHotlineViewModel::class.java).apply {
                    Success.observe(this@ChooseHotlineNumberFragment, Observer {
                        it?.let {
                            if (it) {
                                startActivity(Intent(activity, MainActivity::class.java))
                                activity?.finish()
                            }
                        }
                    })
                    message.observe(this@ChooseHotlineNumberFragment, Observer {
                        it?.let {
                            //                        showToast(it)
                        }
                    })
                    loadingStatus.observe(this@ChooseHotlineNumberFragment, Observer {
                        showOrHideProgressDialog(it)
                    })
                }
        }
    }
}
