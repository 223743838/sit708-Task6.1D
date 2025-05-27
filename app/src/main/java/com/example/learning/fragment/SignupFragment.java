package com.example.learning.fragment;

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
import android.widget.Toast;

import com.example.learning.R;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SignupFragment extends Fragment {

    private EditText editTextUsername, editTextEmail, editTextConfirmEmail;
    private EditText editTextPassword, editTextConfirmPassword, editTextPhone;
    private Button buttonCreateAccount;

    public SignupFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        editTextUsername = view.findViewById(R.id.editTextUsernameSignup);
        editTextEmail = view.findViewById(R.id.editTextEmailSignup);
        editTextConfirmEmail = view.findViewById(R.id.editTextConfirmEmailSignup);
        editTextPassword = view.findViewById(R.id.editTextPasswordSignup);
        editTextConfirmPassword = view.findViewById(R.id.editTextConfirmPasswordSignup);
        editTextPhone = view.findViewById(R.id.editTextPhoneSignup);
        buttonCreateAccount = view.findViewById(R.id.buttonCreateAccount);

        buttonCreateAccount.setOnClickListener(v -> {
            String username = editTextUsername.getText().toString().trim();
            String email = editTextEmail.getText().toString().trim();
            String confirmEmail = editTextConfirmEmail.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            String confirmPassword = editTextConfirmPassword.getText().toString().trim();
            String phone = editTextPhone.getText().toString().trim();

            if (username.isEmpty() || email.isEmpty() || confirmEmail.isEmpty()
                    || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!email.equals(confirmEmail)) {
                Toast.makeText(getContext(), "Emails do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            String apiUrl = "http://10.0.2.2:5002/api/signup"; // Adjust for emulator/local

            new Thread(() -> {
                try {
                    URL url = new URL(apiUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);
                    connection.setRequestProperty("Content-Type", "application/json");

                    JSONObject signupData = new JSONObject();
                    signupData.put("username", username);
                    signupData.put("email", email);
                    signupData.put("password", password);
                    signupData.put("phone", phone);  // Optional — only if backend expects it

                    OutputStream os = connection.getOutputStream();
                    os.write(signupData.toString().getBytes());
                    os.flush();
                    os.close();

                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_CREATED) {
                        requireActivity().runOnUiThread(() -> {
                            Toast.makeText(getContext(), "Account created! Please login.", Toast.LENGTH_SHORT).show();
                            NavOptions options = new NavOptions.Builder()
                                    .setEnterAnim(R.anim.slide_in_rigth)
                                    .setExitAnim(R.anim.slide_out_left)
                                    .build();
                            Navigation.findNavController(v).navigate(R.id.action_signupFragment_to_interestsFragment, null, options);
                        });
                    } else {
                        requireActivity().runOnUiThread(() ->
                                Toast.makeText(getContext(), "Signup failed", Toast.LENGTH_SHORT).show()
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

        return view;
    }
}
