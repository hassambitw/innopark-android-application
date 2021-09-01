package com.autobots.innopark.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.autobots.innopark.R;


public class CustomerServiceFragment extends Fragment {

    private Toolbar toolbar;
    private TextView toolbar_title;
    private Button sendButton;
    private EditText toEditText;
    private EditText subjectEditText;
    private EditText messageEditText;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customer_service, container, false);

        sendButton = view.findViewById(R.id.id_customer_service_send_btn);
        toEditText = view.findViewById(R.id.id_customer_service_to);
        subjectEditText = view.findViewById(R.id.id_customer_service_subject);
        messageEditText = view.findViewById(R.id.id_customer_service_message);

        setupToolbar();

        sendButton.setOnClickListener((v) -> {
            sendMail();
        });

        return view;
    }

    private void sendMail()
    {
        String recipients = toEditText.getText().toString();
        String[] recipientsSplit = recipients.split(",");

        String subject = subjectEditText.getText().toString();
        String message = messageEditText.getText().toString();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, recipientsSplit);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);

        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, "Choose an email app:"));


    }

    private void setupToolbar()
    {
        toolbar = getActivity().findViewById(R.id.id_menu_toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_title = toolbar.findViewById(R.id.id_toolbar_title);
        toolbar_title.setText("Customer Service");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }
}