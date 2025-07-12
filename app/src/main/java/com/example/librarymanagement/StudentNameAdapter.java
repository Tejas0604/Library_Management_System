// ✅ Final StudentNameAdapter.java
package com.example.librarymanagement;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class StudentNameAdapter extends RecyclerView.Adapter<StudentNameAdapter.StudentViewHolder> {

    Context context;
    List<Booking> studentList;
    String labId;

    public StudentNameAdapter(Context context, List<Booking> studentList, String labId) {
        this.context = context;
        this.studentList = studentList;
        this.labId = labId;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_student_name, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Booking booking = studentList.get(position);
        holder.tvStudentName.setText(booking.getStudentName());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, StudentFullDetailsActivity.class);
            intent.putExtra("studentName", booking.getStudentName());
            intent.putExtra("mobile", booking.getMobileNumber());
            intent.putExtra("comingTime", booking.getComingTime());
            intent.putExtra("goTime", booking.getGoTime());
            intent.putExtra("membershipPlan", booking.getMembershipPlan());
            intent.putExtra("remainingDays", String.valueOf(booking.getRemainingDays()));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView tvStudentName;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStudentName = itemView.findViewById(R.id.tvStudentName);
        }
    }
}
