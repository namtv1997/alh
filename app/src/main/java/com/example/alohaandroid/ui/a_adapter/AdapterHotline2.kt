package com.example.alohaandroid.ui.a_adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.alohaandroid.R
import com.example.alohaandroid.domain.remote.pojo.response.Hotline
import kotlinx.android.synthetic.main.item_hotline1.view.*


class AdapterHotline2(private var project: MutableList<Hotline>) :
    RecyclerView.Adapter<AdapterHotline2.ViewHolder>() {
    private var listener: AdapterContactListener? = null

    interface AdapterContactListener {
        fun onclickHotline(hotline: Hotline)
    }

    fun setListener(listener: AdapterContactListener) {
        this.listener = listener
    }

    fun setData(project: List<Hotline>) {
        this.project.clear()
        this.project.addAll(project)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_hotline2,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(project[position])
        holder.itemView.setOnClickListener {
            listener!!.onclickHotline(project[position])
        }
    }

    override fun getItemCount(): Int {
        return project.size
    }


    inner class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        @SuppressLint("SetTextI18n")
        fun bind(historyCall: Hotline) = with(itemView) {

            tvNumberHotline.text = historyCall.phoneNumber
        }
    }
}