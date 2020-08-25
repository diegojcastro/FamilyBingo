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


    private lateinit var viewModel: BoardSetupViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<BoardSetupFragmentBinding>(
            inflater, R.layout.board_setup_fragment, container, false)
        Log.i("BoardSetupFragment", "Called ViewModelProvider.get")
        viewModel = ViewModelProvider(this).get(BoardSetupViewModel::class.java)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(BoardSetupViewModel::class.java)
        // TODO: Use the ViewModel
    }

}