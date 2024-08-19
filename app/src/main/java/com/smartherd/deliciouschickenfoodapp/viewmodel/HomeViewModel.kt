package com.smartherd.deliciouschickenfoodapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smartherd.deliciouschickenfoodapp.db.MealDatabase
import com.smartherd.deliciouschickenfoodapp.pojos.Category
import com.smartherd.deliciouschickenfoodapp.pojos.CategoryList
import com.smartherd.deliciouschickenfoodapp.pojos.MealsByCategoryList
import com.smartherd.deliciouschickenfoodapp.pojos.MealsByCategory
import com.smartherd.deliciouschickenfoodapp.pojos.Meal
import com.smartherd.deliciouschickenfoodapp.pojos.MealList
import com.smartherd.deliciouschickenfoodapp.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(
    private val mealDatabase: MealDatabase
) : ViewModel() {
    private var randomMealLiveData = MutableLiveData<Meal>()
    private var popularMealLiveData = MutableLiveData<List<MealsByCategory>>()
    private var categoryLiveData = MutableLiveData<List<Category>>()
    private var favouritesMealsLiveData = mealDatabase.mealDao().getAllMeal()
    private var bottomSheetMealLivedata = MutableLiveData<Meal>()
    private var searchMealLiveData = MutableLiveData<List<Meal>>()

   fun getRandomMeal(){
       RetrofitInstance.api.getRandomMeal().enqueue(object : Callback<MealList?> {
           override fun onResponse(call: Call<MealList?>, response: Response<MealList?>) {
               if (response.body() != null){
                   val randomMeal : Meal = response.body()!!.meals[0]
                    randomMealLiveData.value = randomMeal
               }
               else{
                   return
               }
           }

           override fun onFailure(call: Call<MealList?>, t: Throwable) {
               Log.d("HomeFragment", t.message.toString())
           }
       })
   }

    fun getPopularMeals(){
        RetrofitInstance.api.getPopularMeal("Seafood").enqueue(object : Callback<MealsByCategoryList?> {
            override fun onResponse(call: Call<MealsByCategoryList?>, response: Response<MealsByCategoryList?>) {
                if (response.body() != null){
                    popularMealLiveData.value = response.body()!!.meals
                    Log.d("HomeFragment", "${response.body()!!.meals}")
                }
                else{
                    return
                }
            }

            override fun onFailure(call: Call<MealsByCategoryList?>, t: Throwable) {
                Log.d("Error", t.message.toString())
            }
        })
    }

    fun getCategory(){
        RetrofitInstance.api.getCategory().enqueue(object : Callback<CategoryList?> {
            override fun onResponse(call: Call<CategoryList?>, response: Response<CategoryList?>) {
                response.body()?.let {categoryList ->
                    categoryLiveData.postValue(categoryList.categories)
                }
            }

            override fun onFailure(call: Call<CategoryList?>, t: Throwable) {
                Log.d("HomeActivity", t.message.toString())
            }
        })
    }

    fun getMealById(id : String){
        RetrofitInstance.api.getMealDetails(id).enqueue(object : Callback<MealList?> {
            override fun onResponse(call: Call<MealList?>, response: Response<MealList?>) {
                val meal = response.body()?.meals?.first()
                meal?.let {meal ->
                        bottomSheetMealLivedata.postValue(meal)
                }
            }

            override fun onFailure(call: Call<MealList?>, t: Throwable) {
                Log.d("HomeViewModel", t.message.toString())
            }
        })
    }

    fun getSearchMeal(searchQuery : String) = RetrofitInstance.api.searchMeals(searchQuery).enqueue(object : Callback<MealList?> {
        override fun onResponse(call: Call<MealList?>, response: Response<MealList?>) {
            var  mealList = response.body()?.meals
            mealList?.let {
                searchMealLiveData.postValue(it)
            }
        }

        override fun onFailure(call: Call<MealList?>, t: Throwable) {
            Log.d("HomeViewModel", t.message.toString())
        }
    })

    fun observeSearchMealLiveData() : LiveData<List<Meal>> = searchMealLiveData

    fun observeBottomSheetLivedata() : LiveData<Meal> = bottomSheetMealLivedata

    fun delete(meal:Meal){
        viewModelScope.launch {
            mealDatabase.mealDao().delete(meal)
        }
    }

    fun insertMeal(meal:Meal){
        viewModelScope.launch {
            mealDatabase.mealDao().upsert(meal)
        }
    }

    fun observeRandomMealLiveData() : LiveData<Meal>{
        return randomMealLiveData
    }

    fun observePopularMealLiveData() : LiveData<List<MealsByCategory>>{
        return popularMealLiveData
    }

    fun observeCategoryLiveData() : LiveData<List<Category>>{
        return categoryLiveData
    }

    fun observeFavouriteMealLiveData() : LiveData<List<Meal>>{
        return favouritesMealsLiveData
    }

}