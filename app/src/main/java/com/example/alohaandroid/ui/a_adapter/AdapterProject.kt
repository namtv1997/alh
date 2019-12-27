package com.example.alohaandroid.ui.a_adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.alohaandroid.R
import com.example.alohaandroid.domain.remote.pojo.response.Project
import kotlinx.android.synthetic.main.item_project.view.*


class AdapterProject(private var project: MutableList<Project>) :
    RecyclerView.Adapter<AdapterProject.ViewHolder>() {
    private var listener: AdapterContactListener? = null

    interface AdapterContactListener {
        fun onclickItemEdit(project: Project)
        fun onclickItem(project: Project)
    }

    fun setListener(listener: AdapterContactListener) {
        this.listener = listener
    }

    fun setData(project: List<Project>) {
        this.project.clear()
        this.project.addAll(project)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_project,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(project[position])
        holder.itemView.ivEdit.setOnClickListener {
            listener!!.onclickItemEdit(project[position])
        }
        holder.itemView.setOnClickListener{
            listener!!.onclickItem(project[position])
        }

    }

    override fun getItemCount(): Int {
        return project.size
    }


    inner class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        @SuppressLint("SetTextI18n")
        fun bind(historyCall: Project) = with(itemView) {

            tvName.text = historyCall.name
            tvDescription.text = historyCall.description
        }
    }
}