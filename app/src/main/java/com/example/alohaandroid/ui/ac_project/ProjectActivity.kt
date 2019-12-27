package com.example.alohaandroid.ui.ac_project

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alohaandroid.R
import com.example.alohaandroid.domain.remote.pojo.response.Project
import com.example.alohaandroid.ui.a_adapter.AdapterProject
import com.example.alohaandroid.ui.a_viewmodel.*
import com.example.alohaandroid.ui.ac_project.swipe.ButtonClickListener
import com.example.alohaandroid.ui.ac_project.swipe.MyButton
import com.example.alohaandroid.ui.ac_project.swipe.SwipeHelper
import com.example.alohaandroid.ui.a_base.BaseActivity
import com.example.alohaandroid.ui.ad_main.MainActivity
import com.example.alohaandroid.utils.Common
import com.example.alohaandroid.utils.extension.SharePrefs
import com.example.alohaandroid.utils.extension.showToast
import com.example.alohaandroid.utils.extension.switchFragment
import kotlinx.android.synthetic.main.activity_project.*

class ProjectActivity : BaseActivity(), View.OnClickListener,
    AdapterProject.AdapterContactListener {

    var count = 0
    var id = 0
    var idProject: Int? = null
    var dialog: AlertDialog? = null
    private lateinit var adapterProject: AdapterProject
    private lateinit var getAllProjectViewModel: GetAllProjectViewModel
    private lateinit var checkExistNameViewModel: CheckExistNameViewModel
    private lateinit var createViewModel: CreateViewModel
    private lateinit var updateViewModel: UpdateViewModel
    private lateinit var getAllHotlineByProjectViewModel: GetAllHotlineByProjectViewModel
    private lateinit var deleteProjectViewModel: DeleteProjectViewModel

    private lateinit var btnCreateProject: Button
    private lateinit var ivExit: ImageView
    private lateinit var etNameProject: EditText
    private lateinit var etDescription: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project)

        initProjectViewModel()
        initAdapter()
        initViewModelCheckExistName()
        initCreateViewModel()
        initUpdateViewModel()
        initGetAllByProjectViewModel()
        initDeleteProjectViewModel()


