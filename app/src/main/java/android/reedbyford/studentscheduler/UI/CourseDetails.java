package android.reedbyford.studentscheduler.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.reedbyford.studentscheduler.R;
import android.reedbyford.studentscheduler.database.Repository;
import android.reedbyford.studentscheduler.entities.Course;
import android.reedbyford.studentscheduler.entities.Term;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CourseDetails extends AppCompatActivity {
    EditText editTitle;
    EditText editStartDate;
    EditText editEndDate;
    EditText editStatus;
    EditText editNote;
    DatePickerDialog.OnDateSetListener datePickerStartDate;
    final Calendar myCalendarStart = Calendar.getInstance();
    String title;
    String startDate;
    String endDate;
    String status;
    int courseId;
    int termId;
    Course course;
    Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);
        editTitle = findViewById(R.id.coursetitle);
        editStartDate = findViewById(R.id.coursestartdate);
        editEndDate = findViewById(R.id.courseenddate);
        editStatus = findViewById(R.id.coursestatus);
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editStartDate.setText(sdf.format(new Date()));
        courseId = getIntent().getIntExtra("id", -1);
        title = getIntent().getStringExtra("title");
        startDate = getIntent().getStringExtra("startDate");
        endDate = getIntent().getStringExtra("endDate");
        status = getIntent().getStringExtra("status");
        termId = getIntent().getIntExtra("termId", -1);
        editTitle.setText(title);
        repository = new Repository(getApplication());
        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<Term> termArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, repository.getAllTerms());
        spinner.setAdapter(termArrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                editNote.setText(termArrayAdapter.getItem(i).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                editNote.setText("Nothing Selected");
            }
        });
        Button button = findViewById(R.id.savecourse);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (courseId == -1) {
                    course = new Course(0, editTitle.getText().toString(), editStartDate.getText().toString(), editEndDate.getText().toString(), editStatus.getText().toString());
                    repository.insert(course);
                } else {
                    course = new Course(courseId, editTitle.getText().toString(), editStartDate.getText().toString(), editEndDate.getText().toString(), editStatus.getText().toString());
                    repository.update(course);
                }
            }
        });
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
                new DatePickerDialog(CourseDetails.this, datePickerStartDate, myCalendarStart
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
        getMenuInflater().inflate(R.menu.menu_coursedetails, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, editNote.getText().toString());
                sendIntent.putExtra(Intent.EXTRA_TITLE, "Message Title");
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser();
                startActivity(shareIntent);
                return true;
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
                Long trigger = myDate.getTime();
                Intent intent = new Intent(CourseDetails.this, MyReceiver.class);
                intent.putExtra("key", dateFromScreen + " should trigger");
                PendingIntent sender = PendingIntent.getBroadcast(CourseDetails.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
                return true;
            case R.id.notifyend:
                // will need more code
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}