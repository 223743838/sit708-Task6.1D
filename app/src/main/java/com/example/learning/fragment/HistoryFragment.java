package com.example.learning.fragment;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.learning.R;
import com.example.learning.adapter.HistoryAdapter;
import com.example.learning.model.HistoryItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class HistoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private HistoryAdapter adapter;
    private ArrayList<HistoryItem> historyList = new ArrayList<>();

    public HistoryFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new HistoryAdapter(historyList);
        recyclerView.setAdapter(adapter);

        // Fetch userId from shared prefs
        String userId = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                .getString("userId", null);

        if (userId != null) {
            fetchHistory(userId);
        }

        return view;
    }

    private void fetchHistory(String userId) {
        new Thread(() -> {
            try {
                URL url = new URL("http://10.0.2.2:5002/tasks/" + userId);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                int code = connection.getResponseCode();
                if (code == 200) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }

                    JSONArray jsonArray = new JSONArray(response.toString());
                    historyList.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        String title = obj.optString("title", "Untitled");
                        String date = obj.optString("date", "N/A");
                        int score = obj.optInt("score", 0);
                        historyList.add(new HistoryItem(title, date, score));
                    }

                    requireActivity().runOnUiThread(() -> adapter.notifyDataSetChanged());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
