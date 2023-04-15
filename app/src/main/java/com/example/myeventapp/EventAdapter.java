package com.example.myeventapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    Context context;
    ArrayList<Event> eventList;

    DatabaseReference databaseReference;

    public EventAdapter(Context context, ArrayList<Event> eventList) {
        this.context = context;
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.entry_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.eventName.setText(event.getEventName());
        holder.eventDate.setText(event.getEventDate());
        holder.eventTime.setText(event.getEventTime());
        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialogPlus = DialogPlus.newDialog(holder.eventName.getContext())
                        .setContentHolder(new ViewHolder(R.layout.edit_dialog_popup))
                        .setExpanded(true,1500)
                        .create();

                View eventView = dialogPlus.getHolderView();

                EditText currentEventName = eventView.findViewById(R.id.editEventName);
                EditText currentEventDate = eventView.findViewById(R.id.editEventDate);
                EditText currentEventTime = eventView.findViewById(R.id.editEventTime);

                Button updateBtn = eventView.findViewById(R.id.updateBtn);

                currentEventName.setText(event.getEventName());
                currentEventDate.setText(event.getEventDate());
                currentEventTime.setText(event.getEventTime());

                dialogPlus.show();

                updateBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        databaseReference = FirebaseDatabase.getInstance().getReference();
                        String key = databaseReference.push().getKey();
                        String name = currentEventName.getText().toString();
                        String date = currentEventDate.getText().toString();
                        String time = currentEventTime.getText().toString();
                        Event updatedEvent = new Event(name, date, time);
                        Map<String, Object> eventValues = updatedEvent.eventMap();


//                        Map<String, Object> childUpdates = new HashMap<>();
//                        childUpdates.put(key, eventValues);
//                        databaseReference.updateChildren(childUpdates)
//                                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void unused) {
//                                        Toast.makeText(holder.eventName.getContext(), "Event Details Updated Successfully ", Toast.LENGTH_SHORT).show();
//                                        dialogPlus.dismiss();
//                                    }
//                                })
//                                .addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        Toast.makeText(holder.eventName.getContext(), "Failed to update event details", Toast.LENGTH_SHORT).show();
//                                        dialogPlus.dismiss();
//                                    }
//                                });



                        databaseReference.child("events")
                                .child(key)
                                .updateChildren(eventValues)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(holder.eventName.getContext(), "Event Details Updated Successfully ", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(holder.eventName.getContext(), "Failed to update event details", Toast.LENGTH_SHORT).show();
                                        dialogPlus.dismiss();
                                    }
                                });
                    }
                });
            }
        });
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.eventName.getContext());
                builder.setTitle("Are you sure?");
                builder.setMessage("Deleted event can't be undone.");

                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //FIXME:Need to work on
                        databaseReference.child("events");
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(holder.eventName.getContext(), "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView eventName, eventDate, eventTime;

        Button editBtn, deleteBtn;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            eventName = itemView.findViewById(R.id.textEventName);
            eventDate = itemView.findViewById(R.id.textEventDate);
            eventTime = itemView.findViewById(R.id.textEventTime);

            editBtn = itemView.findViewById(R.id.editEvent);
            deleteBtn = itemView.findViewById(R.id.deleteEvent);
        }
    }
}
