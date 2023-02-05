package com.tushar.project;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class CustomDialog {
    private Activity activity;
    private Dialog dialog;
    String text;
    TextView textView;


    public CustomDialog(Activity myActivity, String text){
        activity = myActivity;
        this.text=text;

    }
    public static CustomDialog instance;

    public static CustomDialog getInstance(Activity activity, String text){
        if(instance==null){
            return new CustomDialog(activity, text);

        }

        return instance;
    }
    public void changeText(String text){
        this.text=text;

        if(textView!=null){
            textView.setText(text);

        }
    }
    public void startDialog(){

        if(dialog!=null && dialog.isShowing()){
            return ;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View view=inflater.inflate(R.layout.custom_dialog,null);
        textView= view.findViewById(R.id.textView2);

        builder.setView(view);
        textView.setText(text);

        builder.setCancelable(false);
        dialog = builder.create();
        dialog.show();
    }
    public void endDialog() {

        if(dialog.isShowing())
            dialog.dismiss();
    }

}