//        swipeRefresh.setOnRefreshListener {
//            getAllProjectViewModel.getAllProject()
//        }
        getAllProjectViewModel.getAllProject()


        btnNewProject.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnNewProject -> {
                val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                val inflater =
                    this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val view1: View = inflater.inflate(R.layout.dialog_new_project, null)
                builder.setView(view1)
                builder.setCancelable(false)
                dialog = builder.create()
                dialog!!.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                btnCreateProject = view1.findViewById(R.id.btnCreateProject)
                ivExit = view1.findViewById(R.id.ivExit)
                etNameProject = view1.findViewById(R.id.etNameProject)
                etDescription = view1.findViewById(R.id.etDescription)

                btnCreateProject.setOnClickListener {
                    count = 1
                    if (etNameProject.text.trim().isEmpty()) {
                        Toast.makeText(this, R.string.chua_nhap_ten_du_an, Toast.LENGTH_LONG).show()
                    } else {
                        checkExistNameViewModel.checkExistName(etNameProject.text.toString())
                        dialog?.dismiss()
                    }
                }

                ivExit.setOnClickListener {
                    dialog!!.dismiss()
                }
                val layoutManager =
                    LinearLayoutManager(applicationContext)
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL)
                dialog!!.show()
            }
        }
    }

    private fun initProjectViewModel() {
        getAllProjectViewModel =
            ViewModelProviders.of(this).get(GetAllProjectViewModel::class.java).apply {
                list.observe(this@ProjectActivity, Observer {
                    it?.let {
                        //                        swipeRefresh.isRefreshing = false
                        adapterProject.setData(it)
                        if (it.isNotEmpty()) {
                            tvNoProject.visibility = View.GONE
                            tvTitleNotification.visibility = View.VISIBLE
                        } else {
                            tvTitleNotification.visibility = View.GONE
                            tvNoProject.visibility = View.VISIBLE

                        }
                    }
                })
                loadingStatus.observe(this@ProjectActivity, Observer {
                    it.let {
                        //                        if (!swipeRefresh.isRefreshing) {
                        showOrHideProgressDialog(it)
//                        }
                    }
                })

            }
    }

    private fun initAdapter() {
        adapterProject = AdapterProject(mutableListOf())
        adapterProject.setListener(this)
        rvProject.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = adapterProject
        }
        val swipe = object : SwipeHelper(this, rvProject, 150) {
            override fun instantiateMyButton(
                viewHolder: RecyclerView.ViewHolder,
                buffer: MutableList<MyButton>
            ) {
                buffer.add(
                    MyButton(this@ProjectActivity, "", 0, R.drawable.ic_va_delete,
                        Color.parseColor("#FF6969"),
                        object : ButtonClickListener {
                            override fun onClick(position: Int) {

                                val builder: AlertDialog.Builder =
                                    AlertDialog.Builder(this@ProjectActivity)
                                val inflater =
                                    this@ProjectActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                                val view1: View = inflater.inflate(R.layout.dialog_delete, null)
                                builder.setView(view1)
                                builder.setCancelable(false)
                                dialog = builder.create()
                                dialog!!.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                                var btnYes: Button = view1.findViewById(R.id.btnYes)
                                var btnNo: Button = view1.findViewById(R.id.btnNo)

                                btnYes.setOnClickListener {
                                    deleteProjectViewModel.deleteProject(SharePrefs().getInstance()[Common.ID_PROJECT, Int::class.java]!!)
                                    dialog?.dismiss()
                                }

                                btnNo.setOnClickListener {
                                    dialog!!.dismiss()
                                }
                                val layoutManager =
                                    LinearLayoutManager(applicationContext)
                                layoutManager.setOrientation(LinearLayoutManager.VERTICAL)
                                dialog!!.show()
                            }
                        })
                )

            }
        }
    }

    private fun initViewModelCheckExistName() {
        checkExistNameViewModel =
            ViewModelProviders.of(this).get(CheckExistNameViewModel::class.java).apply {
                exist.observe(this@ProjectActivity, Observer {
                    it?.let {
                        if (!it) {
                            if (count == 1) {
                                createViewModel.Create(
                                    etNameProject.text.toString(),
                                    etDescription.text.toString()
                                )
                            } else {
                                if (id != 0) {
                                    updateViewModel.Update(
                                        id, etNameProject.text.toString(),
                                        etDescription.text.toString()
                                    )
                                } else {
                                    showToast(getString(R.string.da_co_loi_thu_lai))
                                }
                            }
                        } else {
                            Toast.makeText(
                                this@ProjectActivity,
                                R.string.ten_du_an_da_ton_tai,
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                })
                message.observe(this@ProjectActivity, Observer {
                    it?.let {
                        //                        showToast(it)
                    }
                })
                loadingStatus.observe(this@ProjectActivity, Observer {
                    showOrHideProgressDialog(it)
                })
            }
    }

    fun initCreateViewModel() {
        createViewModel = ViewModelProviders.of(this).get(CreateViewModel::class.java).apply {
            Success.observe(this@ProjectActivity,
                Observer {
                    it?.let {
                        if (it > 0) {
                            getAllProjectViewModel.getAllProject()
                            showToast(getString(R.string.thanh_cong))
                        } else {
                            showToast(getString(R.string.da_co_loi_thu_lai))
                        }
                    }
                })
            Failr.observe(this@ProjectActivity, Observer {

            })
            loadingStatus.observe(this@ProjectActivity, Observer {
                showOrHideProgressDialog(it)
            })
        }
    }

    fun initUpdateViewModel() {
        updateViewModel = ViewModelProviders.of(this).get(UpdateViewModel::class.java).apply {
            Success.observe(this@ProjectActivity,
                Observer {
                    it?.let {
                        if (it) {
                            getAllProjectViewModel.getAllProject()
                            showToast(getString(R.string.thanh_cong))
                        } else {
                            showToast(getString(R.string.da_co_loi_thu_lai))
                        }
                    }
                })
            Failr.observe(this@ProjectActivity, Observer {

            })
            loadingStatus.observe(this@ProjectActivity, Observer {
                showOrHideProgressDialog(it)
            })
        }
    }

    override fun onclickItemEdit(project: Project) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        val inflater =
            this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view1: View = inflater.inflate(R.layout.dialog_new_project, null)
        builder.setView(view1)
        builder.setCancelable(false)
        dialog = builder.create()
        dialog!!.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        btnCreateProject = view1.findViewById(R.id.btnCreateProject)
        ivExit = view1.findViewById(R.id.ivExit)
        etNameProject = view1.findViewById(R.id.etNameProject)
        etDescription = view1.findViewById(R.id.etDescription)
        var tvTitle = view1.findViewById<TextView>(R.id.tvTitle)

        etNameProject.setText(project.name)
        etDescription.setText(project.description)
        tvTitle.text = getString(R.string.sua_du_an)
        btnCreateProject.text = getString(R.string.luu_thay_doi)
        btnCreateProject.setOnClickListener {
            count = 2
            id = project.id!!
            if (etNameProject.text.trim().isEmpty()) {
                Toast.makeText(this, R.string.chua_nhap_ten_du_an, Toast.LENGTH_LONG).show()
            } else {
                checkExistNameViewModel.checkExistName(etNameProject.text.toString())
                dialog?.dismiss()
            }
        }

        ivExit.setOnClickListener {
            dialog!!.dismiss()
        }
        val layoutManager =
            LinearLayoutManager(applicationContext)
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL)
        dialog!!.show()
    }

    override fun onclickItem(project: Project) {
        SharePrefs().getInstance().put(Common.ID_PROJECT, project.id)
        SharePrefs().getInstance().put(Common.CODE, project.code)
        getAllHotlineByProjectViewModel.getAllByProject(project.code.toString())
    }

    private fun initGetAllByProjectViewModel() {
        getAllHotlineByProjectViewModel =
            ViewModelProviders.of(this).get(GetAllHotlineByProjectViewModel::class.java).apply {
                list.observe(this@ProjectActivity, Observer {
                    it?.let {
                        if (it.isEmpty()) {
                            idProject =
                                SharePrefs().getInstance()[Common.ID_PROJECT, Int::class.java]
//                            val bundle = Bundle()
//                            bundle.putInt("id", idProject!!)
                            val chooseHotlineNumberFragment = ChooseHotlineNumberFragment()
//                            chooseHotlineNumberFragment.arguments = bundle
                            switchFragment(chooseHotlineNumberFragment, R.id.fl)
                        } else {
                            startActivity(Intent(this@ProjectActivity, MainActivity::class.java))
                        }
                    }
                })
                loadingStatus.observe(this@ProjectActivity, Observer {
                    it.let {
                        //                        if (!swipeRefresh.isRefreshing) {
                        showOrHideProgressDialog(it)
//                        }
                    }
                })

            }
    }

    private fun initDeleteProjectViewModel() {
        deleteProjectViewModel =
            ViewModelProviders.of(this).get(DeleteProjectViewModel::class.java).apply {
                deleteSuccess.observe(this@ProjectActivity, Observer {
                    it?.let {
                        if (it) {
                            getAllProjectViewModel.getAllProject()
                            showToast(getString(R.string.thanh_cong))
                        } else {
                            showToast(getString(R.string.da_co_loi_thu_lai))
                        }
                    }
                })
                message.observe(this@ProjectActivity, Observer {
                    it?.let {
                        showToast(it)
                    }
                })
                loadingStatus.observe(this@ProjectActivity, Observer {
                    showOrHideProgressDialog(it)
                })

            }
    }

}
