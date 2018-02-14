package com.example.gorgesamir.inventory;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.gorgesamir.inventory.data.InventoryContract;
import com.example.gorgesamir.inventory.data.InventoryDbHelper;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    InventoryCursorAdapter adapter;
    InventoryDbHelper helper = new InventoryDbHelper(this);

    @Override
    protected void onStart() {
        super.onStart();
        displayDataInfo();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onItemPressed();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Editor.class);
                startActivity(intent);
            }
        });
        onItemPressed();
    }

    public void onItemPressed() {
        listView = findViewById(R.id.list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int ID = listView.getPositionForView(view);
                ID = ID + 1;
                Intent intent = new Intent(MainActivity.this, ItemEditor.class);
                intent.putExtra("my-ID", ID);
                startActivity(intent);
            }
        });
    }
//    public void deleteData() {
//        ImageButton deleteImageButton = (ImageButton) findViewById(R.id.delete_button);
//        deleteImageButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                        SQLiteDatabase database = helper.getWritableDatabase();
//                        database.delete(InventoryContract.InventoryEntry.TABLE_NAME,
//                                InventoryContract.InventoryEntry._ID + "=" + adapter.getItem(i), null);
//                    }
//                });
//            }
//        });
//    }

    private void displayDataInfo() {
        SQLiteDatabase database = helper.getReadableDatabase();
        String[] projection =
                {
                        InventoryContract.InventoryEntry._ID,
                        InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME,
                        InventoryContract.InventoryEntry.COLUMN_PRODUCT_PRICE,
                        InventoryContract.InventoryEntry.COLUMN_PRODUCT_QUANTITY,
                        InventoryContract.InventoryEntry.COLUMN_PRODUCT_DESCRIPTION,
                };
        Cursor cursor = database.query(
                InventoryContract.InventoryEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null,
                null
        );

        ListView inventoryListView = findViewById(R.id.list);
        InventoryCursorAdapter adapter = new InventoryCursorAdapter(this, cursor);
        inventoryListView.setAdapter(adapter);
    }
//
//    private void updateQuantity(int quantity) {
//        SQLiteDatabase writableDatabase = helper.getWritableDatabase();
//        SQLiteDatabase readableDatabase = helper.getReadableDatabase();
//
//        quantity = quantity - 1;
//        ContentValues values = new ContentValues();
//        values.put(InventoryContract.InventoryEntry.COLUMN_PRODUCT_QUANTITY, quantity);
//        writableDatabase.update(InventoryContract.InventoryEntry.TABLE_NAME, quantity, );
//    }
}
