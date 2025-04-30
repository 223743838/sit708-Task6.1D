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
            textViewResult.setText("You answered " + correctAnswers + " out of " + totalQuestions + " correctly!");
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
