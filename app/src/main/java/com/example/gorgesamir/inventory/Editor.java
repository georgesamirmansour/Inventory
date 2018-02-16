package com.example.gorgesamir.inventory;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gorgesamir.inventory.data.Inventory;
import com.example.gorgesamir.inventory.data.InventoryDbHelper;

public class Editor extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 1;
    Uri uri;
    Inventory inventory = new Inventory();
    EditText nameEditText;
    EditText priceEditText;
    EditText descriptionEditText;
    InventoryDbHelper content = new InventoryDbHelper(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        displayQuantities(inventory.getProductQuantity());
        increment();
        decrement();
        insertData();
        addImage();

    }

    private void insertData() {
        ImageButton saveImageButton = findViewById(R.id.save_button);
        saveImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getEditTextEntry(view);
                if (!inventory.getProductName().isEmpty() && !inventory.getProductDescription().isEmpty() &&
                        inventory.getProductPrice() != 0 && inventory.getProductQuantity() != 0 &&
                        inventory.getProductImage() != null) {

                    content.insert(inventory.getProductName(),
                            inventory.getProductDescription(),
                            inventory.getProductQuantity(),
                            inventory.getProductPrice(),
                            inventory.getProductImage());

                    Toast.makeText(getApplicationContext(),
                            getString(R.string.data_added), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Editor.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(),
                            getString(R.string.fill_required_filled), Toast.LENGTH_LONG).show();
                    return;
                }
            }
        });
    }

    private void getEditTextEntry(View view) {
        nameEditText = findViewById(R.id.edit_product_name);
        priceEditText = findViewById(R.id.edit_product_price);
        descriptionEditText = findViewById(R.id.edit_product_description);
        inventory.setProductName(nameEditText.getText().toString());
        inventory.setProductDescription(descriptionEditText.getText().toString());
        try {
            inventory.setProductPrice(Integer.parseInt(priceEditText.getText().toString()));
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }


    }

    public void increment() {
        ImageButton incrementImageButton = findViewById(R.id.increment_product);
        incrementImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inventory.setProductQuantity(inventory.getProductQuantity() + 1);
                displayQuantities(inventory.getProductQuantity());
            }
        });
    }

    private void decrement() {
        ImageButton decrementImageButton = findViewById(R.id.decrement_product);
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

    private void displayQuantities(int productQuantity) {
        TextView quantityTextView = findViewById(R.id.text_view_product_quantity);
        quantityTextView.setText(productQuantity + " Products");
    }

    private void addImage() {
        ImageButton takePhotoImageButton = findViewById(R.id.take_photo_button);
        takePhotoImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                if (Build.VERSION.SDK_INT < 19) {
                    intent = new Intent(Intent.ACTION_GET_CONTENT);
                } else {
                    intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                }
                checkSDK();
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });
    }

    private void checkSDK() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this,
                            android.Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK && data.getData() != null) {
            uri = data.getData();
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver()
                    .query(uri,
                            projection,
                            null,
                            null,
                            null);

            assert cursor != null;
            cursor.moveToFirst();
            cursor.close();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                inventory.setProductImage(uri.toString());
                ImageView productImageView = findViewById(R.id.product_image_view);
                productImageView.setImageBitmap(bitmap);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}
