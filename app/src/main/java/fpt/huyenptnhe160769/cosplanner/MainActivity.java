package fpt.huyenptnhe160769.cosplanner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

import java.util.Date;

import fpt.huyenptnhe160769.cosplanner.dao.AppDatabase;
import fpt.huyenptnhe160769.cosplanner.models.Cos;
import fpt.huyenptnhe160769.cosplanner.services.DateConverter;

public class MainActivity extends AppCompatActivity {
    Button btn;
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
        setContentView(R.layout.activity_main_test);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_test), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        LoadDB();

        createTestCosplay();

        btn = findViewById(R.id.btn_toDetails);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), DetailsActivity.class);
                i.putExtra("id", db.cosDao().getAll().stream().findFirst().get().cid);
                startActivity(i);
            }
        });
    }

    private void createTestCosplay() {
        if (db.cosDao().getAll().size() == 0){
            Cos cos  = new Cos();
            cos.name = "iam";
            cos.series = "apant";
            cos.note ="This is note";
            cos.budget = 100000;
            cos.initDate = DateConverter.toTimestamp(new Date());
            cos.dueDate = DateConverter.toTimestamp(new Date(new Date().getYear(), new Date().getMonth(), (new Date().getDate() + 1 > 28 ? new Date().getDate() + 1 : new Date().getDate() - 1)));

            db.cosDao().insert(cos);
        }
    }
}