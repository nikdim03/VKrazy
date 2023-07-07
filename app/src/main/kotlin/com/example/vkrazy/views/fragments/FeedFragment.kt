package com.example.vkrazy.views.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
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
        binding = FragmentFeedBinding.bind(view)
        Log.d("FeedFragment", "onViewCreated()")
        (requireActivity().application as MyApplication).feedComponent.inject(this)

        adapter = FeedAdapter(emptyList())
        binding.feedRecycler.adapter = adapter
        binding.feedRecycler.layoutManager = LinearLayoutManager(requireContext())
        viewModel.onViewCreated()

        viewModel.feedItems.observe(viewLifecycleOwner) { feedItems ->
            feedItems?.let { adapter.setData(it) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.onDestroyView()
    }
}
