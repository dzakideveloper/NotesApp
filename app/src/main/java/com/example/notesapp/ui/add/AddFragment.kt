package com.example.notesapp.ui.add

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.notesapp.NotesViewModel
import com.example.notesapp.R
import com.example.notesapp.ViewModelFactory
import com.example.notesapp.data.entity.Notes
import com.example.notesapp.data.entity.Priority
import com.example.notesapp.databinding.FragmentAddBinding
import com.example.notesapp.utils.ExtensionFunction.setActionBar
import com.example.notesapp.utils.HelperFunctions
import com.example.notesapp.utils.HelperFunctions.parseToPriority
import java.text.SimpleDateFormat
import java.util.*

class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding as FragmentAddBinding

    private val addViewModel by viewModels<NotesViewModel>()

    private val _addViewModel: NotesViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

//        _addViewModel = activity?.let { obtainViewModel(it) }

        binding.toolbarAdd.setActionBar(requireActivity())

        binding.spinnerPriorities.onItemSelectedListener =
            HelperFunctions.spinnerListener(requireContext(), binding.priorityIndicator)
    }

    private fun obtainViewModel(activity: FragmentActivity) : NotesViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[NotesViewModel::class.java]
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_save, menu)
        super.onCreateOptionsMenu(menu, inflater)

        val action = menu.findItem(R.id.menu_save)
        action.actionView.findViewById<AppCompatImageButton>(R.id.btn_save).setOnClickListener{
            insertNotes()
            findNavController().navigate(R.id.action_addFragment_to_homeFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun insertNotes() {
        binding.apply {
            val title = edtTitle.text.toString()
            val priority = spinnerPriorities.selectedItem.toString()
            val desc = edtDescription.text.toString()
            val calendar = Calendar.getInstance().time
            val date = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(calendar)

            val note = Notes(
                0,
                title,
                parseToPriority(priority, context),
                desc,
                date
            )

            if (edtTitle.text.isEmpty() || edtDescription.text.isEmpty()){
                Toast.makeText(context, "Please fill fields.", Toast.LENGTH_SHORT).show()
            } else{
                addViewModel.insertData(note)
                Toast.makeText(context, "Succesfully add note.", Toast.LENGTH_SHORT).show()
            }
            Log.i("AddFragment", "insetNote: $note")
        }
    }
}