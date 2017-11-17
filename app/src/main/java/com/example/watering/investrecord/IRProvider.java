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
    static final String AUTHORITY = "watering.investrecord.provider";
    static final String PATH_GROUP = "group";
    static final String PATH_ACCOUNT = "account";
    static final String PATH_INFO_IO = "info_io";
    static final String PATH_INFO_DAIRY = "info_dairy";
    static final int CODE_GROUP = 0;
    static final int CODE_ACCOUNT = 1;
    static final int CODE_INFO_IO = 2;
    static final int CODE_INFO_DAIRY = 3;

    static final UriMatcher Matcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        Matcher.addURI(AUTHORITY,PATH_GROUP,CODE_GROUP);
        Matcher.addURI(AUTHORITY,PATH_ACCOUNT,CODE_ACCOUNT);
        Matcher.addURI(AUTHORITY,PATH_INFO_IO,CODE_INFO_IO);
        Matcher.addURI(AUTHORITY,PATH_INFO_DAIRY,CODE_INFO_DAIRY);
    }

    GroupDBHelper DB_group;
    AccountDBHelper DB_account;
    Info_DairyDBHelper DB_info_dairy;
    Info_IODBHelper DB_info_IO;

    public boolean onCreate() {
        DB_group = new GroupDBHelper(getContext());
        DB_account = new AccountDBHelper(getContext());
        DB_info_dairy = new Info_DairyDBHelper(getContext());
        DB_info_IO = new Info_IODBHelper(getContext());

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        switch (Matcher.match(uri)) {
            case CODE_GROUP:
                return DB_group.query(projection, selection, selectionArgs, null, null, sortOrder);
            case CODE_ACCOUNT:
                return DB_account.query(projection, selection, selectionArgs, null, null, sortOrder);
            case CODE_INFO_IO:
                return DB_info_IO.query(projection, selection, selectionArgs, null, null, sortOrder);
            case CODE_INFO_DAIRY:
                return DB_info_dairy.query(projection, selection, selectionArgs, null, null, sortOrder);
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (Matcher.match(uri)) {
            case CODE_GROUP:
                return "vnd.android.cursor.dir/vnd.investrecord.group";
            case CODE_ACCOUNT:
                return "vnd.android.cursor.dir/vnd.investrecord.account";
            case CODE_INFO_IO:
                return "vnd.android.cursor.dir/vnd.investrecord.info_io";
            case CODE_INFO_DAIRY:
                return "vnd.android.cursor.dir/vnd.investrecord.info_dairy";
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        switch (Matcher.match(uri)) {
            case CODE_GROUP:
                DB_group.insert(values);
                break;
            case CODE_ACCOUNT:
                DB_account.insert(values);
                break;
            case CODE_INFO_IO:
                DB_info_IO.insert(values);
                break;
            case CODE_INFO_DAIRY:
                DB_info_dairy.insert(values);
                break;
            default:
                return null;
        }
        return uri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count = 0;

        switch (Matcher.match(uri)) {
            case CODE_GROUP:
                DB_group.delete(selection, selectionArgs);
                break;
            case CODE_ACCOUNT:
                DB_account.delete(selection, selectionArgs);
                break;
            case CODE_INFO_IO:
                DB_info_IO.delete(selection, selectionArgs);
                break;
            case CODE_INFO_DAIRY:
                DB_info_dairy.delete(selection, selectionArgs);
                break;
            default:
        }
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count = 0;

        switch (Matcher.match(uri)) {
            case CODE_GROUP:
                DB_group.update(values, selection, selectionArgs);
                break;
            case CODE_ACCOUNT:
                DB_account.update(values, selection, selectionArgs);
                break;
            case CODE_INFO_IO:
                DB_info_IO.update(values, selection, selectionArgs);
                break;
            case CODE_INFO_DAIRY:
                DB_info_dairy.update(values, selection, selectionArgs);
                break;
            default:
        }
        return count;
    }


}
