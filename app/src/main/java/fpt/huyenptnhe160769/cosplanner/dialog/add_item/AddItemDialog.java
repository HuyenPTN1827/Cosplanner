package fpt.huyenptnhe160769.cosplanner.dialog.add_item;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import fpt.huyenptnhe160769.cosplanner.R;

public abstract class AddItemDialog extends DialogFragment {
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
                        AddItemDialog.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }

    public abstract void AddNewItem(String name, String price, boolean priority);

}
