package com.example.alohaandroid.ui.ab_login

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.request.RequestOptions
import com.example.alohaandroid.R
import com.example.alohaandroid.ui.a_viewmodel.*
import com.example.alohaandroid.ui.ac_project.ProjectActivity
import com.example.alohaandroid.ui.a_base.BaseActivity
import com.example.alohaandroid.ui.ad_main.MainActivity
import com.example.alohaandroid.ui.a_viewmodel.SignInViewModel
import com.example.alohaandroid.utils.Common
import com.example.alohaandroid.utils.extension.SharePrefs
import com.example.alohaandroid.utils.extension.switchFragment
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.zing.zalo.zalosdk.oauth.LoginVia
import com.zing.zalo.zalosdk.oauth.OAuthCompleteListener
import com.zing.zalo.zalosdk.oauth.OauthResponse
import com.zing.zalo.zalosdk.oauth.ZaloSDK
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONException
import org.json.JSONObject
import pub.devrel.easypermissions.EasyPermissions


class LoginActivity : BaseActivity(), View.OnClickListener, EasyPermissions.PermissionCallbacks {

    private var registerUserViewModel: RegisterUserViewModel? = null
    private var signInViewModel: SignInViewModel? = null
    private var checkExistIDFacebookViewModel: CheckExistIDFacebookViewModel? = null
    private var checkExistIDGoogleViewModel: CheckExistIDGoogleViewModel? = null
    private var checkExistIDZaloViewModel: CheckExistIDZaloViewModel? = null
    private lateinit var getAllHotlineByProjectViewModel: GetAllHotlineByProjectViewModel
    private var callbackManager: CallbackManager? = null
    private var mGoogleSignInClient: GoogleSignInClient? = null
    private val RC_SIGN_IN = 0
    private var idLogin = 0

    var idFB = ""
    var idGoogle = ""
    var idZalo = ""
    var nameFB = ""
    var nameGoogle = ""
    var nameZalo = ""
    var imageFB = ""
    var imageGoogle = ""
    var imageZalo = ""
    var gmail = ""

    private var idProject: Int? = null
    var code: String? = null
    var firebase: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        idProject = SharePrefs().getInstance()[Common.ID_PROJECT, Int::class.java]
        code = SharePrefs().getInstance()[Common.CODE, String::class.java]

