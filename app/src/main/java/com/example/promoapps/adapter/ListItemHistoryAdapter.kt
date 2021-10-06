package com.example.promoapps.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.promoapps.R
import com.example.promoapps.model.HistoryModel
import java.text.SimpleDateFormat

class ListItemHistoryAdapter(private val listItem: ArrayList<HistoryModel>, private val role: String): RecyclerView.Adapter<ListItemHistoryAdapter.ListItemHolder>() {
    inner class ListItemHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val txtTitle: TextView = itemView.findViewById(R.id.txt_title)
        val txtDate: TextView = itemView.findViewById(R.id.txt_date)
        val txtUser: TextView = itemView.findViewById(R.id.txt_user)
        val txtUserId: TextView = itemView.findViewById(R.id.txt_userid)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_list_history, parent, false)
        return ListItemHolder(view)
    }

    override fun onBindViewHolder(holder: ListItemHolder, position: Int) {
        val item = listItem[position]

        holder.txtTitle.text = item.title
        val dateFormat = SimpleDateFormat("dd-MM-yyyy")
        holder.txtDate.text = dateFormat.format(item.timestamp!!)
        if (role == Helper.ADMIN){
            holder.txtUser.text = item.user
            holder.txtUserId.text = item.userid
        }else if(role == Helper.USER){
            holder.txtUserId.visibility = View.INVISIBLE
            holder.txtUser.text = item.description
        }
    }

    override fun getItemCount(): Int = listItem.size
}