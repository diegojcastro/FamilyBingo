package com.example.familybingo.screens.load

import android.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.familybingo.R
import com.example.familybingo.database.BoardHolder
import kotlinx.android.synthetic.main.game_mark_field_dialog.view.*
import kotlinx.android.synthetic.main.load_delete_dialog.view.*
import java.text.SimpleDateFormat
import java.util.*

class BoardHolderAdapter : RecyclerView.Adapter<BoardHolderAdapter.ViewHolder>() {

    var data = listOf<BoardHolder>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    private val _selectedTitleText = MutableLiveData<String>()
    val selectedTitleText: LiveData<String>
        get() = _selectedTitleText

    private val _flagForDeletion = MutableLiveData<Boolean>()
    val flagForDeletion: LiveData<Boolean>
        get() = _flagForDeletion

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

        holder.trashImage.setOnClickListener {view : View ->
            Log.i("LoadGameViewModel", "Trash image clicked on ${holder.title.text}")
            val mDialogView = LayoutInflater.from(holder.trashImage.context).inflate(R.layout.load_delete_dialog, null)
            val mBuilder = AlertDialog.Builder(holder.trashImage.context)
                .setView(mDialogView)
                .setTitle("Confirm deletion of this game:")
            val mAlertDialog = mBuilder.show()

            mDialogView.deleteFieldText.text = holder.title.text.toString()
            _selectedTitleText.value = holder.title.text.toString()



            mDialogView.deleteDialogCancel.setOnClickListener {
                mAlertDialog.dismiss()
            }

            mDialogView.deleteDialogDelete.setOnClickListener {
                Log.i("BoardHolderAdapter", "Clicked the delete button.")
                _flagForDeletion.value = true
                Log.i("BoardHolderAdapter", "Flagged \"${_selectedTitleText.value}\" for deletion: ${_flagForDeletion.value}.")

                mAlertDialog.dismiss()


            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(R.layout.list_item_board_title, parent, false)
        return ViewHolder(view)
    }

    fun setDeletionFalse() {
        _flagForDeletion.value = false
    }

}