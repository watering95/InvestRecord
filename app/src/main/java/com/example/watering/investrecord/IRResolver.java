package com.example.watering.investrecord;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.example.watering.investrecord.data.*;
import com.example.watering.investrecord.info.InfoDairyForeign;
import com.example.watering.investrecord.info.InfoDairyKRW;
import com.example.watering.investrecord.info.InfoDairyTotal;
import com.example.watering.investrecord.info.InfoIOForeign;
import com.example.watering.investrecord.info.InfoIOKRW;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by watering on 17. 11. 1.
 */

@SuppressWarnings("DefaultFileTemplate")
public class IRResolver {

    private ContentResolver cr;
    private static int currentGroup = -1;
    private static int currentAccount = -1;
    private static final String TAG = "InvestRecord";

    private static final int CODE_GROUP = 0;
    private static final int CODE_ACCOUNT = 1;
    private static final int CODE_INFO_IO_KRW = 2;
    private static final int CODE_INFO_IO_FOREIGN = 14;
    private static final int CODE_INFO_DAIRY_KRW = 3;
    private static final int CODE_INFO_DAIRY_FOREIGN = 15;
    private static final int CODE_INFO_DAIRY_TOTAL = 16;
    private static final int CODE_CARD = 4;
    private static final int CODE_CATEGORY_MAIN = 5;
    private static final int CODE_CATEGORY_SUB = 6;
    private static final int CODE_INCOME = 7;
    private static final int CODE_SPEND = 8;
    private static final int CODE_SPEND_CARD = 9;
    private static final int CODE_SPEND_CASH = 10;

    private static final int KIND_ALL = 0, KIND_SPEND = 1, KIND_INCOME = 2;

    private final List<Group> groups = new ArrayList<>();
    private final List<Account> accounts = new ArrayList<>();
    private final List<InfoIOKRW> IOs_krw = new ArrayList<>();
    private final List<InfoIOForeign> IOs_foreign = new ArrayList<>();
    private final List<InfoDairyKRW> dairies_krw = new ArrayList<>();
    private final List<InfoDairyForeign> dairies_foreign = new ArrayList<>();
    private final List<InfoDairyTotal> dairies_total = new ArrayList<>();
    private final List<CategoryMain> categoryMains = new ArrayList<>();
    private final List<CategorySub> categorySubs = new ArrayList<>();
    private final List<Card> cards = new ArrayList<>();
    private final List<Spend> spends = new ArrayList<>();
    private final List<SpendCard> spends_card = new ArrayList<>();
    private final List<SpendCash> spends_cash = new ArrayList<>();
    private final List<Income> incomes = new ArrayList<>();

    private static final String URI_GROUP = "content://watering.investrecord.provider/group";
    private static final String URI_ACCOUNT = "content://watering.investrecord.provider/account";
    private static final String URI_INFO_IO_KRW = "content://watering.investrecord.provider/info_io_krw";
    private static final String URI_INFO_IO_FOREIGN = "content://watering.investrecord.provider/info_io_foreign";
    private static final String URI_INFO_DAIRY_KRW = "content://watering.investrecord.provider/info_dairy_krw";
    private static final String URI_INFO_DAIRY_FOREIGN = "content://watering.investrecord.provider/info_dairy_foreign";
    private static final String URI_INFO_DAIRY_TOTAL = "content://watering.investrecord.provider/info_dairy_total";
    private static final String URI_CARD = "content://watering.investrecord.provider/card";
    private static final String URI_CATEGORY_MAIN = "content://watering.investrecord.provider/category_main";
    private static final String URI_CATEGORY_SUB = "content://watering.investrecord.provider/category_sub";
    private static final String URI_INCOME = "content://watering.investrecord.provider/income";
    private static final String URI_SPEND = "content://watering.investrecord.provider/spend";
    private static final String URI_SPEND_CARD = "content://watering.investrecord.provider/spend_card";
    private static final String URI_SPEND_CASH = "content://watering.investrecord.provider/spend_cash";
    private static final String URI_JOIN = "content://watering.investrecord.provider/join";
    private static final String URI_TABLE = "content://watering.investrecord.provider/table";

    void getContentResolver(ContentResolver cr) {
        this.cr = cr;
    }

