package com.example.adminapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminapp.R;
import com.example.adminapp.models.Complaint;
import com.example.adminapp.models.Mechanic;
import com.firebase.ui.database.paging.DatabasePagingOptions;
import com.firebase.ui.database.paging.FirebaseRecyclerPagingAdapter;
import com.firebase.ui.database.paging.LoadingState;
import com.google.firebase.database.DataSnapshot;

import org.parceler.Parcels;

public class MechanicListAdapter extends FirebaseRecyclerPagingAdapter<Mechanic, MechanicListAdapter.MechanicHolder> {

    /**
     * Construct a new FirestorePagingAdapter from the given {@link DatabasePagingOptions}.
     *
     * @param options
     */
    Context c;

    public MechanicListAdapter(DatabasePagingOptions<Mechanic> options, Context c)                                               //Enter the type of data in the space for model
    {
        super(options);
        this.c = c;

    }

    @NonNull


    @Override
    protected void onBindViewHolder(@NonNull MechanicListAdapter.MechanicHolder myholder1, int position, Mechanic model) {

        myholder1.bind(model);
    }

    protected void onLoadingStateChanged(@NonNull LoadingState state){

    }

    @Override
    public MechanicListAdapter.MechanicHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_design,null);
        return new MechanicListAdapter.MechanicHolder(view);
    }


    class MechanicHolder extends RecyclerView.ViewHolder{


        public MechanicHolder(@NonNull View itemView) {
            super(itemView);






        }
        public void bind(Mechanic model) {

            Log.i("username", model.getUserName());


        }
    }

}