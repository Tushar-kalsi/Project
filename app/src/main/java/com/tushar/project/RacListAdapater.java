package com.tushar.project;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tushar.project.databinding.ItemRacDocumentListBinding;
import com.tushar.project.databinding.ItemStudentBinding;
import com.tushar.project.databinding.PastRacDialogBinding;

import java.util.ArrayList;
import java.util.List;

public class RacListAdapater extends RecyclerView.Adapter<RacListViewHolder> {

    interface SeeMoreClickListener{
        void onSeeMoreClicked(RacDataModel racDataModel);

    }
    SeeMoreClickListener seeMoreClickListener;
    List<RacDataModel> dataModels;

    public RacListAdapater(List<RacDataModel> dataModelList, SeeMoreClickListener seeMoreClickListener){
        this.seeMoreClickListener=seeMoreClickListener;
        this.dataModels=dataModelList;

    }
    @NonNull
    @Override
    public RacListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        return new RacListViewHolder(ItemRacDocumentListBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false),seeMoreClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RacListViewHolder holder, int position) {

        RacDataModel racDataModel=dataModels.get(position);
        holder.setData(racDataModel);
        holder.binding.serialNumberText.setText((position+1)+". ");
        Log.d("errorCar", racDataModel.dorDate);

        holder.binding.dateTextView.setText(racDataModel.dorDate);



    }

    @Override
    public int getItemCount() {
        return dataModels.size();
    }
}

class RacListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    ItemRacDocumentListBinding binding;

    RacDataModel racDataModel;

    RacListAdapater.SeeMoreClickListener seeMoreClickListener;

    public RacListViewHolder(@NonNull ItemRacDocumentListBinding itemView, RacListAdapater.SeeMoreClickListener seeMoreClickListener) {
        super(itemView.getRoot());
        this.binding=itemView;
        this.seeMoreClickListener=seeMoreClickListener;
        binding.seeMoreTextView.setOnClickListener(this);
    }

    public void setData(RacDataModel racDataModel){
        this.racDataModel=racDataModel;
    }

    @Override
    public void onClick(View view) {
        seeMoreClickListener.onSeeMoreClicked(racDataModel);


    }
}
