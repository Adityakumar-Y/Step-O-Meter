package com.example.steptracker.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.steptracker.Models.Record;
import com.example.steptracker.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordViewHolder> {

    Context context;
    ArrayList<Record> recordList;

    public RecordAdapter(Context context, ArrayList<Record> recordList) {
        this.context = context;
        this.recordList = recordList;
    }

    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_record_items, parent, false);
        return new RecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordViewHolder holder, int position) {

        String date = recordList.get(position).getDate();
        long steps = recordList.get(position).getSteps();

        holder.tvDate.setText(date);
        holder.tvSteps.setText(String.valueOf(steps));
    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }

    public class RecordViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tvDate)
        TextView tvDate;
        @BindView(R.id.tvStepsCompleted)
        TextView tvSteps;

        public RecordViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
