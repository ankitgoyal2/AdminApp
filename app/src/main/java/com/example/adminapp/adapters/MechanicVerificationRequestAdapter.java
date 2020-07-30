package com.example.adminapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class MechanicVerificationRequestAdapter extends FirebaseRecyclerPagingAdapter<Mechanic, MechanicVerificationRequestAdapter.RequestHolder> {
    /**
     * Construct a new FirestorePagingAdapter from the given {@link DatabasePagingOptions}.
     *
     * @param options
     */
    private final int[] mColors = {R.color.list_color_1, R.color.list_color_2, R.color.list_color_3, R.color.list_color_4, R.color.list_color_5,
            R.color.list_color_6, R.color.list_color_7, R.color.list_color_8, R.color.list_color_9, R.color.list_color_10, R.color.list_color_11};

    Context c;
    public MechanicVerificationRequestAdapter(@NonNull DatabasePagingOptions<Mechanic> options, Context c) {
        super(options);
        this.c = c;
    }

    @Override
    protected void onBindViewHolder(@NonNull MechanicVerificationRequestAdapter.RequestHolder viewHolder, int position, @NonNull Mechanic model) {
        int bgColor = ContextCompat.getColor(c, mColors[position % 12]);
        viewHolder.cardView.setCardBackgroundColor(bgColor);
        viewHolder.bind(model);
    }

    @Override
    protected void onLoadingStateChanged(@NonNull LoadingState state) {

    }

    @NonNull
    @Override
    public MechanicVerificationRequestAdapter.RequestHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.verify_mechanic_item, parent, false);
        return new MechanicVerificationRequestAdapter.RequestHolder(view);
    }

    public class RequestHolder extends RecyclerView.ViewHolder {

        TextView name, phoneNumber, email, address, empId, department, designation;
        CardView cardView;
        Button accept, decline;

        public RequestHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name_text);
            phoneNumber = itemView.findViewById(R.id.phone_text);
            email = itemView.findViewById(R.id.email_text);
            address = itemView.findViewById(R.id.address_text);
            empId = itemView.findViewById(R.id.employee_id_text);
            department = itemView.findViewById(R.id.department_text);
            designation = itemView.findViewById(R.id.designation_text);
            cardView = itemView.findViewById(R.id.cardview);
            accept = itemView.findViewById(R.id.accept_button);
            decline = itemView.findViewById(R.id.decline_button);

        }

        public void bind(Mechanic mechanic)
        {
            name.setText(mechanic.getUserName());
//            phoneNumber.setText(mechanic.getPhoneNumber());
            email.setText(mechanic.getEmail());
//            address.setText(mechanic.getSavedAddress());
//            empId.setText(mechanic.getEmpId());
//            department.setText(mechanic.getDepartment());
//            designation.setText(mechanic.getDesignation());
        }
    }
}
