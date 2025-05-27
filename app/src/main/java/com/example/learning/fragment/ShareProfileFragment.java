package com.example.learning.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.learning.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ShareProfileFragment extends Fragment {

    private TextView textUsername, textScore, textLevel, textLink;
    private ImageView imageViewQr;
    private Button buttonShare, buttonCopyLink;
    private String profileLink;

    public ShareProfileFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_share_profile, container, false);

        textUsername = view.findViewById(R.id.textUsername);
        textScore = view.findViewById(R.id.textScore);
        textLevel = view.findViewById(R.id.textLevel);
        imageViewQr = view.findViewById(R.id.imageViewQrCode);
        buttonShare = view.findViewById(R.id.buttonShare);
        buttonCopyLink = view.findViewById(R.id.buttonCopyLink);
        textLink = view.findViewById(R.id.textViewPublicLink);

        String userId = requireActivity().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                .getString("userId", null);

        if (userId == null) {
            Toast.makeText(getContext(), "User ID not found", Toast.LENGTH_SHORT).show();
            return view;
        }

        profileLink = "http://10.0.2.2:5002/public-profile/" + userId;
        textLink.setText(profileLink);

        new Thread(() -> {
            try {
                URL url = new URL("http://10.0.2.2:5002/public-profile/" + userId);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    JSONObject json = new JSONObject(response.toString());
                    String username = json.optString("name", "N/A");
                    String level = "Basic"; // Hardcoded if not returned by API
                    int score = json.optInt("score", 0);

                    requireActivity().runOnUiThread(() -> {
                        textUsername.setText("@" + username);
                        textLevel.setText("Level: " + level);
                        textScore.setText("Score: " + score);
                        generateQrCode(profileLink);
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(), "Error fetching profile", Toast.LENGTH_SHORT).show());
            }
        }).start();

        buttonShare.setOnClickListener(v -> {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, profileLink);
            startActivity(Intent.createChooser(shareIntent, "Share via"));
        });

        buttonCopyLink.setOnClickListener(v -> {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager)
                    requireActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Profile Link", profileLink);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(getContext(), "Link copied to clipboard", Toast.LENGTH_SHORT).show();
        });

        return view;
    }

    private void generateQrCode(String text) {
        QRCodeWriter writer = new QRCodeWriter();
        try {
            int size = 512;
            BitMatrix bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, size, size);

            Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565);
            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            imageViewQr.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}
