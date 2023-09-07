package com.example.meet_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;

public class CreateDashboardActivity extends AppCompatActivity {

    EditText secretCodeBox;
    Button createBtn;
    private JitsiMeetConferenceOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_dashboard);

        secretCodeBox = findViewById(R.id.codeBox);
        createBtn = findViewById(R.id.generateBtn);

        try {
            options = new JitsiMeetConferenceOptions.Builder()
                    .setServerURL(new URL("https://meet.jit.si"))
                    .setRoom(String.valueOf(secretCodeBox))
                    .build();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), JitsiMeetActivity.class);
                intent.setAction("org.jitsi.meet.CONFERENCE");
                intent.putExtra("JitsiMeetConferenceOptions", options);
                startActivity(intent);

            }

        });

    }
}