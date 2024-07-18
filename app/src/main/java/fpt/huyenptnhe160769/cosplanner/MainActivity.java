package fpt.huyenptnhe160769.cosplanner;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

import java.util.Date;

import fpt.huyenptnhe160769.cosplanner.dao.AppDatabase;
import fpt.huyenptnhe160769.cosplanner.models.Cos;
import fpt.huyenptnhe160769.cosplanner.services.DateConverter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import fpt.huyenptnhe160769.cosplanner.dao.AppDatabase;
import fpt.huyenptnhe160769.cosplanner.models.Cos;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    private AppDatabase db;
    private List<Cos> cosList;
    private LinearLayout rowInitDate, rowDueDate, rowBudget;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

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

        db = AppDatabase.getInstance(this);
        listView = findViewById(R.id.CosList_ListView);
        
        loadCosList();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cos selectedCos = cosList.get(position);
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                intent.putExtra("cosId", selectedCos.cid);
                startActivity(intent);
            }
        });

        ImageButton btnAdd = findViewById(R.id.CosList_ButtonAddCos);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddDialog();
            }
        });
    }

    private void loadCosList() {
        cosList = db.cosDao().getAllCos();
        ArrayAdapter<Cos> adapter = new ArrayAdapter<Cos>(this, R.layout.row_cos_list, cosList) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                if(convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.row_cos_list, parent,false);
                }
                Cos cos = cosList.get(position);

                TextView nameTextView = convertView.findViewById(R.id.CosListName);
                TextView seriesTextView = convertView.findViewById(R.id.CosListSeries);
                nameTextView.setText(cos.name);
                seriesTextView.setText(cos.series);

                return convertView;
            }
        };

        listView.setAdapter(adapter);
    }

    private void showAddDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_cos);

        RadioButton btnInProcess = dialog.findViewById(R.id.Cos_StatusRadio_InProcess);
        RadioButton btnPlanned = dialog.findViewById(R.id.Cos_StatusRadio_Planned);

        EditText characterEdt = dialog.findViewById(R.id.Cos_EditTextCharacter);
        EditText seriesEdt = dialog.findViewById(R.id.Cos_EditTextSeries);

        rowInitDate = dialog.findViewById(R.id.Cos_RowEditTextInitDate);
        EditText initDateEdt = dialog.findViewById(R.id.Cos_EditTextInitDate);

        rowDueDate = dialog.findViewById(R.id.Cos_RowEditTextDueDate);
        EditText dueDateEdt = dialog.findViewById(R.id.Cos_EditTextDueDate);

        rowBudget = dialog.findViewById(R.id.Cos_RowEditTextBudget);
        EditText budgetEdt = dialog.findViewById(R.id.Cos_EditTextBudget);

        Button btnCancel = dialog.findViewById(R.id.Cos_ButtonCancel);
        Button btnSave = dialog.findViewById(R.id.Cos_ButtonSave);

        btnInProcess.setChecked(true);
        btnInProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioInProcessClicked();
            }
        });

        btnPlanned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioPlannedClicked();
            }
        });

        initDateEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(initDateEdt);
            }
        });

        dueDateEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(dueDateEdt);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = characterEdt.getText().toString();
                String series = seriesEdt.getText().toString();
                long initDate = 0;
                long dueDate = 0;
                double budget = 0.0;

                if (name.isEmpty() || series.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter the correct Cosplay Project information!", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    budget = Double.parseDouble(budgetEdt.getText().toString());
                } catch (NumberFormatException e) {
                    budget = 0.0;
                }

                try {
                    initDate = dateFormat.parse(initDateEdt.getText().toString()).getTime();
                } catch (Exception e) {
                    initDate = 0;
                }

                try {
                    dueDate = dateFormat.parse(dueDateEdt.getText().toString()).getTime();
                } catch (Exception e) {
                    dueDate = 0;
                }

                Cos cos = new Cos();
                cos.name = name;
                cos.series = series;
                cos.note = null;
                cos.initDate = initDate;
                cos.dueDate = dueDate;
                cos.endDate = 0;
                cos.budget = budget;
                cos.isComplete = false;
                cos.pictureURL = null;

                db.cosDao().insert(cos);

                loadCosList();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void showDatePickerDialog(EditText editText) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    calendar.set(year, monthOfYear, dayOfMonth);
                    editText.setText(dateFormat.format(calendar.getTime()));
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
        }

    public void radioInProcessClicked() {
        this.rowInitDate.setVisibility(View.VISIBLE);
        this.rowDueDate.setVisibility(View.VISIBLE);
        this.rowBudget.setVisibility(View.VISIBLE);
    }

    public void radioPlannedClicked() {
        this.rowInitDate.setVisibility(View.GONE);
        this.rowDueDate.setVisibility(View.GONE);
        this.rowBudget.setVisibility(View.GONE);
    }
}