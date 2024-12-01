package com.dicoding.sutoriku.ui.home

import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.fragment.app.*
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.sutoriku.data.adapter.LoadingStateAdapter
import com.dicoding.sutoriku.data.adapter.SutoriAdapter
import com.dicoding.sutoriku.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels {
        HomeViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObserver()
    }

    private fun setupObserver() {
        val adapter = SutoriAdapter()
        binding.rvSutori.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvSutori.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        homeViewModel.story.observe(requireActivity()) {
            adapter.submitData(lifecycle, it)
        }

        adapter.addLoadStateListener { loadState ->
            _binding?.let { binding ->
                binding.progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                if (loadState.source.refresh is LoadState.Error) {
                    val error = (loadState.source.refresh as LoadState.Error).error
                    Snackbar.make(
                        binding.root,
                        "Error: ${error.message}",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}