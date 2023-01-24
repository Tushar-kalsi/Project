package com.tushar.project;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tushar.project.databinding.ItemStatusViewBinding;

import java.util.List;


public class StatusRecyclerViewAdapter extends RecyclerView.Adapter<StatusViewHolder> {



    List<StatusModel> data;

    public StatusRecyclerViewAdapter(List<StatusModel> data){

        this.data=data;
    }
    @NonNull
    @Override
    public StatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new StatusViewHolder(ItemStatusViewBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull StatusViewHolder holder, int position) {

        StatusModel statusModel=data.get(position);
        if(statusModel.getCoursework()>=4){
            holder.binding.courseworkPendingButton.setText("Completed");
            holder.binding.courseworkPendingButton.setBackgroundColor(Color.GREEN);


        }
        if(statusModel.getRac()>0){

            holder.binding.racPendingButton.setText("Completed");
            holder.binding.racPendingButton.setBackgroundColor(Color.GREEN);

        }

        if(statusModel.getPublication()>0){
            holder.binding.thesisSubmission.setText("Completed");
            holder.binding.thesisSubmission.setBackgroundColor(Color.GREEN);


        }
        holder.binding.nameInput.setText(statusModel.getName());
        holder.binding.enrollmentNumberInput.setText(statusModel.getEnrollment_number());

    }

    @Override
    public int getItemCount() {
        return data.size();

    }
}

class StatusViewHolder extends RecyclerView.ViewHolder  {

    ItemStatusViewBinding binding;

    public StatusViewHolder(@NonNull  ItemStatusViewBinding itemView) {
        super(itemView.getRoot());
        this.binding=itemView;


    }


}