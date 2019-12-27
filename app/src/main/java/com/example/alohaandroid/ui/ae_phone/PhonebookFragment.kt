package com.example.alohaandroid.ui.ae_phone


import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alohaandroid.R
import com.example.alohaandroid.domain.remote.pojo.response.Contact
import com.example.alohaandroid.ui.a_adapter.AdapterContact
import com.example.alohaandroid.ui.a_base.BaseFragment
import com.example.alohaandroid.ui.ae_phone.aloha.AlohaContactFragment
import com.example.alohaandroid.ui.ae_phone.infor_contact.InforContactActivity
import com.trendyol.bubblescrollbarlib.BubbleTextProvider
import kotlinx.android.synthetic.main.fragment_phonebook.*
import kotlinx.android.synthetic.main.fragment_phonebook.bubbleScrollBar
import java.lang.StringBuilder
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class PhonebookFragment : BaseFragment(), AdapterContact.AdapterContactListener {

//    var results = ArrayList<ContactResult>()

    private lateinit var adapterContact: AdapterContact

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_phonebook, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initAdapter()
        getContactFromDevice()

    }


    private fun initAdapter() {
        adapterContact =
            AdapterContact(mutableListOf())
        adapterContact.setListener(this)
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = adapterContact
        }
    }

    override fun onclickItemContact(contact: Contact) {
        val intent = Intent(activity, InforContactActivity::class.java)
        intent.putExtra("CONTACT", contact)
        startActivity(intent)
    }

    override fun onLongclickItemContact(contact: Contact) {
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
                tvNoContact.visibility = View.GONE
            }else{
                tvNoContact.visibility = View.VISIBLE
            }
            adapterContact.setData(contactList)
            bubbleScrollBar.attachToRecyclerView(recyclerView)

            bubbleScrollBar.bubbleTextProvider = BubbleTextProvider { i ->
                StringBuilder(
                    contactList[i].fullName?.substring(0,1)
                ).toString()
            }
        }
        phones.close()

    }
}
