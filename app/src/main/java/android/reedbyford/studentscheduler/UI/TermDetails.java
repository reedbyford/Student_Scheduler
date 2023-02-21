package android.reedbyford.studentscheduler.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.reedbyford.studentscheduler.R;
import android.reedbyford.studentscheduler.database.Repository;
import android.reedbyford.studentscheduler.entities.Course;
import android.reedbyford.studentscheduler.entities.Term;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class TermDetails extends AppCompatActivity {
    EditText editTitle;
    EditText editStartDate;
    EditText editEndDate;
    String title;
    String startDate;
    String endDate;
    int termId;
    Term term;
    Term currentTerm;
    int numCourses;
    Repository repository;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_details);
        editTitle = findViewById(R.id.termtitle);
        editStartDate = findViewById(R.id.termstartdate);
        editEndDate = findViewById(R.id.termenddate);
        termId = getIntent().getIntExtra("id", -1);
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
        List<Course> filteredCourses = new ArrayList<>();
        for(Course c : repository.getAllCourses()) {
            if(c.getTermId() == termId) filteredCourses.add(c);
        }
        courseAdapter.setCourses(repository.getAllCourses());

        Button button = findViewById(R.id.saveterm);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(termId == -1){
                    term = new Term(0,editTitle.getText().toString(),editStartDate.getText().toString(),editEndDate.getText().toString());
                    repository.insert(term);
                } else {
                    term = new Term(termId,editTitle.getText().toString(),editStartDate.getText().toString(),editEndDate.getText().toString());
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

    @Override
    protected void onResume() {

        super.onResume();
        RecyclerView recyclerView = findViewById(R.id.courserecyclerview);
        final CourseAdapter courseAdapter = new CourseAdapter(this);
        recyclerView.setAdapter(courseAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Course> filteredCourses = new ArrayList<>();
        for (Course p : repository.getAllCourses()) {
            if (p.getTermId() == termId) filteredCourses.add(p);
        }
        courseAdapter.setCourses(filteredCourses);

        //Toast.makeText(ProductDetails.this,"refresh list",Toast.LENGTH_LONG).show();
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.deleteterm, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.deleteterm:
                for (Term term : repository.getAllTerms()) {
                    if (term.getTermId() == termId) currentTerm = term;
                }

                numCourses = 0;
                for (Course course : repository.getAllCourses()) {
                    if (course.getTermId() == termId) ++numCourses;
                }

                if (numCourses == 0) {
                    repository.delete(currentTerm);
                    Toast.makeText(TermDetails.this, currentTerm.getTitle() + " was deleted", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(TermDetails.this, "Can't delete a term with courses", Toast.LENGTH_LONG).show();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}