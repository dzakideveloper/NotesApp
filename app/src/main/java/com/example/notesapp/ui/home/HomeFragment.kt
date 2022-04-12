package com.example.notesapp.ui.home

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notesapp.NotesViewModel
import com.example.notesapp.R
import com.example.notesapp.data.entity.Notes
import com.example.notesapp.databinding.FragmentHomeBinding
import com.example.notesapp.utils.ExtensionFunction.setActionBar
import com.example.notesapp.utils.HelperFunctions
import com.example.notesapp.utils.HelperFunctions.checkIsDataEmpty
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment(), SearchView.OnQueryTextListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding as FragmentHomeBinding

    private val homeViewModel by viewModels<NotesViewModel>()
    private val homeAdapter by lazy { HomeAdapter() }

    private var _courotineData: List<Notes>? = null
    private val currentData get() = _courotineData as List<Notes>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mHelperFunction = HelperFunctions

        setHasOptionsMenu(true)

        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)

        binding.apply {
            toolbarHome.setActionBar(requireActivity())

            fabAdd.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_addFragment)
            }
        }
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding.rvHome.apply {
            homeViewModel.getAllData().observe(viewLifecycleOwner) {
                checkIsDataEmpty(it)
                showEmptyDataLayout(it)
                homeAdapter.setData(it)
                _courotineData = it
            }
            adapter = homeAdapter
            layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
            swipeToDelete(this)
        }
    }

    private fun showEmptyDataLayout(data: List<Notes>?) {
        when (data?.isEmpty()) {
            true -> {
                binding.rvHome.visibility = View.INVISIBLE
                binding.imgNoNotes.visibility = View.VISIBLE
            }
            else -> {
                binding.rvHome.visibility = View.VISIBLE
                binding.imgNoNotes.visibility = View.INVISIBLE
            }
        }
    }

    //buat manggil menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_home, menu)
        super.onCreateOptionsMenu(menu, inflater)

        val search = menu.findItem(R.id.menu_search)
        val searchAction = search.actionView as? SearchView
        searchAction?.setOnQueryTextListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_priority_high -> homeViewModel.shortByHighPriority().observe(this) {
                homeAdapter.setData(it)
            }
            R.id.menu_priority_low -> homeViewModel.shortByLowPriority().observe(this) {
                homeAdapter.setData(it)
            }
            R.id.menu_delete_all -> confirmDeleteAll()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun confirmDeleteAll() {
        if (currentData.isEmpty()) {
            AlertDialog.Builder(requireContext())
                .setTitle("No Notes")
                .setMessage("Data Kosong !!")
                .setPositiveButton("Closed") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        } else {
            AlertDialog.Builder(requireContext())
                .setTitle("Delete All Your Notes?")
                .setMessage("Are you sure want clear all of this data ?")
                .setPositiveButton("Yes") { _, _ ->
                    homeViewModel.deleteAllData()
                    Toast.makeText(requireContext(), "Succesfully delete data", Toast.LENGTH_SHORT)
                        .show()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        val querySearch = "%$query%"
        query?.let {
            homeViewModel.searchByQuery(querySearch).observe(this) {
                homeAdapter.setData(it)
            }
        }
        return true
    }

    private fun swipeToDelete(recyclerView: RecyclerView) {
        val swipeToDelete = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT

        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deteledItem = homeAdapter.listNotes[viewHolder.adapterPosition]
                homeViewModel.deleteNote(deteledItem)
                restoredData(viewHolder.itemView, deteledItem)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDelete)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun restoredData(view: View, deteledItem: Notes) {
        val snackBar = Snackbar.make(
            view, "Deleted: '${deteledItem.title}'", Snackbar.LENGTH_LONG
        )
        snackBar.setTextColor(ContextCompat.getColor(view.context, R.color.black))
        snackBar.setAction("Undo") {
            homeViewModel.insertData(deteledItem)
        }
        snackBar.setActionTextColor(ContextCompat.getColor(view.context, R.color.black))
        snackBar.show()
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        val querySearch = "%$newText%"
        newText?.let {
            homeViewModel.searchByQuery(querySearch).observe(this) {
                homeAdapter.setData(it)
            }
        }
        return true
    }
}


