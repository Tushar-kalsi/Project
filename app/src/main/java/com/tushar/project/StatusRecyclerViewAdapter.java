package com.tushar.project;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tushar.project.databinding.ItemStatusViewBinding;

import java.util.List;
import java.util.function.LongFunction;


public class StatusRecyclerViewAdapter extends RecyclerView.Adapter<StatusViewHolder> {



    List<StatusModel> data;
    Context context;

    public StatusRecyclerViewAdapter(List<StatusModel> data, Context context){
        this.context=context;
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


        }else{
            holder.binding.courseworkPendingButton.setText("Pending");
            holder.binding.courseworkPendingButton.setBackgroundColor(context.getResources().getColor(R.color.yellow));

        }
        if(statusModel.getRac()>0){

            holder.binding.racPendingButton.setText("Completed");
            holder.binding.racPendingButton.setBackgroundColor(Color.GREEN);

        }else {

            holder.binding.racPendingButton.setText("Pending");
            holder.binding.racPendingButton.setBackgroundColor(context.getResources().getColor(R.color.yellow));



        }

        if(statusModel.getPublication()>0){
            holder.binding.ThesisSubmissionButton.setText("Completed");
            holder.binding.ThesisSubmissionButton.setBackgroundColor(Color.GREEN);

        }
        else{
            holder.binding.ThesisSubmissionButton.setText("Pending");
            holder.binding.ThesisSubmissionButton.setBackgroundColor(context.getResources().getColor(R.color.yellow));

        }
        if(statusModel.getMarksheet()>0){

            holder.binding.marksheetButton.setText("Completed");
            holder.binding.marksheetButton.setBackgroundColor(Color.GREEN);

        }
        else{
            holder.binding.marksheetButton.setText("Pending");
            holder.binding.marksheetButton.setBackgroundColor(context.getResources().getColor(R.color.yellow));

        }
        if(statusModel.getRdc()>0){

            holder.binding.rdcButton.setText("Completed");
            holder.binding.rdcButton.setBackgroundColor(Color.GREEN);

        }
        else{
            holder.binding.rdcButton.setText("Pending");
            holder.binding.rdcButton.setBackgroundColor(context.getResources().getColor(R.color.yellow));

        }


        if(statusModel.getSynopsis()>0){

            holder.binding.synopsisButton.setText("Completed");
            holder.binding.synopsisButton.setBackgroundColor(Color.GREEN);

        }
        else{
            holder.binding.synopsisButton.setText("Pending");
            holder.binding.synopsisButton.setBackgroundColor(context.getResources().getColor(R.color.yellow));

        }

        if(statusModel.getPredefenceLetter()>0){

            holder.binding.PreDefenceLetterUplaodButton.setText("Completed");
            holder.binding.PreDefenceLetterUplaodButton.setBackgroundColor(Color.GREEN);

        }
        else{
            holder.binding.PreDefenceLetterUplaodButton.setText("Pending");
            holder.binding.PreDefenceLetterUplaodButton.setBackgroundColor(context.getResources().getColor(R.color.yellow));

        }

        if(statusModel.getThesisSubmission()>0){

            holder.binding.thesisSubmissionButton.setText("Completed");
            holder.binding.thesisSubmissionButton.setBackgroundColor(Color.GREEN);

        }
        else{
            holder.binding.thesisSubmissionButton.setText("Pending");
            holder.binding.thesisSubmissionButton.setBackgroundColor(context.getResources().getColor(R.color.yellow));

        }
        if(statusModel.getThesisAwarded()>0){

            holder.binding.thesisAwardedButton.setText("Completed");
            holder.binding.thesisAwardedButton.setBackgroundColor(Color.GREEN);


        }
        else{
            holder.binding.thesisAwardedButton.setText("Pending");
            holder.binding.thesisAwardedButton.setBackgroundColor(context.getResources().getColor(R.color.yellow));

        }
        holder.binding.nameInput.setText(statusModel.getName());
        holder.binding.enrollmentNumberInput.setText(statusModel.getEnrollment_number());
        Log.d("valueTu", statusModel.getTitle() +" akcx ");

        holder.binding.titleInput.setText(statusModel.getTitle()+" ");
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