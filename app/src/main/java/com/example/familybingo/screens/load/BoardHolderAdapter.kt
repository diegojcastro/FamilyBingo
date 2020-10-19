package com.example.familybingo.screens.load

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.familybingo.R
import com.example.familybingo.database.BoardHolder

class BoardHolderAdapter : RecyclerView.Adapter<BoardHolderAdapter.ViewHolder>() {

    var data = listOf<BoardHolder>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val title: TextView = itemView.findViewById(R.id.list_item_title)
        val statusTitle: TextView = itemView.findViewById(R.id.list_status_title)
        val status: TextView = itemView.findViewById(R.id.list_status)
        val scoreTitle: TextView = itemView.findViewById(R.id.list_score_title)
        val score: TextView = itemView.findViewById(R.id.list_score)
        val lastPlayed: TextView = itemView.findViewById(R.id.list_last_opened)
        val date: TextView = itemView.findViewById(R.id.list_date)
        val trashImage: ImageView = itemView.findViewById(R.id.list_trash_image)
    }


    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.title.text = item.title
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(R.layout.list_item_board_title, parent, false)
        return ViewHolder(view)
    }

}