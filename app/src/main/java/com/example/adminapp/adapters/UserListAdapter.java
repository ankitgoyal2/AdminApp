package com.example.adminapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminapp.R;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.Holder> {
    @NonNull
    @Override
    public UserListAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_design, parent, false);

        return new Holder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserListAdapter.Holder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 6;
    }

    public class Holder extends RecyclerView.ViewHolder {

        public Holder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
