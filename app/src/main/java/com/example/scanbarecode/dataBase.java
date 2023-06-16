package com.example.scanbarecode;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

class dataBase extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "hanoutLhaj.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "hanout";
    private static final String COLUMN_BARCODE = "barecode";
    private static final String COLUMN_NAME = "articleName";
    private static final String COLUMN_SELLER = "seller";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_BUYINGPRICE = "buyingprice";
    private static final String COLUMN_SELLINGPRICE = "sellingprice";
    private static final String COLUMN_QUANTITY = "quantity";

    dataBase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_BARCODE + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_SELLER + " TEXT, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_BUYINGPRICE + " INTEGER, " +
                COLUMN_SELLINGPRICE + " INTEGER, " +
                COLUMN_QUANTITY + " INTEGER);";
        db.execSQL(query);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    void addItem(Activity activity, long barcode, String name, String seller, String date, int quantity, int buyingPrice, int sellingPrice) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_BARCODE, barcode);
        cv.put(COLUMN_QUANTITY, quantity);
        cv.put(COLUMN_NAME, name);
        cv.put(COLUMN_SELLER, seller);
        cv.put(COLUMN_DATE, date);
        cv.put(COLUMN_BUYINGPRICE, buyingPrice);
        cv.put(COLUMN_SELLINGPRICE, sellingPrice);

        try {
            long result = db.insertOrThrow(TABLE_NAME, null, cv);
            Toast.makeText(context, "Added Successfully!", Toast.LENGTH_SHORT).show();
            // Return to previous activity
            activity.finish();
        } catch (SQLiteConstraintException e) {
            Toast.makeText(context, "Item with the same barcode already exists!", Toast.LENGTH_SHORT).show();
        } finally {
            db.close();
        }
    }

    Cursor readAllData(){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    /*void updateData(String row_id, String title, String author, String pages){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TITLE, title);
        cv.put(COLUMN_AUTHOR, author);
        cv.put(COLUMN_PAGES, pages);

        long result = db.update(TABLE_NAME, cv, "_id=?", new String[]{row_id});
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Updated Successfully!", Toast.LENGTH_SHORT).show();
        }

    }

    void deleteOneRow(String row_id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "_id=?", new String[]{row_id});
        if(result == -1){
            Toast.makeText(context, "Failed to Delete.", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Successfully Deleted.", Toast.LENGTH_SHORT).show();
        }
    }

    void deleteAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
    }*/

}