package com.example.notesapp.utils

import android.view.View
import android.widget.Spinner
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.example.notesapp.R
import com.example.notesapp.data.entity.Notes
import com.example.notesapp.data.entity.Priority
import com.example.notesapp.ui.home.HomeFragmentDirections

object BindingAdapters {

    @BindingAdapter("android:parsePriorityColor")
    @JvmStatic

    fun parsePriorityColor(cardView: CardView, priority: Priority) {
        when (priority) {
            Priority.LOW -> cardView.setCardBackgroundColor(cardView.context.getColor(R.color.pink))
            Priority.MEDUIM -> cardView.setCardBackgroundColor(cardView.context.getColor(R.color.yellow))
            Priority.HIGH -> cardView.setCardBackgroundColor(cardView.context.getColor(R.color.green))
        }
    }

    @BindingAdapter("android:emptyDatabase")
    @JvmStatic

    fun emptyDatabase(view: View, emptyDatabase: MutableLiveData<Boolean>){
        when (emptyDatabase.value){
            true -> view.visibility = View.VISIBLE
            else -> view.visibility = View.INVISIBLE
        }
    }

    @BindingAdapter("android:sendDataToDetail")
    @JvmStatic
    fun sendDataToDetail(view: ConstraintLayout, currentItem: Notes){
        view.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(currentItem)
            view.findNavController().navigate(action)
        }
    }

    @BindingAdapter("android:parsePriorityToInt")
    @JvmStatic
    fun parsePriorityToInt(view: Spinner, priority: Priority) {
        when (priority) {
            Priority.HIGH -> {
                view.setSelection(0)
            }
            Priority.MEDUIM -> {
                view.setSelection(1)
            }
            Priority.LOW -> {
                view.setSelection(2)
            }
        }
    }

}