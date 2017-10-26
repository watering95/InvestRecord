package com.example.watering.investrecord;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by watering on 17. 10. 21.
 */

public class IRProvider extends ContentProvider {
    static final String AUTHORITY = "com.invest_record.provider";

    static final UriMatcher Matcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        Matcher.addURI(AUTHORITY,"User",0);
        Matcher.addURI(AUTHORITY,"Account",1);
        Matcher.addURI(AUTHORITY,"Info_IO",2);
        Matcher.addURI(AUTHORITY,"Info_Dairy",3);
    }

    UserDBHelper user;
    AccountDBHelper account;
    Info_DairyDBHelper info_dairy;
    Info_IODBHelper info_IO;

    public boolean onCreate() {
        user = new UserDBHelper(getContext());
        account = new AccountDBHelper(getContext());
        info_dairy = new Info_DairyDBHelper(getContext());
        info_IO = new Info_IODBHelper(getContext());

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;

        switch (Matcher.match(uri)) {
            case 0:

                break;
            case 1:

                break;
            case 2:

                break;
            case 3:

                break;
        }

        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        switch (Matcher.match(uri)) {
            case 0:
                user.setItem(values);
                break;
            case 1:
                account.setItem(values);
                break;
            case 2:
                info_dairy.setItem(values);
                break;
            case 3:
                info_IO.setItem(values);
                break;
        }
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count = 0;

        switch (Matcher.match(uri)) {
            case 0:

                break;
            case 1:

                break;
            case 2:

                break;
            case 3:

                break;
        }
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count = 0;

        switch (Matcher.match(uri)) {
            case 0:

                break;
            case 1:

                break;
            case 2:

                break;
            case 3:

                break;
        }
        return count;
    }
}
