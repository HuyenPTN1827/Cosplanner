package fpt.huyenptnhe160769.cosplanner.dialog;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import fpt.huyenptnhe160769.cosplanner.R;
import fpt.huyenptnhe160769.cosplanner.models.Element;
import fpt.huyenptnhe160769.cosplanner.services.ImageSaver;

public abstract class EditItemDialog extends DialogFragment {
    EditText name, price;
    CheckBox done, priority;
    ImageView picture, addPicture, removePicture;
    ActivityResultLauncher<Intent> arl;
    ImageSaver saver;

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
        name = view.findViewById(R.id.edit_item_name);
        price = view.findViewById(R.id.edit_item_price);
        done = view.findViewById(R.id.edit_item_done);
        priority = view.findViewById(R.id.edit_item_priority);
        addPicture = view.findViewById(R.id.edit_item_addPicture);
        removePicture = view.findViewById(R.id.edit_item_removePicture);
        picture = view.findViewById(R.id.edit_item_picture);

        saver = new ImageSaver(context);

        name.setText(item.name);
        price.setText(String.valueOf(item.cost));
        done.setChecked(item.isComplete);
        priority.setChecked(item.isPriority);
        addPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser(picture);
            }
        });
        removePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RemovePicture();
            }
        });

        builder.setView(view)
                .setNeutralButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DeleteElement();
                    }
                })
                .setPositiveButton("Lưu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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
    private void imageChooser(ImageView image) {

        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        arl = ((ComponentActivity)context).registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult o) {
                        if (o.getResultCode() == RESULT_OK){
                            Uri uri = o.getData().getData();
                            if (uri != null){
                                image.setImageURI(uri);
                                String url = saver.saveToInternalStorage(saver.convertToBitmap(image), ImageSaver.IMAGE_FOR.ELEMENT, item.eid);
                                AddPicture(url);
                            }
                        }
                    }
                }
        );
        // pass the constant to compare it
        // with the returned requestCode
        arl.launch(i);
    }

    public abstract void EditItem(String name, String price, boolean done, boolean priority);
    public abstract void DeleteElement();
    public abstract void AddPicture(String url);
    public abstract void RemovePicture();
}
