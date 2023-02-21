package android.reedbyford.studentscheduler.UI;

import android.reedbyford.studentscheduler.database.Repository;
import android.reedbyford.studentscheduler.entities.Assessment;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class AssessmentDetails extends AppCompatActivity {
    EditText editTitle;
    EditText editStartDate;
    EditText editEndDate;
    String title;
    String startDate;
    String endDate;
    int courseId;
    int termId;
    Assessment assessment;
    Repository repository;


}
