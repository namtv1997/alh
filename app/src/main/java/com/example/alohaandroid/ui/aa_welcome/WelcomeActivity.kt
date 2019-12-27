package com.example.alohaandroid.ui.aa_welcome

import android.Manifest
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.alohaandroid.R
import com.example.alohaandroid.ui.a_viewmodel.GetAllHotlineByProjectViewModel
import com.example.alohaandroid.ui.ab_login.LoginActivity
import com.example.alohaandroid.ui.ab_login.RegisterFragment
import com.example.alohaandroid.ui.ab_register.RegisterActivity
import com.example.alohaandroid.ui.ac_project.ProjectActivity
import com.example.alohaandroid.ui.ad_main.MainActivity
import com.example.alohaandroid.ui.a_base.BaseActivity
import com.example.alohaandroid.utils.Common
import com.example.alohaandroid.utils.extension.SharePrefs
import com.example.alohaandroid.utils.extension.switchFragment
import kotlinx.android.synthetic.main.activity_welcome.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

class WelcomeActivity : BaseActivity(), View.OnClickListener {

    private var idRegister: Int = 1
    var firebase: String? = null
    private lateinit var getAllHotlineByProjectViewModel: GetAllHotlineByProjectViewModel


    //    private lateinit var auth: FirebaseAuth
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        requestPermission()
//        auth = FirebaseAuth.getInstance()
//        FirebaseApp.initializeApp(this)
//        firebase = FirebaseInstanceId.getInstance().getToken()
//        SharePrefs().getInstance().put(Common.FIREBASE_TOKEN, firebase)
        initGetAllByProjectViewModel()
        switchToMainScreen()

        val adapter = MyViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(Welcome1Fragment())
        adapter.addFragment(Welcome2Fragment())
        adapter.addFragment(Welcome3Fragment())
        viewPager.adapter = adapter
        // CircleIndicator3 for RecyclerView
        indicator.setViewPager(viewPager)

        btnLogin.setOnClickListener(this)
        btnRegister.setOnClickListener(this)
    }

    class MyViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {

        private val fragmentList: MutableList<Fragment> = ArrayList()
        private val titleList: MutableList<String> = ArrayList()

        override fun getItem(position: Int): Fragment {
            return fragmentList[position]
        }

        override fun getCount(): Int {
            return fragmentList.size
        }

        fun addFragment(fragment: Fragment) {
            fragmentList.add(fragment)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return titleList[position]
        }

    }

    @AfterPermissionGranted(RC_RECORD_AUDIO_AND_READ_WRITE_CONTACT)
    fun requestPermission() {
        if (EasyPermissions.hasPermissions(this, *RECORD_AUDIO_AND_READ_WRITE_CONTACT)) {
            // Have permissions, do the thing!
        } else {
            // Ask for both permissions
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.rationale_read_and_write_to_storage),
                RC_RECORD_AUDIO_AND_READ_WRITE_CONTACT,
                *RECORD_AUDIO_AND_READ_WRITE_CONTACT
            )
        }
    }

    companion object {
        private val RECORD_AUDIO_AND_READ_WRITE_CONTACT = arrayOf(
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_CONTACTS
        )
        private const val RC_RECORD_AUDIO_AND_READ_WRITE_CONTACT = 1122
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnLogin -> {
                startActivity(Intent(this, LoginActivity::class.java))
            }
            R.id.btnRegister -> {
                val bundle = Bundle()
                bundle.putInt(RegisterActivity.EXTRA_ID, idRegister)
                val registerFragment =
                    RegisterFragment()
                registerFragment.arguments = bundle
                switchFragment(registerFragment, R.id.fl)
            }
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setTitle(R.string.thong_bao)
            builder.setMessage(R.string.ban_co_muon_thoat_ung_dung)
            builder.setNegativeButton(R.string.co,
                DialogInterface.OnClickListener { dialog, which -> super.onBackPressed() })
            builder.setPositiveButton(
                R.string.khong,
                DialogInterface.OnClickListener { dialog, which -> })
            builder.show()
        }
    }

    private fun switchToMainScreen() {
//        Handler().postDelayed({
        //            val accessToken = SharePrefs().getInstance()[Common.ACCESS_TOKEN, String::class.java]
        val uid = SharePrefs().getInstance()[Common.UID, String::class.java]
        val idProject = SharePrefs().getInstance()[Common.ID_PROJECT, Int::class.java]
        val code = SharePrefs().getInstance()[Common.CODE, String::class.java]
//            if (accessToken != null) {
//                if (accessToken.isNotEmpty()) {
        if (uid == "" || uid == "UID" || uid == "999999999" || uid == "9001") {
//                startActivity(Intent(this, WelcomeActivity::class.java).apply {
//                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
//                })
        } else {
            if (idProject == 0) {
                startActivity(Intent(this, ProjectActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                })
            } else {
                if (code.equals("CODE") || code.isNullOrEmpty()) {
                    startActivity(Intent(this, ProjectActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    })
                } else {
                    getAllHotlineByProjectViewModel.getAllByProject(code.toString())
                }
            }
        }
//                } else {
//                    startActivity(Intent(this, WelcomeActivity::class.java).apply {
//                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
//                    })
//        }, 1000)
    }

    private fun initGetAllByProjectViewModel() {
        getAllHotlineByProjectViewModel =
            ViewModelProviders.of(this).get(GetAllHotlineByProjectViewModel::class.java).apply {
                list.observe(this@WelcomeActivity, Observer {
                    it?.let {
                        if (it.isEmpty()) {
                            startActivity(
                                Intent(this@WelcomeActivity, ProjectActivity::class.java).apply {
                                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                })
                        } else {
                            startActivity(
                                Intent(this@WelcomeActivity, MainActivity::class.java).apply {
                                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                })
                        }
                    }
                })
                loadingStatus.observe(this@WelcomeActivity, Observer {
                    it.let {
                        //                        if (!swipeRefresh.isRefreshing) {
                        showOrHideProgressDialog(it)
//                        }
                    }
                })

            }
    }
}
