package com.example.learning.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.learning.R;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

public class UpgradeFragment extends Fragment implements PaymentResultListener {

    private Button buttonStarter, buttonIntermediate, buttonAdvanced;
    private String selectedPlan;
    private double selectedAmount;

    public UpgradeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upgrade, container, false);

        Checkout.preload(requireContext());

        buttonStarter = view.findViewById(R.id.buttonUpgradeStarter);
        buttonIntermediate = view.findViewById(R.id.buttonUpgradeIntermediate);
        buttonAdvanced = view.findViewById(R.id.buttonUpgradeAdvanced);

        buttonStarter.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Starter Plan Activated (Free)", Toast.LENGTH_SHORT).show();
        });

        buttonIntermediate.setOnClickListener(v -> startPayment("Intermediate", 4.99));
        buttonAdvanced.setOnClickListener(v -> startPayment("Advanced", 9.99));

        return view;
    }

    private void startPayment(String planName, double amount) {
        selectedPlan = planName;
        selectedAmount = amount;

        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_XvuE88ay0SzduD"); // Replace with your Razorpay Key ID

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Learning App");
            options.put("description", planName + " Plan");
            options.put("currency", "INR");
            options.put("amount", (int)(amount * 100)); // Amount in paise

            JSONObject prefill = new JSONObject();
            prefill.put("email", "test@example.com");
            prefill.put("contact", "9123456789");
            options.put("prefill", prefill);

            checkout.open(requireActivity(), options);
        } catch (Exception e) {
            Toast.makeText(getContext(), "Payment init failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        Toast.makeText(getContext(), selectedPlan + " purchased successfully!", Toast.LENGTH_LONG).show();
        // Optionally update backend
    }

    @Override
    public void onPaymentError(int code, String response) {
        Toast.makeText(getContext(), "Payment failed: " + response, Toast.LENGTH_LONG).show();
    }
}
