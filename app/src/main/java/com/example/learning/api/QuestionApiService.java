package com.example.learning.api;

import com.example.learning.model.Question;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface QuestionApiService {
    @GET("getQuestions")
    Call<List<Question>> getQuestions(@Query("topic") String topic);
}
