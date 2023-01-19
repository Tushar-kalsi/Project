package com.tushar.project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tushar.project.databinding.ItemStudentBinding;

import java.util.List;

public class AdapterRecyclerview extends RecyclerView.Adapter<ReyclerViewViewHolder> {

    List<StudentModel> studentLists;
    RecyclerViewButtonClickListener recyclerViewButtonClickListener;

    public AdapterRecyclerview(List<StudentModel> studentModelList, RecyclerViewButtonClickListener recyclerViewButtonClickListener){
        this.studentLists=studentModelList;
        this.recyclerViewButtonClickListener=recyclerViewButtonClickListener;

    }
    @NonNull
    @Override
    public ReyclerViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        return new ReyclerViewViewHolder(ItemStudentBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false), recyclerViewButtonClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ReyclerViewViewHolder holder, int position) {

        holder.setStudentModel(studentLists.get(position));

        holder.binding.enrollementNumberText.setText("En. No :"+studentLists.get(position).getEN());

        if(studentLists.get(position).getType()==StudentModel.RACMODEL){

            holder.binding.titleNameText.setText("Name :"+studentLists.get(position).getFullName());

        }else{
            holder.binding.titleNameText.setText("Name :"+studentLists.get(position).getFirstName()+" "+studentLists.get(position).getLastName());

        }

    }

    @Override
    public int getItemCount() {
        return studentLists.size();
    }


}

class ReyclerViewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    ItemStudentBinding binding;
    RecyclerViewButtonClickListener recyclerViewButtonClickListener;

    StudentModel associatedStudentModel;

    public ReyclerViewViewHolder(@NonNull ItemStudentBinding itemView, RecyclerViewButtonClickListener recyclerViewButtonClickListener ) {
        super(itemView.getRoot());
        binding=itemView;
        this.recyclerViewButtonClickListener=recyclerViewButtonClickListener;
        binding.viewMoreButton.setOnClickListener(this);

    }

    public void setStudentModel(StudentModel studentModel){
        this.associatedStudentModel=studentModel;

    }
    @Override
    public void onClick(View view) {

        recyclerViewButtonClickListener.onClick(associatedStudentModel);

    }
}
