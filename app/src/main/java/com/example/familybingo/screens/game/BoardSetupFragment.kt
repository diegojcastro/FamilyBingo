package com.example.familybingo.screens.game


import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.familybingo.R
import com.example.familybingo.databinding.BoardSetupFragmentBinding

class BoardSetupFragment : Fragment() {

    private lateinit var binding: BoardSetupFragmentBinding
    private lateinit var viewModel: BoardSetupViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate<BoardSetupFragmentBinding>(
            inflater, R.layout.board_setup_fragment, container, false)
        Log.i("BoardSetupFragment", "Called ViewModelProvider.get")
        viewModel = ViewModelProvider(this).get(BoardSetupViewModel::class.java)

        binding.field11.setOnClickListener { editField(it as TextView) }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(BoardSetupViewModel::class.java)

    }

    //Button click functions will go here, listeners on onCreateView I think
    //but they'll mostly just call viewModel.buttonFuctionName to do the work there


    // If I rotate the screen this is reset. Should I try changing it?
    private fun editField (view : TextView) {
        val editText = binding.editTextBingoField
        val backgroundBlur = binding.blurBackgroundImageView
        val editButton = binding.editFieldButton
        val doneButton = binding.doneEditingFieldButton
        editButton.visibility = View.VISIBLE
        doneButton.visibility = View.VISIBLE
        backgroundBlur.visibility = View.VISIBLE
        editText.visibility = View.VISIBLE
        editText.setText(view.text)
        editText.requestFocus()

        //Gotta add getActivity() below because we're calling this from a fragment
        // Show the keyboard.
        val imm = getActivity()?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, 0)

    }

}