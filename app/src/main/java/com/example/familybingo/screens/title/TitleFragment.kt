package com.example.familybingo.screens.title

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.familybingo.R
import com.example.familybingo.databinding.FragmentTitleBinding
import kotlinx.android.synthetic.main.new_game_dialog.view.*


class TitleFragment : Fragment() {



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentTitleBinding>(inflater,
            R.layout.fragment_title, container, false)


        // Original code to navigate to boardSetupFragment
//        binding.playButton.setOnClickListener{view : View ->
//            view.findNavController().navigate(R.id.action_titleFragment_to_boardSetupFragment)
//        }


        binding.playButton.setOnClickListener {view : View ->
            val myNavC = view.findNavController()
            val mDialogView = LayoutInflater.from(this.context).inflate(R.layout.new_game_dialog, null)
            val mBuilder = AlertDialog.Builder(this.context)
                .setView(mDialogView)
                .setTitle("New Game Title")
            val mAlertDialog = mBuilder.show()

            mDialogView.dialogConfirm.setOnClickListener {
                mAlertDialog.dismiss()
                val argTextGameTitle = mDialogView.newGameTitle.text.toString()
                myNavC.navigate(R.id.action_titleFragment_to_boardSetupFragment)

            }
            mDialogView.dialogCancel.setOnClickListener {
                mAlertDialog.dismiss()
            }
        }


        return binding.root
    }




}