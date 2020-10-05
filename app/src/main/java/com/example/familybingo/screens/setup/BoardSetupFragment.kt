package com.example.familybingo.screens.setup



import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.familybingo.R
import com.example.familybingo.database.BingoDatabase
import com.example.familybingo.databinding.BoardSetupFragmentBinding

class BoardSetupFragment : Fragment() {

    private lateinit var binding: BoardSetupFragmentBinding
    private lateinit var viewModel: BoardSetupViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate<BoardSetupFragmentBinding>(
            inflater, R.layout.board_setup_fragment, container, false)
        Log.i("BoardSetupFragment", "Called ViewModelProvider.get")

        // This creation of ViewModel was before I added database+factory, now irrelevant
        // viewModel = ViewModelProvider(this).get(BoardSetupViewModel::class.java)

        // This is the block that adds all the Database+ViewModelFactory stuff
        // Reference to application context
        val application = requireNotNull(this.activity).application
        // Reference to data source
        val dataSource = BingoDatabase.getInstance(application).bingoDatabaseDao
        // TODO create the proper parent name here by getting it from arguments?
        val viewModelFactory = BoardsetupViewModelFactory(dataSource, application, BoardSetupFragmentArgs.fromBundle(requireArguments()).boardTitle)
        viewModel = ViewModelProvider(this, viewModelFactory).get(BoardSetupViewModel::class.java)

        // Gives access to the ViewModel data for the binding object
        binding.boardSetupViewModel = viewModel
        // Lets binding observe LiveData updates
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(BoardSetupViewModel::class.java)

    }


}