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
    private List<Group> groups = new ArrayList<>();
    private List<Account> accounts = new ArrayList<>();
    private List<Info_Dairy> dairies = new ArrayList<>();
    private List<Info_IO> inouts = new ArrayList<>();
    private ContentResolver mContentResolver;
    private static int id_group = 0;

    private static String URI_GROUP = "content://watering.investrecord.provider/group";

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

    public Group getGroup(int id) {
        return groups.get(id);
    }
    public List<Group> getGroup() {
        return groups;
    }

    public void addGroup(String name) {
        ContentValues cv = new ContentValues();

        id_group += 1;
        cv.put("id_group",id_group);
        cv.put("name",name);

        mContentResolver.insert(Uri.parse(URI_GROUP),cv);
        initGroup();
    }

    public void removeGroup(String[] name) {
        mContentResolver.delete(Uri.parse(URI_GROUP),"name",name);
        initGroup();
    }
}
