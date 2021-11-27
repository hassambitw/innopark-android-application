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

import com.autobots.innopark.JavaMailAPI;
import com.autobots.innopark.R;
import com.autobots.innopark.data.UserApi;

import org.w3c.dom.Text;


public class CustomerServiceFragment extends Fragment {

    private Toolbar toolbar;
    private TextView toolbarTitle;
    private Button sendButton;
    private TextView fromEditText;
    private EditText subjectEditText;
    private EditText messageEditText;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customer_service, container, false);

        sendButton = view.findViewById(R.id.id_customer_service_send_btn);
//        fromEditText = view.findViewById(R.id.id_customer_service_from);
        subjectEditText = view.findViewById(R.id.id_customer_service_subject);
        messageEditText = view.findViewById(R.id.id_customer_service_message);

        setupToolbar(view);

        sendButton.setOnClickListener((v) -> {
            sendMail();
        });

        return view;
    }

    private void sendMail()
    {
//        UserApi userApi = UserApi.getInstance();
//        String userEmail = userApi.getUserEmail();
        String[] toEmail = {"fromEmail@gmail.com"};

//        fromEditText.setText(userEmail);

        String subject = subjectEditText.getText().toString();
        String message = messageEditText.getText().toString();

//        JavaMailAPI javaMailAPI = new JavaMailAPI(getActivity(), toEmail, subject, message);
//
//        javaMailAPI.execute();


//        String[] recipientsSplit = recipients.split(",");
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, toEmail);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);

        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, "Choose an email app:"));


    }

    private void setupToolbar(View view)
    {
        toolbar = view.findViewById(R.id.id_menu_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarTitle = toolbar.findViewById(R.id.id_toolbar_title);
        toolbarTitle.setText("Customer Service");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }
}