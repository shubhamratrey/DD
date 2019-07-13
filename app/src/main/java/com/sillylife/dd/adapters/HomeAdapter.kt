package com.sillylife.dd.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sillylife.dd.R
import com.sillylife.dd.modal.MixedDataItem
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_home_layout.*

class HomeAdapter(val context: Context, val simpleList: ArrayList<MixedDataItem>, val listener: (Any, Int) -> Unit) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    val commonItemLists = ArrayList<Any>()
    var TAG = HomeAdapter::class.java.simpleName

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_home_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return simpleList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = simpleList[holder.adapterPosition]
        holder.text.text = "${data.title}  ${data.type} "
        holder.containerView.setOnClickListener {
            listener(data, holder.adapterPosition)
        }
    }

    class ViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer

}