package com.smartherd.deliciouschickenfoodapp.fargments.bottomsheet

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.smartherd.deliciouschickenfoodapp.R
import com.smartherd.deliciouschickenfoodapp.activities.MainActivity
import com.smartherd.deliciouschickenfoodapp.activities.MealActivity
import com.smartherd.deliciouschickenfoodapp.databinding.FragmentCategoriesBinding
import com.smartherd.deliciouschickenfoodapp.databinding.FragmentMealBottomSheetBinding
import com.smartherd.deliciouschickenfoodapp.fargments.HomeFragment
import com.smartherd.deliciouschickenfoodapp.viewmodel.HomeViewModel

private const val MEAL_ID = "param1"


class MealBottomSheetFragment : BottomSheetDialogFragment() {
    private var mealId: String? = null

    private lateinit var binding: FragmentMealBottomSheetBinding
    private lateinit var viewModel: HomeViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = (activity as MainActivity).viewModel

        arguments?.let {
            mealId = it.getString(MEAL_ID)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMealBottomSheetBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mealId?.let {
            viewModel.getMealById(it)
        }

        observeBottomSheet()
        onBottomSheetDialogClick()
    }

    private fun onBottomSheetDialogClick() {
        binding.bottomSheet.setOnClickListener {
            if (mealName != null && mealThump != null){
                val intent = Intent(activity, MealActivity::class.java)
                intent.apply {
                    putExtra(HomeFragment.MEAL_ID, mealId)
                    putExtra(HomeFragment.MEAL_NAME, mealName)
                    putExtra(HomeFragment.MEAL_THUMB, mealThump)
                }

                startActivity(intent)
            }
        }
    }

    private var mealName : String? = null
    private var mealThump : String? = null
    private fun observeBottomSheet() {
        viewModel.observeBottomSheetLivedata().observe(viewLifecycleOwner, Observer {meal ->
            Glide.with(this).load(meal.strMealThumb).into(binding.imgBottomSheet)
            binding.tvMealArea.text = meal.strArea
            binding.tvMealCategory.text = meal.strCategory
            binding.tvMealName.text = meal.strMeal

            mealName = meal.strMeal
            mealThump = meal.strMealThumb

        })
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String) =
            MealBottomSheetFragment().apply {
                arguments = Bundle().apply {
                    putString(MEAL_ID, param1)
                }
            }

    }
}