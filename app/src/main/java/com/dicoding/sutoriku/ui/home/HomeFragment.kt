package com.dicoding.sutoriku.ui.home

import android.os.Bundle
import android.view.*
import com.dicoding.sutoriku.data.Result
import androidx.fragment.app.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.sutoriku.R
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

        setupObservers()
        setupRv()

        homeViewModel.findStory()
    }

    override fun onResume() {
        super.onResume()
        homeViewModel.findStory()
    }

    private fun setupRv() {
        binding.rvSutori.layoutManager = LinearLayoutManager(requireActivity())
        binding.rvSutori.adapter = SutoriAdapter()
    }

    private fun setupObservers() {
        homeViewModel.story.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> binding.progressBar.visibility = View.VISIBLE
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    (binding.rvSutori.adapter as SutoriAdapter).submitList(result.data)
                }

                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Snackbar.make(
                        requireView(),
                        getString(R.string.failed_load_story), Snackbar.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}