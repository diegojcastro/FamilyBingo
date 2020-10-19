package com.example.familybingo.screens.load

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
import com.example.familybingo.databinding.LoadGameFragmentBinding

class LoadGameFragment : Fragment() {

    private lateinit var binding: LoadGameFragmentBinding
    private lateinit var viewModel: LoadGameViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.load_game_fragment, container, false)
        Log.i("LoadGameFragment", "Called ViewModelProvider.get")

        // This is the block that adds all the Database+ViewModelFactory stuff
        // Reference to application context
        val application = requireNotNull(this.activity).application
        // Reference to data source
        val dataSource = BingoDatabase.getInstance(application).bingoDatabaseDao

        val viewModelFactory = LoadGameViewModelFactory(dataSource, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(LoadGameViewModel::class.java)

        // Gives access to the ViewModel data for the binding object
        binding.loadGameViewModel = viewModel
        // Lets binding observe LiveData updates
        binding.lifecycleOwner = viewLifecycleOwner

        val adapter = BoardHolderAdapter()
        binding.bingoBoardTitlesList.adapter = adapter

        viewModel.allTitles.observe(viewLifecycleOwner, Observer {
            // A different way of doing a null check.
            it?.let {
                adapter.data = it
            }
        })

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoadGameViewModel::class.java)
    }

}