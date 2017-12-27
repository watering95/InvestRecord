package com.example.watering.investrecord;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by watering on 17. 11. 1.
 */

@SuppressWarnings("ALL")
public class IRResolver {

    private ContentResolver cr;
    private static int currentGroup=-1;
    private static int currentAccount=-1;

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

    private final List<Group> groups = new ArrayList<>();
    private final List<Account> accounts = new ArrayList<>();
    private final List<Info_Dairy> dairies = new ArrayList<>();
    private final List<CategoryMain> categoryMains = new ArrayList<>();
    private final List<CategorySub> categorySubs = new ArrayList<>();
    private final List<Card> cards = new ArrayList<>();

    private static final String URI_GROUP = "content://watering.investrecord.provider/group";
    private static final String URI_ACCOUNT = "content://watering.investrecord.provider/account";
    private static final String URI_INFO_IO = "content://watering.investrecord.provider/info_io";
    private static final String URI_INFO_DAIRY = "content://watering.investrecord.provider/info_dairy";
    private static final String URI_CARD = "content://watering.investrecord.provider/card";
    private static final String URI_CATEGORY_MAIN = "content://watering.investrecord.provider/category_main";
    private static final String URI_CATEGORY_SUB = "content://watering.investrecord.provider/category_sub";
    private static final String URI_INCOME = "content://watering.investrecord.provider/income";
    private static final String URI_OUT = "content://watering.investrecord.provider/out";
    private static final String URI_OUT_CARD = "content://watering.investrecord.provider/out_card";
    private static final String URI_OUT_CASH = "content://watering.investrecord.provider/out_cash";
    private static final String URI_OUT_SCHEDULE = "content://watering.investrecord.provider/out_schedule";

    public void getContentResolver(ContentResolver cr) {
        this.cr = cr;
    }

    public List<Group> getGroups() {
        groups.clear();
        getData(CODE_GROUP, URI_GROUP, null,null,null);
        return groups;
    }
    public List<Account> getAccounts() {
        String[] selectionArgs = new String[] {String.valueOf(currentGroup)};

        accounts.clear();
        getData(CODE_ACCOUNT,URI_ACCOUNT,"id_group=?",selectionArgs,null);
        return accounts;
    }
    public List<Info_Dairy> getInfoDaires() {
        String[] selectionArgs = new String[] {String.valueOf(currentAccount)};

        dairies.clear();
        getData(CODE_INFO_DAIRY, URI_INFO_DAIRY,"id_account=?",selectionArgs,"date DESC");
        return dairies;
    }
    public List<CategoryMain> getCategoryMains(int c) {
        categoryMains.clear();
        String selection = null;
        String[] selectionArgs = null;

        switch(c) {
            case 0:
                selection = null;
                selectionArgs = null;
                break;
            case 1:
                selection = "kind=?";
                selectionArgs = new String[] {"spend"};
                break;
            case 2:
                selection = "kind=?";
                selectionArgs = new String[] {"income"};
                break;
        }
        getData(CODE_CATEGORY_MAIN, URI_CATEGORY_MAIN,selection,selectionArgs,null);
        return categoryMains;
    }
    public List<CategorySub> getCategorySubs(int id_main) {
        String[] selectionArgs = new String[] {String.valueOf(id_main)};

        categorySubs.clear();
        getData(CODE_CATEGORY_SUB, URI_CATEGORY_SUB,"id_main=?",selectionArgs,null);
        return categorySubs;
    }
    public List<Card> getCards() {
        cards.clear();
        getData(CODE_CARD, URI_CARD,null,null,null);
        return cards;
    }

