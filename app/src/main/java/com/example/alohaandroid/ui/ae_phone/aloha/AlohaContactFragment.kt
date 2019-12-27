package com.example.alohaandroid.ui.ae_phone.aloha

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.alohaandroid.R
import com.example.alohaandroid.domain.remote.pojo.response.Contact
import com.example.alohaandroid.ui.a_base.BaseFragment
import com.example.alohaandroid.ui.a_adapter.AdapterContact
import com.example.alohaandroid.ui.ae_phone.addcontact.AddContactActivity
import com.example.alohaandroid.ui.ae_phone.infor_contact.InforContactActivity
import com.example.alohaandroid.utils.extension.showToast
import com.example.alohaandroid.utils.linphone.AddressText
import com.example.alohaandroid.utils.linphone.Digit
import com.example.alohaandroid.utils.linphone.LinphoneManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_aloha_contact.*
import kotlinx.android.synthetic.main.fragment_bottom_sheet_dial.*
import java.util.*

class AlohaContactFragment : BaseFragment(),
    View.OnClickListener,
    AddressText.AddressChangedListener,
    AdapterContact.AdapterContactListener {

    private lateinit var getallContactViewModel: GetallContactViewModel
    private lateinit var deleteContactViewModel: DeleteContactViewModel
    private lateinit var adapterContact: AdapterContact
    private lateinit var dialog: Dialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_aloha_contact, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initDeleteContactViewModel()
        initContactViewModel()
        initAdapter()
        fabConTact.setOnClickListener(this)
        edtInputNumber.setOnClickListener(this)
        tvFavorite.setOnClickListener(this)

        view?.let {
            initUI(it)
        }

        // Set OnRefreshListener
        swipeRefresh.setOnRefreshListener {
            getallContactViewModel.getAllContact()
        }

        getallContactViewModel.getAllContact()

    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fabConTact -> {
                val bottomSheetBehavior = BottomSheetBehavior.from(nestedScrollViewContact)

                if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
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

            tvFavorite.id -> {
                val intent = Intent(activity, AddContactActivity::class.java)
                intent.putExtra(ALOHA_OR_MOBILE_CONTACT,2)
                startActivityForResult(intent, 1)
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        when (requestCode == 1) {

            Activity.RESULT_OK == -1->{
                getallContactViewModel.getAllContact()
            }

//            if (resultCode == Activity.RESULT_OK) {
//
//
//            }
//                if (resultCode == Activity.RESULT_CANCELED) {
//
//                //Write your code if there's no result
//            }
        }
    }

    override fun onAddressChanged() {
        val core = LinphoneManager.getCore()
        ivAddNumber.setEnabled(
            core != null && core.callsNb > 0 || !edtInputNumber.getText().toString().equals("")
        )
    }

    override fun onclickItemContact(contact: Contact) {
        val intent = Intent(activity, InforContactActivity::class.java)
        intent.putExtra(CONTACT, contact)
        startActivity(intent)
    }

    override fun onLongclickItemContact(contact: Contact) {
        dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog)
        dialog.setCancelable(false)
        Objects.requireNonNull<Window>(dialog.window).setBackgroundDrawable(
            ColorDrawable(Color.TRANSPARENT)
        )
        val window = dialog.window
        val size = Point()
        val display = window?.windowManager?.defaultDisplay
        display?.getSize(size)
        window?.setLayout((size.x * 1), WindowManager.LayoutParams.WRAP_CONTENT)
        window?.setGravity(Gravity.CENTER)
        val title = dialog.findViewById(R.id.tvContent) as TextView
        val btnYes = dialog.findViewById(R.id.btnYes) as Button
        val btnNo = dialog.findViewById(R.id.btnNo) as Button
        btnNo.setOnClickListener { dialog.dismiss() }
        btnYes.setOnClickListener {
            deleteContactViewModel.deleteContact(contact.id!!)
        }
        title.text = getString(R.string.notify_delete_contact)
        dialog.show()
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

    fun setUpNumpad(view: View?) {
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

    private fun initAdapter() {
        adapterContact =
            AdapterContact(mutableListOf())
        adapterContact.setListener(this)
        rvContact.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = adapterContact
        }
    }

    private fun initContactViewModel() {
        activity?.let {
            getallContactViewModel =
                ViewModelProviders.of(this).get(GetallContactViewModel::class.java).apply {
                    listcontact.observe(this@AlohaContactFragment, Observer {
                        it?.let {
                            swipeRefresh.isRefreshing = false
                            adapterContact.setData(it)
                            if (it.isNotEmpty()) {
                                tvLabelNobody.visibility = View.GONE
                            } else {
                                tvLabelNobody.visibility = View.VISIBLE
                            }
                        }
                    })
                    loadingStatus.observe(this@AlohaContactFragment, Observer {
                        it.let {
                            if (!swipeRefresh.isRefreshing) {
                                showOrHideProgressDialog(it)
                            }
                        }
                    })

                }
        }
    }

    private fun initDeleteContactViewModel() {
        activity?.let {
            deleteContactViewModel =
                ViewModelProviders.of(this).get(DeleteContactViewModel::class.java).apply {
                    deleteContactSuccess.observe(this@AlohaContactFragment, Observer {
                        it?.let {
                            dialog.dismiss()
                            getallContactViewModel.getAllContact()
                        }
                    })
                    message.observe(this@AlohaContactFragment, Observer {
                        it?.let {
                            showToast(it)
                        }
                    })
                    loadingStatus.observe(this@AlohaContactFragment, Observer {
                        showOrHideProgressDialog(it)
                    })

                }
        }
    }

    companion object {
        const val CONTACT = "CONTACT"
        const val ALOHA_OR_MOBILE_CONTACT = "ALOHA_CONTACT"
    }
}
