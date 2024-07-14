package fpt.huyenptnhe160769.cosplanner.dialog.edit_cosplay;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Date;

import fpt.huyenptnhe160769.cosplanner.R;
import fpt.huyenptnhe160769.cosplanner.models.Cos;
import fpt.huyenptnhe160769.cosplanner.models.Element;

public abstract class EditCosplayDialog extends DialogFragment {
    Context context;
    Cos cos;
    public EditCosplayDialog(Cos cos, Context context){
        this.cos = cos;
        this.context = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.edit_cosplay_dialog, null);

        //Set view data
        EditText name = view.findViewById(R.id.edit_cosplay_name);
        EditText sub = view.findViewById(R.id.edit_cosplay_sub);
        EditText note = view.findViewById(R.id.edit_cosplay_note);
        EditText image = view.findViewById(R.id.edit_cosplay_imageURL);
        DatePicker est = view.findViewById(R.id.edit_cosplay_est);
        Button addImage = view.findViewById(R.id.edit_cosplay_addImage);

        name.setText(cos.getName());
        sub.setText(cos.getSeries());
        note.setText(cos.getNotes());
        est.init(cos.getEndDate().get(Calendar.YEAR), cos.getEndDate().get(Calendar.MONTH), cos.getEndDate().get(Calendar.DATE), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            }
        });
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText image = view.findViewById(R.id.edit_cosplay_imageURL);

                AddImage(image.getText().toString());
            }
        });
        builder.setView(view)
                .setPositiveButton("Lưu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText name = view.findViewById(R.id.edit_cosplay_name);
                        EditText sub = view.findViewById(R.id.edit_cosplay_sub);
                        EditText note = view.findViewById(R.id.edit_cosplay_note);
                        EditText image = view.findViewById(R.id.edit_cosplay_imageURL);
                        DatePicker est = view.findViewById(R.id.edit_cosplay_est);

                        AddImage(image.getText().toString());
                        EditCosplay(name.getText().toString(), sub.getText().toString(), note.getText().toString(), image.getText().toString(), new Date(est.getYear(), est.getMonth() + 1, est.getDayOfMonth()));
                    }
                })
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditCosplayDialog.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }

    public abstract void EditCosplay(String name, String sub, String note, String image, Date est);
    public abstract void AddImage(String image);
}
