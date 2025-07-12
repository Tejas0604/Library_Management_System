package com.example.librarymanagement;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class libraryAdapter extends RecyclerView.Adapter<libraryAdapter.LibraryViewHolder> {

    private List<AdminFirstPageHelper> libraryList;

    public libraryAdapter(List<AdminFirstPageHelper> libraryList) {
        this.libraryList = libraryList;
    }

    @NonNull
    @Override
    public LibraryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_library, parent, false);
        return new LibraryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LibraryViewHolder holder, int position) {
        AdminFirstPageHelper library = libraryList.get(position);
        holder.tvName.setText("🏢 Name: " + library.getName());
        holder.tvLocation.setText("📍 Location: " + library.getLocation());
        holder.tvTime.setText("⏰ " + library.getStart_time() + " - " + library.getEnd_time());
        holder.tvFloor.setText("🏬 Floor: " + library.getFloor());
        holder.tvRoom.setText("🚪 Room: " + library.getRoom());
        holder.tvSeats.setText("💺 Seats: " + library.getSeats());
        holder.tvFee.setText("💰 Fee: ₹" + library.getFee());
    }

    @Override
    public int getItemCount() {
        return libraryList.size();
    }

    static class LibraryViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvLocation, tvTime, tvFloor, tvRoom, tvSeats, tvFee;

        public LibraryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvFloor = itemView.findViewById(R.id.tvFloor);
            tvRoom = itemView.findViewById(R.id.tvRoom);
            tvSeats = itemView.findViewById(R.id.tvSeats);
            tvFee = itemView.findViewById(R.id.tvFee);
        }
    }
}
