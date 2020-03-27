package com.example.adminapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminapp.R;
import com.example.adminapp.models.Manager;
import com.example.adminapp.models.Mechanic;
import com.firebase.ui.database.paging.DatabasePagingOptions;
import com.firebase.ui.database.paging.FirebaseRecyclerPagingAdapter;
import com.firebase.ui.database.paging.LoadingState;

public class ManagerListAdapter extends FirebaseRecyclerPagingAdapter<Manager, ManagerListAdapter.ManagerHolder> {

    /**
     * Construct a new FirestorePagingAdapter from the given {@link DatabasePagingOptions}.
     *
     * @param options
     */
    Context c;

    public ManagerListAdapter(DatabasePagingOptions<Manager> options, Context c)                                               //Enter the type of data in the space for model
    {
        super(options);
        this.c = c;

    }

    @NonNull


    @Override
    protected void onBindViewHolder(@NonNull ManagerListAdapter.ManagerHolder myholder1, int position, Manager model) {

        myholder1.bind(model);
    }

    protected void onLoadingStateChanged(@NonNull LoadingState state){

    }

    @Override
    public ManagerListAdapter.ManagerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_profile_card,null);
        return new ManagerListAdapter.ManagerHolder(view);
    }


    class ManagerHolder extends RecyclerView.ViewHolder{


        public ManagerHolder(@NonNull View itemView) {
            super(itemView);






        }
        public void bind(Manager model) {

            Log.i("username", model.getUserName());

        }
    }

}
