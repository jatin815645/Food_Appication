package com.smartherd.deliciouschickenfoodapp.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.room.util.ViewInfo
import com.bumptech.glide.Glide
import com.smartherd.deliciouschickenfoodapp.R
import com.smartherd.deliciouschickenfoodapp.databinding.ActivityMealBinding
import com.smartherd.deliciouschickenfoodapp.db.MealDatabase
import com.smartherd.deliciouschickenfoodapp.fargments.HomeFragment
import com.smartherd.deliciouschickenfoodapp.pojos.Meal
import com.smartherd.deliciouschickenfoodapp.viewmodel.MealViewModel
import com.smartherd.deliciouschickenfoodapp.viewmodel.MealViewModelFactory

class MealActivity : AppCompatActivity() {

    private lateinit var mealId : String
    private lateinit var mealName : String
    private lateinit var mealThumb : String
    private lateinit var binding: ActivityMealBinding
    private lateinit var mealMvvm : MealViewModel
    private lateinit var youtubeLink : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMealBinding.inflate(layoutInflater)

        val mealDatabase = MealDatabase.getInstance(this)
        val viewModelFactory = MealViewModelFactory(mealDatabase)

        mealMvvm = ViewModelProviders.of(this,viewModelFactory)[MealViewModel::class.java]

        getMealInformationFromIntent()
        setInformationInView()

        onLoadingCase()
        mealMvvm.getMealDetails(mealId)
        observeMealDetailsLiveData()

        onYoutubeImageClick()
        onFavItemClicked()

        setContentView(binding.root)
    }

    private fun onFavItemClicked() {
        binding.btnAddToFav.setOnClickListener {
            mealToSave?.let {
                mealMvvm.insertMeal(it)
                Toast.makeText(this,"Meal Saved", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onYoutubeImageClick() {
        binding.imgYouTube.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink))
            startActivity(intent)
        }
    }

    private var mealToSave : Meal? = null
    private fun observeMealDetailsLiveData() {
        mealMvvm.observeMealDetailsLiveData().observe(this
        ) { meal ->
            onResponseCase()
            binding.tvCategory.text = "Catagory : ${meal.strCategory}"
            binding.tvArea.text = "Area : ${meal.strArea}"
            binding.tvInstructionsContent.text = meal.strInstructions
            mealToSave = meal

            youtubeLink = meal.strYoutube.toString()
        }
    }

    private fun setInformationInView() {
        Glide.with(applicationContext)
            .load(mealThumb)
            .into(binding.imgMealDetail)

        binding.collapsingToolbar.title = mealName
        binding.collapsingToolbar.setCollapsedTitleTextColor(resources.getColor(R.color.white))
        binding.collapsingToolbar.setExpandedTitleColor(resources.getColor(R.color.white))
    }

    private fun getMealInformationFromIntent() {
        val intent = intent
        mealId = intent.getStringExtra(HomeFragment.MEAL_ID)!!
        mealName = intent.getStringExtra(HomeFragment.MEAL_NAME)!!
        mealThumb = intent.getStringExtra(HomeFragment.MEAL_THUMB)!!
    }

    private fun onLoadingCase(){
            binding.progressBar.visibility = View.VISIBLE
            binding.btnAddToFav.visibility = View.INVISIBLE
            binding.tvCategory.visibility = View.INVISIBLE
            binding.tvArea.visibility = View.INVISIBLE
            binding.tvInstructions.visibility = View.INVISIBLE
            binding.tvInstructionsContent.visibility = View.INVISIBLE
            binding.imgYouTube.visibility = View.INVISIBLE
    }

    private fun onResponseCase(){
        binding.progressBar.visibility = View.INVISIBLE
        binding.btnAddToFav.visibility = View.VISIBLE
        binding.tvCategory.visibility = View.VISIBLE
        binding.tvArea.visibility = View.VISIBLE
        binding.tvInstructions.visibility = View.VISIBLE
        binding.tvInstructionsContent.visibility = View.VISIBLE
        binding.imgYouTube.visibility = View.VISIBLE
    }
}