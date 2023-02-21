package android.reedbyford.studentscheduler.UI;

import android.content.Context;
import android.content.Intent;
import android.reedbyford.studentscheduler.R;
import android.reedbyford.studentscheduler.entities.Assessment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AssessmentAdapter extends RecyclerView.Adapter<AssessmentAdapter.AssessmentViewHolder>{
    class AssessmentViewHolder extends RecyclerView.ViewHolder {
        private final TextView assessmentItemView;
        private final TextView assessmentItemView2;
        private AssessmentViewHolder(View itemView){
            super(itemView);
            assessmentItemView = itemView.findViewById(R.id.textViewassessmenttype);
            assessmentItemView2 = itemView.findViewById(R.id.textViewassessmenttitle);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    final Assessment current = mAssessments.get(position);
                    Intent intent = new Intent(context,CourseDetails.class);
                    intent.putExtra("id", current.getAssessmentId());
                    intent.putExtra("type", current.getType());
                    intent.putExtra("title", current.getTitle());
                    intent.putExtra("startDate", current.getStartDate());
                    intent.putExtra("endDate", current.getEndDate());
                    intent.putExtra("termId",current.getCourseId());
                    context.startActivity(intent);
                }
            });
        }
    }
    private List<Assessment> mAssessments;
    private final Context context;
    private final LayoutInflater mInflater;
    public AssessmentAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public AssessmentAdapter.AssessmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.assessment_list_item,parent,false);
        return new AssessmentAdapter.AssessmentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AssessmentAdapter.AssessmentViewHolder holder, int position) {
        if(mAssessments != null){
            Assessment current = mAssessments.get(position);
            String type = current.getType();
            String title = current.getTitle();
            String startDate = current.getStartDate();
            holder.assessmentItemView.setText(title);
        } else {
            holder.assessmentItemView.setText("No Assessment Name");
            holder.assessmentItemView2.setText("No Start Date");
        }
    }

    public void setAssessments(List<Assessment> assessments){
        mAssessments = assessments;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mAssessments.size();
    }
}


