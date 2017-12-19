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

@SuppressWarnings("ALL")
public class IRProvider extends ContentProvider {
    private static final String AUTHORITY = "watering.investrecord.provider";
    private static final String PATH_GROUP = "group";
    private static final String PATH_ACCOUNT = "account";
    private static final String PATH_INFO_IO = "info_io";
    private static final String PATH_INFO_DAIRY = "info_dairy";
    private static final String PATH_CARD = "card";
    private static final String PATH_CATEGORY_MAIN = "category_main";
    private static final String PATH_CATEGORY_SUB = "category_sub";
    private static final String PATH_INCOME = "income";
    private static final String PATH_OUT = "out";
    private static final String PATH_OUT_CARD = "out_card";
    private static final String PATH_OUT_CASH = "out_cash";
    private static final String PATH_OUT_SCHEDULE = "out_schedule";

    private static final int CODE_GROUP = 0;
    private static final int CODE_ACCOUNT = 1;
    private static final int CODE_INFO_IO = 2;
    private static final int CODE_INFO_DAIRY = 3;
    private static final int CODE_CARD = 4;
    private static final int CODE_CATEGORY_MAIN = 5;
    private static final int CODE_CATEGORY_SUB = 6;
    private static final int CODE_INCOME = 7;
    private static final int CODE_OUT = 8;
    private static final int CODE_OUT_CARD = 9;
    private static final int CODE_OUT_CASH = 10;
    private static final int CODE_OUT_SCHEDULE = 11;

    private GroupDBHelper DB_group;
    private AccountDBHelper DB_account;
    private Info_DairyDBHelper DB_info_dairy;
    private Info_IODBHelper DB_info_IO;
    private CardDBHelper DB_card;
    private CategoryMainDBHelper DB_category_main;
    private CategorySubDBHelper DB_category_sub;
    private IncomeDBHelper DB_income;
    private SpendDBHelper DB_out;
    private SpendCardDBHelper DB_out_card;
    private SpendCashDBHelper DB_out_cash;
    private SpendScheduleDBHelper DB_out_schedule;

