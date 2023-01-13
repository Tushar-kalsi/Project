package com.tushar.project;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.tushar.project.databinding.RegistraionSelectionBinding;
import com.tushar.project.databinding.ThesisSelectionBinding;

public class ThesisDialog {

    private Activity activity;
    private Dialog dialog;
    private ThesisSelection registrationSelection;

    ThesisSelectionBinding binding;


    public ThesisDialog(Activity myActivity, ThesisSelection registrationSelection){
        activity = myActivity;
        this.registrationSelection=registrationSelection;

    }
    public void startDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);


        binding = DataBindingUtil.inflate(LayoutInflater.from(builder.getContext()), R.layout. thesis_selection, null, false);
        builder.setView(binding.getRoot());


        binding.viewpublication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrationSelection.selectedOption(2);
                endDialog();
            }
        });

        binding.createPublication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrationSelection.selectedOption(1);
                endDialog();
            }
        });

        builder.setCancelable(true);
        dialog = builder.create();
        dialog.show();


    }
    public void endDialog(){
        dialog.dismiss();
    }


}
