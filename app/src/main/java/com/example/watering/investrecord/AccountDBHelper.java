package com.example.watering.investrecord;

import android.content.Context;

/**
 * Created by watering on 17. 10. 21.
 */

public class AccountDBHelper extends DBHelper {

    public AccountDBHelper(Context context) {
        super(context);

        TABLE_NAME = "tbl_Account";
        COLUMNS = new String [] {"id_account INTEGER PRIMARY KEY",
                "inst TEXT","num TEXT","disc TEXT",
                "id_group INTEGER"};
    }
}
