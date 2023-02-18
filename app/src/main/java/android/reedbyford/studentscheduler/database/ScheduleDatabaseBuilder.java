package android.reedbyford.studentscheduler.database;

import android.content.Context;
import android.reedbyford.studentscheduler.dao.AssessmentDAO;
import android.reedbyford.studentscheduler.dao.CourseDAO;
import android.reedbyford.studentscheduler.dao.InstructorDAO;
import android.reedbyford.studentscheduler.dao.NoteDAO;
import android.reedbyford.studentscheduler.dao.TermDAO;
import android.reedbyford.studentscheduler.entities.Assessment;
import android.reedbyford.studentscheduler.entities.Course;
import android.reedbyford.studentscheduler.entities.Instructor;
import android.reedbyford.studentscheduler.entities.Note;
import android.reedbyford.studentscheduler.entities.Term;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Term.class, Course.class, Assessment.class, Instructor.class, Note.class}, version=2, exportSchema = false)
public abstract class ScheduleDatabaseBuilder extends RoomDatabase {
    public abstract TermDAO termDAO();
    public abstract CourseDAO courseDAO();
    public abstract AssessmentDAO assessmentDAO();
    public abstract InstructorDAO instructorDAO();
    public abstract NoteDAO noteDAO();

    private static volatile ScheduleDatabaseBuilder INSTANCE;

    static ScheduleDatabaseBuilder getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (ScheduleDatabaseBuilder.class){
                if(INSTANCE == null){
                    INSTANCE= Room.databaseBuilder(context.getApplicationContext(),ScheduleDatabaseBuilder.class, "MyScheduleDatabase.db")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
