package com.example.accountingapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.*;

public class LedgerActivity extends AppCompatActivity {

    EditText etFilterAccount, etFilterDate;
    Button btnFilter;
    ListView listView;
    TextView balanceText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("📒 دفتر الأستاذ");
        setContentView(R.layout.activity_ledger);

        // ربط العناصر
        etFilterAccount = findViewById(R.id.etFilterAccount);
        etFilterDate = findViewById(R.id.etFilterDate);
        btnFilter = findViewById(R.id.btnFilter);
        listView = findViewById(R.id.listView);
        balanceText = findViewById(R.id.totalTextView);

        etFilterDate.setOnClickListener(v -> showDatePicker());
        btnFilter.setOnClickListener(v -> applyFilter());

        applyFilter();

        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            if (position >= MainActivity.entries.size()) return true;

            JournalEntry entry = MainActivity.entries.get(position);

            new AlertDialog.Builder(this)
                    .setTitle("📌 اختر الإجراء")
                    .setItems(new String[]{"✏️ تعديل", "🗑️ حذف"}, (dialog, which) -> {
                        if (which == 0) {
                            Intent intent = new Intent(this, JournalActivity.class);
                            intent.putExtra("editIndex", position);
                            startActivity(intent);
                        } else if (which == 1) {
                            MainActivity.entries.remove(position);
                            Toast.makeText(this, "🗑️ تم حذف القيد", Toast.LENGTH_SHORT).show();
                            applyFilter();
                        }
                    })
                    .show();

            return true;
        });
    }

    void applyFilter() {
        String accountFilter = etFilterAccount.getText().toString().trim();
        String dateFilter = etFilterDate.getText().toString().trim();

        ArrayList<String> list = new ArrayList<>();
        double totalDebit = 0;
        double totalCredit = 0;

        for (JournalEntry e : MainActivity.entries) {
            boolean matches = true;

            if (!accountFilter.isEmpty() && !e.account.toLowerCase().contains(accountFilter.toLowerCase())) {
                matches = false;
            }

            if (!dateFilter.isEmpty() && !e.date.equals(dateFilter)) {
                matches = false;
            }

            if (matches) {
                String line = e.date + " | " + e.account + " | مدين: " + e.debit + " | دائن: " + e.credit;
                if (e.currency != null && !e.currency.isEmpty()) {
                    line += " | " + e.currency;
                }
                if (!e.description.isEmpty()) {
                    line += " | " + e.description;
                }

                list.add(line);
                totalDebit += e.debit;
                totalCredit += e.credit;
            }
        }

        list.add("----------------------------");
        list.add("📊 الإجمالي - مدين: " + totalDebit + " | دائن: " + totalCredit);

        if (Math.abs(totalDebit - totalCredit) < 0.001) {
            list.add("✅ القيد متوازن");
        } else {
            list.add("❌ القيد غير متوازن!");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);

        balanceText.setText("مدين: " + totalDebit + " | دائن: " + totalCredit);
    }

    void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String date = year + "-" + String.format(Locale.getDefault(), "%02d", month + 1) + "-" + String.format("%02d", dayOfMonth);
            etFilterDate.setText(date);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
}
