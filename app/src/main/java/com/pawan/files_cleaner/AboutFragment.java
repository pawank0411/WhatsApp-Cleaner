package com.pawan.files_cleaner;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AboutFragment extends Fragment {

    private TextView sourceCodeTV, feedbackTV, logoDesignerTV, aboutDevTV, privacyPolicyTV;

    public AboutFragment() {
        // Required empty public constructor
    }

    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sourceCodeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uriUrl = Uri.parse("https://github.com/pawank0411/WhatsApp-Cleaner");
                startActivity(new Intent(Intent.ACTION_VIEW, uriUrl));
            }
        });
        feedbackTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://forms.gle/gsxE4QaqvAM6D3xJ9")));
            }
        });
        logoDesignerTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.behance.net/vizdash1998")));
            }
        });
        aboutDevTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://pawan0411.github.io/")));

            }
        });
        privacyPolicyTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://whats-app-cleaner.flycricket.io/privacy.html")));
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_about, container, false);
        sourceCodeTV = inflate.findViewById(R.id.aboutPageViewSourceCodeTextView);
        feedbackTV = inflate.findViewById(R.id.aboutPageFeedbackTextView);
        logoDesignerTV = inflate.findViewById(R.id.aboutPageCreditLogoDesignerTV);
        aboutDevTV = inflate.findViewById(R.id.aboutPageAboutDeveloperTextView);
        privacyPolicyTV = inflate.findViewById(R.id.aboutPagePrivacyPolicyTextView);
        ((TextView) inflate.findViewById(R.id.aboutSampleVersion)).setText(String.format("v%s", BuildConfig.VERSION_NAME));
        return inflate;
    }

}