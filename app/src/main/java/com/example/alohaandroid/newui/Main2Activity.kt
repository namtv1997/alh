package com.example.alohaandroid.newui

import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.alohaandroid.R
import com.example.alohaandroid.ui.a_base.BaseActivity
import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.HttpMethod
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.zing.zalo.zalosdk.oauth.ZaloSDK
import kotlinx.android.synthetic.main.activity_main2.*
import org.json.JSONObject


class Main2Activity : BaseActivity(), View.OnClickListener {

    private var idLogin = 0
    private var mGoogleSignInClient: GoogleSignInClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        val intent = intent
        val acct = GoogleSignIn.getLastSignedInAccount(this)
        if (intent != null) {
            if (intent.hasExtra("idLogin")) {
                idLogin = intent.getIntExtra("idLogin", 0)
                if (idLogin == 1) {
                    if (intent.hasExtra("name")) {
                        var name1: String = intent.getStringExtra("name")
                        name.text = name1
                    }
                    if (intent.hasExtra("id")) {
                        var id1: String = intent.getStringExtra("id")
                        id.text = id1
                    }
                    if (intent.hasExtra("image")) {
                        var image1: String = intent.getStringExtra("image")
                        Glide.with(this).load(image1).into(image)
                    }
                } else if (idLogin == 2) {
                    if (acct != null) {
                        var personName: String? = acct.displayName
                        var personEmail: String? = acct.email
                        var personId: String? = acct.id
                        var personPhoto: Uri? = acct.photoUrl
                        name.text = personName
                        id.text = personId
                        email.text = personEmail
                        Glide.with(this).load(personPhoto.toString()).into(image)
                    }
                } else if (idLogin == 3) {
                    ZaloSDK.Instance.getProfile(this, { data: JSONObject ->
                        id.text = data.optString("id")
                        name.text = data.optString("name")

                        val pic = data.optJSONObject("picture")
                        val picData = pic?.optJSONObject("data")
                        val url = picData?.optString("url")
                        if (!TextUtils.isEmpty(url)) {
                            Glide.with(this).load(url).into(image)
                        }

                    }, arrayOf("id", "name", "picture"))
                }
            }
        }


        btnSignOut.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnSignOut -> {
                if (idLogin == 1) {
                    disconnectFromFacebook()
                    finish()
                    Toast.makeText(this, "Thành công", Toast.LENGTH_LONG).show()
                } else if (idLogin == 2) {
                    signOutGmail()
                    Toast.makeText(this, "Thành công", Toast.LENGTH_LONG).show()
                } else if (idLogin == 3) {
                    ZaloSDK.Instance.unauthenticate()
                    finish()
                    Toast.makeText(this, "Thành công", Toast.LENGTH_LONG).show()
                }else{

                }
            }
        }
    }

    private fun signOutGmail() {
        mGoogleSignInClient!!.signOut()
            .addOnCompleteListener(
                this
            ) {
                finish()
            }
    }

    fun disconnectFromFacebook() {
        if (AccessToken.getCurrentAccessToken() == null) {
            return  // already logged out
        }
        GraphRequest(
            AccessToken.getCurrentAccessToken(),
            "/me/permissions/",
            null,
            HttpMethod.DELETE,
            GraphRequest.Callback { LoginManager.getInstance().logOut() }).executeAsync()
    }
}
