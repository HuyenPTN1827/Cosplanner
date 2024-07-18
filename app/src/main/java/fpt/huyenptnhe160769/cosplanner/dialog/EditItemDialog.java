package fpt.huyenptnhe160769.cosplanner.dialog;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import fpt.huyenptnhe160769.cosplanner.R;
import fpt.huyenptnhe160769.cosplanner.dao.AppDatabase;
import fpt.huyenptnhe160769.cosplanner.models.Cos;
import fpt.huyenptnhe160769.cosplanner.models.Element;
import fpt.huyenptnhe160769.cosplanner.services.ImageSaver;

public class EditItemDialog extends ListenDialogFragment {
    EditText name, price, note;
    CheckBox done, priority;
    ImageView picture, addPicture, removePicture;
    ActivityResultLauncher<Intent> arl;
    ImageSaver saver;
    Bitmap initialPicture;

    Context context;
    Element item;
    AppDatabase db;
    public EditItemDialog(Element item, Context context, AppDatabase db){
        this.item = item;
        this.context = context;
        this.db = db;
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
        note = view.findViewById(R.id.edit_item_note);
        done = view.findViewById(R.id.edit_item_done);
        priority = view.findViewById(R.id.edit_item_priority);
        addPicture = view.findViewById(R.id.edit_item_addPicture);
        removePicture = view.findViewById(R.id.edit_item_removePicture);
        picture = view.findViewById(R.id.edit_item_picture);

        saver = new ImageSaver(context);

        initialPicture = null;
        if (item.pictureURL != null && saver.loadImageFromStorage(item.pictureURL) != null){
            initialPicture = saver.loadImageFromStorage(item.pictureURL);
        }
        if (item.pictureURL != null && saver.loadImageFromStorage(item.pictureURL) != null){
            picture.setImageBitmap(saver.loadImageFromStorage(item.pictureURL));
        }
        note.setText(item.note);
        name.setText(item.name);
        DecimalFormat df = new DecimalFormat("#");
        price.setText(df.format(item.cost));
        done.setChecked(item.isComplete);
        priority.setChecked(item.isPriority);
        addPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
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
                        EditItem(name.getText().toString(), note.getText().toString(), price.getText().toString(), done.isChecked(), priority.isChecked());
                        EditItemDialog.this.getDialog().dismiss();
                    }
                })
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditItemDialog.this.getDialog().dismiss();
                    }
                });

        return builder.create();
    }

    private void imageChooser() {

        // create an instance of the
        // intent of the type image
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

//        arl = ((ComponentActivity)context).registerForActivityResult(
//                new ActivityResultContracts.StartActivityForResult(),
//                new ActivityResultCallback<ActivityResult>() {
//                    @Override
//                    public void onActivityResult(ActivityResult o) {
//                        if (o.getResultCode() == RESULT_OK){
//                            Uri uri = o.getData().getData();
//                            if (uri != null){
//                                image.setImageURI(uri);
//                                String url = saver.saveToInternalStorage(saver.convertToBitmap(image), ifor, id);
//                                AddPicture(url, ifor, id);
//                            }
//                        }
//                    }
//                }
//        );
//        // pass the constant to compare it
//        // with the returned requestCode
//        arl.launch(i);

        startActivityForResult(i, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1){
            try {
                Uri uri = data.getData();
                if (uri != null){
                    setImage(uri);
                }
            }
            catch (Exception ex){

            }
        }
    }


    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        revertData();
        super.onDismiss(dialog);
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        revertData();
        super.onCancel(dialog);
    }

    private void revertData() {
        if (initialPicture != saver.convertToBitmap(picture)){
            if (initialPicture == null){
                picture.setImageResource(R.drawable.empty_character);
                saver.removeFromInternalStorage(item.pictureURL);
                item.pictureURL = null;
                db.elementDao().update(item);
            }
            else {
                String url = saver.saveToInternalStorage(initialPicture, ImageSaver.IMAGE_FOR.COSPLAY, item.eid);
                AddPicture(url, ImageSaver.IMAGE_FOR.COSPLAY, item.eid);
            }
        }
//        Toast.makeText(context, initialPicture == null ? "Null" : "Not null", Toast.LENGTH_SHORT).show();
    }

    private void setImage(Uri uri) {
        picture.setImageURI(uri);
        String url = saver.saveToInternalStorage(saver.convertToBitmap(picture), ImageSaver.IMAGE_FOR.ELEMENT, item.eid);
        AddPicture(url, ImageSaver.IMAGE_FOR.ELEMENT, item.eid);
    }

    public void EditItem(String name, String note, String price, boolean done, boolean priority){
        Element e = db.elementDao().findById(item.eid);
        if(e == null) {
            Log.e(this.getClass().getName(), "Cannot find item in database");
            return;
        }
        e.name = name;
        if (note == "") {
            note = null;
        }
        Toast.makeText(context, note == null? "true" : "false", Toast.LENGTH_SHORT).show();
        e.note = note;
        e.cost = Double.valueOf(price);
        e.isComplete = done;
        e.isPriority = priority;

        db.elementDao().update(e);
    }
    public void DeleteElement(){
        Element e = db.elementDao().findById(item.eid);
        if(e == null) {
            Log.e(this.getClass().getName(), "Cannot find item in database");
            return;
        }
        db.elementDao().delete(e);
    }
    public void AddPicture(String url, ImageSaver.IMAGE_FOR ifor, int id){
        switch (ifor) {
            case COSPLAY:
                Cos c = db.cosDao().findById(id);
                if(c == null) {
                    Log.e(this.getClass().getName(), "Cannot find item in database");
                    return;
                }
                c.pictureURL = url;
                db.cosDao().update(c);
                break;
            case ELEMENT:
                Element e = db.elementDao().findById(id);
                if(e == null) {
                    Log.e(this.getClass().getName(), "Cannot find item in database");
                    return;
                }
                e.pictureURL = url;
                db.elementDao().update(e);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + ifor);
        }
    }
    public void RemovePicture(){
        Element e = db.elementDao().findById(item.eid);
        if(e == null) {
            Log.e(this.getClass().getName(), "Cannot find item in database");
            return;
        }
        e.pictureURL = "";
        db.elementDao().update(e);
    }
}
