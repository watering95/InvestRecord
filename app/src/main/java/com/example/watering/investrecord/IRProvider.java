package com.example.watering.investrecord;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.watering.investrecord.dbhelper.*;

/**
 * Created by watering on 17. 10. 21.
 */

@SuppressWarnings("DefaultFileTemplate")
public class IRProvider extends ContentProvider {
    private static final String AUTHORITY = "watering.investrecord.provider";
    private static final String PATH_GROUP = "group";
    private static final String PATH_ACCOUNT = "account";
    private static final String PATH_INFO_IO_KRW = "info_io_krw";
    private static final String PATH_INFO_DAIRY_KRW = "info_dairy_krw";
    private static final String PATH_CARD = "card";
    private static final String PATH_CATEGORY_MAIN = "category_main";
    private static final String PATH_CATEGORY_SUB = "category_sub";
    private static final String PATH_INCOME = "income";
    private static final String PATH_SPEND = "spend";
    private static final String PATH_SPEND_CARD = "spend_card";
    private static final String PATH_SPEND_CASH = "spend_cash";
    private static final String PATH_SPEND_SCHEDULE = "spend_schedule";
    private static final String PATH_JOIN = "join";
    private static final String PATH_TABLE = "table";

    private static final int CODE_GROUP = 0;
    private static final int CODE_ACCOUNT = 1;
    private static final int CODE_INFO_IO_KRW = 2;
    private static final int CODE_INFO_DAIRY_KRW = 3;
    private static final int CODE_CARD = 4;
    private static final int CODE_CATEGORY_MAIN = 5;
    private static final int CODE_CATEGORY_SUB = 6;
    private static final int CODE_INCOME = 7;
    private static final int CODE_SPEND = 8;
    private static final int CODE_SPEND_CARD = 9;
    private static final int CODE_SPEND_CASH = 10;
    private static final int CODE_SPEND_SCHEDULE = 11;
    private static final int CODE_JOIN = 12;
    private static final int CODE_TABLE = 13;

    private GroupDBHelper DB_group;
    private AccountDBHelper DB_account;
    private InfoDairyKRWDBHelper DB_info_dairy_krw;
    private InfoIOKRWDBHelper DB_info_IO_krw;
    private CardDBHelper DB_card;
    private CategoryMainDBHelper DB_category_main;
    private CategorySubDBHelper DB_category_sub;
    private IncomeDBHelper DB_income;
    private SpendDBHelper DB_spend;
    private SpendCardDBHelper DB_spend_card;
    private SpendCashDBHelper DB_spend_cash;

