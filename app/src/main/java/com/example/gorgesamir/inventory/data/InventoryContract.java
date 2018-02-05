package com.example.gorgesamir.inventory.data;

import android.provider.BaseColumns;

/**
 * Created by gorge samir on 2018-02-04.
 */

public final class InventoryContract {

    public static abstract class InventoryEntry implements BaseColumns {

        public static final String TABLE_NAME = "inventory";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_PRODUCT_NAME = "name";
        public static final String COLUMN_PRODUCT_PRICE = "price";
        public static final String COLUMN_PRODUCT_QUANTITY = "quantity";
        public static final String COLUMN_PRODUCT_DESCRIPTION = "description";
    }
}
