package com.ajdi.yassin.bakingapp.data;

import com.ajdi.yassin.bakingapp.data.remote.RecipeService;
import com.ajdi.yassin.bakingapp.data.remote.model.Recipe;
import com.ajdi.yassin.bakingapp.utils.AppExecutors;

import java.util.Collections;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Yassin Ajdi
 * @since 12/11/2018.
 */
public class RecipeRepository {

    private static volatile RecipeRepository sInstance;

    private final RecipeService mRecipeService;

    private final AppExecutors mExecutors;

    private RecipeRepository(AppExecutors executors,
                             RecipeService recipeService) {
        mExecutors = executors;
        mRecipeService = recipeService;
    }

    public static RecipeRepository getInstance(AppExecutors executors,
                                               RecipeService recipeService) {
        if (sInstance == null) {
            synchronized (RecipeRepository.class) {
                if (sInstance == null) {
                    sInstance = new RecipeRepository(executors, recipeService);
                }
            }
        }
        return sInstance;
    }

    public LiveData<List<Recipe>> loadAllRecipes() {
        final MutableLiveData<List<Recipe>> recipeListLiveData = new MutableLiveData<>();
        mRecipeService.getAllRecipes().enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                if (response.isSuccessful()) {
                    List<Recipe> data = response.body();
                    List<Recipe> recipes = data != null ? data : Collections.<Recipe>emptyList();
                    recipeListLiveData.postValue(recipes);
                } else {
                    // TODO: 12/11/2018 handle errors
                }
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                // TODO: 12/11/2018 handle errors
            }
        });
        return recipeListLiveData;
    }
}
