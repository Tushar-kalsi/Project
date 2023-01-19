package com.tushar.project;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tushar.project.databinding.ItemApproveCardBinding;
import com.tushar.project.databinding.ItemStudentBinding;

import java.util.List;

public class HOD_DOC_verifyAdapter extends RecyclerView.Adapter<HodViewCard> {

    List<StudentModel> data;
    HodClickListener clickListener;

    public HOD_DOC_verifyAdapter(List<StudentModel> data, HodClickListener clickListener){
        this.clickListener=clickListener;
        this.data=data;

    }


    @NonNull
    @Override
    public HodViewCard onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new HodViewCard(ItemApproveCardBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false), clickListener);

    }

    @Override
    public void onBindViewHolder(@NonNull HodViewCard holder, int position) {

        if(data.size()==0)
            return ;

        holder.setStudentModel(data.get(position));
        holder.binding.titleNameText.setText("Name : "+data.get(position).getFullName());
        holder.binding.enrollementNumberText.setText("En No. "+data.get(position).getEN());


    }

    @Override
    public int getItemCount() {
        return data.size();
    }



}

class HodViewCard extends RecyclerView.ViewHolder  implements View.OnClickListener {

    ItemApproveCardBinding binding;
    HodClickListener clickListener;
    StudentModel associatedStudentModel;

    public HodViewCard(@NonNull ItemApproveCardBinding itemView, HodClickListener clickListener) {
        super(itemView.getRoot());
        binding=itemView;
        this.clickListener=clickListener;

        binding.denyButton.setOnClickListener(this);
        binding.approveButton.setOnClickListener(this);
        binding.viewMoreButton.setOnClickListener(this);

    }

    public void setStudentModel(StudentModel studentModel){
        this.associatedStudentModel=studentModel;

    }

    @Override
    public void onClick(View view) {

        if(view.getId()==binding.denyButton.getId()){

            clickListener.clicked(HOD_racVerifyActivity.DENY_COMMOND, associatedStudentModel);
        }else if(view.getId()==binding.approveButton.getId()){


            clickListener.clicked(HOD_racVerifyActivity.APPROVE_COMMOND, associatedStudentModel);
        }else{

            clickListener.clicked(HOD_racVerifyActivity.DOCUMENT_COMMOND,associatedStudentModel);

        }

    }
}

