package com.example.adminapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.adminapp.AddEmployeeActivity;
import com.example.adminapp.BroadcastActivity;
import com.example.adminapp.GenerateQRActivity;
import com.example.adminapp.R;
import com.smarteist.autoimageslider.SliderViewAdapter;


public class ViewPagerAdapter extends SliderViewAdapter<ViewPagerAdapter.SliderAdapterVH> {
    private Context context;
    private LayoutInflater layoutInflater;
    private int[] images = {R.drawable.addemployee, R.drawable.broadcastmesg,R.drawable.generateqrimg};

    public ViewPagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_viewpager, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {

        Glide.with(viewHolder.itemView)
                .load(images[position])
                .fitCenter()
                .into(viewHolder.imageViewBackground);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                switch (position)
                {
                    case 0:
                        intent = new Intent(context, AddEmployeeActivity.class);
                        context.startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(context, BroadcastActivity.class);
                        context.startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(context, GenerateQRActivity.class);
                        context.startActivity(intent);
                        break;
                }
            }
        });
    }

    @Override
    public int getCount() {
        return images.length;
    }


    public class SliderAdapterVH extends SliderViewAdapter.ViewHolder {
        View itemView;
        ImageView imageViewBackground;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
            this.itemView = itemView;

        }
    }
}
