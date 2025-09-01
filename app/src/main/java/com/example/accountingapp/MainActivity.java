package com.example.accountingapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static ArrayList<JournalEntry> entries = new ArrayList<>();
    public static ArrayList<JournalEntry> backupEntries = new ArrayList<>();
    private static final int PICK_CSV_FILE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("📘 Accounting App");
        setContentView(R.layout.activity_main);

        checkStoragePermission();

        Button btnJournal = findViewById(R.id.btnJournal);
        Button btnLedger = findViewById(R.id.btnLedger);
        Button btnExport = findViewById(R.id.btnExport);
        Button btnImport = findViewById(R.id.btnImport);

        btnJournal.setOnClickListener(v -> startActivity(new Intent(this, JournalActivity.class)));
        btnLedger.setOnClickListener(v -> startActivity(new Intent(this, LedgerActivity.class)));

        findViewById(R.id.btnRestore).setOnClickListener(v -> {
            entries = new ArrayList<>(backupEntries);
            Toast.makeText(this, "✅ تم استرجاع البيانات السابقة", Toast.LENGTH_SHORT).show();
        });
        Button btnHelp = findViewById(R.id.btnHelp);
        btnHelp.setOnClickListener(v -> startActivity(new Intent(this, HelpActivity.class)));

        btnExport.setOnClickListener(v -> {
            EditText input = new EditText(this);
            input.setHint("اسم المجلد (مثال: معاملات_يوليو)");

            new AlertDialog.Builder(this)
                    .setTitle("تسمية مجلد التصدير")
                    .setView(input)
                    .setPositiveButton("تصدير", (dialog, which) -> {
                        String folderName = input.getText().toString().trim();
                        if (folderName.isEmpty()) folderName = "AccountingApp";
                        exportToCSV(folderName);
                    })
                    .setNegativeButton("إلغاء", null)
                    .show();
        });

        btnImport.setOnClickListener(v -> openFilePicker());
    }

    private void exportToCSV(String folderName) {
        try {
            File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), folderName);
            if (!dir.exists()) dir.mkdirs();

            File file = new File(dir, "journal.csv");
            FileWriter writer = new FileWriter(file);
            writer.append("Date,Account,Debit,Credit,Description,Currency\n");

            for (JournalEntry e : entries) {
                writer.append(e.date).append(",")
                        .append(e.account).append(",")
                        .append(String.valueOf(e.debit)).append(",")
                        .append(String.valueOf(e.credit)).append(",")
                        .append(e.description.replace(",", ";")).append(",")
                        .append(e.currency).append("\n");
            }

            writer.flush();
            writer.close();

            Toast.makeText(this, "📤 تم الحفظ في:\n" + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "❌ فشل التصدير", Toast.LENGTH_SHORT).show();
        }
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("text/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "اختر ملف CSV"), PICK_CSV_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_CSV_FILE && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();

            new AlertDialog.Builder(this)
                    .setTitle("استيراد ملف")
                    .setMessage("هل تريد استبدال جميع القيود الحالية بالملف الجديد؟")
                    .setPositiveButton("نعم", (dialog, which) -> importCSV(uri))
                    .setNegativeButton("إلغاء", null)
                    .show();
        }
    }

    private void importCSV(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            reader.readLine(); // skip header

            backupEntries = new ArrayList<>(entries);
            int oldCount = entries.size();
            entries.clear();

            int newCount = 0;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    String date = parts[0].trim();
                    String account = parts[1].trim();
                    double debit = Double.parseDouble(parts[2].trim());
                    double credit = Double.parseDouble(parts[3].trim());
                    String description = parts[4].trim();
                    String currency = parts[5].trim();

                    JournalEntry newEntry = new JournalEntry(date, account, debit, credit, description, currency);

                    boolean isDuplicate = false;
                    for (JournalEntry existing : entries) {
                        if (existing.date.equals(newEntry.date)
                                && existing.account.equals(newEntry.account)
                                && existing.debit == newEntry.debit
                                && existing.credit == newEntry.credit
                                && existing.description.equals(newEntry.description)
                                && existing.currency.equals(newEntry.currency)) {
                            isDuplicate = true;
                            break;
                        }
                    }

                    if (!isDuplicate) {
                        entries.add(newEntry);
                        newCount++;
                    }
                }
            }

            reader.close();

            Toast.makeText(this, "📥 تم الاستيراد: " + newCount + " جديدة / " + oldCount + " انحذفت", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, LedgerActivity.class));

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "❌ فشل الاستيراد", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkStoragePermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != getPackageManager().PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
    }
}
