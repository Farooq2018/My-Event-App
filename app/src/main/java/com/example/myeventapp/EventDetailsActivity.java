package com.example.myeventapp;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;


import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class EventDetailsActivity extends AppCompatActivity {
    EditText textEventName, dateEvent, timeEvent;
    Button addEventBtn;

    SwitchCompat notificationSwitch;
    DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        textEventName = findViewById(R.id.inputEventName);
        dateEvent = findViewById(R.id.inputEventDate);
        timeEvent = findViewById(R.id.editTextTime);
        addEventBtn = findViewById(R.id.addEventDetailsBtn);
        notificationSwitch = findViewById(R.id.notificationSwitch);
        myRef = FirebaseDatabase.getInstance().getReference();
        addEventBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                InsertEvent();
                //startActivity(new Intent(EventDetailsActivity.this, EventActivity.class));
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
}