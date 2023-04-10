package com.example.datapaging;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class StudentAdapter extends PagedListAdapter<Student, StudentAdapter.StudentViewHolder> {

    public StudentAdapter() {
        super(new DiffUtil.ItemCallback<Student>() {
            @Override
            public boolean areItemsTheSame(@NonNull Student oldItem, @NonNull Student newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Student oldItem, @NonNull Student newItem) {
                return oldItem.getNumber() == newItem.getNumber();
            }
        });
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.cell, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Student student = getItem(position);

        //由於採 page 時，可能容器有了但資料還沒到，造成 null student 所以要擋，快速亂滑動就可以看到 loading...
        if (student == null) {
            holder.textView.setText("loading...");
        } else {
            holder.textView.setText(String.valueOf(student.getNumber()));
        }
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textView = itemView.findViewById(R.id.cellText);
        }
    }
}
