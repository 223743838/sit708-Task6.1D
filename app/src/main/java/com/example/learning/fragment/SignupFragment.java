package com.example.learning.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.learning.R;
import java.io.IOException;

public class SignupFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView imageViewProfile;
    private EditText editTextUsername, editTextEmail, editTextConfirmEmail, editTextPassword, editTextConfirmPassword, editTextPhone;
    private Button buttonCreateAccount;
    private Uri profileImageUri;

    public SignupFragment() {
        // Required empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);

        imageViewProfile = view.findViewById(R.id.imageViewProfilePic);
        editTextUsername = view.findViewById(R.id.editTextUsernameSignup);
        editTextEmail = view.findViewById(R.id.editTextEmailSignup);
        editTextConfirmEmail = view.findViewById(R.id.editTextConfirmEmailSignup);
        editTextPassword = view.findViewById(R.id.editTextPasswordSignup);
        editTextConfirmPassword = view.findViewById(R.id.editTextConfirmPasswordSignup);
        editTextPhone = view.findViewById(R.id.editTextPhoneSignup);
        buttonCreateAccount = view.findViewById(R.id.buttonCreateAccount);

        imageViewProfile.setOnClickListener(v -> openImageChooser());

        buttonCreateAccount.setOnClickListener(v -> {
            if (validateForm()) {
                // After signup, navigate to InterestsFragment
                NavOptions options = new NavOptions.Builder()
                        .setEnterAnim(R.anim.slide_in_rigth)
                        .setExitAnim(R.anim.slide_out_left)
                        .build();
                Navigation.findNavController(v).navigate(R.id.action_signupFragment_to_interestsFragment, null, options);
            }
        });

        return view;
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && data != null && data.getData() != null) {
            profileImageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), profileImageUri);
                imageViewProfile.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean validateForm() {
        String username = editTextUsername.getText().toString();
        String email = editTextEmail.getText().toString();
        String confirmEmail = editTextConfirmEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        String confirmPassword = editTextConfirmPassword.getText().toString();
        String phone = editTextPhone.getText().toString();

        if (username.isEmpty() || email.isEmpty() || confirmEmail.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || phone.isEmpty()) {
            Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!email.equals(confirmEmail)) {
            Toast.makeText(getContext(), "Emails do not match", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!password.equals(confirmPassword)) {
            Toast.makeText(getContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
