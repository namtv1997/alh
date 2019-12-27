package com.example.alohaandroid.ui.ae_phone


import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alohaandroid.R
import com.example.alohaandroid.domain.remote.pojo.response.Contact
import com.example.alohaandroid.ui.a_adapter.AdapterContact
import com.example.alohaandroid.ui.a_base.BaseFragment
import com.example.alohaandroid.ui.ae_phone.aloha.AlohaContactFragment.Companion.CONTACT
import com.example.alohaandroid.ui.ae_phone.infor_contact.InforContactActivity
import com.trendyol.bubblescrollbarlib.BubbleTextProvider
import kotlinx.android.synthetic.main.fragment_mobile_contact.*
import java.lang.StringBuilder
import java.util.*

class MobileContactFragment : BaseFragment(), AdapterContact.AdapterContactListener {

    private lateinit var adapterContact: AdapterContact
    private lateinit var dialog: Dialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mobile_contact, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initAdapter()
        getContactFromDevice()
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
            deleteContact(context!!, contact.phone.toString(), contact.fullName.toString())
            dialog.dismiss()

        }
        title.text = getString(R.string.notify_delete_contact)
        dialog.show()
    }

    private fun initAdapter() {
        adapterContact =
            AdapterContact(mutableListOf())
        adapterContact.setListener(this)
        rvContact1.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = adapterContact
        }

    }

    fun getContactFromDevice() {
        val contactList: MutableList<Contact> = ArrayList()
        val phones = context!!.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )
        while (phones!!.moveToNext()) {
            val name =
                phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            val number =
                phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
//            val id = phones.getString(phones.getColumnIndex(ContactsContract.Contacts._ID))
            val email =
                phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA))
            val photo_uri =
                phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI))

            val obj = Contact()
            obj.fullName = name
            obj.phone = number
            obj.email = email
            obj.avatar = photo_uri
            contactList.add(obj)

            if (contactList.size > 0) {
                tvLabelNobody.visibility = View.GONE
            }
            adapterContact.setData(contactList)
            bubbleScrollBar.attachToRecyclerView(rvContact1)

            bubbleScrollBar.bubbleTextProvider = BubbleTextProvider { i ->
                StringBuilder(
                    contactList[i].fullName?.substring(0,1)
                ).toString()
            }
            Log.d("name>>", "$name  $number $email")
        }
        phones.close()

    }

    private fun deleteContact(ctx: Context, phone: String, name: String): Boolean {
            val contactUri =
                Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phone))
            val cur = ctx.contentResolver.query(contactUri, null, null, null, null)
            try {
                if (cur.moveToFirst()) {
                    do {
                        if (cur.getString(cur.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME)).equals(
                                name,
                                false
                            )
                        ) {
                            val lookupKey =
                                cur.getString(cur.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY))
                            val uri = Uri.withAppendedPath(
                                ContactsContract.Contacts.CONTENT_LOOKUP_URI,
                                lookupKey
                            )
                            ctx.contentResolver.delete(uri, null, null)
                            getContactFromDevice()
                            return true
                        }
                    } while (cur.moveToNext())
                }

            } catch (e: Exception) {
                println(e.stackTrace)
            }
        return false
    }

}