    public Account getAccount(String id_account) {
        Cursor c;
        Account account = new Account();

        String where = "_id=?";
        String[] selectionArgs = {id_account};

        c = cr.query(Uri.parse(URI_ACCOUNT), null, where, selectionArgs, null);

        assert c != null;
        if(c.getCount() == 0) return null;

        c.moveToNext();

        account.setGroup(c.getInt(c.getColumnIndex("id_group")));
        account.setNumber(c.getString(c.getColumnIndex("num")));
        account.setInstitute(c.getString(c.getColumnIndex("inst")));
        account.setDiscription(c.getString(c.getColumnIndex("disc")));
        account.setId(c.getInt(c.getColumnIndex("_id")));

        c.close();

        return account;
    }
    public Info_IO getInfoIO(int id_account, String date) {
        Cursor c;
        Info_IO io = new Info_IO();

        String where = "id_account=? and date=?";
        String[] selectionArgs = new String[]{"",""};
        selectionArgs[0] = String.valueOf(id_account);
        selectionArgs[1] = date;

        c = cr.query(Uri.parse(URI_INFO_IO), null, where, selectionArgs, null);

        assert c != null;
        if(c.getCount() == 0) return null;

        c.moveToNext();

        io.setId(c.getInt(c.getColumnIndex("_id")));
        io.setInput(c.getInt(c.getColumnIndex("input")));
        io.setOutput(c.getInt(c.getColumnIndex("output")));
        io.setEvaluation(c.getInt(c.getColumnIndex("evaluation")));
        io.setAccount(c.getInt(c.getColumnIndex("id_account")));
        io.setDate(c.getString(c.getColumnIndex("date")));

        c.close();

        return io;
    }
    public Info_Dairy getInfoDairy(String account, String date) {
        Cursor c;
        Info_Dairy dairy = new Info_Dairy();

        String where = "id_account=? and date=?";
        String[] selectionArgs = new String[]{"",""};
        selectionArgs[0] = account;
        selectionArgs[1] = date;

        c = cr.query(Uri.parse(URI_INFO_DAIRY), null, where, selectionArgs, null);

        assert c != null;
        if(c.getCount() == 0) return null;

        c.moveToNext();

        dairy.setId(c.getInt(c.getColumnIndex("_id")));
        dairy.setRate(c.getInt(c.getColumnIndex("rate")));
        dairy.setPrincipal(c.getInt(c.getColumnIndex("principal")));
        dairy.setAccount(c.getInt(c.getColumnIndex("id_account")));
        dairy.setDate(c.getString(c.getColumnIndex("date")));

        c.close();

        return dairy;
    }
    public CategoryMain getCategoryMain(String id_main) {
        Cursor c;
        CategoryMain categoryMain = new CategoryMain();

        String where = "_id=?";
        String[] selectionArgs = {id_main};

        c = cr.query(Uri.parse(URI_CATEGORY_MAIN), null, where, selectionArgs, null);

        assert c != null;
        if(c.getCount() == 0) return null;

        c.moveToNext();

        categoryMain.setId(c.getInt(c.getColumnIndex("_id")));
        categoryMain.setName(c.getString(c.getColumnIndex("name")));
        categoryMain.setKind(c.getString(c.getColumnIndex("kind")));

        c.close();

        return categoryMain;
    }
    public CategorySub getCategorySub(String id_sub) {
        Cursor c;
        CategorySub categorySub = new CategorySub();

        String where = "_id=?";
        String[] selectionArgs = {id_sub};

        c = cr.query(Uri.parse(URI_CATEGORY_SUB), null, where, selectionArgs, null);

        assert c != null;
        if(c.getCount() == 0) return null;

        c.moveToNext();

        categorySub.setId(c.getInt(c.getColumnIndex("_id")));
        categorySub.setName(c.getString(c.getColumnIndex("name")));
        categorySub.setCategoryMain(c.getInt(c.getColumnIndex("id_main")));

        c.close();

        return categorySub;
    }

    public int getSum(String[] column, String selectedDate) {
        Cursor c;

        String[] total = {"total(" + column[0] + ") AS SUM"};
        String where = "date<=? and id_account=?";
        String[] selectionArgs = new String[]{"",""};
        selectionArgs[0] = selectedDate;
        selectionArgs[1] = String.valueOf(currentAccount);

        c = cr.query(Uri.parse(URI_INFO_IO), total, where, selectionArgs, null);
        assert c != null;
        c.moveToNext();

        int sum = c.getInt(0);
        c.close();

        return sum;
    }
    public Info_Dairy getLastInfoDairy(String account) {
        String[] selectionArgs = new String[] {account};

        dairies.clear();
        getData(CODE_INFO_DAIRY, URI_INFO_DAIRY, "id_account=?",selectionArgs,"date DESC");
        if(dairies.isEmpty()) return null;
        else return dairies.get(0);
    }

    public int getCurrentGroup() {
        return currentGroup;
    }
    public int getCurrentAccount() {
        return currentAccount;
    }

