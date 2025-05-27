package com.example.learning.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learning.R;
import com.example.learning.adapters.TaskAdapter;
import com.example.learning.model.Task;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment implements TaskAdapter.OnTaskClickListener {

    private RecyclerView recyclerViewTasks;
    private TextView textViewWelcome;
    private ImageView imageViewProfile;
    private List<Task> taskList = new ArrayList<>();

    private Button buttonShareProfile;
    public DashboardFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        buttonShareProfile = view.findViewById(R.id.buttonShareProfile);

        recyclerViewTasks = view.findViewById(R.id.recyclerViewTasks);
        textViewWelcome = view.findViewById(R.id.textViewWelcomeDashboard);
        imageViewProfile = view.findViewById(R.id.imageViewProfileDashboard);

        textViewWelcome.setText("Hello, Student!");

        imageViewProfile.setOnClickListener(v -> {
            // Later navigate to Profile
        });
        Button buttonShareProfile = view.findViewById(R.id.buttonShareProfile);
        buttonShareProfile.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_dashboardFragment_to_shareProfileFragment);
        });
        Button buttonUpgrade = view.findViewById(R.id.buttonUpgrade);
        buttonUpgrade.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.upgradeFragment);
        });

        taskList = generatePersonalizedTasks();
        TaskAdapter taskAdapter = new TaskAdapter(taskList, this);
        recyclerViewTasks.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewTasks.setAdapter(taskAdapter);
        Button buttonHistory = view.findViewById(R.id.buttonHistory);
        buttonHistory.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_dashboardFragment_to_historyFragment);
        });

        return view;
    }
    private void generateAndShareQRCode(String content) {
        try {
            // Generate QR code
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, 400, 400);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(bitMatrix);

            // Save QR code to cache
            File cachePath = new File(requireContext().getCacheDir(), "images");
            cachePath.mkdirs();
            File file = new File(cachePath, "qr.png");
            FileOutputStream stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();

            // Share QR code image
            Uri uri = FileProvider.getUriForFile(requireContext(), requireContext().getPackageName() + ".provider", file);
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/png");
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(shareIntent, "Share QR code using"));

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "QR code generation failed", Toast.LENGTH_SHORT).show();
        }
    }
    private List<Task> generatePersonalizedTasks() {
        List<Task> list = new ArrayList<>();

        SharedPreferences preferences = requireActivity().getSharedPreferences("LearningAppPrefs", Context.MODE_PRIVATE);
        String interestsString = preferences.getString("user_interest", "[]");

        // Cleaning up the interests
        interestsString = interestsString.replace("[", "").replace("]", "");
        String[] selectedInterests = interestsString.split(",");

        for (String interestRaw : selectedInterests) {
            String interest = interestRaw.trim();

            if (interest.equalsIgnoreCase("Artificial Intelligence")) {
                list.add(new Task("AI Basics Quiz", "5 Questions on AI Basics"));
                list.add(new Task("Machine Learning Quiz", "5 Questions on ML Basics"));
            }
            else if (interest.equalsIgnoreCase("Web Development")) {
                list.add(new Task("HTML Quiz", "5 Questions on HTML"));
                list.add(new Task("CSS Styling Quiz", "5 Questions on CSS"));
                list.add(new Task("JavaScript Quiz", "5 Questions on JS Basics"));
            }
            else if (interest.equalsIgnoreCase("Cloud Computing")) {
                list.add(new Task("Cloud Basics Quiz", "5 Questions on Cloud Models"));
                list.add(new Task("AWS Quiz", "5 Questions on AWS Services"));
            }
            else if (interest.equalsIgnoreCase("Data Structures")) {
                list.add(new Task("Data Structures Basics", "5 Questions on Lists and Trees"));
            }
            else if (interest.equalsIgnoreCase("Mobile Apps")) {
                list.add(new Task("Android Development Basics", "5 Questions on Activities and Intents"));
            }
            else if (interest.equalsIgnoreCase("Algorithms")) {
                list.add(new Task("Sorting Algorithms Quiz", "5 Questions on Sorting techniques"));
            }
            else if (interest.equalsIgnoreCase("Testing")) {
                list.add(new Task("Software Testing Quiz", "5 Questions on Testing Concepts"));
            }
            else if (interest.equalsIgnoreCase("Machine Learning")) {
                list.add(new Task("Supervised Learning Quiz", "5 Questions on ML concepts"));
            }
        }

        // Fallback
        if (list.isEmpty()) {
            list.add(new Task("General Knowledge Quiz", "5 Questions on random topics"));
        }

        return list;
    }


    @Override
    public void onTaskClick(Task task) {
        Bundle bundle = new Bundle();

        // Map the user-friendly quiz title to API topic
        String apiTopic = mapTaskTitleToApiTopic(task.getTitle());

        bundle.putString("taskTitle", apiTopic);  // ✅ Send correct topic now

        NavOptions options = new NavOptions.Builder()
                .setEnterAnim(R.anim.slide_in_rigth)
                .setExitAnim(R.anim.slide_out_left)
                .build();
        Navigation.findNavController(requireView()).navigate(R.id.action_dashboardFragment_to_taskDetailFragment, bundle, options);
    }
    private String mapTaskTitleToApiTopic(String title) {
        title = title.toLowerCase();

        if (title.contains("ai")) return "AI";
        if (title.contains("machine learning")) return "Machine Learning";
        if (title.contains("html") || title.contains("css") || title.contains("javascript") || title.contains("web")) return "Web Development";
        if (title.contains("cloud") || title.contains("aws")) return "Cloud Computing";
        if (title.contains("data structures")) return "Data Structures";
        if (title.contains("mobile") || title.contains("android")) return "Mobile Apps";
        if (title.contains("algorithms") || title.contains("sorting")) return "Algorithms";
        if (title.contains("testing")) return "Testing";

        // fallback
        return "General";
    }


}
