package com.example.adminapp.adapters;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.transition.Fade;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminapp.ProfileActivity;
import com.example.adminapp.R;
import com.example.adminapp.models.Manager;
import com.example.adminapp.models.Mechanic;
import com.firebase.ui.database.paging.DatabasePagingOptions;
import com.firebase.ui.database.paging.FirebaseRecyclerPagingAdapter;
import com.firebase.ui.database.paging.LoadingState;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class ManagerListAdapter extends FirebaseRecyclerPagingAdapter<Manager, ManagerListAdapter.ManagerHolder> {

    /**
     * Construct a new FirestorePagingAdapter from the given {@link DatabasePagingOptions}.
     *
     * @param options
     */
    Activity activity;

    public ManagerListAdapter(DatabasePagingOptions<Manager> options, Activity activity)                                               //Enter the type of data in the space for model
    {
        super(options);
        this.activity = activity;

    }

    @NonNull


    @Override
    protected void onBindViewHolder(@NonNull ManagerListAdapter.ManagerHolder myholder1, int position, Manager model) {
        ConstraintLayout card;
        card = myholder1.card;
        if(position%3==0) {
            Drawable background = card.getBackground();
            if (background instanceof ShapeDrawable) {
                ((ShapeDrawable) background).getPaint().setColor(ContextCompat.getColor(activity, R.color.darkSurface));
            } else if (background instanceof GradientDrawable) {
                ((GradientDrawable) background).setColor(ContextCompat.getColor(activity, R.color.darkSurface));
            } else if (background instanceof ColorDrawable) {
                ((ColorDrawable) background).setColor(ContextCompat.getColor(activity, R.color.darkSurface));
            }
        }
        else if(position%3==1)
        {
            Drawable background = card.getBackground();
            if (background instanceof ShapeDrawable) {
                ((ShapeDrawable) background).getPaint().setColor(ContextCompat.getColor(activity, R.color.mediumSurface));
            } else if (background instanceof GradientDrawable) {
                ((GradientDrawable) background).setColor(ContextCompat.getColor(activity, R.color.mediumSurface));
            } else if (background instanceof ColorDrawable) {
                ((ColorDrawable) background).setColor(ContextCompat.getColor(activity, R.color.mediumSurface));
            }
        }
        else
        {
            Drawable background = card.getBackground();
            if (background instanceof ShapeDrawable) {
                ((ShapeDrawable) background).getPaint().setColor(ContextCompat.getColor(activity, R.color.lightSurface));
            } else if (background instanceof GradientDrawable) {
                ((GradientDrawable) background).setColor(ContextCompat.getColor(activity, R.color.lightSurface));
            } else if (background instanceof ColorDrawable) {
                ((ColorDrawable) background).setColor(ContextCompat.getColor(activity, R.color.lightSurface));
            }
        }
        myholder1.bind(model);
    }

    protected void onLoadingStateChanged(@NonNull LoadingState state){

    }

    @Override
    public ManagerListAdapter.ManagerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_profile_card_new,null);

        return new ManagerListAdapter.ManagerHolder(view);
    }

    class ManagerHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        CircleImageView userProfilePic;
        ConstraintLayout card;


        public ManagerHolder(@NonNull View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.profile_card);

//            cardView = itemView.findViewById(R.id.card_layout);
//            userProfilePic = itemView.findViewById(R.id.user_profile_pic);
//
//
//            Bitmap bitmap = ((BitmapDrawable)userProfilePic.getDrawable()).getBitmap();
//            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//            final byte[] byteArray = stream.toByteArray();
//
//            Fade fade = new Fade();
//            View decor = activity.getWindow().getDecorView();
//            fade.excludeTarget(android.R.id.statusBarBackground,true);
//            fade.excludeTarget(android.R.id.navigationBarBackground,true);
//            activity.getWindow().setEnterTransition(fade);
//            activity.getWindow().setExitTransition(fade);
//
//            cardView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    Intent intent = new Intent(activity.getApplicationContext(), ProfileActivity.class);
//                  //  Pair<View,String> p1 = Pair.create((View)userProfilePic, ViewCompat.getTransitionName(userProfilePic));
//                    //intent.putExtra("userProfilePic",byteArray);
//                    //Pair<View,String> p2 = Pair.create((View)profile,ViewCompat.getTransitionName(profile));
//                   // ActivityOptions optionsCompat = ActivityOptions.makeSceneTransitionAnimation(activity,p1);
//                    activity.startActivity(intent/*,optionsCompat.toBundle()*/);
//
//                }
//            });

        }

        public void bind(Manager model) {

            Log.i("username", model.getUserName());

        }
    }

}
