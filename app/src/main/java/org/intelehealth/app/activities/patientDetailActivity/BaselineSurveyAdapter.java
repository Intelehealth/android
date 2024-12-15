package org.intelehealth.app.activities.patientDetailActivity;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.intelehealth.app.R;
import org.intelehealth.app.models.FamilyMemberRes;

import java.util.List;

public class BaselineSurveyAdapter extends RecyclerView.Adapter<BaselineSurveyAdapter.BaselineSurveyViewHolder> {

    List<String> mList;
    Context context;

    public BaselineSurveyAdapter(List<String> mList, Context context) {
        this.mList = mList;
        this.context = context;
    }

    @NonNull
    @Override
    public BaselineSurveyAdapter.BaselineSurveyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row = inflater.inflate(R.layout.list_item_baseline_survey, parent, false);
        return new BaselineSurveyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull BaselineSurveyAdapter.BaselineSurveyViewHolder holder, int position) {
        holder.tvBaselineItem.setText(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class BaselineSurveyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvBaselineItem;

        public BaselineSurveyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBaselineItem = itemView.findViewById(R.id.tv_baseline_item);
        }
    }
}
