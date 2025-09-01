# 📘 Accounting App

An Android-based **personal accounting application** that allows you to record, track, and review financial transactions.  
It supports **journal entries, ledger views, CSV export/import, and Arabic-friendly UI**, making it simple yet practical for students, accountants, and project owners.

---

## ✨ Features
- 🧾 **Journal Entry**: Add daily accounting transactions with date, debit, credit, amount, currency, and description.  
- 📒 **Ledger**: View, filter, and validate entries (balanced/unbalanced).  
- ♻️ **Retrieve Previous Data**: Restore backup entries easily.  
- 📤 **Export to CSV**: Save journal entries into CSV files.  
- 📥 **Import from CSV**: Load accounting records from existing CSV files.  
- ℹ️ **About Page**: Arabic help & usage guide included.  
- ✅ **Balance Validation**: Ensures debit = credit before saving.  

---

## 📱 Screenshots

<p align="center">
  <img src="https://github.com/user-attachments/assets/03c393d8-1aac-44a6-adb2-64208d270759" alt="Splash Screen" width="250"/>
  <img src="https://github.com/user-attachments/assets/eefe589e-349d-4cdb-ba24-9335b8f5ed18" alt="Main Menu" width="250"/>
  <img src="[screenshots/ss3.png](https://github.com/user-attachments/assets/b00c4c12-7a18-4ba8-8353-0f6f98a2d118)" alt="Journal Entry" width="250"/>
</p>

<p align="center">
  <img src="https://github.com/user-attachments/assets/b8c2d846-d46c-428e-b2f3-52dd2f02c3d6" alt="Ledger View" width="250"/>
  <img src="https://github.com/user-attachments/assets/cca4ed6c-b6a8-48cb-bf73-6606ae9c9cfa" alt="Retrieve Previous Data" width="250"/>
  <img src="https://github.com/user-attachments/assets/d2c2c5c6-0492-4af1-89d0-0fd79cacde4b" alt="Export Dialog" width="250"/>
</p>

<p align="center">
  <img src="https://github.com/user-attachments/assets/436264bc-f996-4f1b-8fe5-8ffa4fa6705b" alt="Import from CSV" width="250"/>
  <img src="https://github.com/user-attachments/assets/da5a6f06-cbb0-4a5a-b12c-48bd9f57ff6f" alt="About Page" width="250"/>
</p>

---

## 🛠 Tech Stack
- **Language:** Java (Android)  
- **UI:** Material Components + ConstraintLayout  
- **Storage:** In-memory (runtime), CSV export/import  
- **Permissions:** Storage access for CSV handling  

---

## 📂 Project Structure

app/
├─ java/com/example/accountingapp/
│ ├─ MainActivity.java # Main menu, navigation, import/export
│ ├─ JournalActivity.java # Add/Edit journal entries
│ ├─ LedgerActivity.java # View and filter ledger
│ ├─ HelpActivity.java # About/help screen
│ ├─ JournalEntry.java # Data model for entries
├─ res/layout/ # XML layouts
├─ res/values/ # Colors, strings, styles
└─ res/drawable/ # Icons and backgrounds


---

## 🚀 How to Use
1. Open **Journal Entry** to add a transaction.  
2. Enter **date, debit account, credit account, amount, currency, description**.  
3. Save → The system validates debit = credit.  
4. View all records in the **Ledger** with filtering options.  
5. Export data as **CSV** or import existing records.  
6. Use the **About page** for detailed guidance (in Arabic).  

---

## 🌍 Localization
- ✅ Full **Arabic language support** (labels, help, and messages).  
- ✅ RTL-friendly UI.  

---

## 📌 Notes
- Data is stored temporarily and exported to CSV for persistence.  
- Useful for **students, small business owners, and accounting learners**.  
