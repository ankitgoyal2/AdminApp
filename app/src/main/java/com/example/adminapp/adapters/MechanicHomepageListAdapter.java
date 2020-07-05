package com.example.adminapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminapp.ManagerProfileActivity;
import com.example.adminapp.MechanicProfileActivity;
import com.example.adminapp.R;
import com.example.adminapp.models.Manager;
import com.example.adminapp.models.Mechanic;
import com.firebase.ui.database.paging.DatabasePagingOptions;
import com.firebase.ui.database.paging.FirebaseRecyclerPagingAdapter;
import com.firebase.ui.database.paging.LoadingState;
import com.google.firebase.database.DataSnapshot;

import org.parceler.Parcels;

import de.hdodenhof.circleimageview.CircleImageView;

public class MechanicHomepageListAdapter extends FirebaseRecyclerPagingAdapter<Mechanic,MechanicHomepageListAdapter.MyHolder> {
    Context c;

    public MechanicHomepageListAdapter(@NonNull DatabasePagingOptions<Mechanic> options,Context c) {
        super(options);
        this.c = c;
    }

    @Override
    protected void onBindViewHolder(@NonNull MechanicHomepageListAdapter.MyHolder viewHolder, int position, @NonNull Mechanic model) {

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
        TextView mechanicName,buttonViewOption;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
          cardView = itemView.findViewById(R.id.card_item);
          profilepic = itemView.findViewById(R.id.manager_pic);
          mechanicName = itemView.findViewById(R.id.manager_name);
            buttonViewOption =  itemView.findViewById(R.id.textViewOptions);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DataSnapshot dataSnapshot = getItem(getAdapterPosition());
                    Mechanic mechanic= null;
                    if (dataSnapshot != null) {
                        mechanic = dataSnapshot.getValue(Mechanic.class);
                    }
                    if(mechanic!=null && mechanic.getUserName()!=null) {
                        Intent intent = new Intent(c, MechanicProfileActivity.class);
                        intent.putExtra("mechanic", Parcels.wrap(mechanic));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        c.getApplicationContext().startActivity(intent);
                    }
                }
            });
        }
        public void bind(Mechanic model) {

            mechanicName.setText(model.getUserName());
            buttonViewOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(c, buttonViewOption);
                    popup.inflate(R.menu.option_menu);
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.menu1:
                                    //handle menu1 click
                                    break;
                                case R.id.menu2:
                                    //handle menu2 click
                                    break;
                            }
                            return false;
                        }
                    });

                    popup.show();
                }
            });
        }
    }
}
