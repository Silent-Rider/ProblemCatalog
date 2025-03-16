package com.example.problem_catalog.config;

import android.content.Context;

import com.example.problem_catalog.BuildConfig;
import com.example.problem_catalog.client.ApiService;
import com.example.problem_catalog.model.AppDatabase;
import com.example.problem_catalog.model.ProblemDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {

    @Singleton
    @Provides
    public ApiService provideApiService(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.SERVER_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();

        return retrofit.create(ApiService.class);
    }

    @Singleton
    @Provides
    public ProblemDao provideProblemDao(@ApplicationContext Context context){
        AppDatabase appDatabase = AppDatabase.getInstance(context);
        return appDatabase.problemDao();
    }
}
