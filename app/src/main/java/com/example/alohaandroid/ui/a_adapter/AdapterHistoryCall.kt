package com.example.alohaandroid.ui.a_adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.alohaandroid.R
import com.example.alohaandroid.domain.remote.pojo.response.HistoryCall
import kotlinx.android.synthetic.main.item_history_call.view.*


class AdapterHistoryCall(private var historyCalls: MutableList<HistoryCall>) :
    RecyclerView.Adapter<AdapterHistoryCall.ViewHolder>() {

    fun setData(historyCall: List<HistoryCall>) {
        this.historyCalls.clear()
        this.historyCalls.addAll(historyCall)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_history_call,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(historyCalls[position])

    }

    override fun getItemCount(): Int {
        return historyCalls.size
    }


    inner class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        @SuppressLint("SetTextI18n")
        fun bind(historyCall: HistoryCall) = with(itemView) {

            tvPhone.text = historyCall.phone

            val time = historyCall.timeCall
            var hour = time?.div(60)
            val minute = hour?.rem(60)
            val second = time?.rem(60)

            hour = hour?.div(60)
//            tvDate.text = historyCall.dateCreate
            tvTime.text = "$hour : $minute :$second"

            if (historyCall.type == 1){
                ivStatusCall.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_icon_in))
            }
            if (historyCall.type == 2){
                ivStatusCall.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_icon_out_missed))
            }
            if (historyCall.type == 3){
                ivStatusCall.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_icon_in_missed))
            }

        }
    }
}