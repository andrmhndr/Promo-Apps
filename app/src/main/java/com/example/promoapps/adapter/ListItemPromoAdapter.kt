package com.example.promoapps.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.promoapps.R
import com.example.promoapps.model.PromoModel
import java.text.SimpleDateFormat

class ListItemPromoAdapter(private val listItem: ArrayList<PromoModel>, private val role: String): RecyclerView.Adapter<ListItemPromoAdapter.ListItemHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback
    private lateinit var onDeleteClickCallback: OnDeleteClickCallback

    interface OnItemClickCallback{
        fun onItemClicked(data: PromoModel)
    }

    interface OnDeleteClickCallback{
        fun onDeleteClicked(data: PromoModel)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    fun setDeleteClickCallback(onDeleteClickCallback: OnDeleteClickCallback){
        this.onDeleteClickCallback = onDeleteClickCallback
    }

    inner class ListItemHolder(itemVIew: View):RecyclerView.ViewHolder(itemVIew) {
        val txtTitle: TextView = itemVIew.findViewById(R.id.txt_title)
        val txtDescription: TextView = itemVIew.findViewById(R.id.txt_description)
        val txtDate: TextView = itemVIew.findViewById(R.id.txt_date)
        val btnDelete: Button = itemVIew.findViewById(R.id.btn_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_list_promo, parent, false)
        return ListItemHolder(view)
    }

    override fun onBindViewHolder(holder: ListItemHolder, position: Int) {
        val item = listItem[position]

        holder.txtTitle.text = item.title
        val dateFormat = SimpleDateFormat("dd-MM-yyyy")
        holder.txtDate.text = dateFormat.format(item.timestamp)
        holder.txtDescription.text = item.description
        if (role == Helper.USER){
            holder.btnDelete.visibility = View.INVISIBLE
        } else if (role == Helper.ADMIN){
            holder.btnDelete.setOnClickListener { onDeleteClickCallback.onDeleteClicked(item) }
        }
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(item) }
    }

    override fun getItemCount(): Int {
        return listItem.size
    }
}