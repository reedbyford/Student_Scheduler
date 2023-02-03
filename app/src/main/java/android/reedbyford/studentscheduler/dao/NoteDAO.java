package android.reedbyford.studentscheduler.dao;

import android.reedbyford.studentscheduler.entities.Note;
import android.reedbyford.studentscheduler.entities.Term;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NoteDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Note note);
    @Update
    void update(Note note);
    @Delete
    void delete(Note note);
    @Query("SELECT * FROM notes ORDER BY noteID ASC")
    List<Note> getAllNotes();
}
