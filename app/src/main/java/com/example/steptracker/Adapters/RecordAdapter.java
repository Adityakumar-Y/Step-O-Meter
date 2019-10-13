package com.example.steptracker.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.steptracker.Models.RecordData;
import com.example.steptracker.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordViewHolder> {

    private Context context;
    private ArrayList<RecordData> recordList;

    public RecordAdapter(Context context, ArrayList<RecordData> recordList) {
        this.context = context;
        this.recordList = recordList;
    }

    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_grid_record, parent, false);
        return new RecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordViewHolder holder, int position) {
        holder.tvRecordData.setText(recordList.get(position).getRecordData());
        holder.tvRecordLabel.setText(recordList.get(position).getRecordLabel());
    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }

    public class RecordViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tvRecordData)
        TextView tvRecordData;
        @BindView(R.id.tvRecordLabel)
        TextView tvRecordLabel;

        public RecordViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
