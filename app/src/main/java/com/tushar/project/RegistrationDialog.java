package com.tushar.project;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.tushar.project.databinding.RegistraionSelectionBinding;

public class RegistrationDialog {
    private Activity activity;
    private Dialog dialog;
    private RegistrationSelection registrationSelection;

    RegistraionSelectionBinding binding;


    public RegistrationDialog(Activity myActivity, RegistrationSelection registrationSelection){
        activity = myActivity;
        this.registrationSelection=registrationSelection;

    }
    public void startDialog( ){


        AlertDialog.Builder builder = new AlertDialog.Builder(activity);


         binding = DataBindingUtil.inflate(LayoutInflater.from(builder.getContext()), R.layout. registraion_selection, null, false);
         builder.setView(binding.getRoot());



        binding.nonQIPstudents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrationSelection.registrationSelection(2);
                endDialog();
            }
        });

        binding.qipcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrationSelection.registrationSelection(1);
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
