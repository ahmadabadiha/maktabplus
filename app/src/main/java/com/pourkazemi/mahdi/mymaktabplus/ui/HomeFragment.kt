package com.pourkazemi.mahdi.mymaktabplus.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.pourkazemi.mahdi.mymaktabplus.R
import com.pourkazemi.mahdi.mymaktabplus.databinding.FragmentHomeBinding
import com.pourkazemi.mahdi.mymaktabplus.util.ResultWrapper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {
    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding
        get() = _binding!!

    /*
        @Inject
        lateinit var viewModel: HomeViewModel*/
    private val viewModel: HomeViewModel by viewModels()

    @Inject
    lateinit var adapter: RecycleAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)
        binding.recycleView.adapter = adapter

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.pictureFlow.collect {
                    when (it) {
                        is ResultWrapper.Loading -> startAnime()
                        is ResultWrapper.Success -> {
                            adapter.submitList(it.value)
                            stopAnime()
                        }
                        is ResultWrapper.Error -> {
                            stopAnime()
                            Toast.makeText(
                                requireActivity(),
                                it.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    fun startAnime() {
        binding.loadingAnime.isVisible = true
        binding.loadingAnime.playAnimation()
    }

    fun stopAnime() {
        binding.loadingAnime.isVisible = false
        binding.loadingAnime.pauseAnimation()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}