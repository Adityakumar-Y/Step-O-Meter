package com.example.steptracker.Utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.steptracker.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomGoalDialog extends AppCompatDialogFragment {

    private View view;
    private long goal;
    private AlertDialog dialog;
    private ChangeGoalListener listener;

    @BindView(R.id.goal_text_input)
    TextInputLayout goalInputLayout;
    @BindView(R.id.etGoals)
    TextInputEditText etGoals;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.custom_dialog_layout, null);

        ButterKnife.bind(this, view);

        dialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setPositiveButton("Set Goal", null)
                .setNegativeButton("Cancel", null)
                .create();


        dialog.setOnShowListener(dialogInterface -> {
            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(view -> {
                String goalSet = goalInputLayout.getEditText().getText().toString().trim();
                validate(goalSet);
            });
        });

        return dialog;
    }

    private void validate(String goalSet) {
        if(!goalSet.isEmpty()) {
            goal = Long.parseLong(goalSet);
            if (goal >= 1000 && goal <= 50000) {
                listener.updateGoal(goal);
                goalInputLayout.setError(null);
                dialog.dismiss();
            } else {
                goalInputLayout.setError("Invalid goal set !!");
            }
        }else{
            goalInputLayout.setError("Cannot be empty !!");
        }

    }

    public void setOnChangeGoalListener(ChangeGoalListener listener){
        this.listener = listener;
    }


    public interface ChangeGoalListener {
        void updateGoal(long steps);
    }
}
