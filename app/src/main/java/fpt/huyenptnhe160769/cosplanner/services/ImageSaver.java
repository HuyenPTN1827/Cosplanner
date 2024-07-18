package fpt.huyenptnhe160769.cosplanner.services;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageSaver {
    ActivityResultLauncher<Intent> arl;

    Context context;
    public static enum IMAGE_FOR{
        COSPLAY,
        ELEMENT
    }
    public ImageSaver (Context context){
        this.context = context;
    }
    public void removeFromInternalStorage(String path){
        try{
            ContextWrapper cw = new ContextWrapper(context.getApplicationContext());
            File file = new File(path);
            file.delete();
        }
        catch (Exception ex){
            Log.e("Image Saver", "Cannot remove image");
        }
    }
    public String saveToInternalStorage(Bitmap bitmapImage, IMAGE_FOR type, int id){
        ContextWrapper cw = new ContextWrapper(context.getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("images", Context.MODE_PRIVATE);
        // Create imageDir
        File myPath = null;
        switch (type){
            case COSPLAY:
                 myPath = new File(directory,"c"+String.valueOf(id) + ".jpg");
                break;
            case ELEMENT:
                myPath = new File(directory,"e"+String.valueOf(id) + ".jpg");
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }

        if (myPath.exists()) myPath.delete();

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(myPath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return myPath.getAbsolutePath();
    }

    public Bitmap loadImageFromStorage(String path)
    {

        try {
            File f=new File(path);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            return b;
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
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
                            }
                        }
                    }
                }
        );
        // pass the constant to compare it
        // with the returned requestCode
        arl.launch(i);
    }

    public Bitmap convertToBitmap (ImageView image){
        if (image.getDrawable() == null) return null;
        return ((BitmapDrawable) image.getDrawable()).getBitmap();
    }
}
