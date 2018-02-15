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
    InventoryDbHelper content = new InventoryDbHelper(this);

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

    private void displayDataInfo() {
        SQLiteDatabase database = content.getReadableDatabase();
        String[] projection =
                {
                        InventoryContract.InventoryEntry._ID,
                        InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME,
                        InventoryContract.InventoryEntry.COLUMN_PRODUCT_PRICE,
                        InventoryContract.InventoryEntry.COLUMN_PRODUCT_QUANTITY,
                        InventoryContract.InventoryEntry.COLUMN_PRODUCT_DESCRIPTION,
                        InventoryContract.InventoryEntry.COLUMN_PRODUCT_IMAGE
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
}
