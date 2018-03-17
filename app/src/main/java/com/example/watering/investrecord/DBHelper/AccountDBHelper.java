package com.example.watering.investrecord.DBHelper;

import android.content.Context;

/**
 * Created by watering on 17. 10. 21.
 */

@SuppressWarnings("DefaultFileTemplate")
public class AccountDBHelper extends DBHelper {

    public AccountDBHelper(Context context) {
        super(context);

        TABLE_NAME = "tbl_Account";
        COLUMNS = new String [] {"institute TEXT","number TEXT UNIQUE","description TEXT",
                "id_group INTEGER"};
    }
}
