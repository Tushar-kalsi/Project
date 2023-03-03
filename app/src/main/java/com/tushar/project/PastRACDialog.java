package com.tushar.project;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.security.GuardedObject;
import java.util.List;

public class PastRACDialog implements RacListAdapater.SeeMoreClickListener {

    interface MoveToRacModelListener{
        void moveToRacModel(RacDataModel racDataModel);

    }
    MoveToRacModelListener listener;
    private Activity activity;
    private Dialog dialog;
    String text;
    RecyclerView recyclerView;
    List<RacDataModel> dataModelList;
    FloatingActionButton button;
    boolean showAddOption;
    TextView nextRactextView;




    public PastRACDialog(Activity myActivity, String text, MoveToRacModelListener listener, List<RacDataModel> dataModelList, boolean showAddding){
        activity = myActivity;
        this.listener=listener;
        this.text=text;
        this.dataModelList=dataModelList;
        showAddOption=showAddding;


    }

    public void setNextRacDate(String date){

        nextRactextView.setText("Next RAC Date : "+date);

    }

    public void startDialog(){

        if(dialog!=null && dialog.isShowing()){
            return ;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View view=inflater.inflate(R.layout.past_rac_dialog,null);
        recyclerView= view.findViewById(R.id.recyclerViewRAC);
        button=view.findViewById(R.id.fab);
        nextRactextView=view.findViewById(R.id.nextRacDateText);

        RacListAdapater listAdapater=new RacListAdapater(dataModelList, this );

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setAdapter(listAdapater);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                listener.moveToRacModel(null);
            }
        });
        if(!showAddOption) {

            button.setVisibility(View.GONE);


        }else{
            nextRactextView.setVisibility(View.GONE);

        }
        builder.setView(view);



        dialog = builder.create();
        dialog.show();
    }
    public void endDialog() {

        if(dialog.isShowing())
            dialog.dismiss();
    }


    @Override
    public void onSeeMoreClicked(RacDataModel racDataModel) {
        listener.moveToRacModel(racDataModel);
    }
}
