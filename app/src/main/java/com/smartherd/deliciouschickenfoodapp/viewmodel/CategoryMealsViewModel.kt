package com.smartherd.deliciouschickenfoodapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.smartherd.deliciouschickenfoodapp.pojos.MealsByCategory
import com.smartherd.deliciouschickenfoodapp.pojos.MealsByCategoryList
import com.smartherd.deliciouschickenfoodapp.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryMealsViewModel : ViewModel() {

    val mealsLiveData = MutableLiveData<List<MealsByCategory>>()

    fun getMealsByCategory(categoryName : String){
        RetrofitInstance.api.getMealsByCategory(categoryName).enqueue(object : Callback<MealsByCategoryList?> {
            override fun onResponse(
                call: Call<MealsByCategoryList?>,
                response: Response<MealsByCategoryList?>
            ) {
                response.body()?.let { category ->
                    mealsLiveData.postValue(category.meals)
                }
            }

            override fun onFailure(call: Call<MealsByCategoryList?>, t: Throwable) {
                Log.d("CategoryMealsViewModel", t.message.toString())
            }
        })
    }

    fun observeMealsByCategoryLiveData() : LiveData<List<MealsByCategory>>{
        return mealsLiveData
    }
}