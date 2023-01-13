package com.tushar.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.tushar.project.databinding.ActivityHomeBinding;

public class home extends AppCompatActivity  implements RegistrationSelection , ThesisSelection{

    SharedPreferences preferences ;

    int id ;

    ActivityHomeBinding binding;
    RegistrationDialog registrationDialog;
    ThesisDialog thesisSelectionDialog ;
    int view;
    public static final int TEACHER_VIEW=2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding= DataBindingUtil.setContentView(this , R.layout.activity_home);
        preferences= PreferenceManager.getDefaultSharedPreferences(this);
        String name =preferences.getString("name", "");
        view =preferences.getInt("type", 1);
        binding.salutationText.setText("Hi, "+name+" ji ");
        if(view==TEACHER_VIEW){

            performTeacherView();


        }else {


            registrationDialog = new RegistrationDialog(this, (RegistrationSelection) this);
            thesisSelectionDialog = new ThesisDialog(home.this, (ThesisSelection) this);

            id = preferences.getInt("id", 1);

            binding.courseWorkCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    startActivity(new Intent(home.this, CourseWork.class));


                }
            });

            binding.registrationCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    registrationDialog.startDialog();

                }
            });

            binding.RACUpload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    startActivity(new Intent(home.this, RACActivity.class));

                }
            });

            binding.thesis.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    thesisSelectionDialog.startDialog();

                }
            });


        }
    }

    @Override
    public void registrationSelection(int type) {

        if(type==1 ){



              if(view==TEACHER_VIEW){
                  Intent intent=new Intent(this ,StudentListAcitvity.class);
                  intent.putExtra("student" , 1);
                  startActivity(intent);

              }else{
                  startActivity(new Intent(this , QIP_registration.class));
              }

        }else{

            if(view==TEACHER_VIEW){
                Intent intent=new Intent(this ,StudentListAcitvity.class);
                intent.putExtra("student" , 2);
                startActivity(intent);

            }else {
                startActivity(new Intent(this, NONQIP_registration.class));
            }

        }
    }

    @Override
    public void selectedOption(int type) {

        thesisSelectionDialog.endDialog();

        if(type==1){

            startActivity(new Intent(home.this , ThesisSubmission.class));

        }else{

            startActivity(new Intent(home.this, ViewPublication.class));


        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {

            logoutFunction();

            //TODO: logout
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void logoutFunction(){

        preferences.edit().putBoolean("logged", false).apply();
        startActivity(new Intent(this , MainActivity.class));

        finish();


    }

    private void performTeacherView(){


        binding.textStudentProfileText.setText("Student Profile");

        binding.registrationCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                registrationDialog.startDialog();

            }
        });

        binding.RACUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(home.this , StudentListAcitvity.class);
                intent.putExtra("student", );
                startActivity(intent);


            }
        });





    }
}