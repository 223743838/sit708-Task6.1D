package com.example.learning.fragment;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.learning.R;

public class LoginFragment extends Fragment {

    private EditText editTextUsername, editTextPassword;
    private Button buttonLogin;
    private TextView textViewNeedAccount;

    public LoginFragment() {
        // Required empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        editTextUsername = view.findViewById(R.id.editTextUsername);
        editTextPassword = view.findViewById(R.id.editTextPassword);
        buttonLogin = view.findViewById(R.id.buttonLogin);
        textViewNeedAccount = view.findViewById(R.id.textViewNeedAccount);

        buttonLogin.setOnClickListener(v -> {
            String username = editTextUsername.getText().toString();
            String password = editTextPassword.getText().toString();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), "Please enter username and password", Toast.LENGTH_SHORT).show();
            } else {
                // For now, simulate login success
                NavOptions options = new NavOptions.Builder()
                        .setEnterAnim(R.anim.slide_in_rigth)
                        .setExitAnim(R.anim.slide_out_left)
                        .build();
                Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_dashboardFragment, null, options);
            }
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
