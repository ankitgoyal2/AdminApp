package com.example.adminapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminapp.R;
import com.example.adminapp.models.Mechanic;
import com.firebase.ui.database.paging.DatabasePagingOptions;
import com.firebase.ui.database.paging.FirebaseRecyclerPagingAdapter;
import com.firebase.ui.database.paging.LoadingState;

import de.hdodenhof.circleimageview.CircleImageView;

public class MechanicHomepageListAdapter extends FirebaseRecyclerPagingAdapter<Mechanic,MechanicHomepageListAdapter.MyHolder> {
    Context c;
    private final int[] mColors = {R.color.list_color_2,R.color.list_color_3,R.color.list_color_4,R.color.list_color_5,
            R.color.list_color_6,R.color.list_color_7,R.color.list_color_8,R.color.list_color_9,R.color.list_color_10,R.color.list_color_11};

    public MechanicHomepageListAdapter(@NonNull DatabasePagingOptions<Mechanic> options,Context c) {
        super(options);
        this.c = c;
    }

    @Override
    protected void onBindViewHolder(@NonNull MechanicHomepageListAdapter.MyHolder viewHolder, int position, @NonNull Mechanic model) {
        int bgColor = ContextCompat.getColor(c, mColors[position % 10]);
       viewHolder.cardView.setCardBackgroundColor(bgColor);
        viewHolder.bind(model);
    }

    @Override
    protected void onLoadingStateChanged(@NonNull LoadingState state) {

    }

    @NonNull
    @Override
    public MechanicHomepageListAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manager_list_item, null);
        return new MechanicHomepageListAdapter.MyHolder(view);
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        CircleImageView profilepic;
        TextView mechanicName;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
          cardView = itemView.findViewById(R.id.card_item);
          profilepic = itemView.findViewById(R.id.manager_pic);
          mechanicName = itemView.findViewById(R.id.manager_name);
        }
        public void bind(Mechanic model) {
         mechanicName.setText(model.getUserName());
        }
    }
}
