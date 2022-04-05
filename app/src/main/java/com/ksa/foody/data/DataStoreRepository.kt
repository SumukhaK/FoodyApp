package com.ksa.foody.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.*
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.preferencesKey
import com.ksa.foody.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.ksa.foody.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.ksa.foody.util.Constants.Companion.PREF_BACK_ONLINE
import com.ksa.foody.util.Constants.Companion.PREF_DIET_TYPE
import com.ksa.foody.util.Constants.Companion.PREF_DIET_TYPE_ID
import com.ksa.foody.util.Constants.Companion.PREF_MEAL_TYPE
import com.ksa.foody.util.Constants.Companion.PREF_MEAL_TYPE_ID
import com.ksa.foody.util.Constants.Companion.PREF_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

@ActivityRetainedScoped
class DataStoreRepository @Inject constructor(@ApplicationContext private val context: Context){

    private object PrefKeys{
        val selectedMealType = preferencesKey<String>(PREF_MEAL_TYPE)
        val selectedMealTypeId = preferencesKey<Int>(PREF_MEAL_TYPE_ID)
        val selectedDietType = preferencesKey<String>(PREF_DIET_TYPE)
        val selectedDietTypeId = preferencesKey<Int>(PREF_DIET_TYPE_ID)
        val backOnline = preferencesKey<Boolean>(PREF_BACK_ONLINE)
    }


    private val dataStore: DataStore<Preferences> = context.createDataStore(
        PREF_NAME,
    )


    suspend fun saveOnline(backOnline:Boolean){
        dataStore.edit { pref ->

            pref[PrefKeys.backOnline] = backOnline
        }
    }

    val readBackOnline : Flow<Boolean> = dataStore.data.catch { exception ->
        if(exception is IOException){
            emit(emptyPreferences())
        }else{
            throw exception
        }
    }.map { preferences ->
        val backOnline = preferences[PrefKeys.backOnline] ?:false
        backOnline
    }


    suspend fun saveMealAndDietType(selectedMealType:String,
                                    selectedMealTypeId:Int,
                                    selectedDietType:String,
                                    selectedDietTypeId:Int){

        dataStore.edit { preferences ->
            preferences[PrefKeys.selectedMealType] = selectedMealType
            preferences[PrefKeys.selectedMealTypeId] = selectedMealTypeId
            preferences[PrefKeys.selectedDietType] = selectedDietType
            preferences[PrefKeys.selectedDietTypeId] = selectedDietTypeId

        }


    }


    val readMealAndDietTypes : Flow<MealAndDietTypes> = dataStore.data
        .catch { exception ->
            if(exception is IOException){
                emit(emptyPreferences())
            }else{
                throw exception
            }
        }.map {preferences ->
            val selectedMealType = preferences[PrefKeys.selectedMealType] ?: DEFAULT_MEAL_TYPE
            val selectedMealTypeId = preferences[PrefKeys.selectedMealTypeId] ?: 0
            val selectedDietType = preferences[PrefKeys.selectedDietType] ?: DEFAULT_DIET_TYPE
            val selectedDietTypeId = preferences[PrefKeys.selectedDietTypeId] ?: 0
            MealAndDietTypes(selectedMealType,selectedMealTypeId,selectedDietType,selectedDietTypeId)
        }
}

data class MealAndDietTypes(val selectedMealType:String,
                            val selectedMealTypeId:Int,
                            val selectedDietType:String,
                            val selectedDietTypeId:Int)