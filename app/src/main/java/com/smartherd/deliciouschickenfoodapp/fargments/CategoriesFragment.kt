package com.smartherd.deliciouschickenfoodapp.fargments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.smartherd.deliciouschickenfoodapp.R
import com.smartherd.deliciouschickenfoodapp.activities.MainActivity
import com.smartherd.deliciouschickenfoodapp.adapters.CategoriesAdapter
import com.smartherd.deliciouschickenfoodapp.databinding.FragmentCategoriesBinding
import com.smartherd.deliciouschickenfoodapp.viewmodel.HomeViewModel


class CategoriesFragment : Fragment() {

    private lateinit var binding : FragmentCategoriesBinding
    private lateinit var categoryAdapter : CategoriesAdapter
    private lateinit var viewModel : HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoriesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecyclerView()
        observeCategories()
    }

    private fun observeCategories() {
            viewModel.observeCategoryLiveData().observe(viewLifecycleOwner, Observer {
                categoryAdapter.setCategories(it)
            })
    }

    private fun prepareRecyclerView() {
        categoryAdapter = CategoriesAdapter()
        binding.rvCategories.apply {
            layoutManager = GridLayoutManager(context,3,GridLayoutManager.VERTICAL,false)
            adapter = categoryAdapter
        }
    }

}