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
import android.widget.Button;
import android.widget.DatePicker;
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

import java.util.Calendar;
import java.util.Date;

import fpt.huyenptnhe160769.cosplanner.R;
import fpt.huyenptnhe160769.cosplanner.models.Cos;
import fpt.huyenptnhe160769.cosplanner.services.DateConverter;
import fpt.huyenptnhe160769.cosplanner.services.ImageSaver;

public abstract class EditCosplayDialog extends DialogFragment {
    EditText name, sub, note;
    ImageView image, addImage, removeImage;
    DatePicker est;
    ImageSaver saver;

    ActivityResultLauncher<Intent> arl;
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
        name = view.findViewById(R.id.edit_cosplay_name);
        sub = view.findViewById(R.id.edit_cosplay_sub);
        note = view.findViewById(R.id.edit_cosplay_note);
        image = view.findViewById(R.id.edit_cosplay_picture);
        est = view.findViewById(R.id.edit_cosplay_est);
        addImage = view.findViewById(R.id.edit_cosplay_addPicture);
        removeImage = view.findViewById(R.id.edit_cosplay_removePicture);

        saver = new ImageSaver(context);

        name.setText(cos.name);
        sub.setText(cos.series);
        note.setText(cos.note);
        est.init(DateConverter.toDate(cos.dueDate).getYear(), DateConverter.toDate(cos.dueDate).getMonth(), DateConverter.toDate(cos.dueDate).getDate(), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            }
        });
        est.setMinDate(cos.initDate);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser(image);
            }
        });
        removeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (image.getDrawable() != null){
                    image.setImageBitmap(null);
                    saver.removeFromInternalStorage(cos.pictureURL);
                    RemoveImage();
                }
            }
        });

        builder.setView(view)
                .setPositiveButton("Lưu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String imageURL = saver.saveToInternalStorage(saver.convertToBitmap(image), ImageSaver.IMAGE_FOR.COSPLAY, cos.cid);

                        AddImage(imageURL);
                        EditCosplay(name.getText().toString(), sub.getText().toString(), note.getText().toString(), imageURL, new Date(est.getYear(), est.getMonth() + 1, est.getDayOfMonth()));
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
                                String url = saver.saveToInternalStorage(saver.convertToBitmap(image), ImageSaver.IMAGE_FOR.COSPLAY, cos.cid);
                                AddImage(url);
                            }
                        }
                    }
                }
        );
        // pass the constant to compare it
        // with the returned requestCode
        arl.launch(i);
    }

    public abstract void EditCosplay(String name, String sub, String note, String imageURL, Date est);
    public abstract void AddImage(String imageURL);
    public abstract void RemoveImage();
}
