package com.example.problem_catalog.client;

import com.example.problem_catalog.model.entities.Problem;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("precept-factor")
    Call<List<Problem>> getProblems();
}
