package android.reedbyford.studentscheduler.UI;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.reedbyford.studentscheduler.R;
import android.reedbyford.studentscheduler.database.Repository;
import android.reedbyford.studentscheduler.entities.Assessment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AssessmentDetails extends AppCompatActivity {
    EditText editType;
    EditText editTitle;
    EditText editStartDate;
    EditText editEndDate;
    EditText editNotes;
    DatePickerDialog.OnDateSetListener datePickerStartDate;
    final Calendar myCalendarStart = Calendar.getInstance();
    String type;
    String title;
    String startDate;
    String endDate;
    int assessmentId;
    int courseId;
    Assessment assessment;
    Assessment currentAssessment;
    Repository repository;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_details);
        editType = findViewById(R.id.assessmenttype);
        editTitle = findViewById(R.id.assessmenttitle);
        editStartDate = findViewById(R.id.assessmentstartdate);
        editEndDate = findViewById(R.id.assessmentenddate);
        editNotes = findViewById(R.id.editnotes);
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editStartDate.setText(sdf.format(new Date()));
        assessmentId = getIntent().getIntExtra("id", -1);
        type = getIntent().getStringExtra("type");
        title = getIntent().getStringExtra("title");
        startDate = getIntent().getStringExtra("startDate");
        endDate = getIntent().getStringExtra("endDate");
        courseId = getIntent().getIntExtra("termId", -1);
        editType.setText(type);
        editTitle.setText(title);
        editStartDate.setText(startDate);
        editEndDate.setText(endDate);
        repository = new Repository(getApplication());
        /*Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<Course> courseArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, repository.getAllCourses());
        spinner.setAdapter(courseArrayAdapter);*/
        Button button = findViewById(R.id.savecourse);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (assessmentId == -1) {
                    assessment = new Assessment(0, editType.getText().toString(), editTitle.getText().toString(), editStartDate.getText().toString(), editEndDate.getText().toString(), 1);
                    repository.insert(assessment);
                } else {
                    assessment = new Assessment(assessmentId, editType.getText().toString(), editTitle.getText().toString(), editStartDate.getText().toString(), editEndDate.getText().toString(), 1);
                    repository.update(assessment);
                }
            }
        });

        /*spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                editNotes.setText(courseArrayAdapter.getItem(i).toString());
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                editNotes.setText("Nothing Selected");
            }
        });*/
        editStartDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Date date;
                //get value from other screen,but I'm going to hard code it right now
                String info = editStartDate.getText().toString();
                if (info.equals("")) info = "02/10/22";
                try {
                    myCalendarStart.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(AssessmentDetails.this, datePickerStartDate, myCalendarStart
                        .get(Calendar.YEAR), myCalendarStart.get(Calendar.MONTH),
                        myCalendarStart.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        startDate = String.valueOf(new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub

                myCalendarStart.set(Calendar.YEAR, year);
                myCalendarStart.set(Calendar.MONTH, monthOfYear);
                myCalendarStart.set(Calendar.DAY_OF_MONTH, dayOfMonth);


                updateLabelStart();
            }

        });

    }

    private void updateLabelStart() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editStartDate.setText(sdf.format(myCalendarStart.getTime()));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_assessmentdetails, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;

                // Delete Assessment

            case R.id.delete:
                for (Assessment assessment : repository.getAllAssessments()) {
                    if (assessment.getAssessmentId() == assessmentId) {
                        currentAssessment = assessment;
                        repository.delete(currentAssessment);
                        Toast.makeText(AssessmentDetails.this, currentAssessment.getTitle() + " was deleted", Toast.LENGTH_LONG).show();
                    }
                }
                return true;

                // Alerts

            case R.id.notifystart:
                String dateFromScreen = editStartDate.getText().toString();
                String myFormat = "MM/dd/yy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                Date myDate = null;
                try {
                    myDate = sdf.parse(dateFromScreen);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Long trigger = myDate != null ? myDate.getTime() : 0;
                Intent intent = new Intent(AssessmentDetails.this, MyReceiver.class);
                intent.putExtra("key", dateFromScreen + " should trigger");
                PendingIntent sender = PendingIntent.getBroadcast(AssessmentDetails.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
                return true;
            case R.id.notifyend:
                String dateFromScreen2 = editEndDate.getText().toString();
                String myFormat2 = "MM/dd/yy";
                SimpleDateFormat sdf2 = new SimpleDateFormat(myFormat2, Locale.US);
                Date myDate2 = null;
                try {
                    myDate = sdf2.parse(dateFromScreen2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Long trigger2 = myDate2 != null ? myDate2.getTime() : 0;
                Intent intent2 = new Intent(AssessmentDetails.this, MyReceiver.class);
                intent2.putExtra("key", dateFromScreen2 + " should trigger");
                PendingIntent sender2 = PendingIntent.getBroadcast(AssessmentDetails.this, ++MainActivity.numAlert, intent2, PendingIntent.FLAG_IMMUTABLE);
                AlarmManager alarmManager2 = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager2.set(AlarmManager.RTC_WAKEUP, trigger2, sender2);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
