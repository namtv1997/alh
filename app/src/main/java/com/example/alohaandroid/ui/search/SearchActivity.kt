package com.example.alohaandroid.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.alohaandroid.R
import com.example.alohaandroid.ui.a_base.BaseActivity

class SearchActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
    }

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, SearchActivity::class.java))
        }
    }
}
