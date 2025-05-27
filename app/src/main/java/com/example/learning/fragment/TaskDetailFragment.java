package com.example.learning.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.example.learning.R;
import com.example.learning.adapters.QuestionAdapter;
import com.example.learning.model.Question;
import com.example.learning.api.ApiClient;
import com.example.learning.api.QuestionApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.ArrayList;
import java.util.List;

public class TaskDetailFragment extends Fragment {

    private RecyclerView recyclerViewQuestions;
    private Button buttonSubmitAnswers;
    private QuestionAdapter questionAdapter;
    private List<Question> questionList = new ArrayList<>();
    private String taskTitle = "General Quiz";

    public TaskDetailFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_detail, container, false);

        recyclerViewQuestions = view.findViewById(R.id.recyclerViewQuestions);
        buttonSubmitAnswers = view.findViewById(R.id.buttonSubmitAnswers);


        Bundle bundle = getArguments();
        if (bundle != null) {
            taskTitle = bundle.getString("taskTitle", "General Quiz");
        }

        questionAdapter = new QuestionAdapter(questionList);
        recyclerViewQuestions.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewQuestions.setAdapter(questionAdapter);

        fetchQuestions(taskTitle);

        buttonSubmitAnswers.setOnClickListener(v -> {
            int correctCount = 0;
            int totalQuestions = questionList.size();

            for (int i = 0; i < totalQuestions; i++) {
                int selectedAnswer = questionAdapter.getSelectedAnswer(i);
                if (selectedAnswer == questionList.get(i).getCorrectAnswerIndex()) {
                    correctCount++;
                }
            }

            Bundle resultBundle = new Bundle();
            resultBundle.putInt("correctAnswers", correctCount);
            resultBundle.putInt("totalQuestions", totalQuestions);
            resultBundle.putString("quizTitle", taskTitle);
            NavOptions options = new NavOptions.Builder()
                    .setEnterAnim(R.anim.slide_in_rigth)
                    .setExitAnim(R.anim.slide_out_left)
                    .build();
            Navigation.findNavController(requireView()).navigate(R.id.action_taskDetailFragment_to_resultFragment, resultBundle, options);
        });

        return view;
    }

    private void fetchQuestions(String topic) {
        Log.d("TaskDetailFragment", "Requested topic: " + topic);  // ✅ Log before sending request

        QuestionApiService apiService = ApiClient.getClient().create(QuestionApiService.class);
        Call<List<Question>> call = apiService.getQuestions(topic);

        call.enqueue(new Callback<List<Question>>() {
            @Override
            public void onResponse(Call<List<Question>> call, Response<List<Question>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    questionList.clear();
                    questionList.addAll(response.body());
                    questionAdapter.notifyDataSetChanged();
                    Log.d("API_DEBUG", "First Question: " + response.body().get(0).getQuestionText());
                    // ✅ Add this
                } else {
                    Log.e("TaskDetailFragment", "API call failed: " + response.code());  // ✅ Add this
                }
            }

            @Override
            public void onFailure(Call<List<Question>> call, Throwable t) {
                Log.e("TaskDetailFragment", "API call error", t);  // ✅ Proper error log
            }
        });
    }

}
