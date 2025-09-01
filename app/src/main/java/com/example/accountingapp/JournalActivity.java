package com.example.accountingapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.*;

public class JournalActivity extends AppCompatActivity {

    EditText etDate, etDescription, etDebit, etCredit, etAmount, etCurrency;
    Button btnToday, btnSave;

    int editIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("🧾 دفتر اليومية");
        setContentView(R.layout.activity_journal);

        etDate = findViewById(R.id.etDate);
        etDescription = findViewById(R.id.etDescription);
        etDebit = findViewById(R.id.etDebit);
        etCredit = findViewById(R.id.etCredit);
        etAmount = findViewById(R.id.etAmount);
        etCurrency = findViewById(R.id.etCurrency);
        btnToday = findViewById(R.id.btnToday);
        btnSave = findViewById(R.id.btnSave);

        // 📆 عند الضغط على زر اليوم
        btnToday.setOnClickListener(v -> {
            String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            etDate.setText(today);
        });

        // 📅 افتح DatePicker عند الضغط على التاريخ
        etDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                String date = year + "-" + String.format(Locale.getDefault(), "%02d", month + 1) + "-" + String.format("%02d", dayOfMonth);
                etDate.setText(date);
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        // التحقق من التعديل
        editIndex = getIntent().getIntExtra("editIndex", -1);
        if (editIndex != -1 && editIndex < MainActivity.entries.size() - 1) {
            JournalEntry debitEntry = MainActivity.entries.get(editIndex);
            JournalEntry creditEntry = MainActivity.entries.get(editIndex + 1);

            etDate.setText(debitEntry.date);
            etDescription.setText(debitEntry.description);
            etCurrency.setText(debitEntry.currency);
            etAmount.setText(debitEntry.debit > 0 ? String.valueOf(debitEntry.debit) : String.valueOf(creditEntry.credit));
            etDebit.setText(debitEntry.account);
            etCredit.setText(creditEntry.account);

            btnSave.setText("✏️ تعديل القيد");
        }

        // عند الحفظ
        btnSave.setOnClickListener(v -> {
            String date = etDate.getText().toString().trim();
            String description = etDescription.getText().toString().trim();
            String debitAccount = etDebit.getText().toString().trim();
            String creditAccount = etCredit.getText().toString().trim();
            String amountStr = etAmount.getText().toString().trim();
            String currency = etCurrency.getText().toString().trim();

            if (date.isEmpty() || debitAccount.isEmpty() || creditAccount.isEmpty() || amountStr.isEmpty()) {
                Toast.makeText(this, "❌ يرجى تعبئة جميع الحقول المطلوبة", Toast.LENGTH_SHORT).show();
                return;
            }

            double amount;
            try {
                amount = Double.parseDouble(amountStr);
            } catch (Exception e) {
                Toast.makeText(this, "❌ المبلغ غير صالح", Toast.LENGTH_SHORT).show();
                return;
            }

            JournalEntry debit = new JournalEntry(date, debitAccount, amount, 0, description, currency);
            JournalEntry credit = new JournalEntry(date, creditAccount, 0, amount, description, currency);

            if (editIndex != -1) {
                // حذف القديم
                MainActivity.entries.remove(editIndex);     // مدين
                MainActivity.entries.remove(editIndex);     // دائن (صار في نفس المؤشر)
                // إدخال الجديد بنفس المكان
                MainActivity.entries.add(editIndex, credit);
                MainActivity.entries.add(editIndex, debit);
                Toast.makeText(this, "✏️ تم تعديل القيد", Toast.LENGTH_SHORT).show();
            } else {
                MainActivity.entries.add(debit);
                MainActivity.entries.add(credit);
                Toast.makeText(this, "✅ تم حفظ القيد", Toast.LENGTH_SHORT).show();
            }

            finish();
        });
    }
}
