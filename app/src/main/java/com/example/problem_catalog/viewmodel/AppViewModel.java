package com.example.problem_catalog.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.problem_catalog.client.ApiService;
import com.example.problem_catalog.model.AppDatabase;
import com.example.problem_catalog.model.ProblemDao;
import com.example.problem_catalog.model.entities.Problem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@HiltViewModel
public class AppViewModel extends AndroidViewModel {

    private static final Logger logger = LoggerFactory.getLogger(AppViewModel.class);
    private final MutableLiveData<List<Problem>> problemsLiveData = new MutableLiveData<>();

    private final ApiService apiService;
    private final ProblemDao problemDao;
    @Inject
    public AppViewModel(@NonNull Application application, ApiService apiService) {
        super(application);
        this.apiService = apiService;
        AppDatabase database = AppDatabase.getInstance(application);
        problemDao = database.problemDao();
    }

    public LiveData<List<Problem>> getProblemsLiveData(){
        return problemsLiveData;
    }

    public void reduceProblemsByRegex(String regex){
        if(regex == null || regex.isEmpty()) return;
        CompletableFuture.runAsync(() -> {
            List<Problem> originalProblems = problemsLiveData.getValue();
            if (originalProblems != null && !originalProblems.isEmpty()) {
                List<Problem> filteredProblems = originalProblems.stream()
                        .filter(x -> x.getName().toLowerCase().contains(regex.toLowerCase()))
                        .collect(Collectors.toList());
                problemsLiveData.postValue(filteredProblems);
            }
        });
    }

    public void resetProblems(){
       CompletableFuture.runAsync(() -> {
           List<Problem> allProblems = problemDao.findAll();
           problemsLiveData.postValue(allProblems);
       });
    }

    public boolean updateData(){
        MutableLiveData<List<Problem>> problems = requestForUpdate();
        if(problems.getValue() != null) {
            CompletableFuture.runAsync(() -> {
                updateDatabase(problems.getValue());
            }).thenRun(() -> {
                problemsLiveData.postValue(problemDao.findAll());
            });
            return true;
        }
        else return false;
    }

    private MutableLiveData<List<Problem>> requestForUpdate(){
        MutableLiveData<List<Problem>> problemsLiveData = new MutableLiveData<>();
        Call<List<Problem>> call = apiService.getProblems();
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<Problem>> call, @NonNull Response<List<Problem>> response) {
                if(response.isSuccessful()){
                   List<Problem> problems = response.body();
                   problemsLiveData.setValue(problems);
                } else{
                    logger.error("An error occurred during HTTP request");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Problem>> call, Throwable t) {
                logger.error("An error occurred during HTTP request. Error message: {}", t.getMessage());
            }
        });
        return problemsLiveData;
    }

    private void updateDatabase(List<Problem> problems){
        problemDao.updateProblems(problems);
    }
}
