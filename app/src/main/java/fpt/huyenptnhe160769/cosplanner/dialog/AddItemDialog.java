package fpt.huyenptnhe160769.cosplanner.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import fpt.huyenptnhe160769.cosplanner.R;
import fpt.huyenptnhe160769.cosplanner.dao.AppDatabase;
import fpt.huyenptnhe160769.cosplanner.models.Cos;
import fpt.huyenptnhe160769.cosplanner.models.Element;

public class AddItemDialog extends ListenDialogFragment {
    AppDatabase db;
    Cos cos;
    public AddItemDialog (AppDatabase db, Cos cos){
        this.db = db;
        this.cos = cos;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.add_item_dialog, null);
        builder.setView(view)
                .setPositiveButton("Lưu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText name = view.findViewById(R.id.add_item_name);
                        EditText price = view.findViewById(R.id.add_item_price);
                        CheckBox priority = view.findViewById(R.id.add_item_priority);

                        AddNewItem(name.getText().toString(), price.getText().toString(), priority.isChecked());
                    }
                })
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AddItemDialog.this.getDialog().dismiss();
                    }
                });

        return builder.create();
    }

    public void AddNewItem(String name, String price, boolean priority){
        try{
            Element e = new Element();
            e.cid = cos.cid;
            e.name = name;
            e.cost = Double.valueOf(price);
            e.isPriority = priority;
            db.elementDao().insert(e);
        }
        catch (Exception ex){
            Log.e("Add item failed", ex.getMessage());
        }
    }

}
