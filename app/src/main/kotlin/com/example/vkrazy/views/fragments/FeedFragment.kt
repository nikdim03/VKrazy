package com.example.vkrazy.views.fragments

import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vkrazy.R
import com.example.vkrazy.databinding.FragmentFeedBinding
import com.example.vkrazy.viewmodels.FeedViewModel
import com.example.vkrazy.viewmodels.MyApplication
import com.example.vkrazy.views.adapters.CompositeAdapter
import kotlinx.coroutines.launch
import javax.inject.Inject

class FeedFragment : Fragment(R.layout.fragment_feed) {
    @Inject
    lateinit var viewModel: FeedViewModel
    private lateinit var binding: FragmentFeedBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFeedBinding.bind(view)
        Log.d("FeedFragment", "onViewCreated()")
        (requireActivity().application as MyApplication).feedComponent.inject(this)

        binding.feedRecycler.adapter = CompositeAdapter(mutableListOf())
        binding.feedRecycler.layoutManager = LinearLayoutManager(requireContext())
        viewModel.onViewCreated()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.feedItems.collect { feedItems ->
                feedItems?.let {
                    (binding.feedRecycler.adapter as CompositeAdapter).setData(it.toMutableList())
                }
            }
        }
        binding.addPostButton.setOnClickListener {
            showAddPostPopup()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.onDestroyView()
    }

    private fun showAddPostPopup() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("What's on your mind?")
        builder.setMessage("Enter the text here")

        // Inflate a custom view with a checkbox
        val checkBoxView = View.inflate(requireContext(), R.layout.checkbox, null)
        val checkBox = checkBoxView.findViewById<CheckBox>(R.id.checkbox)
        checkBox.text = "Share with friends only"
        builder.setView(checkBoxView)

        val input = EditText(requireContext())
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)
        builder.setPositiveButton("Post") { _, _ ->
            val text = input.text.toString()
            val isPrivate = checkBox.isChecked // Get the checkbox value
            viewModel.sendPost(text, isPrivate)
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }
        builder.show()
    }
}
