package android.reedbyford.studentscheduler.UI;

import android.content.Context;
import android.content.Intent;
import android.reedbyford.studentscheduler.R;
import android.reedbyford.studentscheduler.entities.Course;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {
    class CourseViewHolder extends RecyclerView.ViewHolder {
        private final TextView courseItemView;
        private final TextView courseItemView2;
        private CourseViewHolder(View itemView){
            super(itemView);
            courseItemView = itemView.findViewById(R.id.textViewcoursetitle);
            courseItemView2 = itemView.findViewById(R.id.textViewcoursestartdate);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    final Course current = mCourses.get(position);
                    Intent intent = new Intent(context,CourseDetails.class);
                    intent.putExtra("id", current.getCourseId());
                    intent.putExtra("title", current.getTitle());
                    intent.putExtra("startDate", current.getStartDate());
                    intent.putExtra("endDate", current.getEndDate());
                    intent.putExtra("status",current.getStatus());
                    intent.putExtra("instructorName",current.getInstructorName());
                    intent.putExtra("instructorPhone",current.getInstructorPhone());
                    intent.putExtra("instructorEmail",current.getInstructorEmail());
                    intent.putExtra("termId",current.getTermId());
                    context.startActivity(intent);
                }
            });
        }
    }

    private List<Course> mCourses;
    private final Context context;
    private final LayoutInflater mInflater;
    public CourseAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }
    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.course_list_item,parent,false);
        return new CourseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseAdapter.CourseViewHolder holder, int position) {
        if(mCourses != null){
            Course current = mCourses.get(position);
            String title = current.getTitle();
            String startDate = current.getStartDate();
            holder.courseItemView.setText(title);
            holder.courseItemView2.setText(startDate);
        } else {
            holder.courseItemView.setText("No Course Name");
            holder.courseItemView2.setText("No Start Date");
        }
    }

    public void setCourses(List<Course> courses){
        mCourses = courses;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mCourses.size();
    }
}
