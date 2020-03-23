package com.example.adminapp.adapters;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.transition.Fade;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminapp.MainActivity;
import com.example.adminapp.ProfileActivity;
import com.example.adminapp.R;

import java.io.ByteArrayOutputStream;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.Holder> {

    Activity activity;

    public UserListAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public UserListAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_profile_card, parent, false);

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

        ImageView userProfilePic;
        TextView userName;
        CardView cardView;

        public Holder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.card_layout);
            userProfilePic = itemView.findViewById(R.id.user_profile_pic);


            Bitmap bitmap = ((BitmapDrawable)userProfilePic.getDrawable()).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            final byte[] byteArray = stream.toByteArray();

            Fade fade = new Fade();
            View decor = activity.getWindow().getDecorView();
            fade.excludeTarget(android.R.id.statusBarBackground,true);
            fade.excludeTarget(android.R.id.navigationBarBackground,true);
            activity.getWindow().setEnterTransition(fade);
            activity.getWindow().setExitTransition(fade);

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(activity.getApplicationContext(), ProfileActivity.class);
                    Pair<View,String> p1 = Pair.create((View)userProfilePic, ViewCompat.getTransitionName(userProfilePic));
                    intent.putExtra("userProfilePic",byteArray);
                    //Pair<View,String> p2 = Pair.create((View)profile,ViewCompat.getTransitionName(profile));
                    ActivityOptions optionsCompat = ActivityOptions.makeSceneTransitionAnimation(activity,p1);
                    activity.startActivity(intent,optionsCompat.toBundle());

                }
            });

        }
    }
}
