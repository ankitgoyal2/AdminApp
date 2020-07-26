package com.example.adminapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.adminapp.ManagerProfileActivity;
import com.example.adminapp.R;
import com.example.adminapp.models.Complaint;
import com.example.adminapp.models.Manager;
import com.example.adminapp.models.Mechanic;
import com.firebase.ui.database.paging.DatabasePagingOptions;
import com.firebase.ui.database.paging.FirebaseRecyclerPagingAdapter;
import com.firebase.ui.database.paging.LoadingState;
import com.google.firebase.database.DataSnapshot;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ManagerHomepageListAdapter extends FirebaseRecyclerPagingAdapter<Manager,ManagerHomepageListAdapter.MyHolder> implements Filterable {
    Context c;
    private final List<Manager> userList;

    private final List<Manager> filteredUserList;

    private UserFilter userFilter;

    public ManagerHomepageListAdapter(@NonNull DatabasePagingOptions<Manager> options, Context c) {
        super(options);
        this.c = c;
        this.userList =new ArrayList<>();
        this.filteredUserList = new ArrayList<>();
    }



    @Override
    protected void onBindViewHolder(@NonNull MyHolder viewHolder, int position, @NonNull Manager model) {
        userList.add(model);
        viewHolder.bind(model);
    }

    @Override
    protected void onLoadingStateChanged(@NonNull LoadingState state) {

    }

    @NonNull
    @Override
    public ManagerHomepageListAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manager_list_item, null);
        return new ManagerHomepageListAdapter.MyHolder(view);
    }

    public Filter getFilter() {
        if(userFilter == null)
            userFilter = new UserFilter(this, userList);
        return userFilter;
    }
    private static class UserFilter extends Filter {

        private final ManagerHomepageListAdapter adapter;

        private final List<Manager> originalList;

        private final List<Manager> filteredList;

        private UserFilter(ManagerHomepageListAdapter adapter, List<Manager> originalList) {
            super();
            this.adapter = adapter;
            this.originalList = new ArrayList<>(originalList);
            this.filteredList = new ArrayList<>();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filteredList.clear();
            final FilterResults results = new FilterResults();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(originalList);
            } else {
                final String filterPattern = constraint.toString().toLowerCase().trim();

                for (final Manager user : originalList) {
                    if (user.getUserName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(user);
                    }
                }
            }
            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            adapter.filteredUserList.clear();
            adapter.filteredUserList.addAll((Collection<? extends Manager>) results.values);
            adapter.notifyDataSetChanged();
        }
    }


    public class MyHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        CircleImageView profilepic;
        TextView managerName,buttonViewOption;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_item);
            profilepic = itemView.findViewById(R.id.manager_pic);
            managerName = itemView.findViewById(R.id.manager_name);
            buttonViewOption =  itemView.findViewById(R.id.textViewOptions);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DataSnapshot dataSnapshot = getItem(getAdapterPosition());
                    Manager manager = null;
                    if (dataSnapshot != null) {
                        manager = dataSnapshot.getValue(Manager.class);
                    }

                    if(manager!=null && manager.getUserName()!=null) {
                        Intent intent = new Intent(c, ManagerProfileActivity.class);
                        intent.putExtra("manager", Parcels.wrap(manager));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        c.getApplicationContext().startActivity(intent);
                    }
                }
            });
        }
        public void bind(Manager model) {

            managerName.setText(model.getUserName());
            Glide.with(itemView)
                    .load(model.getProfilePicLink())
                    .fitCenter()
                    .placeholder(R.drawable.profilepicdemo)
                    .into(profilepic);
          buttonViewOption.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  PopupMenu popup = new PopupMenu(c,buttonViewOption);
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
