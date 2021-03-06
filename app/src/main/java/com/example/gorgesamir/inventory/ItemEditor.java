package com.example.gorgesamir.inventory;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gorgesamir.inventory.data.Inventory;
import com.example.gorgesamir.inventory.data.InventoryContract;
import com.example.gorgesamir.inventory.data.InventoryDbHelper;

public class ItemEditor extends AppCompatActivity {

    InventoryDbHelper content = new InventoryDbHelper(this);
    Inventory inventory = new Inventory();
    private String productPrice;
    private String productQuantity;
    private int ID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_editor);
        if (savedInstanceState == null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle == null) {
                ID = 0;
            } else {
                ID = bundle.getInt("my-ID");
            }
        } else {
            ID = (Integer) savedInstanceState.getSerializable("my-ID");
        }
        increment();
        decrement();
        updateQuantity();
        deleteProduct();
        orderButton();
    }

    @Override
    protected void onStart() {
        super.onStart();
        inventory.getProductQuantity();
        selectRow();
        showText();
        displayQuantities(inventory.getProductQuantity());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ItemEditor.this, MainActivity.class);
        startActivity(intent);
    }

    private void showText() {
        TextView nameTextView = findViewById(R.id.product_name_text_view);
        TextView descriptionTextView = findViewById(R.id.product_description_text_view);
        TextView quantityTextView = findViewById(R.id.product_quantity_text_view);
        TextView priceTextView = findViewById(R.id.product_price_text_view);

        productPrice = String.valueOf(inventory.getProductPrice());
        productQuantity = String.valueOf(inventory.getProductQuantity());

        nameTextView.setText(inventory.getProductName());
        descriptionTextView.setText(inventory.getProductDescription());
        quantityTextView.setText(productQuantity);
        priceTextView.setText(productPrice);
    }

    private void selectRow() {
        content.selectData(ID);
        SQLiteDatabase database = content.getReadableDatabase();
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
        try {
            int idColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME);
            int descriptionColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_DESCRIPTION);
            int quantityColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_QUANTITY);
            int priceColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_PRICE);


            while (cursor.moveToNext()) {
                int rowID = cursor.getInt(idColumnIndex);
                if (rowID == ID) {
                    inventory.setProductName(cursor.getString(nameColumnIndex));
                    inventory.setProductDescription(cursor.getString(descriptionColumnIndex));
                    inventory.setProductPrice(cursor.getInt(priceColumnIndex));
                    inventory.setProductQuantity(cursor.getInt(quantityColumnIndex));

                }
            }
        } finally {
            cursor.close();
        }
    }

    private void increment() {
        ImageButton incrementImageButton = findViewById(R.id.product_increment_edit_view);
        incrementImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inventory.setProductQuantity(inventory.getProductQuantity() + 1);
                displayQuantities(inventory.getProductQuantity());
            }
        });
    }

    private void decrement() {
        ImageButton decrementImageButton = findViewById(R.id.product_decrement_edit_view);
        decrementImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inventory.getProductQuantity() == 0) {
                    Toast.makeText(getApplicationContext(), getString(R.string._0_products), Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    inventory.setProductQuantity(inventory.getProductQuantity() - 1);
                    displayQuantities(inventory.getProductQuantity());
                }
            }
        });
    }

    private void updateQuantity() {
        ImageButton saveButton = findViewById(R.id.edit_save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                content.updateQuantity(ID, inventory.getProductQuantity());
                if (content.updateQuantity(ID, inventory.getProductQuantity()) == true) {
                    Toast.makeText(getApplicationContext(), R.string.quantity_updataed, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ItemEditor.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), R.string.can_not_update_quantity, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void displayQuantities(int productQuantity) {
        TextView quantityTextView = findViewById(R.id.product_quantity_text_view);
        quantityTextView.setText(productQuantity + " Products");
    }

    private void deleteProduct() {
        ImageButton deleteImageButton = findViewById(R.id.edit_delete_button);
        deleteImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ItemEditor.this);
                builder.setMessage(R.string.delete_dialog_message);
                builder.setTitle(R.string.delete_item);
                builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        content.delete(ID);
                        Intent intent = new Intent(ItemEditor.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });
    }

    public void orderButton() {
        ImageButton orderImageButton = findViewById(R.id.order_button);
        orderImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inventory.getProductQuantity() != 0 || inventory.getProductQuantity() > 1) {
                    reduceQuantity();
                    composeEmail(getString(R.string.order_subject_message), orderMessage());
                } else {
                    Toast.makeText(getApplicationContext(), R.string.can_not_make_order, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public String orderMessage() {
        String orderMessage = "Product name \t" + inventory.getProductName() + "\n"
                + "ProductPrice \t" + inventory.getProductPrice() + "\n"
                + "Product description \t" + inventory.getProductDescription() + "\n"
                + "Product quantity \t" + 1;
        return orderMessage;
    }

    public void reduceQuantity() {
        int newQuantity = inventory.getProductQuantity() - 1;
        inventory.setProductQuantity(newQuantity);
        content.updateQuantity(ID, inventory.getProductQuantity());
        if (content.updateQuantity(ID, inventory.getProductQuantity()) == true) {
            Toast.makeText(getApplicationContext(), R.string.quantity_updated, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), R.string.can_not_update_quantity, Toast.LENGTH_SHORT).show();
        }
    }

    private void composeEmail(String subject, String text) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto: "));
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }


}

