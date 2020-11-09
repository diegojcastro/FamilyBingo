package com.appsbydiego.familybingo.screens.setup

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
import androidx.navigation.findNavController
import com.appsbydiego.familybingo.R
import com.appsbydiego.familybingo.database.BingoDatabase
import com.appsbydiego.familybingo.databinding.BoardSetupFragmentBinding
import kotlinx.android.synthetic.main.board_setup_edit_field_dialog.*
import kotlinx.android.synthetic.main.board_setup_edit_field_dialog.view.*

class BoardSetupFragment : Fragment() {

    private lateinit var binding: BoardSetupFragmentBinding
    private lateinit var viewModel: BoardSetupViewModel

    // binding declaration was originally:
    // binding = DataBindingUtil.inflate<BoardSetupFragmentBinding>
    // removed the explicit <BoardSetupFragmentBinding>, leaving note for reference.
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.board_setup_fragment, container, false)
//        Log.i("BoardSetupFragment", "Called ViewModelProvider.get")

        // This creation of ViewModel was before I added database+factory, now irrelevant
        // viewModel = ViewModelProvider(this).get(BoardSetupViewModel::class.java)

        // This is the block that adds all the Database+ViewModelFactory stuff
        // Reference to application context
        val application = requireNotNull(this.activity).application
        // Reference to data source
        val dataSource = BingoDatabase.getInstance(application).bingoDatabaseDao

        val viewModelFactory = BoardSetupViewModelFactory(dataSource, application, BoardSetupFragmentArgs.fromBundle(requireArguments()).boardTitle)
        viewModel = ViewModelProvider(this, viewModelFactory).get(BoardSetupViewModel::class.java)

        // Gives access to the ViewModel data for the binding object
        binding.boardSetupViewModel = viewModel
        // Lets binding observe LiveData updates
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.navigateToGameFragment.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                val myNavC = requireView().findNavController()
                val action = BoardSetupFragmentDirections.actionBoardSetupFragmentToGameFragment()
                action.boardTitle = viewModel.boardTitle
//                Log.i("BoardSetupFragment", "Trying to move to GameFragment, board title argument is: ${action.boardTitle}")

                myNavC.navigate(action)
            }

        })

        viewModel.editFieldVisible.observe(viewLifecycleOwner, Observer {
            if (it == true) {
//                Log.i("BoardSetupFragment", "I saw editFieldVisible switch to true.")
                val mDialogView = LayoutInflater.from(this.context).inflate(R.layout.board_setup_edit_field_dialog, null)
                val mBuilder = AlertDialog.Builder(this.context)
                    .setView(mDialogView)
                    .setTitle("Edit this field:")
                val mAlertDialog = mBuilder.show()

                mDialogView.boardSetupEditTextField.setText(viewModel.newBingoBoard.value?.get(viewModel.currentEditField.value!!)?.text)

                mDialogView.boardSetupEditTextField.requestFocus()

                mDialogView.setupEditDialogCancel.setOnClickListener {
                    mAlertDialog.dismiss()
                }

                mDialogView.setupEditDialogConfirm.setOnClickListener {
//                    Log.i("BoardSetupFragment", "Trying to edit the text in this field.")
                    val i = viewModel.currentEditField.value!!
                    val newText = mAlertDialog.boardSetupEditTextField.text.toString()
                    viewModel.editTextEntry(i, newText)
//                    Log.i("BoardSetupFragment", "Trying to edit the text in index $i and set to: $newText")
                    viewModel.finishedEditingEntry()

                    mAlertDialog.dismiss()
                }




            }
        })

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(BoardSetupViewModel::class.java)

    }


}