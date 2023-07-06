package com.example.vkrazy.views.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vkrazy.R
import com.example.vkrazy.databinding.FragmentFeedBinding
import com.example.vkrazy.viewmodels.FeedViewModel
import com.example.vkrazy.viewmodels.MyApplication
import com.example.vkrazy.views.adapters.FeedAdapter
import javax.inject.Inject

class FeedFragment : Fragment(R.layout.fragment_feed) {
    @Inject
    lateinit var viewModel: FeedViewModel
    private lateinit var adapter: FeedAdapter
    private lateinit var binding: FragmentFeedBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Initialize RecyclerView and Adapter
        binding = FragmentFeedBinding.bind(view)
        (requireActivity().application as MyApplication).appComponent.inject(this)
        adapter = FeedAdapter(emptyList()) // Initialize adapter with an empty list for now
        binding.feedRecycler.adapter = adapter
        binding.feedRecycler.layoutManager = LinearLayoutManager(requireContext())
        // Fetch posts using ViewModel
        viewModel.onViewCreated()

        // Observe the LiveData property in the ViewModel and update the adapter's data
        viewModel.feedItems.observe(viewLifecycleOwner) { feedItems ->
            feedItems?.let { adapter.setData(it) }
        }
    }
}
