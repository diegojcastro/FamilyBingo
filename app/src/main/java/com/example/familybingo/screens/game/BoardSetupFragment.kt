package com.example.familybingo.screens.game

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        binding.field11.setOnClickListener { editField(it) }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(BoardSetupViewModel::class.java)

    }

    //Button click functions will go here, listeners on onCreateView I think
    //but they'll mostly just call viewModel.buttonFuctionName to do the work there

    //Button to open field to input text should all go here though

    private fun editField (view : View) {
        val editText = binding.editTextBingoField
        editText.visibility = View.VISIBLE
        editText.requestFocus()

        // TODO automatically open keyboard
        // TODO grey out the background


    }

}