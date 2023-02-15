package android.reedbyford.studentscheduler.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.reedbyford.studentscheduler.R;
import android.reedbyford.studentscheduler.database.Repository;
import android.reedbyford.studentscheduler.entities.Term;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TermDetails extends AppCompatActivity {
    EditText editTitle;
    EditText editStartDate;
    EditText editEndDate;
    String title;
    String startDate;
    String endDate;
    int id;
    Term term;
    Repository repository;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_details);
        editTitle = findViewById(R.id.termtitle);
        editStartDate = findViewById(R.id.termstartdate);
        editEndDate = findViewById(R.id.termenddate);
        id=getIntent().getIntExtra("id", -1);
        title = getIntent().getStringExtra("title");
        startDate = getIntent().getStringExtra("startDate");
        endDate = getIntent().getStringExtra("endDate");
        editTitle.setText(title);
        editStartDate.setText(startDate);
        editEndDate.setText(endDate);
        repository = new Repository(getApplication());
        RecyclerView recyclerView = findViewById(R.id.courserecyclerview);
        repository = new Repository(getApplication());
        final CourseAdapter courseAdapter = new CourseAdapter(this);
        recyclerView.setAdapter(courseAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Button button = findViewById(R.id.saveterm);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(id == -1){
                    term = new Term(0,editTitle.getText().toString(),editStartDate.getText().toString(),editEndDate.getText().toString());
                    repository.insert(term);
                } else {
                    term = new Term(id,editTitle.getText().toString(),editStartDate.getText().toString(),editEndDate.getText().toString());
                    repository.update(term);
                }
            }
        });
        FloatingActionButton fab = findViewById(R.id.floatingActionButton2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TermDetails.this, CourseDetails.class);
                startActivity(intent);
            }
        });
    }
}