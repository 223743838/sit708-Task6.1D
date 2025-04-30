package com.example.learning.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ToggleButton;
import com.example.learning.R;
import java.util.ArrayList;
import java.util.List;

public class InterestsFragment extends Fragment {

    private ToggleButton buttonAI, buttonDataStructures, buttonWebDev, buttonTesting,
            buttonAlgorithms, buttonCloudComputing, buttonMobileApps, buttonMachineLearning;
    private Button buttonContinue;

    public InterestsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_interests, container, false);

        buttonAI = view.findViewById(R.id.buttonAI);
        buttonDataStructures = view.findViewById(R.id.buttonDataStructures);
        buttonWebDev = view.findViewById(R.id.buttonWebDev);
        buttonTesting = view.findViewById(R.id.buttonTesting);
        buttonAlgorithms = view.findViewById(R.id.buttonAlgorithms);
        buttonCloudComputing = view.findViewById(R.id.buttonCloudComputing);
        buttonMobileApps = view.findViewById(R.id.buttonMobileApps);
        buttonMachineLearning = view.findViewById(R.id.buttonMachineLearning);

        buttonContinue = view.findViewById(R.id.buttonContinue);

        buttonContinue.setOnClickListener(v -> {
            List<String> selectedInterests = new ArrayList<>();

            if (buttonAI.isChecked()) selectedInterests.add("Artificial Intelligence");
            if (buttonDataStructures.isChecked()) selectedInterests.add("Data Structures");
            if (buttonWebDev.isChecked()) selectedInterests.add("Web Development");
            if (buttonTesting.isChecked()) selectedInterests.add("Testing");
            if (buttonAlgorithms.isChecked()) selectedInterests.add("Algorithms");
            if (buttonCloudComputing.isChecked()) selectedInterests.add("Cloud Computing");
            if (buttonMobileApps.isChecked()) selectedInterests.add("Mobile Apps");
            if (buttonMachineLearning.isChecked()) selectedInterests.add("Machine Learning");

            if (!selectedInterests.isEmpty()) {
                // Save selected interests
                SharedPreferences preferences = requireActivity().getSharedPreferences("LearningAppPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("user_interest", selectedInterests.toString());
                editor.apply();

                // Navigate to Dashboard
                NavOptions options = new NavOptions.Builder()
                        .setPopUpTo(R.id.interestsFragment, true)
                        .setEnterAnim(R.anim.slide_in_rigth)
                        .setExitAnim(R.anim.slide_out_left)
                        .build();
                Navigation.findNavController(v).navigate(R.id.action_interestsFragment_to_dashboardFragment, null, options);
            } else {
                // No interest selected, show a Toast (optional)
                android.widget.Toast.makeText(requireContext(), "Please select at least one interest", android.widget.Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
