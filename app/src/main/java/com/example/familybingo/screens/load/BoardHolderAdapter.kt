package com.example.familybingo.screens.load

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.familybingo.R
import com.example.familybingo.database.BoardHolder
import java.text.SimpleDateFormat
import java.util.*

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
        holder.status.text = item.playStatus
        if (item.playStatus == "Setup")
            holder.score.text = "N/A"
        else
            holder.score.text = item.score.toString()
        //holder.date.text = item.lastOpened.toString()
        val time = Date(item.lastOpened)
        val format = SimpleDateFormat("yyyy.MM.dd HH:mm")
        holder.date.text = format.format(time)

        // Trying to navigate to BoardSetup with the right title.
        holder.title.setOnClickListener {view : View ->
            val myNavC = view.findNavController()
            when (holder.status.text) {
                "Setup" -> {
                    val actionToSetup = LoadGameFragmentDirections.actionLoadGameFragmentToBoardSetupFragment()
                    actionToSetup.boardTitle = holder.title.text.toString()
                    Log.i("LoadGameViewModel", "Clicked the title that says ${holder.title.text}, navigating to ${holder.status.text}")
                    myNavC.navigate(actionToSetup)
                }
                "Playing Game" -> {
                    val actionToGame = LoadGameFragmentDirections.actionLoadGameFragmentToGameFragment()
                    actionToGame.boardTitle = holder.title.text.toString()
                    Log.i("LoadGameViewModel", "Clicked the title that says ${holder.title.text}, navigating to ${holder.status.text}")
                    myNavC.navigate(actionToGame)
                }
                else -> Log.i("LoadGameViewModel", "Not a suitable game state (setup / playing game)")
            }
        }

        // TODO onclicklistener for the trash icon, open delete dialog
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(R.layout.list_item_board_title, parent, false)
        return ViewHolder(view)
    }

}