    public void insertGroup(String name) {
        ContentValues cv = new ContentValues();

        cv.put("name",name);
        cr.insert(Uri.parse(URI_GROUP),cv);
    }
    public void insertAccount(String institute, String number, String discription) {

        if(currentGroup == -1) return;

        ContentValues cv = new ContentValues();

        cv.put("inst", institute);
        cv.put("num",number);
        cv.put("disc",discription);
        cv.put("id_group",currentGroup);

        cr.insert(Uri.parse(URI_ACCOUNT),cv);
    }
    public void insertInfoIO(String date, int input, int output, int evaluation) {

        if(currentGroup == -1 || currentAccount == -1) return;

        ContentValues cv = new ContentValues();

        cv.put("id_account",currentAccount);
        cv.put("date", date);
        cv.put("input",input);
        cv.put("output",output);
        cv.put("evaluation",evaluation);

        cr.insert(Uri.parse(URI_INFO_IO),cv);
    }
    public void insertInfoDairy(String date, int principal, double rate) {

        if(currentGroup == -1 || currentAccount == -1) return;

        ContentValues cv = new ContentValues();

        cv.put("id_account", currentAccount);
        cv.put("date", date);
        cv.put("principal",principal);
        cv.put("rate",rate);

        cr.insert(Uri.parse(URI_INFO_DAIRY),cv);
    }
    public void insertCategoryMain(String name,String kind) {
        ContentValues cv = new ContentValues();

        cv.put("name",name);
        cv.put("kind",kind);
        cr.insert(Uri.parse(URI_CATEGORY_MAIN),cv);
    }
    public void insertCategorySub(String name,int main) {
        ContentValues cv = new ContentValues();

        cv.put("name",name);
        cv.put("id_main",main);
        cr.insert(Uri.parse(URI_CATEGORY_SUB),cv);
    }
    public void insertCard(String name,String num,String com,int date) {
        ContentValues cv = new ContentValues();

        cv.put("name",name);
        cv.put("number",num);
        cv.put("company",com);
        cv.put("date_draw",date);
        cr.insert(Uri.parse(URI_CARD),cv);
    }

    public void deleteAll() {
        deleteGroup(null,null);
        deleteAccount(null, null);
        deleteInfoIO(null, null);
        deleteInfoDairy();
    }
    public void deleteGroup(String where, String[] args) {
        cr.delete(Uri.parse(URI_GROUP),where,args);
    }
    public void deleteAccount(String where, String[] args) {
        cr.delete(Uri.parse(URI_ACCOUNT),where,args);
    }
    public void deleteInfoIO(String where, String[] args) {
        cr.delete(Uri.parse(URI_INFO_IO),where,args);
    }
    public void deleteCategoryMain(String where, String[] args) {
        cr.delete(Uri.parse(URI_CATEGORY_MAIN),where,args);
    }
    public void deleteCategorySub(String where, String[] args) {
        cr.delete(Uri.parse(URI_CATEGORY_SUB),where,args);
    }
    public void deleteCard(String where, String[] args) {
        cr.delete(Uri.parse(URI_CARD),where,args);
    }
    private void deleteInfoDairy() {
        cr.delete(Uri.parse(URI_INFO_DAIRY), null, null);
    }

