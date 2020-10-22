package com.example.familybingo.screens.game

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.familybingo.R
import com.example.familybingo.database.BingoDatabase
import com.example.familybingo.databinding.GameFragmentBinding
import kotlinx.android.synthetic.main.game_mark_field_dialog.view.*

class GameFragment : Fragment() {

    private lateinit var binding: GameFragmentBinding
    private lateinit var viewModel: GameViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.game_fragment, container, false)
        Log.i("GameFragment", "Called ViewModelProvider.get")

        // Trying to trace a nullpointer exception when creating the GameFragment
        val titleFromArgs = GameFragmentArgs.fromBundle(requireArguments()).boardTitle
        Log.i("GameFragment", "I think the boardTitle is: $titleFromArgs")


        // This is the block that adds all the Database+ViewModelFactory stuff
        // Reference to application context
        val application = requireNotNull(this.activity).application
        // Reference to data source
        val dataSource = BingoDatabase.getInstance(application).bingoDatabaseDao

        val viewModelFactory = GameViewModelFactory(dataSource, application, GameFragmentArgs.fromBundle(requireArguments()).boardTitle)
        viewModel = ViewModelProvider(this, viewModelFactory).get(GameViewModel::class.java)

        // Gives access to the ViewModel data for the binding object
        binding.gameViewModel = viewModel
        // Lets binding observe LiveData updates
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.markFieldDialog.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                val mDialogView = LayoutInflater.from(this.context).inflate(R.layout.game_mark_field_dialog, null)
                val mBuilder = AlertDialog.Builder(this.context)
                    .setView(mDialogView)
                    .setTitle("Mark this box!")
                val mAlertDialog = mBuilder.show()

                mDialogView.dialogCancel.setOnClickListener {
                    mAlertDialog.dismiss()
                }

                mDialogView.dialogMissed.setOnClickListener {
                    val i = viewModel.selectedFieldIndex.value!!
                    viewModel.markFieldMissed(i)
                    Log.i("GameFragment", "Marked entry $i as Missed: ${viewModel.bingoBoard.value?.get(i)?.marking.toString()}!")
                    mAlertDialog.dismiss()
                }

                mDialogView.dialogChecked.setOnClickListener {
                    val i = viewModel.selectedFieldIndex.value!!
                    viewModel.markFieldChecked(i)
                    Log.i("GameFragment", "Marked entry $i as Checked: ${viewModel.bingoBoard.value?.get(i)?.marking.toString()}!")
                    mAlertDialog.dismiss()
                }
            }
            viewModel.closeGameDialog()
        })


        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)

    }



}