package com.example.problem_catalog.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.problem_catalog.client.ApiService;
import com.example.problem_catalog.model.ProblemDao;
import com.example.problem_catalog.model.entities.Problem;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@HiltViewModel
public class AppViewModel extends ViewModel {
    private final MutableLiveData<List<Problem>> problemsLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private final ApiService apiService;
    private final ProblemDao problemDao;
    @Inject
    public AppViewModel(ApiService apiService, ProblemDao problemDao) {
        this.apiService = apiService;
        this.problemDao = problemDao;
    }

    public LiveData<List<Problem>> getProblemsLiveData(){
        return problemsLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public void updateData(){
        CompletableFuture.runAsync(() -> {
            List<Problem> problems = requestForUpdate().join();
            if(problems != null && !problems.isEmpty()) {
                updateDatabase(problems);
                problemsLiveData.postValue(problemDao.findAll());
            }
        });
    }

    public void reduceProblemsByRegex(String regex){
        if(regex == null || regex.isEmpty()) return;
        CompletableFuture.runAsync(() -> {
            List<Problem> originalProblems = getAllProblems();
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

    private CompletableFuture<List<Problem>> requestForUpdate(){
        CompletableFuture<List<Problem>> future = new CompletableFuture<>();
        Call<List<Problem>> call = apiService.getProblems();
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<Problem>> call, @NonNull Response<List<Problem>> response) {
                if (response.isSuccessful()) {
                    List<Problem> problems = response.body();
                    if (problems != null) future.complete(problems);
                    else {
                        future.completeExceptionally(new IllegalStateException("Response body is null"));
                        errorLiveData.setValue("Нет данных от сервера");
                    }
                } else {
                    future.completeExceptionally(new RuntimeException("Request failed with code: "
                            + response.code()));
                    errorLiveData.setValue("Не удалось подключиться к серверу");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Problem>> call, @NonNull Throwable t) {
                future.completeExceptionally(t);
                errorLiveData.setValue("Нет интернет-соединения");
            }
        });
        return future;
    }

    private void updateDatabase(List<Problem> problems){
        problemDao.updateProblems(problems);
    }

    private List<Problem> getAllProblems(){
        return problemDao.findAll();
    }
}
