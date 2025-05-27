package com.example.learning.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.example.learning.R;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ResultFragment extends Fragment {

    private TextView textViewResult;
    private Button buttonContinueResult;

    public ResultFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result, container, false);

        textViewResult = view.findViewById(R.id.textViewResult);
        buttonContinueResult = view.findViewById(R.id.buttonContinueResult);

        Bundle bundle = getArguments();
        if (bundle != null) {
            int correctAnswers = bundle.getInt("correctAnswers", 0);
            int totalQuestions = bundle.getInt("totalQuestions", 0);
            String quizTitle = bundle.getString("quizTitle", "Quick Quiz");
            textViewResult.setText("You answered " + correctAnswers + " out of " + totalQuestions + " correctly!");
        }
        if (bundle != null) {
            int correctAnswers = bundle.getInt("correctAnswers", 0);
            int totalQuestions = bundle.getInt("totalQuestions", 0);
            String quizTitle = bundle.getString("quizTitle", "Quick Quiz");

            textViewResult.setText("You answered " + correctAnswers + " out of " + totalQuestions + " correctly!");

            // Get userId from SharedPreferences
            String userId = requireActivity().getSharedPreferences("MyPrefs", getContext().MODE_PRIVATE)
                    .getString("userId", null);

            if (userId != null) {
                new Thread(() -> {
                    try {
                        URL url = new URL("http://10.0.2.2:5002/api/submit-task"); // Your backend route
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("POST");
                        connection.setRequestProperty("Content-Type", "application/json");
                        connection.setDoOutput(true);

                        JSONObject data = new JSONObject();
                        data.put("userId", userId);
                        data.put("title", quizTitle);
                        data.put("score", correctAnswers);
                        data.put("total", totalQuestions);
                        OutputStream os = connection.getOutputStream();
                        os.write(data.toString().getBytes());
                        os.flush();
                        os.close();

                        int responseCode = connection.getResponseCode();
                        if (responseCode == 200) {
                            // Success (optional: show a Toast)
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        }

        buttonContinueResult.setOnClickListener(v -> {
            // Navigate back to Dashboard
            NavOptions options = new NavOptions.Builder()
                    .setPopUpTo(R.id.resultFragment, true)
                    .setEnterAnim(R.anim.slide_in_rigth)
                    .setExitAnim(R.anim.slide_out_left)
                    .build();
            Navigation.findNavController(v).navigate(R.id.action_resultFragment_to_dashboardFragment, null, options);
        });

        return view;
    }
}
