package com.example.adminapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.adminapp.R;
import com.example.adminapp.models.Machine;
import com.example.adminapp.models.Manager;
import com.firebase.ui.database.paging.DatabasePagingOptions;
import com.firebase.ui.database.paging.FirebaseRecyclerPagingAdapter;
import com.firebase.ui.database.paging.LoadingState;

import java.util.ArrayList;
import java.util.List;

public class StockRecordAdpater extends RecyclerView.Adapter<StockRecordAdpater.MyMachinesHolder> implements Filterable {

    private List<Machine> machineList;
    private List<Machine> machineListFull;

    Context c;
    private final int[] mColors = {R.color.list_color_2,R.color.list_color_3,R.color.list_color_4,R.color.list_color_5,
            R.color.list_color_6,R.color.list_color_7,R.color.list_color_8,R.color.list_color_9,R.color.list_color_10,R.color.list_color_11};

    public StockRecordAdpater(List<Machine>machineList , Context c) {
       this.machineList = machineList;
        this.c = c;
        machineListFull = new ArrayList<>(machineList);
    }




    @NonNull
    @Override
    public MyMachinesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stock_record_item,null);
        return new MyMachinesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyMachinesHolder holder, int position) {
        int bgColor = ContextCompat.getColor(c, mColors[position % 10]);
        holder.cardView.setCardBackgroundColor(bgColor);
        if(position % 2 ==0){
            holder.workingStatus.setBackgroundResource(R.drawable.not_working_background);
        }
        holder.bind(machineList.get(position));
    }

    @Override
    public int getItemCount() {
        return machineList.size();
    }
    public void setMachine(List<Machine> machines){
        this.machineList = machines;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }


    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            List<Machine>filteredList = new ArrayList<Machine>();

            if(constraint==null||constraint.length()==0)
            {
                filteredList.addAll(machineListFull);
            }
            else
            {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(Machine m : machineListFull)
                {
                    if(m.getType().toLowerCase().contains(filterPattern))
                        filteredList.add(m);
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            machineList.clear();
            machineList.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };

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
        }
    }
}
