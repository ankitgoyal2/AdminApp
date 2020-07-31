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
import com.example.adminapp.models.Machine;
import com.firebase.ui.database.paging.DatabasePagingOptions;
import com.firebase.ui.database.paging.FirebaseRecyclerPagingAdapter;
import com.firebase.ui.database.paging.LoadingState;

public class StockRecordAdpater extends FirebaseRecyclerPagingAdapter<Machine, StockRecordAdpater.MyMachinesHolder> {



    Context c;
    private final int[] mColors = {R.color.list_color_2,R.color.list_color_3,R.color.list_color_4,R.color.list_color_5,
            R.color.list_color_6,R.color.list_color_7,R.color.list_color_8,R.color.list_color_9,R.color.list_color_10,R.color.list_color_11};

    public StockRecordAdpater(@NonNull DatabasePagingOptions<Machine> options, Context c) {
        super(options);

        this.c = c;
    }


    @Override
    protected void onBindViewHolder(@NonNull MyMachinesHolder viewHolder, int position, @NonNull Machine model) {
        int bgColor = ContextCompat.getColor(c, mColors[position % 10]);
        viewHolder.cardView.setCardBackgroundColor(bgColor);
        viewHolder.bind(model);
    }

    @Override
    protected void onLoadingStateChanged(@NonNull LoadingState state) {

    }

    @NonNull
    @Override
    public MyMachinesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stock_record_item,null);
        return new MyMachinesHolder(view);
    }

    public class MyMachinesHolder extends RecyclerView.ViewHolder {

        TextView machineId, location,type,serialNumber,workingStatus;
        CardView cardView;

        public MyMachinesHolder(@NonNull View itemView) {
            super(itemView);

            machineId = itemView.findViewById(R.id.machine_id);
            serialNumber = itemView.findViewById(R.id.machine_serial_no);
            location = itemView.findViewById(R.id.machine_location);
            type = itemView.findViewById(R.id.machine_type);
            workingStatus = itemView.findViewById(R.id.machine_working_text);
            cardView = itemView.findViewById(R.id.cardview);
        }

        public void bind(Machine model)
        {
            machineId.setText(model.getMachineId());
            location.setText(model.getDepartment());
            type.setText(model.getType());
            serialNumber.setText(model.getSerialNumber());

            if(!model.isWorking())
            {
                workingStatus.setBackgroundResource(R.drawable.not_working_background);
            }
        }
    }
}
