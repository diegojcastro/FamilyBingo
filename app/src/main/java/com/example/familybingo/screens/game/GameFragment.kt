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
import kotlinx.android.synthetic.main.game_fragment.*
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


        //Observer for markFieldDialog (to popup whether we check/miss each entry on click)
        viewModel.markFieldDialog.observe(viewLifecycleOwner, Observer {
            Log.i("GameFragment", "Started observer on gameViewModel markFieldDialog")

            if (it == true) {
                val info1 = viewModel.markFieldDialog.value
                val info2 = viewModel.selectedFieldIndex.value
                Log.i("GameFragment", "I saw markFieldDialog switch to true.")
                Log.i("GameFragment", "markFieldDialog = $info1 and fieldIndex = $info2.")

                val mDialogView = LayoutInflater.from(this.context).inflate(R.layout.game_mark_field_dialog, null)
                val mBuilder = AlertDialog.Builder(this.context)
                    .setView(mDialogView)
                    .setTitle("Mark this box!")
                val mAlertDialog = mBuilder.show()


                mDialogView.fieldText.text = viewModel.selectedFieldText.value

                mDialogView.dialogCancel.setOnClickListener {
                    mAlertDialog.dismiss()
                }

                mDialogView.dialogUnmarked.setOnClickListener {
                    val i = viewModel.selectedFieldIndex.value!!
                    viewModel.markFieldDefault(i, viewModel.selectedView.value!!)
                    Log.i("GameFragment", "Marked entry $i as Unmarked: ${viewModel.bingoBoard.value?.get(i)?.marking.toString()}!")
                    mAlertDialog.dismiss()
                }

                mDialogView.dialogMissed.setOnClickListener {
                    val i = viewModel.selectedFieldIndex.value!!
                    // if I need a reference to the clicked view, use this one:
                    viewModel.markFieldMissed(i, viewModel.selectedView.value!!)
                    Log.i("GameFragment", "Marked entry $i as Missed: ${viewModel.bingoBoard.value?.get(i)?.marking.toString()}!")
                    mAlertDialog.dismiss()
                }

                mDialogView.dialogChecked.setOnClickListener {
                    val i = viewModel.selectedFieldIndex.value!!
                    // if I need a reference to the clicked view, use this one:
                    viewModel.markFieldChecked(i, viewModel.selectedView.value!!)
                    Log.i("GameFragment", "Marked entry $i as Checked: ${viewModel.bingoBoard.value?.get(i)?.marking.toString()}!")
                    mAlertDialog.dismiss()
                }
                viewModel.closeGameDialog()
            } // end if(it == true)

        })

        // An attempt at an observer to change the backgorund colors.
        viewModel.bingoBoard.observe(viewLifecycleOwner, Observer { newBoard ->
            updateBackgrounds()

        })


        return binding.root
    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)

    }

    private fun updateBackgrounds() {
        field11.setBackgroundResource(viewModel.bingoBoard.value!![0].marking)
        field12.setBackgroundResource(viewModel.bingoBoard.value!![1].marking)
        field13.setBackgroundResource(viewModel.bingoBoard.value!![2].marking)
        field14.setBackgroundResource(viewModel.bingoBoard.value!![3].marking)
        field15.setBackgroundResource(viewModel.bingoBoard.value!![4].marking)

        field21.setBackgroundResource(viewModel.bingoBoard.value!![5].marking)
        field22.setBackgroundResource(viewModel.bingoBoard.value!![6].marking)
        field23.setBackgroundResource(viewModel.bingoBoard.value!![7].marking)
        field24.setBackgroundResource(viewModel.bingoBoard.value!![8].marking)
        field25.setBackgroundResource(viewModel.bingoBoard.value!![9].marking)

        field31.setBackgroundResource(viewModel.bingoBoard.value!![10].marking)
        field32.setBackgroundResource(viewModel.bingoBoard.value!![11].marking)
        field33.setBackgroundResource(viewModel.bingoBoard.value!![12].marking)
        field34.setBackgroundResource(viewModel.bingoBoard.value!![13].marking)
        field35.setBackgroundResource(viewModel.bingoBoard.value!![14].marking)

        field41.setBackgroundResource(viewModel.bingoBoard.value!![15].marking)
        field42.setBackgroundResource(viewModel.bingoBoard.value!![16].marking)
        field43.setBackgroundResource(viewModel.bingoBoard.value!![17].marking)
        field44.setBackgroundResource(viewModel.bingoBoard.value!![18].marking)
        field45.setBackgroundResource(viewModel.bingoBoard.value!![19].marking)

        field51.setBackgroundResource(viewModel.bingoBoard.value!![20].marking)
        field52.setBackgroundResource(viewModel.bingoBoard.value!![21].marking)
        field53.setBackgroundResource(viewModel.bingoBoard.value!![22].marking)
        field54.setBackgroundResource(viewModel.bingoBoard.value!![23].marking)
        field55.setBackgroundResource(viewModel.bingoBoard.value!![24].marking)

    }


}