    private static final UriMatcher Matcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        Matcher.addURI(AUTHORITY,PATH_GROUP,CODE_GROUP);
        Matcher.addURI(AUTHORITY,PATH_ACCOUNT,CODE_ACCOUNT);
        Matcher.addURI(AUTHORITY, PATH_INFO_IO_KRW, CODE_INFO_IO_KRW);
        Matcher.addURI(AUTHORITY, PATH_INFO_DAIRY_KRW, CODE_INFO_DAIRY_KRW);
        Matcher.addURI(AUTHORITY,PATH_CARD,CODE_CARD);
        Matcher.addURI(AUTHORITY,PATH_CATEGORY_MAIN,CODE_CATEGORY_MAIN);
        Matcher.addURI(AUTHORITY,PATH_CATEGORY_SUB,CODE_CATEGORY_SUB);
        Matcher.addURI(AUTHORITY,PATH_INCOME,CODE_INCOME);
        Matcher.addURI(AUTHORITY,PATH_SPEND,CODE_SPEND);
        Matcher.addURI(AUTHORITY,PATH_SPEND_CARD,CODE_SPEND_CARD);
        Matcher.addURI(AUTHORITY,PATH_SPEND_CASH,CODE_SPEND_CASH);
        Matcher.addURI(AUTHORITY,PATH_SPEND_SCHEDULE,CODE_SPEND_SCHEDULE);
        Matcher.addURI(AUTHORITY,PATH_JOIN,CODE_JOIN);
        Matcher.addURI(AUTHORITY,PATH_TABLE,CODE_TABLE);
    }

    public boolean onCreate() {
        DB_group = new GroupDBHelper(getContext());
        DB_account = new AccountDBHelper(getContext());
        DB_info_dairy_krw = new InfoDairyKRWDBHelper(getContext());
        DB_info_IO_krw = new InfoIOKRWDBHelper(getContext());
        DB_card = new CardDBHelper(getContext());
        DB_category_main = new CategoryMainDBHelper(getContext());
        DB_category_sub = new CategorySubDBHelper(getContext());
        DB_income = new IncomeDBHelper(getContext());
        DB_spend = new SpendDBHelper(getContext());
        DB_spend_card = new SpendCardDBHelper(getContext());
        DB_spend_cash = new SpendCashDBHelper(getContext());

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        Cursor cursor;

        switch (Matcher.match(uri)) {
            case CODE_GROUP:
                cursor = DB_group.query(projection, selection, selectionArgs, sortOrder);
                break;
            case CODE_ACCOUNT:
                cursor = DB_account.query(projection, selection, selectionArgs, sortOrder);
                break;
            case CODE_INFO_IO_KRW:
                cursor = DB_info_IO_krw.query(projection, selection, selectionArgs, sortOrder);
                break;
            case CODE_INFO_DAIRY_KRW:
                cursor = DB_info_dairy_krw.query(projection, selection, selectionArgs, sortOrder);
                break;
            case CODE_CARD:
                cursor = DB_card.query(projection, selection, selectionArgs, sortOrder);
                break;
            case CODE_CATEGORY_MAIN:
                cursor = DB_category_main.query(projection, selection, selectionArgs, sortOrder);
                break;
            case CODE_CATEGORY_SUB:
                cursor = DB_category_sub.query(projection, selection, selectionArgs, sortOrder);
                break;
            case CODE_INCOME:
                cursor = DB_income.query(projection, selection, selectionArgs, sortOrder);
                break;
            case CODE_SPEND:
                cursor = DB_spend.query(projection, selection, selectionArgs, sortOrder);
                break;
            case CODE_SPEND_CARD:
                cursor = DB_spend_card.query(projection, selection, selectionArgs, sortOrder);
                break;
            case CODE_SPEND_CASH:
                cursor = DB_spend_cash.query(projection, selection, selectionArgs, sortOrder);
                break;
            case CODE_JOIN:
            case CODE_TABLE:
                @SuppressWarnings("ConstantConditions")
                SQLiteDatabase db = getContext().openOrCreateDatabase("InvestRecord.db",Context.MODE_PRIVATE,null);

                assert projection != null;
                String sql = "SELECT " + projection[0] + " WHERE " + selection;
                cursor = db.rawQuery(sql, selectionArgs);
                break;
            default:
                return null;
        }
        return cursor;
    }
    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (Matcher.match(uri)) {
            case CODE_GROUP:
                return "vnd.android.cursor.dir/vnd.investrecord.group";
            case CODE_ACCOUNT:
                return "vnd.android.cursor.dir/vnd.investrecord.account";
            case CODE_INFO_IO_KRW:
                return "vnd.android.cursor.dir/vnd.investrecord.info_io_krw";
            case CODE_INFO_DAIRY_KRW:
                return "vnd.android.cursor.dir/vnd.investrecord.info_dairy_krw";
            case CODE_CARD:
                return "vnd.android.cursor.dir/vnd.investrecord.card";
            case CODE_CATEGORY_MAIN:
                return "vnd.android.cursor.dir/vnd.investrecord.category_main";
            case CODE_CATEGORY_SUB:
                return "vnd.android.cursor.dir/vnd.investrecord.category_sub";
            case CODE_INCOME:
                return "vnd.android.cursor.dir/vnd.investrecord.income";
            case CODE_SPEND:
                return "vnd.android.cursor.dir/vnd.investrecord.spend";
            case CODE_SPEND_CARD:
                return "vnd.android.cursor.dir/vnd.investrecord.spend_card";
            case CODE_SPEND_CASH:
                return "vnd.android.cursor.dir/vnd.investrecord.spend_cash";
            case CODE_SPEND_SCHEDULE:
                return "vnd.android.cursor.dir/vnd.investrecord.spend_schedule";
            case CODE_JOIN:
                return "vnd.android.cursor.dir/vnd.investrecord.join";
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
            case CODE_INFO_IO_KRW:
                DB_info_IO_krw.insert(values);
                break;
            case CODE_INFO_DAIRY_KRW:
                DB_info_dairy_krw.insert(values);
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
            case CODE_SPEND:
                DB_spend.insert(values);
                break;
            case CODE_SPEND_CARD:
                DB_spend_card.insert(values);
                break;
            case CODE_SPEND_CASH:
                DB_spend_cash.insert(values);
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
            case CODE_INFO_IO_KRW:
                DB_info_IO_krw.delete(selection, selectionArgs);
                break;
            case CODE_INFO_DAIRY_KRW:
                DB_info_dairy_krw.delete(selection, selectionArgs);
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
            case CODE_SPEND:
                DB_spend.delete(selection, selectionArgs);
                break;
            case CODE_SPEND_CARD:
                DB_spend_card.delete(selection, selectionArgs);
                break;
            case CODE_SPEND_CASH:
                DB_spend_cash.delete(selection, selectionArgs);
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
            case CODE_INFO_IO_KRW:
                DB_info_IO_krw.update(values, selection, selectionArgs);
                break;
            case CODE_INFO_DAIRY_KRW:
                DB_info_dairy_krw.update(values, selection, selectionArgs);
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
            case CODE_SPEND:
                DB_spend.update(values, selection, selectionArgs);
                break;
            case CODE_SPEND_CARD:
                DB_spend_card.update(values, selection, selectionArgs);
                break;
            case CODE_SPEND_CASH:
                DB_spend_cash.update(values, selection, selectionArgs);
                break;
            default:
        }
        return count;
    }
}
