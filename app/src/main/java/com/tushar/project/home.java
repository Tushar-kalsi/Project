package com.tushar.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
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

    public static final int HOD_VIEW=3;

    public static final int TEACHER_VIEW=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding= DataBindingUtil.setContentView(this , R.layout.activity_home);
        preferences= PreferenceManager.getDefaultSharedPreferences(this);
        String name =preferences.getString("name", "");
        view =preferences.getInt("type", 1);
        binding.helloChampText.setText("Hello, "+name);
        registrationDialog = new RegistrationDialog(this, (RegistrationSelection) this);

        binding.layoutDrawable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                logoutFunction();

            }
        });
        if(view==TEACHER_VIEW){

            performTeacherView();


        }else if (view ==HOD_VIEW){

            performHodView();

        }else {



            thesisSelectionDialog = new ThesisDialog(home.this, (ThesisSelection) this);

            id = preferences.getInt("id", 1);

            binding.titleSubmission.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(home.this , TitleSubmission.class));

                }
            });

            binding.documentUpload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(home.this , StudentDocumentUplaod.class));
                }
            });
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



              if(view==TEACHER_VIEW || view==HOD_VIEW){
                  Intent intent=new Intent(this ,StudentListAcitvity.class);
                  intent.putExtra("student" , 1);
                  startActivity(intent);

              }else{

                  startActivity(new Intent(this , QIP_registration.class));

              }

        }else{

            Log.d("type", view+" here ");
            if(view==1){
                startActivity(new Intent(this, NONQIP_registration.class));

            }
            else if(view==TEACHER_VIEW || HOD_VIEW==3){
                Log.d("type", view+" here  inside student ");
                Intent intent=new Intent(this ,StudentListAcitvity.class);
                intent.putExtra("student" , 2);
                startActivity(intent);

            }

        }
    }

    @Override
    public void selectedOption(int type) {


        if(view==3){

            if(type==1){


                Intent intent=new Intent(this , StudentListAcitvity.class );

                intent.putExtra("student",7);

                startActivity(intent);



            }else{


                Intent intent=new Intent(home.this, StudentListAcitvity.class);
                intent.putExtra("student",3);

                startActivity(intent);



            }

        }else{

            thesisSelectionDialog.endDialog();

            if (type == 1) {

                startActivity(new Intent(home.this, ThesisSubmission.class));

            } else {

                startActivity(new Intent(home.this, ViewPublication.class));


            }

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
        binding.documentUpload.setVisibility(View.GONE);
        binding.titleSubmission.setVisibility(View.GONE);

        binding.statusCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(home.this , StatusActivity.class));



            }
        });

        binding.statusCardView.setVisibility(View.VISIBLE);

        binding.statusCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(home.this , StatusActivity.class));

            }
        });


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
                intent.putExtra("student",3 );
                startActivity(intent);


            }
        });

        binding.courseWorkCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(home.this , StudentListAcitvity.class);
                intent.putExtra("student",StudentModel.COURSEWORK);
                startActivity(intent);


            }
        });

        binding.thesis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(home.this , StudentListAcitvity.class);
                intent.putExtra("student",StudentModel.PUBLICARION);
                startActivity(intent);


            }
        });




    }
    public void performHodView(){

        binding.ViewDocumentsCardView.setVisibility(View.VISIBLE);
        binding.ViewDocumentsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(home.this , StudentListAcitvity.class);
                intent.putExtra("student",StudentListAcitvity.VIEWDOCUMENTS );

                startActivity(intent);

            }
        });
        binding.documentUpload.setVisibility(View.GONE);
        binding.titleSubmission.setVisibility(View.GONE);


        binding.vaerifyDocumentCard.setVisibility(View.VISIBLE);
        Hod_RAC_dialog hod_rac_dialog=new Hod_RAC_dialog(this , (ThesisSelection) this);

        binding.statusCardView.setVisibility(View.VISIBLE);
        binding.statusCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(home.this , StatusActivity.class));

            }
        });

        binding.vaerifyDocumentCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(home.this , HOD_racVerifyActivity.class));

            }
        });
        binding.RACUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                hod_rac_dialog.startDialog();

            }
        });

        binding.textStudentProfileText.setText("Student \nProfile");
        binding.textStudentProfileText.setGravity(View.TEXT_ALIGNMENT_CENTER);

        binding.registrationCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                registrationDialog.startDialog();

            }
        });

        binding.courseWorkCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("kacb", "im a clicked");
                Intent intent=new Intent(home.this , StudentListAcitvity.class);
                intent.putExtra("student",StudentModel.COURSEWORK);
                startActivity(intent);


            }
        });

        binding.thesis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(home.this , StudentListAcitvity.class);
                intent.putExtra("student",StudentModel.PUBLICARION);
                startActivity(intent);


            }
        });



    }
}