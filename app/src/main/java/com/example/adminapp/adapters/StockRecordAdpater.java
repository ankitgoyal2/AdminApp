package com.example.adminapp.adapters;

import android.content.Context;
import android.content.Intent;
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

import com.example.adminapp.GetMachineDetailsActivity;
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
        holder.bind(machineList.get(position));
    }

    @Override
    public int getItemCount() {
        return machineList.size();
    }

    public List<Machine> getMachine(){ return machineList;}

    public void setMachine(List<Machine> machines){
        this.machineList = machines;
    }

    public void setMachineAll(){ this.machineList = machineListFull;}

    public List<Machine> getMachineListFull(){ return machineListFull;}
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
                    if(m.getType().toLowerCase().contains(filterPattern)) {
                        filteredList.add(m);
                    }else if(m.getSerialNumber().toLowerCase().contains(filterPattern)){
                        filteredList.add(m);
                    }else if(m.getMachineId().toLowerCase().contains(filterPattern)){
                        filteredList.add(m);
                    }else if(m.getDepartment().toLowerCase().contains(filterPattern)){
                        filteredList.add(m);
                    }

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
        String[] mTitles;

        public MyMachinesHolder(@NonNull View itemView) {
            super(itemView);

            machineId = itemView.findViewById(R.id.machine_id);
            serialNumber = itemView.findViewById(R.id.machine_serial_no);
            location = itemView.findViewById(R.id.machine_location);
            type = itemView.findViewById(R.id.machine_type);
            workingStatus = itemView.findViewById(R.id.machine_working_text);
            cardView = itemView.findViewById(R.id.cardview);
            mTitles = c.getResources().getStringArray(R.array.machine_full_form);
        }

        public void bind(final Machine model)
        {

            machineId.setText(model.getMachineId());
            location.setText(model.getDepartment());
            type.setText(model.getType());

            if(model.getType().toLowerCase().equals("hhmd")){
                type.setText(mTitles[3]);

            }else  if(model.getType().toLowerCase().equals("bdds")){

            type.setText(mTitles[1]);
            }else  if(model.getType().toLowerCase().equals("etd")){

                type.setText(mTitles[0]);
            }else  if(model.getType().toLowerCase().equals("xbis")){

                type.setText(mTitles[2]);
            }else  if(model.getType().toLowerCase().equals("fids")){

                type.setText(mTitles[5]);
            }else if(model.getType().toLowerCase().equals("dfmd")){

                type.setText(mTitles[4]);
            }else{
                type.setText(model.getType());
            }
            serialNumber.setText(model.getSerialNumber());
            if(model.isWorking()){
                workingStatus.setBackgroundResource(R.drawable.working_tv_background);
                workingStatus.setText("WORKING");
            }else{
                workingStatus.setBackgroundResource(R.drawable.not_working_background);
                workingStatus.setText("NOT WORKING");
            }
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(c, GetMachineDetailsActivity.class);
                    i.putExtra("generationCode",model.getMachineId());
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    c.startActivity(i);
                }
            });
        }
    }
}
