package com.example.gorgesamir.inventory.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.gorgesamir.inventory.data.InventoryContract.InventoryEntry;

/**
 * Created by gorge samir on 2018-02-04.
 */

public class InventoryDbHelper extends SQLiteOpenHelper {


    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "inventory.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = " ,";
    private static final String NOT_NULL = " NOT NULL";
    private static final String DEFAULT_0 = " DEFAULT 0";

    public InventoryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + InventoryEntry.TABLE_NAME +
            " ( " + InventoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + COMMA_SEP +
            InventoryEntry.COLUMN_PRODUCT_NAME + TEXT_TYPE + NOT_NULL + COMMA_SEP +
            InventoryEntry.COLUMN_PRODUCT_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
            InventoryEntry.COLUMN_PRODUCT_PRICE + INTEGER_TYPE + NOT_NULL + DEFAULT_0 + COMMA_SEP +
            InventoryEntry.COLUMN_PRODUCT_QUANTITY + INTEGER_TYPE + NOT_NULL + DEFAULT_0 + " ); ";

    private static final String SQL_DELETE_ENTRIES = " DROP TABLE IF EXIST " + InventoryEntry.TABLE_NAME;

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }
}
