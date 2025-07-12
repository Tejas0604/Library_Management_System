package com.example.librarymanagement;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LabInfoAdapter extends RecyclerView.Adapter<LabInfoAdapter.LabViewHolder> {

    Context context;
    List<LabInfo> labList;
    String userRole; // "owner" or "user"
    String source;

    public LabInfoAdapter(Context context, List<LabInfo> labList, String userRole, String source) {
        this.context = context;
        this.labList = labList;
        this.userRole = userRole;
        this.source = source;
    }

    public LabInfoAdapter(LabInfoActivity context, List<LabInfo> labList) {
        // This constructor appears unused, or you should remove it if not used.
    }

    @NonNull
    @Override
    public LabViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lab_item_card, parent, false);
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

        holder.btnViewSeats.setOnClickListener(v -> {
            Intent intent = new Intent(context, SeatStatusActivity.class);
            intent.putExtra("labId", lab.labId); // ✅ Send labId instead of lab name
            intent.putExtra("userRole", userRole);
            intent.putExtra("source", source);
            context.startActivity(intent);
        });

        if (userRole != null && userRole.equals("owner")) {
            holder.btnStudentDetails.setVisibility(View.VISIBLE);
            holder.btnStudentDetails.setOnClickListener(v -> {
                Intent intent = new Intent(context, StudentDetailsActivity.class);
                intent.putExtra("labId", lab.labId); // ✅ Send labId to match with Booking.labId
                context.startActivity(intent);
            });
        } else {
            holder.btnStudentDetails.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return labList.size();
    }

    static class LabViewHolder extends RecyclerView.ViewHolder {
        TextView name, location, floor, room, startTime, endTime, fee, seats;
        Button btnViewSeats, btnStudentDetails;

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
            btnViewSeats = itemView.findViewById(R.id.btnViewSeats);
            btnStudentDetails = itemView.findViewById(R.id.btnStudentDetails);
        }
    }
}
