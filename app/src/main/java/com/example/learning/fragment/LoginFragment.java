package com.example.learning.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learning.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginFragment extends Fragment {

    private EditText editTextUsername, editTextPassword;
    private Button buttonLogin;
    private TextView textViewNeedAccount;

    public LoginFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        editTextUsername = view.findViewById(R.id.editTextUsername);
        editTextPassword = view.findViewById(R.id.editTextPassword);
        buttonLogin = view.findViewById(R.id.buttonLogin);
        textViewNeedAccount = view.findViewById(R.id.textViewNeedAccount);

        buttonLogin.setOnClickListener(v -> {
            String username = editTextUsername.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), "Please enter username and password", Toast.LENGTH_SHORT).show();
                return;
            }


            String apiUrl = "http://10.0.2.2:5002/api/login"; // For Android emulator

            new Thread(() -> {
                try {
                    URL url = new URL(apiUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);
                    connection.setRequestProperty("Content-Type", "application/json");

                    JSONObject loginData = new JSONObject();
                    loginData.put("username", username);
                    loginData.put("password", password);

                    OutputStream os = connection.getOutputStream();
                    os.write(loginData.toString().getBytes());
                    os.flush();
                    os.close();

                    int responseCode = connection.getResponseCode();

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        String inputLine;
                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();

                        JSONObject jsonResponse = new JSONObject(response.toString());
                        String userId = jsonResponse.getString("_id");

                        // ✅ Save userId in SharedPreferences
                        SharedPreferences prefs = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
                        prefs.edit().putString("userId", userId).apply();

                        Log.d("LoginFragment", "Saved userId: " + userId);

                        requireActivity().runOnUiThread(() -> {
                            Toast.makeText(getContext(), "Login successful", Toast.LENGTH_SHORT).show();

                            NavOptions options = new NavOptions.Builder()
                                    .setEnterAnim(R.anim.slide_in_rigth)
                                    .setExitAnim(R.anim.slide_out_left)
                                    .build();
                            Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_dashboardFragment, null, options);
                        });

                    } else {
                        requireActivity().runOnUiThread(() ->
                                Toast.makeText(getContext(), "Login failed", Toast.LENGTH_SHORT).show()
                        );
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show()
                    );
                }
            }).start();
        });

        textViewNeedAccount.setOnClickListener(v -> {
            NavOptions options = new NavOptions.Builder()
                    .setEnterAnim(R.anim.slide_in_rigth)
                    .setExitAnim(R.anim.slide_out_left)
                    .build();
            Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_signupFragment, null, options);
        });

        return view;
    }
}
