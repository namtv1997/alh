package com.example.alohaandroid.ui.a_adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.alohaandroid.R
import com.example.alohaandroid.domain.remote.pojo.response.Contact
import kotlinx.android.synthetic.main.item_contact.view.*

class AdapterContact(private var contacts: MutableList<Contact>) :
    RecyclerView.Adapter<AdapterContact.ViewHolder>() {
    private var listener: AdapterContactListener? = null

    interface AdapterContactListener {
        fun onclickItemContact(contact: Contact)
        fun onLongclickItemContact(contact: Contact)
    }

    fun setListener(listener: AdapterContactListener) {
        this.listener = listener
    }

    fun setData(contacts: List<Contact>) {
        this.contacts.clear()
        this.contacts.addAll(contacts)
        notifyDataSetChanged()
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_contact,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(contacts[position])
        holder.itemView.setOnClickListener {
            listener!!.onclickItemContact(contacts[position])
        }
        holder.itemView.setOnLongClickListener{
            listener!!.onLongclickItemContact(contacts[position])
            return@setOnLongClickListener true
        }
    }

    override fun getItemCount(): Int {
        return contacts.size
    }


    inner class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        fun bind(contact: Contact) = with(itemView) {
            tvName.text = contact.fullName

            Glide.with(context).load(contact.avatar).centerCrop().placeholder(R.drawable.iv_call).into(ivAvatarContact)
        }
    }
}