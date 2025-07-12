package com.example.librarymanagement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OwnerLabAdapter extends RecyclerView.Adapter<OwnerLabAdapter.LabViewHolder> {
    Context context;
    List<LabInfo> labList;

    public OwnerLabAdapter(Context context, List<LabInfo> labList) {
        this.context = context;
        this.labList = labList;
    }

    @NonNull
    @Override
    public LabViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lab_item_card, parent, false);
        return new LabViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LabViewHolder holder, int position) {
        LabInfo lab = labList.get(position);
        holder.name.setText("Name: " + lab.name);
        holder.location.setText("Location: " + lab.location);
        holder.floor.setText("Floor: " + lab.floor);
        holder.room.setText("Room: " + lab.room);
        holder.startTime.setText("Start Time: " + lab.start_time);
        holder.endTime.setText("End Time: " + lab.end_time);
        holder.fee.setText("Fee: ₹" + lab.fee);
        holder.seats.setText("Seats: " + lab.seats);
    }

    @Override
    public int getItemCount() {
        return labList.size();
    }

    static class LabViewHolder extends RecyclerView.ViewHolder {
        TextView name, location, floor, room, startTime, endTime, fee, seats;

        public LabViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.labName);
            location = itemView.findViewById(R.id.labLocation);
            floor = itemView.findViewById(R.id.labFloor);
            room = itemView.findViewById(R.id.labRoom);
            startTime = itemView.findViewById(R.id.labStartTime);
            endTime = itemView.findViewById(R.id.labEndTime);
            fee = itemView.findViewById(R.id.labFee);
            seats = itemView.findViewById(R.id.labSeats);
        }
    }
}
