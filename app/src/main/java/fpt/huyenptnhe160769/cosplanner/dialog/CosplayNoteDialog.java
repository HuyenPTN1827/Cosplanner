package fpt.huyenptnhe160769.cosplanner.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;

import fpt.huyenptnhe160769.cosplanner.models.Cos;
import fpt.huyenptnhe160769.cosplanner.models.Element;

public class CosplayNoteDialog extends DialogFragment {
    Cos cos;
    List<Element> elementList;
    public CosplayNoteDialog (Cos cos, List<Element> elementList){
        this.cos = cos;
        this.elementList = elementList;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        String msg = "";
        String completeNote = "";
        String incompleteNote = "";
        for (Element e :
                elementList) {
            if (e.note == null || e.note.length() == 0) continue;
            if (e.isComplete){
                completeNote += "\n\t* " + e.note;
            }
            else {
                incompleteNote += "\n\t* " + e.note;
            }
        }

        msg += "Cosplay:\n\t* " + cos.note;
        if (completeNote != "") msg += "\n\nHoàn thành:" + completeNote;
        if (incompleteNote != "") msg += "\n\nChưa hoàn thành:" + incompleteNote;
        builder
                .setMessage(msg)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CosplayNoteDialog.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }

}