    public List<Group> getGroups() {
        groups.clear();
        getData(CODE_GROUP, URI_GROUP, null,null,null);
        return groups;
    }
    public List<Account> getAccounts(int id_group) {
        String[] selectionArgs = new String[] {String.valueOf(id_group)};

        accounts.clear();
        getData(CODE_ACCOUNT,URI_ACCOUNT,"id_group=?",selectionArgs,null);
        return accounts;
    }
    public List<InfoDairyKRW> getInfoDairesKRW(int id_account) {
        String[] selectionArgs = new String[] {String.valueOf(id_account)};

        dairies_krw.clear();
        getData(CODE_INFO_DAIRY_KRW, URI_INFO_DAIRY_KRW,"id_account=?",selectionArgs,"date DESC");
        return dairies_krw;
    }
    public List<InfoDairyTotal> getInfoDairesTotal(int id_account) {
        String[] selectionArgs = new String[] {String.valueOf(id_account)};

        dairies_total.clear();
        getData(CODE_INFO_DAIRY_TOTAL, URI_INFO_DAIRY_TOTAL,"id_account=?",selectionArgs,"date DESC");
        return dairies_total;
    }
    public List<CategoryMain> getCategoryMains(int kind) {
        String selection = null;
        String[] selectionArgs = null;

        categoryMains.clear();

        switch(kind) {
            case KIND_ALL:
                selection = null;
                selectionArgs = null;
                break;
            case KIND_SPEND:
                selection = "kind=?";
                selectionArgs = new String[] {"spend"};
                break;
            case KIND_INCOME:
                selection = "kind=?";
                selectionArgs = new String[] {"income"};
                break;
        }
        getData(CODE_CATEGORY_MAIN, URI_CATEGORY_MAIN, selection,selectionArgs,null);
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
    public List<Spend> getSpends(String date) {
        String[] selectionArgs = new String[] {String.valueOf(date)};

        spends.clear();
        getData(CODE_SPEND, URI_SPEND, "date_use=?",selectionArgs,null);
        return spends;
    }
    public List<Income> getIncomes(String date) {
        String[] selectionArgs = new String[] {String.valueOf(date)};

        incomes.clear();
        getData(CODE_INCOME, URI_INCOME, "date=?",selectionArgs,null);
        return incomes;
    }

    private List<InfoDairyForeign> getInfoDairesForeign(int id_account) {
        String[] selectionArgs = new String[] {String.valueOf(id_account)};

        dairies_krw.clear();
        getData(CODE_INFO_DAIRY_FOREIGN, URI_INFO_DAIRY_FOREIGN,"id_account=?",selectionArgs,"date DESC");
        return dairies_foreign;
    }

    public Group getGroup(int id_group) {
        Cursor c;
        Group group = new Group();

        String where = "_id=?";
        String[] selectionArgs = {String.valueOf(id_group)};

        c = cr.query(Uri.parse(URI_GROUP), null, where, selectionArgs, null);

        assert c != null;
        if(c.getCount() == 0) return null;

        c.moveToNext();

        group.setId(c.getInt(c.getColumnIndex("_id")));
        group.setName(c.getString(c.getColumnIndex("name")));

        c.close();

        return group;
    }
    public Account getAccount(int id_account) {
        Cursor c;
        Account account = new Account();

        String where = "_id=?";
        String[] selectionArgs = {String.valueOf(id_account)};

        c = cr.query(Uri.parse(URI_ACCOUNT), null, where, selectionArgs, null);

        assert c != null;
        if(c.getCount() == 0) return null;

        c.moveToNext();

        account.setGroup(c.getInt(c.getColumnIndex("id_group")));
        account.setNumber(c.getString(c.getColumnIndex("number")));
        account.setInstitute(c.getString(c.getColumnIndex("institute")));
        account.setDescription(c.getString(c.getColumnIndex("description")));
        account.setId(c.getInt(c.getColumnIndex("_id")));

        c.close();

        return account;
    }
    public InfoIOKRW getInfoIOKRW(int id_account, String date) {
        Cursor c;
        InfoIOKRW io_krw = new InfoIOKRW();

        String where = "id_account=? and date=?";
        String[] selectionArgs = new String[]{String.valueOf(id_account),date};

        c = cr.query(Uri.parse(URI_INFO_IO_KRW), null, where, selectionArgs, null);

        assert c != null;
        if(c.getCount() == 0) return null;

        c.moveToNext();

        io_krw.setId(c.getInt(c.getColumnIndex("_id")));
        io_krw.setInput(c.getInt(c.getColumnIndex("input")));
        io_krw.setOutput(c.getInt(c.getColumnIndex("output")));
        io_krw.setEvaluation(c.getInt(c.getColumnIndex("evaluation")));
        io_krw.setAccount(c.getInt(c.getColumnIndex("id_account")));
        io_krw.setDate(c.getString(c.getColumnIndex("date")));
        io_krw.setIncome(c.getInt(c.getColumnIndex("income")));
        io_krw.setSpendCash(c.getInt(c.getColumnIndex("spend_cash")));
        io_krw.setSpendCard(c.getInt(c.getColumnIndex("spend_card")));

        c.close();

        return io_krw;
    }
    public InfoIOForeign getInfoIOForeign(int id_account, int id_currency, String date) {
        Cursor c;
        InfoIOForeign io_foreign = new InfoIOForeign();

        String where = "id_account=? and id_currency=? and date=?";
        String[] selectionArgs = new String[]{String.valueOf(id_account),String.valueOf(id_currency),date};

        c = cr.query(Uri.parse(URI_INFO_IO_FOREIGN), null, where, selectionArgs, null);

        assert c != null;
        if(c.getCount() == 0) return null;

        c.moveToNext();

        io_foreign.setId(c.getInt(c.getColumnIndex("_id")));
        io_foreign.setInput(c.getDouble(c.getColumnIndex("input")));
        io_foreign.setOutput(c.getDouble(c.getColumnIndex("output")));
        io_foreign.setEvaluation(c.getInt(c.getColumnIndex("evaluation")));
        io_foreign.setAccount(c.getInt(c.getColumnIndex("id_account")));
        io_foreign.setDate(c.getString(c.getColumnIndex("date")));
        io_foreign.setInput_krw(c.getInt(c.getColumnIndex("input_krw")));
        io_foreign.setOutput_krw(c.getInt(c.getColumnIndex("output_krw")));
        io_foreign.setCurrency(c.getInt(c.getColumnIndex("id_currency")));

        c.close();

        return io_foreign;
    }
    public CategoryMain getCategoryMain(int id_main) {
        Cursor c;
        CategoryMain categoryMain = new CategoryMain();

        String where = "_id=?";
        String[] selectionArgs = {String.valueOf(id_main)};

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
    public CategorySub getCategorySub(int id_sub) {
        Cursor c;
        CategorySub categorySub = new CategorySub();

        String where = "_id=?";
        String[] selectionArgs = {String.valueOf(id_sub)};

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
    public Card getCard(int id_card) {
        Cursor c;
        Card card = new Card();

        String where = "_id=?";
        String[] selectionArgs = {String.valueOf(id_card)};

        c = cr.query(Uri.parse(URI_CARD), null, where, selectionArgs, null);

        assert c != null;
        if(c.getCount() == 0) return null;

        c.moveToNext();

        card.setId(c.getInt(c.getColumnIndex("_id")));
        card.setName(c.getString(c.getColumnIndex("name")));
        card.setAccount(c.getInt(c.getColumnIndex("id_account")));
        card.setCompany(c.getString(c.getColumnIndex("company")));
        card.setDrawDate(c.getInt(c.getColumnIndex("date_draw")));
        card.setNumber(c.getString(c.getColumnIndex("number")));

        c.close();

        return card;
    }
    public Income getIncome(int id_income) {
        Cursor c;
        Income income = new Income();

        String where = "_id=?";
        String[] selectionArgs = {String.valueOf(id_income)};

        c = cr.query(Uri.parse(URI_INCOME), null, where, selectionArgs, null);

        assert c != null;
        if(c.getCount() == 0) return null;

        c.moveToNext();

        income.setId(c.getInt(c.getColumnIndex("_id")));
        income.setCategory(c.getInt(c.getColumnIndex("id_sub")));
        income.setAmount(c.getInt(c.getColumnIndex("amount")));
        income.setDetails(c.getString(c.getColumnIndex("details")));
        income.setAccount(c.getInt(c.getColumnIndex("id_account")));
        income.setDate(c.getString(c.getColumnIndex("date")));

        c.close();

        return income;
    }
    public Spend getSpend(String code) {
        Cursor c;
        Spend spend = new Spend();

        String where = "spend_code=?";
        String[] selectionArgs = {code};

        c = cr.query(Uri.parse(URI_SPEND), null, where, selectionArgs, null);

        assert c != null;
        if(c.getCount() == 0) return null;

        c.moveToNext();

        spend.setId(c.getInt(c.getColumnIndex("_id")));
        spend.setCategory(c.getInt(c.getColumnIndex("id_sub")));
        spend.setAmount(c.getInt(c.getColumnIndex("amount")));
        spend.setDetails(c.getString(c.getColumnIndex("details")));
        spend.setDate(c.getString(c.getColumnIndex("date_use")));
        spend.setCode(c.getString(c.getColumnIndex("spend_code")));

        c.close();

        return spend;
    }
    public SpendCash getSpendCash(String code) {
        Cursor c;
        SpendCash spendCash = new SpendCash();

        String where = "spend_code=?";
        String[] selectionArgs = {code};

        c = cr.query(Uri.parse(URI_SPEND_CASH), null, where, selectionArgs, null);

        assert c != null;
        if(c.getCount() == 0) return null;

        c.moveToNext();

        spendCash.setId(c.getInt(c.getColumnIndex("_id")));
        spendCash.setAccount(c.getInt(c.getColumnIndex("id_account")));
        spendCash.setCode(c.getString(c.getColumnIndex("spend_code")));

        c.close();

        return spendCash;

    }
    public SpendCard getSpendCard(String code) {
        Cursor c;
        SpendCard spendCard = new SpendCard();

        String where = "spend_code=?";
        String[] selectionArgs = {code};

        c = cr.query(Uri.parse(URI_SPEND_CARD), null, where, selectionArgs, null);

        assert c != null;
        if(c.getCount() == 0) return null;

        c.moveToNext();

        spendCard.setId(c.getInt(c.getColumnIndex("_id")));
        spendCard.setCard(c.getInt(c.getColumnIndex("id_card")));
        spendCard.setCode(c.getString(c.getColumnIndex("spend_code")));

        c.close();

        return spendCard;
    }

    private InfoIOKRW getInfoIOKRW(int id) {
        Cursor c;
        InfoIOKRW io_krw = new InfoIOKRW();

        String where = "_id=?";
        String[] selectionArgs = new String[]{String.valueOf(id)};

        c = cr.query(Uri.parse(URI_INFO_IO_KRW), null, where, selectionArgs, null);

        assert c != null;
        if(c.getCount() == 0) return null;

        c.moveToNext();

        io_krw.setId(c.getInt(c.getColumnIndex("_id")));
        io_krw.setInput(c.getInt(c.getColumnIndex("input")));
        io_krw.setOutput(c.getInt(c.getColumnIndex("output")));
        io_krw.setEvaluation(c.getInt(c.getColumnIndex("evaluation")));
        io_krw.setAccount(c.getInt(c.getColumnIndex("id_account")));
        io_krw.setDate(c.getString(c.getColumnIndex("date")));
        io_krw.setIncome(c.getInt(c.getColumnIndex("income")));
        io_krw.setSpendCash(c.getInt(c.getColumnIndex("spend_cash")));
        io_krw.setSpendCard(c.getInt(c.getColumnIndex("spend_card")));

        c.close();

        return io_krw;
    }
    private InfoIOForeign getInfoIOForeign(int id) {
        Cursor c;
        InfoIOForeign io_foreign = new InfoIOForeign();

        String where = "_id=?";
        String[] selectionArgs = new String[]{String.valueOf(id)};

        c = cr.query(Uri.parse(URI_INFO_IO_FOREIGN), null, where, selectionArgs, null);

        assert c != null;
        if(c.getCount() == 0) return null;

        c.moveToNext();

        io_foreign.setId(c.getInt(c.getColumnIndex("_id")));
        io_foreign.setInput(c.getDouble(c.getColumnIndex("input")));
        io_foreign.setOutput(c.getDouble(c.getColumnIndex("output")));
        io_foreign.setEvaluation(c.getInt(c.getColumnIndex("evaluation")));
        io_foreign.setAccount(c.getInt(c.getColumnIndex("id_account")));
        io_foreign.setDate(c.getString(c.getColumnIndex("date")));
        io_foreign.setOutput_krw(c.getInt(c.getColumnIndex("output_krw")));
        io_foreign.setInput_krw(c.getInt(c.getColumnIndex("input_krw")));
        io_foreign.setCurrency(c.getInt(c.getColumnIndex("id_currency")));

        c.close();

        return io_foreign;
    }
    private InfoDairyForeign getInfoDairyForeign(int id_account, int id_currency, String date) {
        Cursor c;
        InfoDairyForeign dairy_foreign = new InfoDairyForeign();

        String where = "id_account=? and id_currency=? and date=?";
        String[] selectionArgs = new String[]{String.valueOf(id_account),String.valueOf(id_currency),date};

        c = cr.query(Uri.parse(URI_INFO_DAIRY_FOREIGN), null, where, selectionArgs, null);

        assert c != null;
        if(c.getCount() == 0) return null;

        c.moveToNext();

        dairy_foreign.setId(c.getInt(c.getColumnIndex("_id")));
        dairy_foreign.setPrincipal(c.getDouble(c.getColumnIndex("principal")));
        dairy_foreign.setRate(c.getDouble(c.getColumnIndex("rate")));
        dairy_foreign.setAccount(c.getInt(c.getColumnIndex("id_account")));
        dairy_foreign.setDate(c.getString(c.getColumnIndex("date")));
        dairy_foreign.setPrincipal_krw(c.getInt(c.getColumnIndex("principal_krw")));
        dairy_foreign.setCurrency(c.getInt(c.getColumnIndex("id_currency")));

        c.close();

        return dairy_foreign;
    }
    private InfoDairyTotal getInfoDairyTotal(int id_account, String date) {
        Cursor c;
        InfoDairyTotal dairy_total = new InfoDairyTotal();

        String where = "id_account=? and date=?";
        String[] selectionArgs = new String[]{String.valueOf(id_account),date};

        c = cr.query(Uri.parse(URI_INFO_DAIRY_TOTAL), null, where, selectionArgs, null);

        assert c != null;
        if(c.getCount() == 0) return null;

        c.moveToNext();

        dairy_total.setId(c.getInt(c.getColumnIndex("_id")));
        dairy_total.setPrincipal(c.getInt(c.getColumnIndex("principal")));
        dairy_total.setRate(c.getFloat(c.getColumnIndex("rate")));
        dairy_total.setAccount(c.getInt(c.getColumnIndex("id_account")));
        dairy_total.setDate(c.getString(c.getColumnIndex("date")));
        dairy_total.setEvaluation(c.getInt(c.getColumnIndex("evaluation")));

        c.close();

        return dairy_total;
    }

    public InfoDairyKRW getLastInfoDairyKRW(int id_account) {
        String[] selectionArgs = new String[] {String.valueOf(id_account)};

        dairies_krw.clear();
        getData(CODE_INFO_DAIRY_KRW, URI_INFO_DAIRY_KRW, "id_account=?",selectionArgs,"date DESC LIMIT 1");
        if(dairies_krw.isEmpty()) return null;
        else return dairies_krw.get(0);
    }
    public InfoDairyKRW getLastInfoDairyKRW(int id_account, String date) {
        String selection = "id_account=? and date<=?";
        String[] selectionArgs = new String[] {String.valueOf(id_account),date};

        dairies_krw.clear();
        getData(CODE_INFO_DAIRY_KRW, URI_INFO_DAIRY_KRW, selection,selectionArgs,"date DESC LIMIT 1");
        if(dairies_krw.isEmpty()) return null;
        else return dairies_krw.get(0);
    }
    public InfoDairyForeign getLastInfoDairyForeign(int id_account, int id_currency, String date) {
        String selection = "id_account=? and id_currency=? and date<=?";
        String[] selectionArgs = new String[] {String.valueOf(id_account), String.valueOf(id_currency), date};

        dairies_foreign.clear();
        getData(CODE_INFO_DAIRY_FOREIGN, URI_INFO_DAIRY_FOREIGN, selection, selectionArgs,"date DESC LIMIT 1");
        if(dairies_foreign.isEmpty()) return null;
        else return dairies_foreign.get(0);
    }
    public InfoDairyTotal getLastInfoDairyTotal(int id_account) {
        String selection = "id_account=?";
        String[] selectionArgs = new String[] {String.valueOf(id_account)};

        dairies_total.clear();
        getData(CODE_INFO_DAIRY_TOTAL, URI_INFO_DAIRY_TOTAL, selection, selectionArgs,"date DESC LIMIT 1");
        if(dairies_total.isEmpty()) return null;
        else return dairies_total.get(0);
    }
    public InfoDairyTotal getLastInfoDairyTotal(int id_account, String date) {
        String selection = "id_account=? and date<=?";
        String[] selectionArgs = new String[] {String.valueOf(id_account),date};

        dairies_total.clear();
        getData(CODE_INFO_DAIRY_TOTAL, URI_INFO_DAIRY_TOTAL, selection,selectionArgs,"date DESC LIMIT 1");
        if(dairies_total.isEmpty()) return null;
        else return dairies_total.get(0);
    }
    public InfoIOKRW getLastInfoIOKRW(int id_account, String date) {
        String selection = "id_account=? and date<=?";
        String[] selectionArgs = new String[] {String.valueOf(id_account),date};

        IOs_krw.clear();
        getData(CODE_INFO_IO_KRW, URI_INFO_IO_KRW, selection,selectionArgs,"date DESC LIMIT 1");
        if(IOs_krw.isEmpty()) return null;
        else return IOs_krw.get(0);
    }
    public InfoIOForeign getLastInfoIOForeign(int id_account, int id_currency, String date) {
        String selection = "id_account=? and id_currency=? and date<=?";
        String[] selectionArgs = new String[] {String.valueOf(id_account),String.valueOf(id_currency),date};

        IOs_foreign.clear();
        getData(CODE_INFO_IO_FOREIGN, URI_INFO_IO_FOREIGN, selection,selectionArgs,"date DESC LIMIT 1");
        if(IOs_foreign.isEmpty()) return null;
        else return IOs_foreign.get(0);
    }

    public String getFirstDate() {
        IOs_krw.clear();
        getData(CODE_INFO_IO_KRW, URI_INFO_IO_KRW, null, null, "date ASC LIMIT 1");
        if(IOs_krw.isEmpty()) return null;
        else return IOs_krw.get(0).getDate();
    }
    public String getLastSpendCode(String date) {
        String selection = "date_use=?";
        String[] selectionArgs = new String[] {date};

        spends.clear();
        getData(CODE_SPEND, URI_SPEND, selection,selectionArgs,"_id DESC LIMIT 1");
        if(spends.isEmpty()) return null;
        else return spends.get(0).getCode();
    }
    public int getCategoryMainId(String name) {
        Cursor c;

        String where = "name=?";
        String[] selectionArgs = {name};

        c = cr.query(Uri.parse(URI_CATEGORY_MAIN), null, where, selectionArgs, null);

        assert c != null;
        if(c.getCount() == 0) return -1;

        c.moveToNext();

        int id = c.getInt(c.getColumnIndex("_id"));

        c.close();

        return id;
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

        try {
            cr.insert(Uri.parse(URI_GROUP), cv);
        } catch (Exception e) {
            Log.e(TAG,"DB 추가 error");
        }
    }
    public void insertAccount(String institute, String number, String description) {
        ContentValues cv = new ContentValues();

        if(currentGroup == -1) return;

        cv.put("institute", institute);
        cv.put("number",number);
        cv.put("description",description);
        cv.put("id_group",currentGroup);

        try {
            cr.insert(Uri.parse(URI_ACCOUNT), cv);
        } catch (Exception e) {
            Log.e(TAG,"DB 추가 error");
        }
    }
    public void insertInfoIOKRW(int id_account, String date, int input, int output, int income, int spend_cash, int spend_card, int evaluation) {
        ContentValues cv = new ContentValues();

        if(currentGroup == -1 || currentAccount == -1) return;

        cv.put("id_account",id_account);
        cv.put("date", date);
        cv.put("input",input);
        cv.put("output",output);
        cv.put("income",income);
        cv.put("spend_cash",spend_cash);
        cv.put("spend_card",spend_card);
        cv.put("evaluation",evaluation);

        try {
            cr.insert(Uri.parse(URI_INFO_IO_KRW), cv);
            modifyInfoDiaryKRW(0,id_account, date);
        } catch (Exception e) {
            Log.e(TAG,"DB 추가 error");
        }
    }
    public void insertInfoIOKRW(InfoIOKRW io_krw) {
        ContentValues cv = new ContentValues();

        if(currentGroup == -1 || currentAccount == -1) return;

        cv.put("id_account",io_krw.getAccount());
        cv.put("date",io_krw.getDate());
        cv.put("input",io_krw.getInput());
        cv.put("output",io_krw.getOutput());
        cv.put("income",io_krw.getIncome());
        cv.put("spend_cash",io_krw.getSpendCash());
        cv.put("spend_card",io_krw.getSpendCard());
        cv.put("evaluation",io_krw.getEvaluation());

        try {
            cr.insert(Uri.parse(URI_INFO_IO_KRW), cv);
            modifyInfoDiaryKRW(0,io_krw.getAccount(),io_krw.getDate());
        } catch (Exception e) {
            Log.e(TAG,"DB 추가 error");
        }
    }
    public void insertInfoIOForeign(InfoIOForeign io_foreign) {
        ContentValues cv = new ContentValues();

        if(currentGroup == -1 || currentAccount == -1) return;

        cv.put("id_account",io_foreign.getAccount());
        cv.put("date",io_foreign.getDate());
        cv.put("input",String.format(Locale.getDefault(),"%.2f", io_foreign.getInput()));
        cv.put("input_krw",io_foreign.getInput_krw());
        cv.put("output",String.format(Locale.getDefault(),"%.2f", io_foreign.getOutput()));
        cv.put("output_krw",io_foreign.getOutput_krw());
        cv.put("evaluation",io_foreign.getEvaluation());
        cv.put("id_currency",io_foreign.getCurrency());

        try {
            cr.insert(Uri.parse(URI_INFO_IO_FOREIGN), cv);
            modifyInfoDiaryForeign(0,io_foreign.getAccount(),io_foreign.getCurrency(),io_foreign.getDate());
        } catch (Exception e) {
            Log.e(TAG,"DB 추가 error");
        }
    }
    public void insertCategoryMain(String name, String kind) {
        ContentValues cv = new ContentValues();

        cv.put("name", name);
        cv.put("kind", kind);

        try {
            cr.insert(Uri.parse(URI_CATEGORY_MAIN), cv);
        } catch (Exception e) {
            Log.e(TAG,"DB insert error");
        }
    }
    public void insertCategorySub(String name, int main) {
        ContentValues cv = new ContentValues();

        cv.put("name",name);
        cv.put("id_main",main);

        try {
            cr.insert(Uri.parse(URI_CATEGORY_SUB), cv);
        } catch (Exception e) {
            Log.e(TAG,"DB insert error");
        }
    }
    public void insertCard(String name, String num, String com, int date, int id_account) {
        ContentValues cv = new ContentValues();

        cv.put("name",name);
        cv.put("number",num);
        cv.put("company",com);
        cv.put("date_draw",date);
        cv.put("id_account",id_account);

        try {
            cr.insert(Uri.parse(URI_CARD), cv);
        } catch (Exception e) {
            Log.e(TAG,"DB insert error");
        }
    }
    public void insertSpend(String code, String details, String date, int amount, int id_category) {
        ContentValues cv = new ContentValues();

        cv.put("spend_code",code);
        cv.put("details",details);
        cv.put("id_sub",id_category);
        cv.put("date_use",date);
        cv.put("amount",amount);

        try {
            cr.insert(Uri.parse(URI_SPEND),cv);
        } catch (Exception e) {
            Log.e(TAG,"DB insert error");
        }
    }
    public void insertSpendCard(String code, int id_card) {
        ContentValues cv = new ContentValues();

        Card card = getCard(id_card);
        Spend spend = getSpend(code);
        String date = spend.getDate();
        InfoIOKRW io_krw;
        int id_account = card.getAccount();
        int evaluation, sum;

        if(card == null) {
            Log.i(TAG,"No card");
            return;
        }

        if(spend == null) {
            Log.i(TAG,"No spend");
            return;
        }

        cv.put("spend_code",code);
        cv.put("id_card",id_card);

        try {
            cr.insert(Uri.parse(URI_SPEND_CARD), cv);
        } catch (Exception e) {
            Log.e(TAG,"DB insert error");
        }

        io_krw = getInfoIOKRW(id_account,date);
        evaluation = calEvaluationKRW(id_account, date);
        sum = getSpendsCardSum(date, id_account);

        if(io_krw != null) {
            io_krw.setSpendCard(sum);
            io_krw.setEvaluation(evaluation);
            updateInfoIOKRW(io_krw);
        }
        else {
            try {
                insertInfoIOKRW(id_account, date, 0, 0, 0, 0, sum, evaluation);
            } catch (Exception e) {
                Log.e(TAG,"DB insert error");
            }
        }
    }
    public void insertSpendCash(String code, int id_account) {
        int evaluation, sum;
        String date = null;
        InfoIOKRW io_krw;
        ContentValues cv = new ContentValues();

        Spend spend = getSpend(code);
        if(spend != null) date = spend.getDate();

        cv.put("spend_code",code);
        cv.put("id_account",id_account);

        cr.insert(Uri.parse(URI_SPEND_CASH),cv);

        io_krw = getInfoIOKRW(id_account,date);
        evaluation = calEvaluationKRW(id_account, date);
        sum = getSpendsCashSum(date,id_account);

        if(io_krw != null) {
            io_krw.setSpendCash(sum);
            io_krw.setEvaluation(evaluation);
            updateInfoIOKRW(io_krw);
        }
        else insertInfoIOKRW(id_account, date,0,0,0,sum,0,evaluation);
    }
    public void insertIncome(String details, String date, int id_account, int id_category_sub, int amount) {
        int evaluation, sum;
        InfoIOKRW io_krw;
        ContentValues cv = new ContentValues();

        cv.put("id_sub",id_category_sub);
        cv.put("date",date);
        cv.put("id_account",id_account);
        cv.put("details",details);
        cv.put("amount",amount);

        try {
            cr.insert(Uri.parse(URI_INCOME), cv);
        } catch (Exception e) {
            Log.e(TAG,"DB insert error");
        }

        io_krw = getInfoIOKRW(id_account,date);
        evaluation = calEvaluationKRW(id_account, date);
        sum = getIncomeSum(date, id_account);

        if(io_krw != null) {
            io_krw.setIncome(sum);
            io_krw.setEvaluation(evaluation);
            updateInfoIOKRW(io_krw);
        }
        else {
            try {
                insertInfoIOKRW(id_account, date, 0, 0, sum, 0, 0, evaluation);
            } catch (Exception e) {
                Log.e(TAG,"DB insert error");
            }
        }
    }

    private void insertInfoDairyKRW(int id_account, String date, int principal, double rate) {
        ContentValues cv = new ContentValues();

        if(currentGroup == -1 || currentAccount == -1) return;

        cv.put("id_account", id_account);
        cv.put("date", date);
        cv.put("principal", principal);
        cv.put("rate",String.format(Locale.getDefault(),"%.2f", rate));

        try {
            cr.insert(Uri.parse(URI_INFO_DAIRY_KRW), cv);
        } catch (Exception e) {
            Log.e(TAG,"DB insert error");
        }
    }
    private void insertInfoDairyForeign(int id_account, int id_currency, String date, double principal, int principal_krw, double rate) {
        ContentValues cv = new ContentValues();

        if(currentGroup == -1 || currentAccount == -1) return;

        cv.put("id_account", id_account);
        cv.put("date", date);
        cv.put("principal", String.format(Locale.getDefault(),"%.2f", principal));
        cv.put("principal_krw", principal_krw);
        cv.put("rate",String.format(Locale.getDefault(),"%.2f", rate));
        cv.put("id_currency", id_currency);

        try {
            cr.insert(Uri.parse(URI_INFO_DAIRY_FOREIGN), cv);
        } catch (Exception e) {
            Log.e(TAG,"DB insert error");
        }
    }
    private void insertInfoDairyTotal(int id_account, int evaluation, String date, int principal, double rate) {
        ContentValues cv = new ContentValues();

        if(currentGroup == -1 || currentAccount == -1) return;

        cv.put("id_account", id_account);
        cv.put("date", date);
        cv.put("principal", principal);
        cv.put("rate",String.format(Locale.getDefault(), "%.2f",rate));
        cv.put("evaluation",evaluation);

        try {
            cr.insert(Uri.parse(URI_INFO_DAIRY_TOTAL), cv);
        } catch (Exception e) {
            Log.e(TAG,"DB insert error");
        }
    }

    void deleteAll() {
        deleteGroup(null,null);
        deleteAccount(null, null);
        deleteInfoIOKRW(null, null);
        deleteInfoIOForeign(null, null);
        deleteCategoryMain(null,null);
        deleteCategorySub(null,null);
        deleteCard(null,null);
        deleteSpend(null,null);
        deleteSpendCash(null,null);
        deleteSpendCard(null,null);
        deleteIncome(null,null);
        deleteInfoDairyKRW();
        deleteInfoDairyForeign();
        deleteInfoDairyTotal();
    }
    public void deleteGroup(String where, String[] args) {
        cr.delete(Uri.parse(URI_GROUP),where,args);
    }
    public void deleteAccount(String where, String[] args) {
        cr.delete(Uri.parse(URI_ACCOUNT),where,args);
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
    public void deleteSpendCard(String where, String[] args) {
        SpendCard spendCard = getSpendCard(args[0]);
        Card card;
        Spend spend;
        String date;
        InfoIOKRW io_krw;
        int id_account, evaluation, sum;

        if(spendCard == null) {
            Log.i(TAG,"No spendCard");
            return;
        }

        card = getCard(spendCard.getCard());
        if(card == null) {
            Log.i(TAG,"No card");
            return;
        }

        spend = getSpend(spendCard.getCode());
        if(spend == null) {
            Log.i(TAG,"No spend");
            return;
        }

        cr.delete(Uri.parse(URI_SPEND_CARD),where,args);

        date = spend.getDate();
        id_account = card.getAccount();

        io_krw = getInfoIOKRW(id_account,date);
        evaluation = calEvaluationKRW(id_account, date);
        sum = getSpendsCardSum(date, id_account);

        if(io_krw != null) {
            io_krw.setSpendCard(sum);
            io_krw.setEvaluation(evaluation);
            updateInfoIOKRW(io_krw);
        }
        else {
            try {
                insertInfoIOKRW(id_account, date, 0, 0, 0, 0, sum, evaluation);
            } catch (Exception e) {
                Log.e(TAG,"DB insert error");
            }
        }
    }
    public void deleteSpendCash(String where, String[] args) {

        SpendCash spendCash = getSpendCash(args[0]);
        Spend spend;
        String date;
        int id_account, evaluation, sum;
        InfoIOKRW io_krw;

        if(spendCash == null) {
            Log.i(TAG,"No spendCash");
            return;
        }

        spend = getSpend(spendCash.getCode());
        if(spend == null) {
            Log.i(TAG,"No spend");
            return;
        }

        cr.delete(Uri.parse(URI_SPEND_CASH),where,args);

        date = spend.getDate();
        id_account = spendCash.getAccount();

        io_krw = getInfoIOKRW(id_account,date);
        evaluation = calEvaluationKRW(id_account, date);
        sum = getSpendsCashSum(date,id_account);

        if(io_krw != null) {
            io_krw.setSpendCash(sum);
            io_krw.setEvaluation(evaluation);
            updateInfoIOKRW(io_krw);
        }
        else insertInfoIOKRW(id_account, date,0,0,0,sum,0,evaluation);
    }
    public void deleteIncome(String where, String[] args) {
        String date;
        int id_account, evaluation, sum;
        Income income = getIncome(Integer.valueOf(args[0]));
        InfoIOKRW io_krw;

        if(income == null) return;

        date = income.getDate();
        id_account = income.getAccount();

        cr.delete(Uri.parse(URI_INCOME),where,args);

        io_krw = getInfoIOKRW(id_account, date);
        evaluation = calEvaluationKRW(id_account, date);
        sum = getIncomeSum(date, id_account);

        if(io_krw != null) {
            io_krw.setIncome(sum);
            io_krw.setEvaluation(evaluation);
            updateInfoIOKRW(io_krw);
        }
        else {
            try {
                insertInfoIOKRW(id_account, date, 0, 0, sum, 0, 0, evaluation);
            } catch (Exception e) {
                Log.e(TAG,"DB insert error");
            }
        }
    }
    public void deleteSpend(String where, String[] args) {
        cr.delete(Uri.parse(URI_SPEND),where,args);
    }

    private void deleteInfoIOKRW(String where, String[] args) {
        InfoIOKRW io_krw = getInfoIOKRW(Integer.valueOf(args[0]));
        int id_account;
        String date;

        if(io_krw == null) {
            Log.i(TAG,"The DB is not exist");
            return;
        }

        id_account = io_krw.getAccount();
        date = io_krw.getDate();

        try {
            cr.delete(Uri.parse(URI_INFO_IO_KRW), where, args);
            modifyInfoDiaryKRW(1, id_account, date);
        } catch (Exception e) {
            Log.e(TAG, "DB delete Error");
        }
    }
    private void deleteInfoIOForeign(String where, String[] args) {
        InfoIOForeign io_foreign = getInfoIOForeign(Integer.valueOf(args[0]));
        int id_account, id_currency;
        String date;

        if(io_foreign == null) {
            Log.i(TAG,"The DB is not exist");
            return;
        }

        id_account = io_foreign.getAccount();
        date = io_foreign.getDate();
        id_currency = io_foreign.getCurrency();

        try {
            cr.delete(Uri.parse(URI_INFO_IO_FOREIGN), where, args);
            modifyInfoDiaryForeign(1, id_account, id_currency, date);
        } catch (Exception e) {
            Log.e(TAG, "DB delete Error");
        }
    }

    private void deleteInfoDairyKRW() {
        cr.delete(Uri.parse(URI_INFO_DAIRY_KRW), null, null);
    }
    private void deleteInfoDairyForeign() {
        cr.delete(Uri.parse(URI_INFO_DAIRY_FOREIGN), null, null);
    }
    private void deleteInfoDairyTotal() {
        cr.delete(Uri.parse(URI_INFO_DAIRY_TOTAL), null, null);
    }

    public void updateGroup(Group group) {
        String where = "_id";
        String[] selectionArgs = new String[] {String.valueOf(group.getId())};

        ContentValues cv = new ContentValues();

        cv.put("name",group.getName());

        try {
            cr.update(Uri.parse(URI_GROUP), cv, where, selectionArgs);
        } catch (Exception e) {
            Log.e(TAG,"DB update error");
        }
    }
    public void updateAccount(int id, String institute, String account, String description) {
        String where = "_id";
        String[] selectionArgs = new String[] {String.valueOf(id)};

        ContentValues cv = new ContentValues();

        cv.put("institute",institute);
        cv.put("number",account);
        cv.put("description",description);

        try {
            cr.update(Uri.parse(URI_ACCOUNT), cv, where, selectionArgs);
        } catch (Exception e) {
            Log.e(TAG,"DB update error");
        }
    }
    public void updateInfoIOKRW(InfoIOKRW io_krw) {
        String where = "_id";
        String[] selectionArgs = new String[] {String.valueOf(io_krw.getId())};

        ContentValues cv = new ContentValues();

        cv.put("id_account",io_krw.getAccount());
        cv.put("date", io_krw.getDate());
        cv.put("input",io_krw.getInput());
        cv.put("output",io_krw.getOutput());
        cv.put("income",io_krw.getIncome());
        cv.put("spend_cash",io_krw.getSpendCash());
        cv.put("spend_card",io_krw.getSpendCard());
        cv.put("evaluation",io_krw.getEvaluation());

        try {
            cr.update(Uri.parse(URI_INFO_IO_KRW), cv, where, selectionArgs);
            modifyInfoDiaryKRW(1, io_krw.getAccount(), io_krw.getDate());
        } catch (Exception e) {
            Log.e(TAG,"DB update error");
        }
    }
    public void updateInfoIOForeign(InfoIOForeign io_foreign) {
        String where = "_id";
        String[] selectionArgs = new String[] {String.valueOf(io_foreign.getId())};

        ContentValues cv = new ContentValues();

        cv.put("id_account",io_foreign.getAccount());
        cv.put("date", io_foreign.getDate());
        cv.put("input", String.format(Locale.getDefault(),"%.2f", io_foreign.getInput()));
        cv.put("input_krw",io_foreign.getInput_krw());
        cv.put("output",String.format(Locale.getDefault(),"%.2f", io_foreign.getOutput()));
        cv.put("output_krw",io_foreign.getOutput_krw());
        cv.put("id_currency",io_foreign.getCurrency());
        cv.put("evaluation",io_foreign.getEvaluation());

        try {
            cr.update(Uri.parse(URI_INFO_IO_FOREIGN), cv, where, selectionArgs);
            modifyInfoDiaryForeign(1, io_foreign.getAccount(), io_foreign.getCurrency(), io_foreign.getDate());
        } catch (Exception e) {
            Log.e(TAG,"DB update error");
        }
    }
    public void updateCategoryMain(int id, String name) {
        String where = "_id";
        String[] selectionArgs = new String[]{String.valueOf(id)};

        ContentValues cv = new ContentValues();

        cv.put("name", name);

        try {
            cr.update(Uri.parse(URI_CATEGORY_MAIN), cv, where, selectionArgs);
        } catch (Exception e) {
            Log.e(TAG, "DB update error");
        }
    }
    public void updateCategorySub(int id, String name, int main_id) {
        String where = "_id";
        String[] selectionArgs = new String[] {String.valueOf(id)};

        ContentValues cv = new ContentValues();

        cv.put("name",name);
        cv.put("main_id",main_id);

        try {
            cr.update(Uri.parse(URI_GROUP), cv, where, selectionArgs);
        } catch (Exception e) {
            Log.e(TAG,"DB update error");
        }
    }
    public void updateCard(Card card) {
        String where = "_id";
        String[] selectionArgs = new String[] {String.valueOf(card.getId())};

        ContentValues cv = new ContentValues();

        cv.put("name",card.getName());
        cv.put("company",card.getCompany());
        cv.put("number",card.getNumber());
        cv.put("date_draw",card.getDrawDate());
        cv.put("id_account",card.getAccount());

        try {
            cr.update(Uri.parse(URI_CARD), cv, where, selectionArgs);
        } catch (Exception e) {
            Log.e(TAG,"DB update error");
        }
    }
    public void updateSpendCard(int id, String code, int id_card) {
        int evaluation, sum, id_account;
        String date, where = "_id";
        String[] selectionArgs = new String[] {String.valueOf(id)};

        Spend spend;
        InfoIOKRW io_krw;
        Card card = getCard(id_card);

        ContentValues cv = new ContentValues();

        if(card == null) {
            Log.i(TAG,"No card");
            return;
        }

        spend = getSpend(code);
        if(spend == null) {
            Log.i(TAG,"No spend");
            return;
        }

        cv.put("spend_code",code);
        cv.put("id_card",id_card);

        try {
            cr.update(Uri.parse(URI_SPEND_CARD), cv, where, selectionArgs);
        } catch (Exception e) {
            Log.e(TAG,"DB update error");
        }

        date = spend.getDate();
        id_account = card.getAccount();

        io_krw = getInfoIOKRW(id_account,date);
        evaluation = calEvaluationKRW(id_account, date);
        sum = getSpendsCardSum(date, id_account);

        if(io_krw != null) {
            io_krw.setSpendCard(sum);
            io_krw.setEvaluation(evaluation);
            updateInfoIOKRW(io_krw);
        }
        else {
            try {
                insertInfoIOKRW(id_account, date, 0, 0, 0, 0, sum, evaluation);
            } catch (Exception e) {
                Log.e(TAG, "DB update error");
            }
        }
    }
    public void updateSpendCash(int id, String code, int id_account) {
        ContentValues cv = new ContentValues();
        String date = null, where = "_id";
        String[] selectionArgs = new String[] {String.valueOf(id)};
        InfoIOKRW io_krw;
        int evaluation, sum;

        Spend spend = getSpend(code);
        if(spend != null) date = spend.getDate();

        cv.put("spend_code",code);
        cv.put("id_account",id_account);

        try {
            cr.update(Uri.parse(URI_SPEND_CASH), cv, where, selectionArgs);
        } catch (Exception e) {
            Log.e(TAG,"DB update error");
        }

        io_krw = getInfoIOKRW(id_account,date);
        evaluation = calEvaluationKRW(id_account, date);
        sum = getSpendsCashSum(date, id_account);

        if(io_krw != null) {
            io_krw.setSpendCash(sum);
            io_krw.setEvaluation(evaluation);
            updateInfoIOKRW(io_krw);
        }
        else {
            try {
                insertInfoIOKRW(id_account, date, 0, 0, 0, sum, 0, evaluation);
            } catch (Exception e) {
                Log.e(TAG, "DB update error");
            }
        }
    }
    public void updateIncome(int id, String details, String date, int id_account, int id_category_sub, int amount) {
        ContentValues cv = new ContentValues();
        String where = "_id";
        String[] selectionArgs = new String[] {String.valueOf(id)};
        InfoIOKRW io_krw;
        int evaluation, sum;

        cv.put("details",details);
        cv.put("date",date);
        cv.put("amount",amount);
        cv.put("id_sub",id_category_sub);
        cv.put("id_account",id_account);

        try {
            cr.update(Uri.parse(URI_INCOME),cv,where,selectionArgs);
        } catch (Exception e) {
            Log.e(TAG,"DB update error");
        }

        io_krw = getInfoIOKRW(id_account, date);
        evaluation = calEvaluationKRW(id_account, date);
        sum = getIncomeSum(date, id_account);

        if(io_krw != null) {
            io_krw.setIncome(sum);
            io_krw.setEvaluation(evaluation);
            updateInfoIOKRW(io_krw);
        }
        else {
            try {
                insertInfoIOKRW(id_account, date, 0, 0, sum, 0, 0, evaluation);
            } catch (Exception e) {
                Log.e(TAG,"DB insert error");
            }
        }
    }

    private void updateInfoDairyKRW(int id, int id_account, String date, int principal, double rate) {
        ContentValues cv = new ContentValues();
        String where = "_id";
        String[] selectionArgs = new String[] {String.valueOf(id)};

        cv.put("id_account", id_account);
        cv.put("date", date);
        cv.put("principal",principal);
        cv.put("rate",String.format(Locale.getDefault(),"%.2f",rate));

        try {
            cr.update(Uri.parse(URI_INFO_DAIRY_KRW), cv, where, selectionArgs);
        } catch (Exception e) {
            Log.e(TAG,"DB update error");
        }
    }
    private void updateInfoDairyForeign(int id, int id_account, int id_currency, String date, double principal, int principal_krw, double rate) {
        ContentValues cv = new ContentValues();
        String where = "_id";
        String[] selectionArgs = new String[] {String.valueOf(id)};

        cv.put("id_account", id_account);
        cv.put("date", date);
        cv.put("principal",String.format(Locale.getDefault(),"%.2f",principal));
        cv.put("principal_krw",principal_krw);
        cv.put("rate",String.format(Locale.getDefault(),"%.2f",rate));
        cv.put("id_currency",id_currency);

        try {
            cr.update(Uri.parse(URI_INFO_DAIRY_FOREIGN), cv, where, selectionArgs);
        } catch (Exception e) {
            Log.e(TAG,"DB update error");
        }
    }
    private void updateInfoDairyTotal(int id, int id_account, int evaluation, String date, int principal, double rate) {
        ContentValues cv = new ContentValues();
        String where = "_id";
        String[] selectionArgs = new String[] {String.valueOf(id)};

        cv.put("id_account", id_account);
        cv.put("date", date);
        cv.put("principal",principal);
        cv.put("evaluation",evaluation);
        cv.put("rate",String.format(Locale.getDefault(),"%.2f",rate));

        try {
            cr.update(Uri.parse(URI_INFO_DAIRY_TOTAL), cv, where, selectionArgs);
        } catch (Exception e) {
            Log.e(TAG,"DB update error");
        }
    }

    public void setCurrentGroup(int group) {
        currentGroup = group;
    }
    public void setCurrentAccount(int account) {
        currentAccount = account;
    }

    private void getData(int code, String uri, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor = cr.query(Uri.parse(uri), null, selection, selectionArgs, sortOrder);

        if((cursor != null ? cursor.getCount() : 0) < 1) {
            switch (code) {
                case CODE_GROUP:
                    currentGroup = -1;
                    break;
                case CODE_ACCOUNT:
                    currentAccount = -1;
                    break;
            }
            assert cursor != null;
            cursor.close();
            return;
        }

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
                    account.setDescription(cursor.getString(3));
                    account.setGroup(cursor.getInt(4));

                    accounts.add(account);
                    break;
                case CODE_INFO_IO_KRW:
                    InfoIOKRW io_krw = new InfoIOKRW();
                    io_krw.setId(cursor.getInt(0));
                    io_krw.setDate(cursor.getString(1));
                    io_krw.setInput(cursor.getInt(2));
                    io_krw.setOutput(cursor.getInt(3));
                    io_krw.setEvaluation(cursor.getInt(4));
                    io_krw.setSpendCash(cursor.getInt(5));
                    io_krw.setSpendCard(cursor.getInt(6));
                    io_krw.setIncome(cursor.getInt(7));
                    io_krw.setAccount(cursor.getInt(8));

                    IOs_krw.add(io_krw);
                    break;
                case CODE_INFO_IO_FOREIGN:
                    InfoIOForeign io_foreign = new InfoIOForeign();
                    io_foreign.setId(cursor.getInt(0));
                    io_foreign.setDate(cursor.getString(1));
                    io_foreign.setInput(cursor.getDouble(2));
                    io_foreign.setInput_krw(cursor.getInt(3));
                    io_foreign.setOutput(cursor.getDouble(4));
                    io_foreign.setOutput_krw(cursor.getInt(5));
                    io_foreign.setAccount(cursor.getInt(6));
                    io_foreign.setEvaluation(cursor.getInt(7));
                    io_foreign.setCurrency(cursor.getInt(8));

                    IOs_foreign.add(io_foreign);
                    break;
                case CODE_INFO_DAIRY_KRW:
                    InfoDairyKRW dairy_krw = new InfoDairyKRW();
                    dairy_krw.setId(cursor.getInt(0));
                    dairy_krw.setDate(cursor.getString(1));
                    dairy_krw.setPrincipal(cursor.getInt(2));
                    dairy_krw.setRate(cursor.getDouble(3));
                    dairy_krw.setAccount(cursor.getInt(4));

                    dairies_krw.add(dairy_krw);
                    break;
                case CODE_INFO_DAIRY_FOREIGN:
                    InfoDairyForeign dairy_foreign = new InfoDairyForeign();
                    dairy_foreign.setId(cursor.getInt(0));
                    dairy_foreign.setDate(cursor.getString(1));
                    dairy_foreign.setPrincipal(cursor.getDouble(2));
                    dairy_foreign.setPrincipal_krw(cursor.getInt(3));
                    dairy_foreign.setRate(cursor.getDouble(4));
                    dairy_foreign.setAccount(cursor.getInt(5));
                    dairy_foreign.setCurrency(cursor.getInt(6));

                    dairies_foreign.add(dairy_foreign);
                    break;
                case CODE_INFO_DAIRY_TOTAL:
                    InfoDairyTotal dairy_total = new InfoDairyTotal();
                    dairy_total.setId(cursor.getInt(0));
                    dairy_total.setDate(cursor.getString(1));
                    dairy_total.setPrincipal(cursor.getInt(2));
                    dairy_total.setEvaluation(cursor.getInt(3));
                    dairy_total.setRate(cursor.getDouble(4));
                    dairy_total.setAccount(cursor.getInt(5));

                    dairies_total.add(dairy_total);
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
                    card.setAccount(cursor.getInt(5));

                    cards.add(card);
                    break;
                case CODE_SPEND:
                    Spend spend = new Spend();
                    spend.setId(cursor.getInt(0));
                    spend.setCode(cursor.getString(1));
                    spend.setDetails(cursor.getString(2));
                    spend.setCategory(cursor.getInt(3));
                    spend.setDate(cursor.getString(4));
                    spend.setAmount(cursor.getInt(5));

                    spends.add(spend);
                    break;
                case CODE_SPEND_CARD:
                    SpendCard spend_card = new SpendCard();
                    spend_card.setId(cursor.getInt(0));
                    spend_card.setCode(cursor.getString(1));
                    spend_card.setCard(cursor.getInt(2));

                    spends_card.add(spend_card);
                    break;
                case CODE_SPEND_CASH:
                    SpendCash spend_cash = new SpendCash();
                    spend_cash.setId(cursor.getInt(0));
                    spend_cash.setCode(cursor.getString(1));
                    spend_cash.setAccount(cursor.getInt(2));

                    spends_cash.add(spend_cash);
                    break;
                case CODE_INCOME:
                    Income income = new Income();
                    income.setId(cursor.getInt(0));
                    income.setDate(cursor.getString(1));
                    income.setCategory(cursor.getInt(2));
                    income.setAmount(cursor.getInt(3));
                    income.setDetails(cursor.getString(4));
                    income.setAccount(cursor.getInt(5));

                    incomes.add(income);
                    break;
            }
        }

        cursor.close();
    }

    public int getSpendsCashSum(String date, int id_account) {
        int sum;
        String where = "A.date_use=? AND B.id_account=?";
        String[] selectionArgs = {date,String.valueOf(id_account)};
        String[] select = {"total(A.amount) "
                + "FROM tbl_spend AS A "
                + "LEFT JOIN tbl_spend_cash AS B "
                + "ON A.spend_code=B.spend_code"};
        Cursor c;

        // table이 존재하지 않으면
        if(checkDBTable("tbl_spend_cash") == 0) return 0;

        c = cr.query(Uri.parse(URI_JOIN), select, where, selectionArgs, null);

        assert c != null;
        c.moveToNext();

        sum = c.getInt(0);
        c.close();

        return sum;
    }
    public int getSpendsCardSum(String date, int id_account) {
        int sum;
        String where = "A.date_use=? AND C.id_account=?";
        String[] selectionArgs = {date,String.valueOf(id_account)};
        String[] select = {"total(A.amount) "
                + "FROM tbl_spend AS A "
                + "JOIN tbl_spend_card AS B "
                + "ON A.spend_code=B.spend_code "
                + "JOIN tbl_card AS C "
                + "ON B.id_card=C._id"};

        Cursor c;

        // table이 존재하지 않으면
        if(checkDBTable("tbl_spend_card") == 0) return 0;

        c = cr.query(Uri.parse(URI_JOIN), select, where, selectionArgs, null);

        assert c != null;
        c.moveToNext();

        sum = c.getInt(0);
        c.close();

        return sum;
    }
    public int getIncomeSum(String date, int id_account) {
        int sum;
        String where = "date=? AND id_account=?";
        String[] select = {"total(amount) AS SUM"};
        String[] selectionArgs = {date,String.valueOf(id_account)};

        Cursor c;

        // table이 존재하지 않으면
        if(checkDBTable("tbl_income") == 0) return 0;

        c = cr.query(Uri.parse(URI_INCOME), select, where, selectionArgs, null);

        assert c != null;
        c.moveToNext();

        sum = c.getInt(0);
        c.close();

        return sum;
    }
    public int getSpendMonth(String date) {
        int sum;
        String where = "date_use BETWEEN date('" + date + "','start of month') AND date('" + date + "','start of month','+1 month','-1 day')";
        String[] select = {"total(amount) AS SUM"};

        Cursor c;

        // table이 존재하지 않으면
        if(checkDBTable("tbl_spend") == 0) return 0;

        c = cr.query(Uri.parse(URI_SPEND), select, where, null, null);

        assert c != null;
        c.moveToNext();

        sum = c.getInt(0);
        c.close();

        return sum;
    }
    public int getIncomeMonth(String date) {
        int sum;
        String where = "date BETWEEN date('" + date + "','start of month') AND date('" + date + "','start of month','+1 month','-1 day')";
        String[] select = {"total(amount) AS SUM"};
        Cursor c;

        // table이 존재하지 않으면
        if(checkDBTable("tbl_income") == 0) return 0;

        c = cr.query(Uri.parse(URI_INCOME), select, where, null, null);

        assert c != null;
        c.moveToNext();

        sum = c.getInt(0);
        c.close();

        return sum;
    }
    public int checkDBTable(String name) {
        Cursor c;

        String where = "Name=?";
        String[] selectionArgs = new String[]{name};
        String[] select = {"count(*) FROM sqlite_master"};

        c = cr.query(Uri.parse(URI_TABLE), select, where, selectionArgs, null);

        assert c != null;
        c.moveToNext();

        int result = c.getInt(0);
        c.close();

        return result;
    }

    private int getSum(String uri, int id_account, String[] column, String selectedDate) {
        int sum;
        String[] select = {"total(" + column[0] + ") AS SUM"};
        String where = "date<=? AND id_account=?";
        String[] selectionArgs = new String[]{selectedDate,String.valueOf(id_account)};

        Cursor c;

        c = cr.query(Uri.parse(uri), select, where, selectionArgs, null);
        assert c != null;
        c.moveToNext();

        sum = c.getInt(0);
        c.close();

        return sum;
    }
    private double getSum(String uri, int id_account, int id_currency, String[] column, String selectedDate) {
        double sum;
        String[] select = {"total(" + column[0] + ") AS SUM"};
        String where = "date<=? AND id_account=? AND id_currency=?";
        String[] selectionArgs = new String[]{selectedDate,String.valueOf(id_account),String.valueOf(id_currency)};

        Cursor c;

        c = cr.query(Uri.parse(uri), select, where, selectionArgs, null);
        assert c != null;
        c.moveToNext();

        sum = c.getDouble(0);
        c.close();

        return sum;
    }

    private void modifyInfoDiaryKRW(int select, int id_account, String selectedDate) {
        int evaluation = 0, index = 0;
        String txtDate;
        Date date;
        InfoIOKRW io_krw;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        List<InfoDairyKRW> daires_krw = getInfoDairesKRW(id_account);

        if(select == 0) {
            io_krw = getInfoIOKRW(id_account, selectedDate);

            if(io_krw != null) evaluation = io_krw.getEvaluation();
            calInfoDairyKRW(select,0,id_account,selectedDate,evaluation);
            calInfoDairyTotal(id_account, selectedDate);
            select = 1;
        }

        if(daires_krw.isEmpty()) {
            Log.i(TAG,"No dairy_krw");
            return;
        }

        try {
            //noinspection UnusedAssignment
            date = df.parse(daires_krw.get(index).getDate());

            do {
                txtDate = daires_krw.get(index).getDate();
                date = df.parse(txtDate);

                io_krw = getInfoIOKRW(id_account,txtDate);

                if(io_krw != null) evaluation = io_krw.getEvaluation();

                calInfoDairyKRW(select,daires_krw.get(index).getId(),id_account,txtDate,evaluation);
                calInfoDairyTotal(id_account, selectedDate);
                index++;

            } while(df.parse(selectedDate).compareTo(date) < 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void modifyInfoDiaryForeign(int select, int id_account, int id_currency, String selectedDate) {
        int index = 0;
        int evaluation = 0;
        String txtDate;
        Date date;
        InfoIOForeign io_foreign;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        List<InfoDairyForeign> daires_foreign = getInfoDairesForeign(id_account);

        if(select == 0) {
            io_foreign = getInfoIOForeign(id_account, id_currency, selectedDate);

            if(io_foreign != null) {
                evaluation = io_foreign.getEvaluation();
            }
            calInfoDairyForeign(select,0,id_account,id_currency,selectedDate,evaluation);
            calInfoDairyTotal(id_account, selectedDate);
            select = 1;
        }

        if(daires_foreign.isEmpty()) {
            Log.i(TAG,"No dairy_krw");
            return;
        }

        try {
            //noinspection UnusedAssignment
            date = df.parse(daires_foreign.get(index).getDate());

            do {
                txtDate = daires_foreign.get(index).getDate();
                date = df.parse(txtDate);

                io_foreign = getInfoIOForeign(id_account,id_currency,txtDate);

                if(io_foreign != null) evaluation = io_foreign.getEvaluation();

                calInfoDairyForeign(select,daires_foreign.get(index).getId(),id_account,id_currency,txtDate,evaluation);
                calInfoDairyTotal(id_account, selectedDate);
                index++;

            } while(df.parse(selectedDate).compareTo(date) < 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void calInfoDairyKRW(int select, int id, int id_account, String date, int evaluation) {
        int sum_in, sum_out, sum_spend_card, sum_spend_cash, sum_income, principal;
        double rate = 0;

        sum_in = getSum(URI_INFO_IO_KRW,id_account,new String[]{"input"},date);
        sum_income = getSum(URI_INFO_IO_KRW,id_account,new String[]{"income"},date);
        sum_out = getSum(URI_INFO_IO_KRW,id_account,new String[]{"output"},date);
        sum_spend_card = getSum(URI_INFO_IO_KRW,id_account,new String[]{"spend_card"},date);
        sum_spend_cash = getSum(URI_INFO_IO_KRW,id_account,new String[]{"spend_cash"},date);

        principal = sum_in + sum_income - sum_out - sum_spend_cash - sum_spend_card;

        if(principal != 0 && evaluation != 0) rate = (double)evaluation / (double)principal * 100 - 100;

        switch(select) {
            case 0:
                insertInfoDairyKRW(id_account, date, principal, rate);
                break;
            case 1:
                updateInfoDairyKRW(id, id_account, date, principal, rate);
                break;
        }
    }
    private void calInfoDairyForeign(int select, int id, int id_account, int id_currency, String date, float evaluation) {
        double sum_in, sum_out, principal_foreign, rate = 0f;
        int sum_in_krw, sum_out_krw, principal_krw;

        sum_in = getSum(URI_INFO_IO_FOREIGN,id_account,id_currency,new String[]{"input"},date);
        sum_out = getSum(URI_INFO_IO_FOREIGN,id_account,id_currency,new String[]{"output"},date);

        sum_in_krw = (int) getSum(URI_INFO_IO_FOREIGN,id_account,id_currency,new String[]{"input_krw"},date);
        sum_out_krw = (int) getSum(URI_INFO_IO_FOREIGN,id_account,id_currency,new String[]{"output_krw"},date);

        principal_foreign = sum_in - sum_out;
        principal_krw = sum_in_krw - sum_out_krw;

        if(principal_krw != 0 && evaluation != 0) rate = (double)evaluation / (double)principal_krw * 100 - 100;

        switch(select) {
            case 0:
                insertInfoDairyForeign(id_account, id_currency, date, principal_foreign, principal_krw, rate);
                break;
            case 1:
                updateInfoDairyForeign(id, id_account, id_currency, date, principal_foreign, principal_krw, rate);
                break;
        }
    }
    private void calInfoDairyTotal(int id_account, String date) {
        InfoDairyKRW dairy_krw = getLastInfoDairyKRW(id_account, date);
        InfoIOKRW io_krw = getLastInfoIOKRW(id_account, date);

        InfoDairyForeign[] dairy_foreign = new InfoDairyForeign[4];
        InfoIOForeign[] io_foreign = new InfoIOForeign[4];

        int principal = 0, evaluation = 0;
        if (dairy_krw != null) {
            principal = dairy_krw.getPrincipal();
        }
        if (io_krw != null) {
            evaluation = io_krw.getEvaluation();
        }

        for (int i = 0, limit = 4; i < limit; i++) {
            dairy_foreign[i] = getInfoDairyForeign(id_account, i, date);
            io_foreign[i] = getInfoIOForeign(id_account, i, date);
            if (dairy_foreign[i] != null) principal += dairy_foreign[i].getPrincipal_krw();
            if (io_foreign[i] != null) evaluation += io_foreign[i].getEvaluation();
        }

        double rate = 0f;
        if (principal != 0 && evaluation != 0)
            rate = (double) evaluation / (double) principal * 100 - 100;

        InfoDairyTotal dairy_total = getInfoDairyTotal(id_account, date);

        if(dairy_total == null) {
            insertInfoDairyTotal(id_account, evaluation, date, principal, rate);
        }
        else {
            updateInfoDairyTotal(dairy_total.getId(), id_account, evaluation, date, principal, rate);
        }
    }

    private int calEvaluationKRW(int id_account, String txtDate) {
        int evaluation = 0;
        InfoIOKRW io_krw_latest, io_krw = getInfoIOKRW(id_account, txtDate);

        // 전날 데이터 가져오기
        String year = txtDate.substring(0,4);
        String month = txtDate.substring(5,7);
        String day = txtDate.substring(8,10);

        Calendar before = Calendar.getInstance();
        before.set(Integer.parseInt(year),Integer.parseInt(month)-1,Integer.parseInt(day));
        before.add(Calendar.DATE,-1);

        io_krw_latest = getLastInfoIOKRW(id_account, String.format(Locale.getDefault(), "%04d-%02d-%02d", before.get(Calendar.YEAR),before.get(Calendar.MONTH)+1,before.get(Calendar.DATE)));

        // io_latest가 없으면 0
        if(io_krw_latest != null) {
            evaluation = io_krw_latest.getEvaluation();
            // 현재값이 있을 경우 무시하고 전날 데이터에 입출력값 반영
            if(io_krw != null) evaluation = evaluation - io_krw.getOutput() + io_krw.getInput();
        }
        // evaluation에 해당일 spendcash, spendcard, income 반영
        return evaluation - getSpendsCashSum(txtDate,id_account) - getSpendsCardSum(txtDate,id_account) + getIncomeSum(txtDate,id_account);
    }
}