    private static final UriMatcher Matcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        Matcher.addURI(AUTHORITY,PATH_GROUP,CODE_GROUP);
        Matcher.addURI(AUTHORITY,PATH_ACCOUNT,CODE_ACCOUNT);
        Matcher.addURI(AUTHORITY,PATH_INFO_IO,CODE_INFO_IO);
        Matcher.addURI(AUTHORITY,PATH_INFO_DAIRY,CODE_INFO_DAIRY);
        Matcher.addURI(AUTHORITY,PATH_CARD,CODE_CARD);
        Matcher.addURI(AUTHORITY,PATH_CATEGORY_MAIN,CODE_CATEGORY_MAIN);
        Matcher.addURI(AUTHORITY,PATH_CATEGORY_SUB,CODE_CATEGORY_SUB);
        Matcher.addURI(AUTHORITY,PATH_INCOME,CODE_INCOME);
        Matcher.addURI(AUTHORITY,PATH_OUT,CODE_OUT);
        Matcher.addURI(AUTHORITY,PATH_OUT_CARD,CODE_OUT_CARD);
        Matcher.addURI(AUTHORITY,PATH_OUT_CASH,CODE_OUT_CASH);
        Matcher.addURI(AUTHORITY,PATH_OUT_SCHEDULE,CODE_OUT_SCHEDULE);
    }

    public boolean onCreate() {
        DB_group = new GroupDBHelper(getContext());
        DB_account = new AccountDBHelper(getContext());
        DB_info_dairy = new Info_DairyDBHelper(getContext());
        DB_info_IO = new Info_IODBHelper(getContext());
        DB_card = new CardDBHelper(getContext());
        DB_category_main = new CategoryMainDBHelper(getContext());
        DB_category_sub = new CategorySubDBHelper(getContext());
        DB_income = new IncomeDBHelper(getContext());
        DB_out = new SpendDBHelper(getContext());
        DB_out_card = new SpendCardDBHelper(getContext());
        DB_out_cash = new SpendCashDBHelper(getContext());
        DB_out_schedule = new SpendScheduleDBHelper(getContext());

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        switch (Matcher.match(uri)) {
            case CODE_GROUP:
                return DB_group.query(projection, selection, selectionArgs, sortOrder);
            case CODE_ACCOUNT:
                return DB_account.query(projection, selection, selectionArgs, sortOrder);
            case CODE_INFO_IO:
                return DB_info_IO.query(projection, selection, selectionArgs, sortOrder);
            case CODE_INFO_DAIRY:
                return DB_info_dairy.query(projection, selection, selectionArgs, sortOrder);
            case CODE_CARD:
                return DB_card.query(projection, selection, selectionArgs, sortOrder);
            case CODE_CATEGORY_MAIN:
                return DB_category_main.query(projection, selection, selectionArgs, sortOrder);
            case CODE_CATEGORY_SUB:
                return DB_category_sub.query(projection, selection, selectionArgs, sortOrder);
            case CODE_INCOME:
                return DB_income.query(projection, selection, selectionArgs, sortOrder);
            case CODE_OUT:
                return DB_out.query(projection, selection, selectionArgs, sortOrder);
            case CODE_OUT_CARD:
                return DB_out_card.query(projection, selection, selectionArgs, sortOrder);
            case CODE_OUT_CASH:
                return DB_out_cash.query(projection, selection, selectionArgs, sortOrder);
            case CODE_OUT_SCHEDULE:
                return DB_out_schedule.query(projection, selection, selectionArgs, sortOrder);
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
            case CODE_CARD:
                return "vnd.android.cursor.dir/vnd.investrecord.card";
            case CODE_CATEGORY_MAIN:
                return "vnd.android.cursor.dir/vnd.investrecord.category_main";
            case CODE_CATEGORY_SUB:
                return "vnd.android.cursor.dir/vnd.investrecord.category_sub";
            case CODE_INCOME:
                return "vnd.android.cursor.dir/vnd.investrecord.income";
            case CODE_OUT:
                return "vnd.android.cursor.dir/vnd.investrecord.out";
            case CODE_OUT_CARD:
                return "vnd.android.cursor.dir/vnd.investrecord.out_card";
            case CODE_OUT_CASH:
                return "vnd.android.cursor.dir/vnd.investrecord.out_cash";
            case CODE_OUT_SCHEDULE:
                return "vnd.android.cursor.dir/vnd.investrecord.out_schedule";
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
            case CODE_CARD:
                DB_card.insert(values);
                break;
            case CODE_CATEGORY_MAIN:
                DB_category_main.insert(values);
                break;
            case CODE_CATEGORY_SUB:
                DB_category_sub.insert(values);
                break;
            case CODE_INCOME:
                DB_income.insert(values);
                break;
            case CODE_OUT:
                DB_out.insert(values);
                break;
            case CODE_OUT_CARD:
                DB_out_card.insert(values);
                break;
            case CODE_OUT_CASH:
                DB_out_cash.insert(values);
                break;
            case CODE_OUT_SCHEDULE:
                DB_out_schedule.insert(values);
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
            case CODE_CARD:
                DB_card.delete(selection, selectionArgs);
                break;
            case CODE_CATEGORY_MAIN:
                DB_category_main.delete(selection, selectionArgs);
                break;
            case CODE_CATEGORY_SUB:
                DB_category_sub.delete(selection, selectionArgs);
                break;
            case CODE_INCOME:
                DB_income.delete(selection, selectionArgs);
                break;
            case CODE_OUT:
                DB_out.delete(selection, selectionArgs);
                break;
            case CODE_OUT_CARD:
                DB_out_card.delete(selection, selectionArgs);
                break;
            case CODE_OUT_CASH:
                DB_out_cash.delete(selection, selectionArgs);
                break;
            case CODE_OUT_SCHEDULE:
                DB_out_schedule.delete(selection, selectionArgs);
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
            case CODE_CARD:
                DB_card.update(values, selection, selectionArgs);
                break;
            case CODE_CATEGORY_MAIN:
                DB_category_main.update(values, selection, selectionArgs);
                break;
            case CODE_CATEGORY_SUB:
                DB_category_sub.update(values, selection, selectionArgs);
                break;
            case CODE_INCOME:
                DB_income.update(values, selection, selectionArgs);
                break;
            case CODE_OUT:
                DB_out.update(values, selection, selectionArgs);
                break;
            case CODE_OUT_CARD:
                DB_out_card.update(values, selection, selectionArgs);
                break;
            case CODE_OUT_CASH:
                DB_out_cash.update(values, selection, selectionArgs);
                break;
            case CODE_OUT_SCHEDULE:
                DB_out_schedule.update(values, selection, selectionArgs);
                break;
            default:
        }
        return count;
    }
}