    public void updateGroup(int id, String name) {
        ContentValues cv = new ContentValues();
        String where = "_id";
        String[] selectionArgs = new String[] {String.valueOf(id)};

        cv.put("name",name);

        cr.update(Uri.parse(URI_GROUP),cv,where,selectionArgs);
    }
    public void updateAccount(int id, String institute,String account,String discript) {
        ContentValues cv = new ContentValues();
        String where = "_id";
        String[] selectionArgs = new String[] {String.valueOf(id)};

        cv.put("inst",institute);
        cv.put("num",account);
        cv.put("disc",discript);

        cr.update(Uri.parse(URI_ACCOUNT),cv,where,selectionArgs);
    }
    public void updateInfoIO(int id, String date, int input, int output, int evaluation) {
        ContentValues cv = new ContentValues();
        String where = "_id";
        String[] selectionArgs = new String[] {String.valueOf(id)};

        cv.put("id_account",currentAccount);
        cv.put("date", date);
        cv.put("input",input);
        cv.put("output",output);
        cv.put("evaluation",evaluation);

        cr.update(Uri.parse(URI_INFO_IO),cv,where,selectionArgs);
    }
    public void updateInfoDairy(int id, String date, int principal, double rate) {
        ContentValues cv = new ContentValues();
        String where = "_id";
        String[] selectionArgs = new String[] {String.valueOf(id)};

        cv.put("id_account", currentAccount);
        cv.put("date", date);
        cv.put("principal",principal);
        cv.put("rate",String.format(Locale.getDefault(),"%.2f",rate));

        cr.update(Uri.parse(URI_INFO_DAIRY),cv,where,selectionArgs);
    }
    public void updateCategoryMain(int id, String name) {
        ContentValues cv = new ContentValues();
        String where = "_id";
        String[] selectionArgs = new String[] {String.valueOf(id)};

        cv.put("name",name);

        cr.update(Uri.parse(URI_CATEGORY_MAIN),cv,where,selectionArgs);
    }
    public void updateCategorySub(int id, String name, int main_id) {
        ContentValues cv = new ContentValues();
        String where = "_id";
        String[] selectionArgs = new String[] {String.valueOf(id)};

        cv.put("name",name);
        cv.put("main_id",main_id);

        cr.update(Uri.parse(URI_GROUP),cv,where,selectionArgs);
    }
    public void updateCard(int id, String name, String num, String com, int date) {
        ContentValues cv = new ContentValues();
        String where = "_id";
        String[] selectionArgs = new String[] {String.valueOf(id)};

        cv.put("name",name);
        cv.put("company",com);
        cv.put("number",num);
        cv.put("date_draw",date);

        cr.update(Uri.parse(URI_CARD),cv,where,selectionArgs);
    }

    public void setCurrentGroup(int group) {
        currentGroup = group;
    }
    public void setCurrentAccount(int account) {
        currentAccount = account;
    }

    private void getData(int code, String uri, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor;

        cursor = cr.query(Uri.parse(uri),null, selection, selectionArgs, sortOrder);

        if((cursor != null ? cursor.getCount() : 0) < 1) {
            switch (code) {
                case CODE_GROUP:
                    currentGroup = -1;
                    break;
                case CODE_ACCOUNT:
                    currentAccount = -1;
                    break;
            }
            return;
        }

        assert cursor != null;
        while(cursor.moveToNext()) {
            switch (code) {
                case CODE_GROUP:
                    Group group = new Group();

                    group.setId(cursor.getInt(0));
                    group.setName(cursor.getString(1));

                    groups.add(group);
                    break;
                case CODE_ACCOUNT:
                    Account account = new Account();

                    account.setId(cursor.getInt(0));
                    account.setInstitute(cursor.getString(1));
                    account.setNumber(cursor.getString(2));
                    account.setDiscription(cursor.getString(3));
                    account.setGroup(cursor.getInt(4));

                    accounts.add(account);
                    break;
                case CODE_INFO_IO:
                    break;
                case CODE_INFO_DAIRY:
                    Info_Dairy dairy = new Info_Dairy();

                    dairy.setId(cursor.getInt(0));
                    dairy.setDate(cursor.getString(1));
                    dairy.setPrincipal(cursor.getInt(2));
                    dairy.setRate(cursor.getDouble(3));
                    dairy.setAccount(cursor.getInt(4));

                    dairies.add(dairy);
                    break;
                case CODE_CATEGORY_MAIN:
                    CategoryMain categoryMain = new CategoryMain();

                    categoryMain.setId(cursor.getInt(0));
                    categoryMain.setName(cursor.getString(1));

                    categoryMains.add(categoryMain);
                    break;
                case CODE_CATEGORY_SUB:
                    CategorySub categorySub = new CategorySub();

                    categorySub.setId(cursor.getInt(0));
                    categorySub.setName(cursor.getString(1));
                    categorySub.setCategoryMain(cursor.getInt(2));

                    categorySubs.add(categorySub);
                    break;
                case CODE_CARD:
                    Card card = new Card();

                    card.setId(cursor.getInt(0));
                    card.setName(cursor.getString(1));
                    card.setNumber(cursor.getString(2));
                    card.setCompany(cursor.getString(3));
                    card.setDrawDate(cursor.getInt(4));

                    cards.add(card);
                    break;
            }
        }

        cursor.close();
    }
}
