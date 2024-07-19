package fpt.huyenptnhe160769.cosplanner;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.Spinner;
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

import java.text.NumberFormat;
import java.util.Collections;
import java.util.Currency;
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
    private EditText searchEditText;
    private TextView emptyTextView;
    private LinearLayout rowInitDate, rowDueDate, rowBudget;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
    private Spinner spinnerFilterStatus, spinnerSortType, spinnerOrderType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.cos_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.CosList), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = AppDatabase.getInstance(this);
        listView = findViewById(R.id.CosList_ListView);
        searchEditText = findViewById(R.id.CosList_EditTextSearch);
        emptyTextView = findViewById(R.id.CosList_TextViewEmpty);
        spinnerFilterStatus = findViewById(R.id.CosList_SpinnerFilterStatus);
        spinnerSortType = findViewById(R.id.CosList_SpinnerSortType);
        spinnerOrderType = findViewById(R.id.CosList_SpinnerOrderType);

        // Update the list when starting the application
        updateCosList();

        // Filter Cos by Type
        spinnerFilterStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                filterCosList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
            }
        });

        // Sort Cos by Type
        spinnerSortType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                sortCosList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
            }
        });

        // Order Cos by Type
        spinnerOrderType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                sortCosList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
            }
        });

        //Intent Cos Detail
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cos selectedCos = cosList.get(position);
                Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                intent.putExtra("cosId", selectedCos.cid);
                startActivity(intent);
            }
        });

        //Add New Cos
        ImageButton btnAdd = findViewById(R.id.CosList_ButtonAddCos);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddDialog();
            }
        });

        //Search Cos by Name or Series
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchCos(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCosList();
    }

    private void filterCosList() {
        String filterStatus = spinnerFilterStatus.getSelectedItem().toString();
        if (filterStatus.equals("Tất cả Project Cosplay")) {
            cosList = db.cosDao().getAllCos();
        } else if (filterStatus.equals("Các Project đang chuẩn bị")) {
            cosList = db.cosDao().getCompletedCos(false);
        } else if (filterStatus.equals("Các Project đã hoàn thành")) {
            cosList = db.cosDao().getCompletedCos(true);
        }
        sortCosList();
    }

    private void sortCosList() {
        String sortType = spinnerSortType.getSelectedItem().toString();
        String orderType = spinnerOrderType.getSelectedItem().toString();

        if (sortType.equals("Tên nhân vật")) {
            cosList = db.cosDao().orderByName();
        } else if (sortType.equals("Tên series")) {
            cosList = db.cosDao().orderBySeries();
        } else if (sortType.equals("Ngày bắt đầu")) {
            cosList = db.cosDao().orderByInitDate();
        } else if (sortType.equals("Ngày kết thúc")) {
            cosList = db.cosDao().orderByDueDate();
        } else if (sortType.equals("Ngân quỹ")) {
            cosList = db.cosDao().orderByBudget();
        }

        // Đảo ngược danh sách nếu thứ tự là "Lớn đến Nhỏ"
        if (orderType.equals("Lớn đến Nhỏ")) {
            Collections.reverse(cosList);
        }
        loadCosList();
    }

    private void updateCosList() {
        filterCosList();
        loadCosList();
    }

    private void searchCos(String string) {
        cosList = db.cosDao().searchCos("%" + string + "%");
        loadCosList();
        updateEmptyView();
    }

    //Cos list is null
    private void updateEmptyView() {
        if (cosList.isEmpty()) {
            emptyTextView.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        } else {
            emptyTextView.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        }
    }

    private void loadCosList() {
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
                TextView datesInit = convertView.findViewById(R.id.CosListViewDatesInit);
                TextView datesEnd = convertView.findViewById(R.id.CosListViewDatesEnd);
                TextView budgetTextView = convertView.findViewById(R.id.CosListViewGeneralInfoText);

                nameTextView.setText(cos.name);
                seriesTextView.setText(cos.series);
                datesInit.setText(dateFormat.format(DateConverter.toDate(cos.initDate)));
                if (cos.isComplete) {
                    datesEnd.setText(dateFormat.format(DateConverter.toDate(cos.endDate)));
                }
                else {
                    datesEnd.setText(dateFormat.format(DateConverter.toDate(cos.dueDate)));
                }

                NumberFormat format = NumberFormat.getCurrencyInstance();
                format.setCurrency(Currency.getInstance("VND"));
                format.setMaximumFractionDigits(0);
                budgetTextView.setText(format.format(cos.budget));

                return convertView;
            }
        };

        listView.setAdapter(adapter);
        updateEmptyView();
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

        //Save new Cos to Database
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = characterEdt.getText().toString();
                String series = seriesEdt.getText().toString();
                long initDate = DateConverter.toTimestamp(new Date());
                long dueDate = DateConverter.toTimestamp(new Date());
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
                    initDate = DateConverter.toTimestamp(new Date());
                }

                try {
                    dueDate = dateFormat.parse(dueDateEdt.getText().toString()).getTime();
                } catch (Exception e) {
                    dueDate = DateConverter.toTimestamp(new Date());
                }

                Cos cos = new Cos();
                cos.name = name;
                cos.series = series;
                cos.note = null;
                cos.initDate = initDate;
                cos.dueDate = dueDate;
                cos.endDate = dueDate;
                cos.budget = budget;
                cos.isComplete = false;
                cos.pictureURL = null;

                db.cosDao().insert(cos);

                cosList = db.cosDao().getAllCos();
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