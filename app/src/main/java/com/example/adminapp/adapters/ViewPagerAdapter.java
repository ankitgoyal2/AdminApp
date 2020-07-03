package com.example.adminapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.adminapp.R;


public class ViewPagerAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private int [] images = {R.drawable.addemployee,R.drawable.broadcastmesg};

    public ViewPagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        layoutInflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.activity_viewpager,null);
        ImageView imageView = (ImageView) view.findViewById(R.id.img_viewpager);
        imageView.setImageResource(images[position]);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(position==0)
                {
                    Toast.makeText(context,"Slide 1",Toast.LENGTH_SHORT);
                }
                else if(position==1)
                {
                    Toast.makeText(context,"Slide 2",Toast.LENGTH_SHORT);
                }
                else
                {
                    Toast.makeText(context,"Slide 3",Toast.LENGTH_SHORT);
                }
            }
        });
        ViewPager vp =(ViewPager)container;
        vp.addView(view,0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        ViewPager vp =(ViewPager)container;
        View view =(View)object;
        vp.removeView(view);
    }
}
