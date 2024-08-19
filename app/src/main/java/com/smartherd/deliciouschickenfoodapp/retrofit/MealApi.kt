package com.smartherd.deliciouschickenfoodapp.retrofit


import com.smartherd.deliciouschickenfoodapp.pojos.CategoryList
import com.smartherd.deliciouschickenfoodapp.pojos.MealsByCategoryList
import com.smartherd.deliciouschickenfoodapp.pojos.MealList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApi {

    @GET("random.php")
    fun getRandomMeal() : Call<MealList>

    @GET("lookup.php?")
    fun getMealDetails(@Query("i") id : String) : Call<MealList>

    @GET("filter.php?")
    fun getPopularMeal(@Query("c") categoryName : String) : Call<MealsByCategoryList>

    @GET("categories.php")
    fun getCategory() : Call<CategoryList>

    @GET("filter.php")
    fun getMealsByCategory(@Query("c") categoryName : String) : Call<MealsByCategoryList>

    @GET("search.php")
    fun searchMeals(@Query("s") searchName : String) : Call<MealList>
}