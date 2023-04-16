package com.example.myeventapp;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import android.content.Intent;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class EventDetailsActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    EditText textEventName, dateEvent, timeEvent, phoneNum;
    Button addEventBtn;

    SwitchCompat notificationSwitch;
    DatabaseReference myRef;

    boolean switchOnOff;

    String phoneNumber, message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        textEventName = findViewById(R.id.inputEventName);
        dateEvent = findViewById(R.id.inputEventDate);
        timeEvent = findViewById(R.id.inputEventTime);
        addEventBtn = findViewById(R.id.addEventDetailsBtn);
        phoneNum = findViewById(R.id.inputPhoneNumber);
        notificationSwitch = findViewById(R.id.notificationSwitch);
        myRef = FirebaseDatabase.getInstance().getReference();
        switchOnOff = false;
        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    switchOnOff = true;
                }
            }
        });
        addEventBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                InsertEvent();
                sendMessage();
                startActivity(new Intent(EventDetailsActivity.this, EventActivity.class));
            }
        });
    }


    private void InsertEvent() {
        String eventName = textEventName.getText().toString();
        String eventDate = dateEvent.getText().toString();
        String eventTime = timeEvent.getText().toString();
        String id = myRef.push().getKey();

        Event event = new Event(eventName, eventDate, eventTime);

        myRef.child("events").child(id).setValue(event)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(EventDetailsActivity.this, "Event Details inserted",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void sendMessage() {
        if (switchOnOff) {
            phoneNumber = phoneNum.getText().toString();
            message = "Below Event has been edit to your list:\n" +
                    "Event Name:\t" + textEventName.getText().toString() +
                    "Event Date:\t" + dateEvent.getText().toString() +
                    "Event Time:\t" + timeEvent.getText().toString();
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.SEND_SMS)
                    != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        android.Manifest.permission.SEND_SMS)) {
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{android.Manifest.permission.SEND_SMS},
                            MY_PERMISSIONS_REQUEST_SEND_SMS);
                }
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNumber, null, message, null, null);
                    Toast.makeText(EventDetailsActivity.this, "SMS sent.",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(EventDetailsActivity.this,
                            "SMS faild, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

    }

}