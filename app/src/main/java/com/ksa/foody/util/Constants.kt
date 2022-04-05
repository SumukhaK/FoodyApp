package com.ksa.foody.util

class Constants {

    companion object{
        const val API_KEY = "e7b8eb3963724c238a5913f11899017b"
        const val BASE_URL = "https://api.spoonacular.com"
        const val BASE_IMAGE_URL = "https://spoonacular.com/cdn/ingredients_100x100/"
        const val RECIPE_BUNDLE = "recipeBundle"

        //API QUERY PARAMs
        const val QUERY_NUMBER = "number"
        const val QUERY_SEARCH = "query"
        const val QUERY_API_KEY = "apiKey"
        const val QUERY_TYPE = "type"
        const val QUERY_DIET = "diet"
        const val QUERY_ADD_RECIPE_INFO = "addRecipeInformation"
        const val QUERY_FILL_INGREDIENTS = "fillIngredients"

        //ROOM Db
        const val DB_NAME = "recipes_database"
        const val RECIPE_TABLE = "recipes"
        const val FAV_RECIPE_TABLE = "fav_recipes"
        const val FOOD_JOKE_TABLE = "food_joke"

        //Bottom sheet pref's
        const val PREF_NAME = "foody_pref"
        const val DEFAULT_MEAL_TYPE = "main course"
        const val DEFAULT_RECIPES_NUMBER = 50
        const val DEFAULT_DIET_TYPE = "gluten free"
        const val PREF_MEAL_TYPE = "mealType"
        const val PREF_MEAL_TYPE_ID = "mealTypeId"
        const val PREF_DIET_TYPE = "dietType"
        const val PREF_DIET_TYPE_ID = "dietTypeId"
        const val PREF_BACK_ONLINE = "backOnline"
    }
}