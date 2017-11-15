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

public class IRResolver {

    private ContentResolver cr;
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
    private static String URI_INFO_IO = "content://watering.investrecord.provider/info_io";
    private static String URI_INFO_DAIRY = "content://watering.investrecord.provider/info_dairy";

    public void getContentResolver(ContentResolver cr) {
        this.cr = cr;
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
    public List<Info_IO> getInouts() {
        return inouts;
    }
    public List<Info_Dairy> getDairies() {
        return dairies;
    }

    public int initGroup() {
        groups.clear();

        Cursor cursor;
        cursor = cr.query(Uri.parse(URI_GROUP),null,null,null,null);

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
        cursor = cr.query(Uri.parse(URI_ACCOUNT),null,null,null,null);

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

    public void addGroup(String name) {
        Group group = new Group();
        ContentValues cv = new ContentValues();

        id_group += 1;
        group.setId(id_group);
        group.setName(name);
        cv.put("id_group",id_group);
        cv.put("name",name);

        cr.insert(Uri.parse(URI_GROUP),cv);
        groups.add(group);
    }
    public void addAccount(String institute, String number, String discription) {
        Account account = new Account();
        ContentValues cv = new ContentValues();

        id_account += 1;
        account.setId(id_account);
        account.setInstitute(institute);
        account.setNumber(number);
        account.setDiscription(discription);
        account.setGroup(currentGroup);

        cv.put("id_account",id_account);
        cv.put("inst", institute);
        cv.put("num",number);
        cv.put("disc",discription);
        cv.put("id_group",currentGroup);

        cr.insert(Uri.parse(URI_ACCOUNT),cv);
        accounts.add(account);
    }
    public void addInfoIO(String date, int input, int output, int evaluation) {
        Info_IO inout = new Info_IO();
        ContentValues cv = new ContentValues();

        inout.setDate(date);
        inout.setAccount(currentAccount);
        inout.setInput(input);
        inout.setOutput(output);
        inout.setEvaluation(evaluation);

        cv.put("id_account",currentAccount);
        cv.put("date", date);
        cv.put("input",input);
        cv.put("output",output);
        cv.put("evaluation",evaluation);

        cr.insert(Uri.parse(URI_INFO_IO),cv);
        inouts.add(inout);
    }
    public void addInfoDairy(String date,int evaluation,int principal,double rate) {
        Info_Dairy dairy = new Info_Dairy();
        ContentValues cv = new ContentValues();

        dairy.setDate(date);
        dairy.setAccount(currentAccount);
        dairy.setPrincipal(principal);
        dairy.setRate(rate);

        cv.put("id_account", currentAccount);
        cv.put("date", date);
        cv.put("principal",principal);
        cv.put("rate",rate);

        cr.insert(Uri.parse(URI_INFO_DAIRY),cv);
        dairies.add(dairy);
    }

    public void removeGroup(String[] name) {
        cr.delete(Uri.parse(URI_GROUP),"name",name);
        initGroup();
    }
    public void removeAccount(String[] number) {
        cr.delete(Uri.parse(URI_ACCOUNT),"num",number);
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

    public Info_IO getInfoIO(String account, String date) {
        Cursor c;
        Info_IO io = new Info_IO();

        String where = "id_account = '" + account + "' AND date = '" + date + "';";

        c = cr.query(Uri.parse(URI_INFO_IO), null, where, null, null);

        if(c.getCount() == 0) return null;

        c.moveToNext();

        io.setInput(c.getInt(c.getColumnIndex("input")));
        io.setOutput(c.getInt(c.getColumnIndex("output")));
        io.setEvaluation(c.getInt(c.getColumnIndex("evaluation")));
        io.setAccount(c.getInt(c.getColumnIndex("id_account")));
        io.setDate(c.getString(c.getColumnIndex("date")));

        return io;
    }

    public int getSum(String[] column, String selectedDate) {

        Cursor c;

        String[] total = {"total(" + column[0] + ") AS SUM"};
        String where = "date < date('" + selectedDate + "')";

        c = cr.query(Uri.parse(URI_INFO_IO), total, where, null, null);
        c.moveToNext();
        return c.getInt(0);
    }
}
