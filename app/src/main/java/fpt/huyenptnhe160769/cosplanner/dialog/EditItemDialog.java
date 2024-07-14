package fpt.huyenptnhe160769.cosplanner.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import fpt.huyenptnhe160769.cosplanner.R;
import fpt.huyenptnhe160769.cosplanner.models.Element;

public abstract class EditItemDialog extends DialogFragment {
    Context context;
    Element item;
    public EditItemDialog(Element item, Context context){
        this.item = item;
        this.context = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.edit_item_dialog, null);

        //Set view data
        EditText name = view.findViewById(R.id.edit_item_name);
        EditText price = view.findViewById(R.id.edit_item_price);
        CheckBox done = view.findViewById(R.id.edit_item_done);
        CheckBox priority = view.findViewById(R.id.edit_item_priority);
        EditText pictureURL = view.findViewById(R.id.edit_cosplay_pictureURL);
        ImageView addPicture = view.findViewById(R.id.edit_item_addPicture);
        ImageView removePicture = view.findViewById(R.id.edit_item_removePicture);
        ImageView picture = view.findViewById(R.id.edit_item_picture);

        name.setText(item.getName());
        price.setText(String.valueOf(item.getCost()));
        done.setChecked(item.isDone());
        priority.setChecked(item.isPriority());
        addPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddPicture(pictureURL.getText().toString());
                pictureURL.setText("");
            }
        });
        removePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pictureURL.getText().length() == 0) loadPicture();
                else RemovePicture(pictureURL.getText().toString());
            }
        });

        builder.setView(view)
                .setNeutralButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DeleteElement(item.getId());
                    }
                })
                .setPositiveButton("Lưu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText name = view.findViewById(R.id.edit_item_name);
                        EditText price = view.findViewById(R.id.edit_item_price);
                        CheckBox done = view.findViewById(R.id.edit_item_done);
                        CheckBox priority = view.findViewById(R.id.edit_item_priority);
                        ImageView picture = view.findViewById(R.id.edit_item_picture);

                        EditItem(name.getText().toString(), price.getText().toString(), done.isChecked(), priority.isChecked());
                    }
                })
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditItemDialog.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }

    private void loadPicture() {
    }

    public abstract void EditItem(String name, String price, boolean done, boolean priority);
    public abstract void DeleteElement(long id);
    public abstract void AddPicture(String url);
    public abstract void RemovePicture(String url);
}
