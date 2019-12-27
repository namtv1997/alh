package com.example.alohaandroid.ui.favorite



import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager

import com.example.alohaandroid.R
import com.example.alohaandroid.ui.a_base.BaseFragment
import com.example.alohaandroid.ui.ad_main.MainActivity
import com.example.alohaandroid.utils.linphone.AddressText
import com.example.alohaandroid.utils.linphone.Digit
import com.example.alohaandroid.utils.linphone.LinphoneManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_bottom_sheet_dial.*
import kotlinx.android.synthetic.main.fragment_favorite.*
import org.linphone.core.Call
import org.linphone.core.Core
import org.linphone.core.CoreListenerStub
import java.util.ArrayList

class FavoriteFragment : BaseFragment(), View.OnClickListener, AddressText.AddressChangedListener {
    private var mListener: CoreListenerStub? = null
    private var mInterfaceLoaded: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        fabConTactFavorite.setOnClickListener(this)
        edtInputNumber.setOnClickListener(this)
        view?.let { initUI(it)
            mInterfaceLoaded = true
        }
        mInterfaceLoaded = false
        mListener = object : CoreListenerStub() {
            override fun onCallStateChanged(
                core: Core?, call: Call?, state: Call.State?, message: String?
            ) {
            }
        }
//        (activity as MainActivity).checkFragment(this)
    }


     override fun onResume() {
        super.onResume()
        val core = LinphoneManager.getCore()

        core?.addListener(mListener)
         if (mInterfaceLoaded) {

         }

    }

     override fun onPause() {

        val core = LinphoneManager.getCore()
        core?.removeListener(mListener)

        super.onPause()
    }

     override fun onDestroy() {
        if (mListener != null) mListener = null
         btnVoiceCall==null
        super.onDestroy()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fabConTactFavorite -> {

                val bottomSheetBehavior = BottomSheetBehavior.from(nestedScrollViewFavorite)
                if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
                } else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
                }
            }

            R.id.edtInputNumber -> {

                val imm =
                    context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view?.getWindowToken(), 0);

            }

        }
    }


    override fun onAddressChanged() {
        val core = LinphoneManager.getCore()
        ivAddNumber.setEnabled(
            core != null && core.callsNb > 0 || !edtInputNumber.getText().toString().equals("")
        )
    }


    private fun initUI(view: View) {
        edtInputNumber.setAddressListener(this)

        btnDeleteNumber.setAddressWidget(edtInputNumber)

        btnVoiceCall.setAddressWidget(edtInputNumber)


        ivAddNumber.setEnabled(false)
//        ivAddNumber.setOnClickListener(
//            View.OnClickListener {
//                val intent = Intent(activity, ContactsActivity::class.java)
//                intent.putExtra("EditOnClick", true)
//                intent.putExtra("SipAddress", edtInputNumber.getText().toString())
//                startActivity(intent)
//            })


        setUpNumpad(view)

    }

    private fun setUpNumpad(view: View?) {
        if (view == null) return
        for (v in retrieveChildren((view as ViewGroup?)!!, Digit::class.java)) {
            v.setAddressWidget(edtInputNumber)
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



}
