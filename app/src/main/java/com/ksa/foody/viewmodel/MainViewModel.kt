package com.ksa.foody.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.ksa.foody.data.Repository
import com.ksa.foody.data.db.entities.FavoritesEntity
import com.ksa.foody.data.db.entities.FoodJokeEntity
import com.ksa.foody.data.db.entities.RecipesEntity
import com.ksa.foody.models.FoodJoke
import com.ksa.foody.models.FoodRecipe
import com.ksa.foody.util.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel @ViewModelInject constructor(
        private val repository: Repository, application: Application):AndroidViewModel(application) {
    /*
     *
     ROOM DB
     *
     */
    val readRecipes : LiveData<List<RecipesEntity>> = repository.localDS.readRecipesDatabase().asLiveData()
    val readFavRecipes : LiveData<List<FavoritesEntity>> = repository.localDS.readFavouriteRecipes().asLiveData()
    val readFoodJoke : LiveData<List<FoodJokeEntity>> = repository.localDS.readFoodJokes().asLiveData()

    private fun insertRecipes(recipesEntity: RecipesEntity) =
            viewModelScope.launch(Dispatchers.IO) {
                repository.localDS.insertRecipe(recipesEntity)
            }

    fun insertFavouriteRecipe(favoritesEntity: FavoritesEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.localDS.insertFavouriteRecipes(favoritesEntity)
    }

    fun insertFoodJoke(foodJokeEntity: FoodJokeEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.localDS.insertFoodJoke(foodJokeEntity)
    }

    fun deleteFavouriteRecipe(favoritesEntity: FavoritesEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.localDS.deleteFavRecipe(favoritesEntity)
    }

    fun deleteAllFavouriteRecipes() = viewModelScope.launch(Dispatchers.IO) {
        repository.localDS.deleteAllFavRecipes()
    }

    /*
     *
       RETROFIT
    *
    * */
    var recipesResponse : MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()
    var searchedRecipeResponse: MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()
    var foodJokeResponse : MutableLiveData<NetworkResult<FoodJoke>> = MutableLiveData()

    fun getRecipes(queries:Map<String,String>) = viewModelScope.launch {
        getRecipesSafeCall(queries)
    }

    fun searchRecipes(searchQuery: Map<String,String>) = viewModelScope.launch {
        searchRecipeSafeCall(searchQuery)
    }

    fun getFoodJokes(apiKey:String) = viewModelScope.launch {
        getFoodJokesSafeCall(apiKey)
    }

    private suspend fun getFoodJokesSafeCall(apiKey: String){
        foodJokeResponse.value = NetworkResult.Loading()
        if(hasInternetConnection()){
            try{
                val response = repository.remoteDS.getFoodJoke(apiKey)
                foodJokeResponse.value = handleFoodJokeResponse(response)
                val foodJoke = foodJokeResponse.value!!.data
                if(foodJoke != null){
                    offlineCacheFoodJoke(foodJoke)
                }
            }catch (e:Exception){
                foodJokeResponse.value = NetworkResult.Error("Jokes Not Found !!..")
            }
        }else{
            foodJokeResponse.value = NetworkResult.Error("No Internet Connection.")
        }

    }

    private suspend fun searchRecipeSafeCall(searchQuery: Map<String, String>) {

        searchedRecipeResponse.value = NetworkResult.Loading()
        if(hasInternetConnection()){
            try{
                val response = repository.remoteDS.searchRecipes(searchQuery)
                searchedRecipeResponse.value = handleFoodRecipeResponse(response)
            }catch (e:Exception){
                searchedRecipeResponse.value = NetworkResult.Error("Recipes Not found !..")
            }
        }else{
            searchedRecipeResponse.value = NetworkResult.Error("No Internet Connection.")
        }
    }

    private suspend fun getRecipesSafeCall(queries: Map<String, String>) {

        recipesResponse.value = NetworkResult.Loading()
        if(hasInternetConnection()){

            try{
                val response = repository.remoteDS.getRecipes(queries)
                recipesResponse.value = handleFoodRecipeResponse(response)
                val foodRecipe = recipesResponse.value!!.data
                if(foodRecipe != null){
                    offlineCacheRecipes(foodRecipe)
                }
            }catch (e:Exception){
                recipesResponse.value = NetworkResult.Error("Recipes Not found !..")
            }
        }else{
            recipesResponse.value = NetworkResult.Error("No Internet Connection.")
        }

    }


    private fun offlineCacheRecipes(foodRecipe: FoodRecipe) {
        val recipeEntity = RecipesEntity(foodRecipe)
        insertRecipes(recipeEntity)
    }

    private fun offlineCacheFoodJoke(foodJoke: FoodJoke) {
        val foodJokeEntity = FoodJokeEntity(foodJoke)
        insertFoodJoke(foodJokeEntity)
    }

    private fun handleFoodRecipeResponse(response: Response<FoodRecipe>): NetworkResult<FoodRecipe>? {

        when{
            response.message().toString().contains("timeout") ->{
                return NetworkResult.Error("Timeout")
            }
            response.code() == 402 -> {
                return NetworkResult.Error("API key Limited..")
            }
            response.body()!!.results.isNullOrEmpty() -> {
                return NetworkResult.Error("Recipe Not Found..")
            }

            response.isSuccessful ->{

                val foodRecipe = response.body()
                return NetworkResult.Success(foodRecipe!!)

            }else ->{
            return NetworkResult.Error(response.message())
        }
        }

    }

    private fun handleFoodJokeResponse(response: Response<FoodJoke>): NetworkResult<FoodJoke>? {
        when{
            response.message().toString().contains("timeout") ->{
                return NetworkResult.Error("Timeout")
            }
            response.code() == 402 -> {
                return NetworkResult.Error("API key Limited..")
            }

//            response.body()!!.results.isNullOrEmpty() -> {
//                return NetworkResult.Error("Recipe Not Found..")
//            }

            response.isSuccessful ->{

                val foodJoke = response.body()
                return NetworkResult.Success(foodJoke!!)

            }else ->{
            return NetworkResult.Error(response.message())
        }
        }
    }

    private fun hasInternetConnection():Boolean{

        val connectivityManager = getApplication<Application>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false

        return when{
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }


}