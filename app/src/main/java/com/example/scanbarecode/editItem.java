package com.example.scanbarecode;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class editItem extends AppCompatActivity {
    private EditText ItemName, ItemSeller, BuyingPrice, SellingPrice, Quantity;
    private TextView BareCode;
    private Button saveEdit, scanCode,Editdate;
    private TextView date;
    private Calendar myCalendar;
    dataBase myDB;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        BareCode = findViewById(R.id.barecode);
        ItemName = findViewById(R.id.ItemName);
        ItemSeller = findViewById(R.id.ItemSeller);
        BuyingPrice = findViewById(R.id.BuyingPrice);
        SellingPrice = findViewById(R.id.SellingPrice);
        Quantity = findViewById(R.id.Quantity);
        saveEdit = findViewById(R.id.saveEdit);
        scanCode = findViewById(R.id.scanCode);
        Editdate = findViewById(R.id.editdate);
        date = findViewById(R.id.Date);
        myCalendar = Calendar.getInstance();
        myDB = new dataBase(editItem.this);

        Intent intent = getIntent();
        String barcodeString = intent.getStringExtra("barcode");
        long barcodeValue = Long.parseLong(barcodeString);
        storeDataInArrays(barcodeValue);


        Editdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        scanCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBarcodeScanning();
            }
        });
        saveEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(editItem.this);
                builder.setTitle("enregistrement article")
                        .setMessage("vous êtes sûr ?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                myDB = new dataBase(editItem.this);
                                long updatedBarcode = Long.parseLong(BareCode.getText().toString().trim());
                                String updatedItemName = ItemName.getText().toString().trim();
                                String updatedItemSeller = ItemSeller.getText().toString().trim();
                                int updatedBuyingPrice = Integer.parseInt(BuyingPrice.getText().toString().trim());
                                int updatedSellingPrice = Integer.parseInt(SellingPrice.getText().toString().trim());
                                int updatedQuantity = Integer.parseInt(Quantity.getText().toString().trim());
                                String updatedDate = date.getText().toString().trim();
                                myDB.updateData(barcodeValue,updatedBarcode, updatedItemName, updatedItemSeller, updatedDate,
                                        updatedBuyingPrice, updatedSellingPrice, updatedQuantity);
                                Intent intent = new Intent(editItem.this,itemDetails.class);
                                intent.putExtra("barcode", String.valueOf(updatedBarcode));
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });




    }

    private void showDatePickerDialog() {
        // Get the current date for initialization
        int year = myCalendar.get(Calendar.YEAR);
        int month = myCalendar.get(Calendar.MONTH);
        int day = myCalendar.get(Calendar.DAY_OF_MONTH);

        // Create and show a DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                dateSetListener, // The listener for date selection
                year, month, day
        );
        datePickerDialog.show();
    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            // Update the selected date in the EditText
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            updateEditTextDate();
        }
    };

    private void updateEditTextDate() {
        // Format the selected date and update the EditText text
        String dateFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.getDefault());
        date.setText(sdf.format(myCalendar.getTime()));
    }

    private void startBarcodeScanning() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setPrompt("Scan a barcode");
        integrator.setCaptureActivity(Capture.class);// Optional: Set a prompt message
        integrator.setOrientationLocked(true);  // Optional: Lock the orientation
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null && result.getContents() != null) {
            BareCode.setText(result.getContents());
        } else {
            Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show();
        }
    }
    private void storeDataInArrays(long barcode) {
        dataBase myDB = new dataBase(this);
        Cursor cursor = myDB.readData(barcode);
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "Not found", Toast.LENGTH_SHORT).show();
        } else {
            if (cursor.moveToFirst()) {
                BareCode.setText(String.valueOf(cursor.getLong(0)));
                ItemName.setText(cursor.getString(1));
                ItemSeller.setText(cursor.getString(2));
                date.setText(cursor.getString(3));
                Quantity.setText(String.valueOf(cursor.getInt(6)));
                BuyingPrice.setText(String.valueOf(cursor.getInt(4)));
                SellingPrice.setText(String.valueOf(cursor.getInt(5)));
            }
        }
    }
}