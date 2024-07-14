package fpt.huyenptnhe160769.cosplanner;

import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

import fpt.huyenptnhe160769.cosplanner.models.Cos;
import fpt.huyenptnhe160769.cosplanner.models.Element;
import fpt.huyenptnhe160769.cosplanner.recycler_view.details_item.ItemsAdapter;

public class DetailsActivity extends AppCompatActivity {
    Cos cos;
    RecyclerView rv;
    ItemsAdapter adapter;
    TextView cosplayName, subName, startDate, estimateDate;
    ImageView add;
    LinearLayout edit, finish, note, total;
    ImageView picture;

    void LoadDB(){

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

        //Load DB here

        //Load cosplay here
        cos = new Cos();

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

        cosplayName.setText(cos.getName());
        subName.setText(cos.getSeries());
        startDate.setText(String.valueOf(cos.getInitDate()));
        estimateDate.setText(String.valueOf(cos.getDueDateString()));
//        picture.
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        total.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //Recycler view
        rv = findViewById(R.id.rec_BoughtItem);
        updateRecyclerView();
    }

    void updateRecyclerView(){

        adapter = new ItemsAdapter(new ArrayList<Element>(), getApplicationContext());
        LinearLayoutManager manager = new LinearLayoutManager(this);

        rv.setAdapter(adapter);
        rv.setLayoutManager(manager);
    }
}