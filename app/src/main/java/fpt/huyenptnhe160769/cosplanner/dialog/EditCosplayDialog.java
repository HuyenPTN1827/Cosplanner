package fpt.huyenptnhe160769.cosplanner.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.text.DecimalFormat;
import java.util.Date;

import fpt.huyenptnhe160769.cosplanner.DetailsActivity;
import fpt.huyenptnhe160769.cosplanner.MainActivity;
import fpt.huyenptnhe160769.cosplanner.R;
import fpt.huyenptnhe160769.cosplanner.dao.AppDatabase;
import fpt.huyenptnhe160769.cosplanner.models.Cos;
import fpt.huyenptnhe160769.cosplanner.models.Element;
import fpt.huyenptnhe160769.cosplanner.services.DateConverter;
import fpt.huyenptnhe160769.cosplanner.services.ImageSaver;

public class EditCosplayDialog extends ListenDialogFragment {
    EditText name, sub, note, budget;
    ImageView picture, addImage, removeImage;
    DatePicker est;
    ImageSaver saver;
    Bitmap initialPicture;

    Context context;
    Cos cos;
    AppDatabase db;
    public EditCosplayDialog(Cos cos, Context context, AppDatabase db){
        this.cos = cos;
        this.context = context;
        this.db = db;
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
        budget = view.findViewById(R.id.edit_cosplay_budget);
        picture = view.findViewById(R.id.edit_cosplay_picture);
        est = view.findViewById(R.id.edit_cosplay_est);
        addImage = view.findViewById(R.id.edit_cosplay_addPicture);
        removeImage = view.findViewById(R.id.edit_cosplay_removePicture);

        saver = new ImageSaver(context);

        initialPicture = null;
        if (cos.pictureURL != null && saver.loadImageFromStorage(cos.pictureURL) != null){
            initialPicture = saver.loadImageFromStorage(cos.pictureURL);
        }

        if (cos.pictureURL != null && saver.loadImageFromStorage(cos.pictureURL) != null)
            picture.setImageBitmap(saver.loadImageFromStorage(cos.pictureURL));
        name.setText(cos.name);
        DecimalFormat df = new DecimalFormat("#");
        budget.setText(df.format(cos.budget));
        sub.setText(cos.series);
        note.setText(cos.note);
        Date setEstDate = DateConverter.toDate(cos.dueDate);
        est.setMinDate(cos.initDate);
//        Toast.makeText(context, String.valueOf(setEstDate.getYear()), Toast.LENGTH_SHORT).show();

        est.updateDate(setEstDate.getYear() + 1900, setEstDate.getMonth(), setEstDate.getDate());
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser(picture);
            }
        });
        removeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cos.pictureURL != null && saver.loadImageFromStorage(cos.pictureURL) != null){
                    picture.setImageResource(R.drawable.empty_character);
                    saver.removeFromInternalStorage(cos.pictureURL);
                    RemoveImage();
                }
            }
        });

        builder.setView(view)
                .setNeutralButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.cosDao().delete(cos);
                        Intent intent = new Intent(context, MainActivity.class);
                        context.startActivity(intent);
                    }
                })
                .setPositiveButton("Lưu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String imageURL = saver.saveToInternalStorage(saver.convertToBitmap(picture), ImageSaver.IMAGE_FOR.COSPLAY, cos.cid);

                        cos.pictureURL = imageURL;
                        db.cosDao().update(cos);

                        ImageView empty = new ImageView(context);
                        empty.setImageResource(R.drawable.empty_character);
                        if (saver.convertToBitmap(picture) == saver.convertToBitmap(empty)) initialPicture = null;
                        else initialPicture = saver.convertToBitmap(picture);
                        EditCosplay(name.getText().toString(), sub.getText().toString(), Double.parseDouble(budget.getText().toString()), note.getText().toString(), new Date(est.getYear() - 1900, est.getMonth(), est.getDayOfMonth()));
                    }
                })
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditCosplayDialog.this.getDialog().dismiss();
                    }
                });

        return builder.create();
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
                saver.removeFromInternalStorage(cos.pictureURL);
                RemoveImage();
            }
            else {
                String url = saver.saveToInternalStorage(initialPicture, ImageSaver.IMAGE_FOR.COSPLAY, cos.cid);
                AddPicture(url, ImageSaver.IMAGE_FOR.COSPLAY, cos.cid);
            }
        }
//        Toast.makeText(context, initialPicture == null ? "Null" : "Not null", Toast.LENGTH_SHORT).show();
    }

    private void imageChooser(ImageView image) {

        // create an instance of the
        // intent of the type image
        Intent i = new Intent();
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
//                                String url = saver.saveToInternalStorage(saver.convertToBitmap(image), ImageSaver.IMAGE_FOR.COSPLAY, cos.cid);
//                                AddImage(url);
//                            }
//                        }
//                    }
//                }
//        );
        // pass the constant to compare it
        // with the returned requestCode
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

    private void setImage(Uri uri) {
        picture.setImageURI(uri);
        String url = saver.saveToInternalStorage(saver.convertToBitmap(picture), ImageSaver.IMAGE_FOR.COSPLAY, cos.cid);
        AddPicture(url, ImageSaver.IMAGE_FOR.COSPLAY, cos.cid);
    }

    public void EditCosplay(String name, String sub, double budget, String note, Date est){
        cos.name = name;
        cos.budget = budget;
        cos.series = sub;
        cos.note = note;
        cos.dueDate = DateConverter.toTimestamp(est);
        db.cosDao().update(cos);
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
    public void RemoveImage(){
        cos.pictureURL = null;
        db.cosDao().update(cos);
    }
}
