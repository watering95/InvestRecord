package com.example.watering.investrecord;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by watering on 17. 11. 1.
 */

public class DBManager {
    private ContentResolver mContentResolver;
    private static int id_group = 0;
    private static int id_account = 0;
    private static int currentGroup;
    private static int currentAccount;

    private List<Group> groups = new ArrayList<>();
    private List<Account> accounts = new ArrayList<>();
    private List<Info_Dairy> dairies = new ArrayList<>();
    private List<Info_IO> inouts = new ArrayList<>();

    private static String URI_GROUP = "content://watering.investrecord.provider/group";
    private static String URI_ACCOUNT = "content://watering.investrecord.provider/account";

    public void getContentResolver(ContentResolver cr) {
        this.mContentResolver = cr;
    }

    public int initGroup() {
        groups.clear();

        Cursor cursor;
        cursor = mContentResolver.query(Uri.parse(URI_GROUP),null,null,null,null);

        if(cursor.getCount() < 1) {
            return -1;
        }

        while(cursor.moveToNext()) {
            Group group = new Group();
            id_group = cursor.getInt(0);
            group.setId(id_group);
            group.setName(cursor.getString(1));
            groups.add(group);
        }

        return cursor.getCount();
    }
    public int initAccount() {
        accounts.clear();

        Cursor cursor;
        cursor = mContentResolver.query(Uri.parse(URI_ACCOUNT),null,null,null,null);

        if(cursor.getCount() < 1) {
            return -1;
        }

        while(cursor.moveToNext()) {
            Account account = new Account();

            id_account = cursor.getInt(0);
            account.setId(id_account);
            account.setInstitute(cursor.getString(1));
            account.setNumber(cursor.getString(2));
            account.setDiscription(cursor.getString(3));
            account.setGroup(cursor.getInt(4));
            accounts.add(account);
        }

        return cursor.getCount();
    }

    public Group getGroup(int id) {
        return groups.get(id);
    }
    public List<Group> getGroups() {
        return groups;
    }
    public Account getAccount(int id) {
        return accounts.get(id);
    }
    public List<Account> getAccounts() {
        return accounts;
    }

    public void addGroup(String name) {
        ContentValues cv = new ContentValues();

        id_group += 1;
        cv.put("id_group",id_group);
        cv.put("name",name);

        mContentResolver.insert(Uri.parse(URI_GROUP),cv);
        initGroup();
    }

    public void addAccount(String institute, String number, String discription) {
        ContentValues cv = new ContentValues();

        id_account += 1;
        cv.put("id_account",id_account);
        cv.put("inst", institute);
        cv.put("num",number);
        cv.put("disc",discription);
        cv.put("id_group",currentGroup);

        mContentResolver.insert(Uri.parse(URI_ACCOUNT),cv);
        initAccount();
    }

    public void removeGroup(String[] name) {
        mContentResolver.delete(Uri.parse(URI_GROUP),"name",name);
        initGroup();
    }

    public void removeAccount(String[] number) {
        mContentResolver.delete(Uri.parse(URI_ACCOUNT),"num",number);
        initAccount();
    }

    public void setCurrentGroup(int group) {
        currentGroup = group;
    }
    public int getCurrentGroup() {
        return currentGroup;
    }
    public void setCurrentAccount(int account) {
        currentAccount = account;
    }
    public int getCurrentAccount() {
        return currentAccount;
    }
}
