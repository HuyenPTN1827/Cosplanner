package fpt.huyenptnhe160769.cosplanner;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import fpt.huyenptnhe160769.cosplanner.dao.AppDatabase;
import fpt.huyenptnhe160769.cosplanner.dialog.AddItemDialog;
import fpt.huyenptnhe160769.cosplanner.dialog.CosplayNoteDialog;
import fpt.huyenptnhe160769.cosplanner.dialog.CosplayTotalDialog;
import fpt.huyenptnhe160769.cosplanner.dialog.EditCosplayDialog;
import fpt.huyenptnhe160769.cosplanner.models.Cos;
import fpt.huyenptnhe160769.cosplanner.models.Element;
import fpt.huyenptnhe160769.cosplanner.recycler_view.details_item.ItemsAdapter;
import fpt.huyenptnhe160769.cosplanner.services.DateConverter;
import fpt.huyenptnhe160769.cosplanner.services.ImageSaver;

public class DetailsActivity extends AppCompatActivity {
    Cos cos;
    RecyclerView rv;
    ItemsAdapter adapter;
    TextView cosplayName, subName, startDate, estimateDate;
    ImageView add, icon;
    LinearLayout edit, finish, note, total;
    ImageView picture;
    ImageSaver saver;
    AppDatabase db;

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
        note = findViewById(R.id.lin_Note);
        total = findViewById(R.id.lin_Total);
        rv = findViewById(R.id.rec_BoughtItem);
        icon = findViewById(R.id.img_icon);

        if(cos != null) {
            updateViews();
        }

//        picture.
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddItemDialog addItemDialog = new AddItemDialog() {
                    @Override
                    public void AddNewItem(String name, String price, boolean priority) {
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
                };
                addItemDialog.show(getSupportFragmentManager(), "AddItemDialog");
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditCosplayDialog editCosplayDialog = new EditCosplayDialog(cos, DetailsActivity.this) {

                    @Override
                    public void EditCosplay(String name, String sub, String note, String imageURL, Date est) {
                        cos.name = name;
                        cos.series = sub;
                        cos.note = note;
                        cos.pictureURL = imageURL;
                        cos.dueDate = DateConverter.toTimestamp(est);
                        db.cosDao().update(cos);
                    }

                    @Override
                    public void AddImage(String imageURL) {
                        cos.pictureURL = imageURL;
                        db.cosDao().update(cos);
                    }

                    @Override
                    public void RemoveImage() {
                        cos.pictureURL = null;
                        db.cosDao().update(cos);
                    }
                };
                editCosplayDialog.show(getSupportFragmentManager(), editCosplayDialog.getClass().getName());
                updateViews();
            }
        });
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cos.isComplete = !cos.isComplete;
                if (cos.isComplete){
                    cos.endDate = DateConverter.toTimestamp(new Date());
                }
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
        cosplayName.setText(cos.name);
        subName.setText(cos.series);
        startDate.setText(DateConverter.toDate(cos.initDate).toString());
        estimateDate.setText(DateConverter.toDate(cos.dueDate).toString());
        updateRecyclerView();
    }

    void updateRecyclerView(){

        adapter = new ItemsAdapter(new ArrayList<Element>(), getApplicationContext(), db);
        LinearLayoutManager manager = new LinearLayoutManager(this);

        rv.setAdapter(adapter);
        rv.setLayoutManager(manager);
    }
}