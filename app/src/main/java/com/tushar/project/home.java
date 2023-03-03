package com.tushar.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.tushar.project.databinding.ActivityHomeBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class home extends AppCompatActivity  implements RegistrationSelection , ThesisSelection, PastRACDialog.MoveToRacModelListener{

    SharedPreferences preferences ;
    int id ;

    ActivityHomeBinding binding;
    RegistrationDialog registrationDialog;
    CustomDialog customDialog;
    List<RacDataModel> racDataModelList;

    ThesisDialog thesisSelectionDialog ;

    int view;

    public static final int HOD_VIEW=3;

    public static final int TEACHER_VIEW=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        customDialog=new CustomDialog(this ,"Custom dialog.... " );


        binding=  DataBindingUtil.setContentView(this , R.layout.activity_home);
        preferences= PreferenceManager.getDefaultSharedPreferences(this);
        String name =preferences.getString("name", "");
        view =preferences.getInt("type", 1);
        binding.helloChampText.setText("Hello, "+name);
        registrationDialog = new RegistrationDialog(this, (RegistrationSelection) this);

        racDataModelList=new ArrayList<>();

        binding.layoutDrawable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                logoutFunction();

            }
        });
        if(view==TEACHER_VIEW){

            performTeacherView();
            binding.nextRacDate.setVisibility(View.GONE);

        }else if (view ==HOD_VIEW){

            performHodView();
            binding.nextRacDate.setVisibility(View.GONE);

        }else {

            String en=preferences.getString("enrollment_number", "");

            makeRacCall(en);

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

                    loadDialogForRac();


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

    private void loadDialogForRac() {


        PastRACDialog pastRACDialog=new PastRACDialog(this ,"Loading",  (PastRACDialog.MoveToRacModelListener) this, racDataModelList, true);
        pastRACDialog.startDialog();

    }

    private void makeRacCall(String enNumnber) {

        String url =getString(R.string.domain_url)+"rac?en="+enNumnber;
        Log.d("errorVolley", url);
        customDialog.startDialog();


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.

                        try {
                            JSONObject myJsonObject = new JSONObject(response);

                            boolean success= myJsonObject.optBoolean("success");
                            JSONArray data =myJsonObject.optJSONArray("results1");

                            if(success){


                                for(int i=0;i<data.length();i++){
                                    RacDataModel racDataModel=new RacDataModel();


                                    JSONObject jsonObject=data.getJSONObject(i);

                                    String name= (jsonObject.optString("name"));
                                    String enrollment_number=(jsonObject.optString("EN"));
                                    String dorDate = (jsonObject.optString("DOR"));
                                    String batch=(jsonObject.optString("Batch"));
                                    String supervisor=jsonObject.optString("SuperVisor");
                                    String coSuperVisor=jsonObject.optString("CoSupervisor");
                                    String documentLink=jsonObject.optString("Document");
                                    int hodDoc=jsonObject.optInt("HodDocumentInserted");
                                    String hodDocUrl=jsonObject.optString("HODDocument");
                                    String departmentName =jsonObject.optString("DepartmentName");

                                    racDataModel.setBatch(batch);
                                    racDataModel.setName(name);
                                    racDataModel.setEnrollment_number(enrollment_number);
                                    racDataModel.setDorDate(dorDate);
                                    racDataModel.setSupervisor(supervisor);
                                    racDataModel.setCoSuperVisor(coSuperVisor);
                                    racDataModel.setDocumentLink(documentLink);
                                    racDataModel.setHodDoc(hodDoc);
                                    racDataModel.setHodDocUrl(hodDocUrl);
                                    racDataModel.setDepartmentName(departmentName);

                                    racDataModelList.add(racDataModel);

                                }


                                JSONObject jsonObject= (JSONObject) data.getJSONObject(data.length()-1);
                                String dorLast= jsonObject.optString("DOR");

                                Log.d("errorWq", "insisder here " );
                                calculcateNextRacDate(dorLast);



                            }

                        }
                        catch (Exception e){

                            Log.d("errorVolley", "Inside exeption "+e.toString());
                        }

                        customDialog.endDialog();


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(home.this , "There is some error ", Toast.LENGTH_LONG).show();

                customDialog.endDialog();


            }
        });

// Add the request to the RequestQueue.
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void calculcateNextRacDate(String dorLast) {





        try{

            Log.d("errorWq", "date is"+dorLast);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                Instant s = Instant.parse(dorLast);
                ZoneId.of("Asia/Kolkata");
                LocalDateTime l = LocalDateTime.ofInstant(s, ZoneId.of("Asia/Kolkata")).plusMonths(6);

                String date =l.getDayOfMonth()+"/"+l.getMonthValue()+"/"+l.getYear();
                Log.d("errorWq", "date is"+date);
                binding.nextRacDate.setText("Your next Rac date: "+ date);




            }







        }catch (Exception e){

            Log.d("errorWq", "excetion found" +e.toString());


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

        binding.ViewDocumentsCardView.setVisibility(View.VISIBLE);

        binding.ViewDocumentsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(home.this , StudentListAcitvity.class);
                intent.putExtra("student",StudentListAcitvity.VIEWDOCUMENTS );

                startActivity(intent);

            }
        });
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

    @Override
    public void moveToRacModel(RacDataModel racDataModel) {

        Intent intent=new Intent(this , RACActivity.class);
        Gson gson = new Gson();


        if(racDataModel!=null) {
            String json = gson.toJson(racDataModel);
            intent.putExtra(RACActivity.KEY_RAC_STUDENT_FORM, json);

        }
        startActivity(intent);

        finish();
    }
}