package fpt.huyenptnhe160769.cosplanner;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.ComponentActivity;
import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import fpt.huyenptnhe160769.cosplanner.dao.AppDatabase;
import fpt.huyenptnhe160769.cosplanner.dialog.AddItemDialog;
import fpt.huyenptnhe160769.cosplanner.dialog.CosplayNoteDialog;
import fpt.huyenptnhe160769.cosplanner.dialog.CosplayTotalDialog;
import fpt.huyenptnhe160769.cosplanner.dialog.DialogFinishListener;
import fpt.huyenptnhe160769.cosplanner.dialog.EditCosplayDialog;
import fpt.huyenptnhe160769.cosplanner.models.Cos;
import fpt.huyenptnhe160769.cosplanner.models.Element;
import fpt.huyenptnhe160769.cosplanner.recycler_view.details_item.ItemsAdapter;
import fpt.huyenptnhe160769.cosplanner.services.DateConverter;
import fpt.huyenptnhe160769.cosplanner.services.ImageSaver;

public class DetailsActivity extends AppCompatActivity implements DialogFinishListener {
    Cos cos;
    RecyclerView rv;
    ItemsAdapter adapter;
    TextView cosplayName, subName, startDate, estimateDate;
    ImageView add, icon, finishCosplay;
    LinearLayout edit, finish, note, total;
    ImageView picture;
    ImageSaver saver;
    AppDatabase db;
    DateFormat df;

    @Override
    public void notifyDialogFinish(DialogFragment dialog) {
        updateViews();
//        Toast.makeText(DetailsActivity.this, dialog.getTag(), Toast.LENGTH_SHORT).show();
    }

    void LoadDB(){
        db = Room
                .databaseBuilder(getApplicationContext(), AppDatabase.class, "database")
                .allowMainThreadQueries()
                .build();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Services
        saver = new ImageSaver(DetailsActivity.this);

        //Load DB here
        LoadDB();

        //Load cosplay here
//        cos = new Cos();
        int cosId = getIntent().getIntExtra("cosId", -1);
        if (cosId < 0){
            finish();
        }
        cos = db.cosDao().findById(cosId);

        //Buttons and Images
        cosplayName = findViewById(R.id.txt_name);
        subName = findViewById(R.id.txt_sub);
        startDate = findViewById(R.id.txt_startDate);
        estimateDate = findViewById(R.id.txt_estimateDate);
        picture = findViewById(R.id.img_icon);
        add = findViewById(R.id.ic_addItem);
        edit = findViewById(R.id.lin_Edit);
        finish = findViewById(R.id.lin_Complete);
        finishCosplay = findViewById(R.id.ic_finish_cosplay);
        note = findViewById(R.id.lin_Note);
        total = findViewById(R.id.lin_Total);
        rv = findViewById(R.id.rec_BoughtItem);
        icon = findViewById(R.id.img_icon);
        df = new SimpleDateFormat("dd/MM/yyyy");

        updateViews();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddItemDialog addItemDialog = new AddItemDialog(db, cos);
                addItemDialog.setOnFinishListener(DetailsActivity.this);
                addItemDialog.show(getSupportFragmentManager(), "AddItemDialog");
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditCosplayDialog editCosplayDialog = new EditCosplayDialog(cos, DetailsActivity.this, db);
                editCosplayDialog.setOnFinishListener(DetailsActivity.this);
                editCosplayDialog.show(getSupportFragmentManager(), editCosplayDialog.getClass().getName());
                updateViews();
            }
        });
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cos.isComplete = !cos.isComplete;
                if (cos.isComplete){
                    Toast.makeText(DetailsActivity.this, "Finish cosplay", Toast.LENGTH_SHORT).show();
                    cos.endDate = DateConverter.toTimestamp(new Date());
                }
                else
                    Toast.makeText(DetailsActivity.this, "Unfinish cosplay", Toast.LENGTH_SHORT).show();
                db.cosDao().update(cos);
                updateViews();
            }
        });
        note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CosplayNoteDialog cosplayNoteDialog = new CosplayNoteDialog(cos, db.elementDao().getByCosId(cos.cid));
                cosplayNoteDialog.show(getSupportFragmentManager(), cosplayNoteDialog.getClass().getName());
            }
        });
        total.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CosplayTotalDialog cosplayTotalDialog = new CosplayTotalDialog(cos, db.elementDao().getByCosId(cos.cid));
                cosplayTotalDialog.show(getSupportFragmentManager(), cosplayTotalDialog.getClass().getName());
            }
        });
    }

    private void updateViews() {
//        Toast.makeText(DetailsActivity.this, "Update data", Toast.LENGTH_SHORT).show();
        if (cos.pictureURL != null && saver.loadImageFromStorage(cos.pictureURL) != null){
            picture.setImageBitmap(saver.loadImageFromStorage(cos.pictureURL));
        }
        cosplayName.setText(cos.name);
        subName.setText(cos.series);

        if (cos.isComplete) finishCosplay.setImageResource(R.drawable.ic_finalize);
        else finishCosplay.setImageResource(R.drawable.ic_cancel);

        startDate.setText(df.format(DateConverter.toDate(cos.initDate)));
        estimateDate.setText(df.format(DateConverter.toDate(cos.dueDate)));
        updateRecyclerView();
    }

    void updateRecyclerView(){
        adapter = new ItemsAdapter(cos.cid, DetailsActivity.this, db);
//        adapter.da
        adapter.notifyDataSetChanged();
        LinearLayoutManager manager = new LinearLayoutManager(this);

        rv.setAdapter(adapter);
        rv.setLayoutManager(manager);
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
//    private void imageChooser(ImageView image, ImageSaver.IMAGE_FOR ifor, int id) {
//
//        // create an instance of the
//        // intent of the type image
//        Intent i = new Intent();
//        i.setType("image/*");
//        i.setAction(Intent.ACTION_GET_CONTENT);
//
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
//                                AddPicture(url);
//                            }
//                        }
//                    }
//                }
//        );
//        // pass the constant to compare it
//        // with the returned requestCode
//        arl.launch(i);
//    }
}