        initViewModelCheckExistIDFacebook()
        initViewModelCheckExistIDGoogle()
        initViewModelCheckExistIDZalo()
        initViewModelregisterUser()
        initGetAllByProjectViewModel()

//        getApplicationHashKey(this)
        this.let {
            signInViewModel = ViewModelProviders.of(this).get(SignInViewModel::class.java).apply {
                loginSuccess.observe(this@LoginActivity, Observer {
                    it?.let {
                        if (idProject == 0) {
                            startActivity(Intent(this@LoginActivity, ProjectActivity::class.java))
                        } else {
                            if (code.equals("CODE") || code == "") {
                                startActivity(Intent(this@LoginActivity, ProjectActivity::class.java))
                            } else {
                                getAllHotlineByProjectViewModel.getAllByProject(code.toString())
                            }
                        }
                    }
                })
                loginState.observe(this@LoginActivity, Observer {
                    //                    tvErrorAccount.text = it
                    tvErrorAccount.visibility = View.VISIBLE
                })
                loadingStatus.observe(this@LoginActivity, Observer {
                    showOrHideProgressDialog(it)
                })
            }
        }
        btnLogin.setOnClickListener(this)
        tvRegistration.setOnClickListener(this)
        tvForgotPassWord.setOnClickListener(this)
        btnLoginFacebook.setOnClickListener(this)
        btnLoginMail.setOnClickListener(this)
        btnLoginZalo.setOnClickListener(this)
        imBack.setOnClickListener(this)
        llLogin.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.llLogin -> {
                closeKeyboard()
            }
            R.id.imBack -> {
                onBackPressed()
            }
            R.id.btnLogin -> {
//                idLogin = 0
                if (etPassword.text.toString().trim().isEmpty() || etAccount.text.toString().trim().isEmpty()) {
                    Toast.makeText(this, R.string.hay_nhap_tai_khoan_va_mat_khau, Toast.LENGTH_LONG)
                        .show()
                } else {
                    signInViewModel?.loginWithApp(
                        etPassword.text.toString().trim(),
                        etAccount.text.toString().trim(),
                        0
                    )
                }
            }
            R.id.tvRegistration -> {
                val registerFragment =
                    RegisterFragment()
                switchFragment(registerFragment, R.id.fl)
            }
            R.id.tvForgotPassWord -> {
                val forgotPasswordFragment =
                    ForgotPasswordFragment()
                switchFragment(forgotPasswordFragment, R.id.fl)
            }
            R.id.btnLoginFacebook -> {
//                idLogin = 1
                callbackManager = CallbackManager.Factory.create()
                LoginManager.getInstance()
                    .logInWithReadPermissions(this, listOf("public_profile", "email"))
                LoginManager.getInstance().registerCallback(callbackManager,
                    object : FacebookCallback<LoginResult?> {
                        @SuppressLint("CheckResult")
                        override fun onSuccess(loginResult: LoginResult?) {
                            val request =
                                GraphRequest.newMeRequest(loginResult?.accessToken) { `object`, response ->
                                    try {
                                        nameFB = `object`.getString("name")
                                        idFB = `object`.getString("id")
                                        imageFB =
                                            `object`.getJSONObject("picture").getJSONObject("data")
                                                .getString("url")
                                        val requestOptions = RequestOptions()
                                        requestOptions.dontAnimate()

                                        checkExistIDFacebookViewModel?.CheckExistIDFacebookViewModel(
                                            idFB
                                        )
                                    } catch (e: JSONException) {
                                        e.printStackTrace()
                                    }
                                }
                            val parameters = Bundle()
                            parameters.putString("fields", "name,email,id,picture.type(large)")
                            request.parameters = parameters
                            request.executeAsync()
                        }

                        override fun onCancel() {
                            Toast.makeText(applicationContext, "Cancel", Toast.LENGTH_SHORT).show()
                        }

                        override fun onError(exception: FacebookException) {
                            Toast.makeText(applicationContext, "Error", Toast.LENGTH_SHORT).show()
                        }
                    })
            }
            R.id.btnLoginMail -> {
//                idLogin = 2
                val gso =
                    GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build()
                mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
                val signInIntent = mGoogleSignInClient!!.signInIntent
                startActivityForResult(signInIntent, RC_SIGN_IN)
            }
            R.id.btnLoginZalo -> {
//                idLogin = 3
                ZaloSDK.Instance.authenticate(this, LoginVia.APP_OR_WEB, object :
                    OAuthCompleteListener() {
                    override fun onGetOAuthComplete(response: OauthResponse?) {
                        if (TextUtils.isEmpty(response?.oauthCode)) {
                            Toast.makeText(this@LoginActivity, "Error", Toast.LENGTH_LONG).show()
                        } else {
                            ZaloSDK.Instance.getProfile(this@LoginActivity, { data: JSONObject ->
                                idZalo = data.optString("id")
                                nameZalo = data.optString("name")
                                val pic = data.optJSONObject("picture")
                                val picData = pic?.optJSONObject("data")
                                imageZalo = picData?.optString("url").toString()
                                checkExistIDZaloViewModel?.CheckExistIDZalo(idZalo)
                            }, arrayOf("id", "name", "picture"))

                        }
                    }
                })
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager?.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResultGmail(task)
        }
        ZaloSDK.Instance.onActivityResult(this, requestCode, resultCode, data)
    }

    private fun handleSignInResultGmail(completedTask: Task<GoogleSignInAccount>) {
        try {
//            val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)
            val acct = GoogleSignIn.getLastSignedInAccount(this)
            nameGoogle = acct?.displayName.toString()
            gmail = acct?.email.toString()
            idGoogle = acct?.id.toString()
            imageGoogle = acct?.photoUrl.toString()
            checkExistIDGoogleViewModel?.CheckExistIDGoogle(idGoogle)
            Toast.makeText(this, "SuccessGoogle", Toast.LENGTH_LONG).show()
        } catch (e: ApiException) { // The ApiException status code indicates the detailed failure reason.
            Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
    }

    private fun initViewModelCheckExistIDFacebook() {
        checkExistIDFacebookViewModel =
            ViewModelProviders.of(this).get(CheckExistIDFacebookViewModel::class.java).apply {
                Exist.observe(this@LoginActivity, Observer {
                    it?.let {
                        if (it) {
                            signInViewModel?.loginWithApp("", idFB, 1)
                        } else {
                            registerUserViewModel?.register(
                                "",
                                "",
                                nameFB,
                                "",
                                imageFB,
                                idFB,
                                "",
                                ""
                            )
                        }
                    }
                })
                message.observe(this@LoginActivity, Observer {
                    it?.let {
                        // showToast(it)
                    }
                })
                loadingStatus.observe(this@LoginActivity, Observer {
                    showOrHideProgressDialog(it)
                })
            }
    }

    private fun initViewModelCheckExistIDGoogle() {
        checkExistIDGoogleViewModel =
            ViewModelProviders.of(this).get(CheckExistIDGoogleViewModel::class.java).apply {
                Exist.observe(this@LoginActivity, Observer {
                    it?.let {
                        if (it) {
                            signInViewModel?.loginWithApp("", idGoogle, 2)
                        } else {
                            registerUserViewModel?.register(
                                gmail,
                                "",
                                nameGoogle,
                                "",
                                imageGoogle,
                                "",
                                idGoogle,
                                ""
                            )
                        }
                    }
                })
                message.observe(this@LoginActivity, Observer {
                    it?.let {
                        // showToast(it)
                    }
                })
                loadingStatus.observe(this@LoginActivity, Observer {
                    showOrHideProgressDialog(it)
                })
            }
    }

    private fun initViewModelCheckExistIDZalo() {
        checkExistIDZaloViewModel =
            ViewModelProviders.of(this).get(CheckExistIDZaloViewModel::class.java).apply {
                Exist.observe(this@LoginActivity, Observer {
                    it?.let {
                        if (it) {
                            signInViewModel?.loginWithApp("", idZalo, 3)
                        } else {
                            registerUserViewModel?.register(
                                "",
                                "",
                                nameZalo,
                                "",
                                imageZalo,
                                "",
                                "",
                                idZalo
                            )
                        }
                    }
                })
                message.observe(this@LoginActivity, Observer {
                    it?.let {
                        // showToast(it)
                    }
                })
                loadingStatus.observe(this@LoginActivity, Observer {
                    showOrHideProgressDialog(it)
                })
            }
    }

    private fun initViewModelregisterUser() {

        registerUserViewModel =
            ViewModelProviders.of(this).get(RegisterUserViewModel::class.java).apply {
                registerSuccess.observe(this@LoginActivity, Observer {
                    it?.let {
                        if (it == 1) {
                            startActivity(Intent(this@LoginActivity, ProjectActivity::class.java))
                        }
                    }
                })

                message.observe(this@LoginActivity, Observer {
                    it?.let {
                        //                        showToast(it)
                    }
                })
                loadingStatus.observe(this@LoginActivity, Observer {
                    showOrHideProgressDialog(it)
                })
            }

    }

    private fun closeKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun initGetAllByProjectViewModel() {
        getAllHotlineByProjectViewModel =
            ViewModelProviders.of(this).get(GetAllHotlineByProjectViewModel::class.java).apply {
                list.observe(this@LoginActivity, Observer {
                    it?.let {
                        if (it.isEmpty()) {
                            startActivity(Intent(this@LoginActivity, ProjectActivity::class.java))
                        } else {
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))

                        }
                    }
                })
                loadingStatus.observe(this@LoginActivity, Observer {
                    it.let {
                        //                        if (!swipeRefresh.isRefreshing) {
                        showOrHideProgressDialog(it)
//                        }
                    }
                })
            }
    }


//    fun getApplicationHashKey(ctx: Context) {
//        val info =
//            ctx.packageManager.getPackageInfo(ctx.getPackageName(), PackageManager.GET_SIGNATURES)
//        for(signature : Signature in info.signatures){
//            val md = MessageDigest.getInstance("SHA")
//            md.update(signature.toByteArray())
//            val sig = Base64.encodeToString(md.digest(), Base64.DEFAULT).trim()
//            Log.d("aaa",sig)
//            if (sig.trim().length > 0) {
//            }
//        }
//    }
}
