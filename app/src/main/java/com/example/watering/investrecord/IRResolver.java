package com.example.watering.investrecord;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by watering on 17. 11. 1.
 */

@SuppressWarnings("DefaultFileTemplate")
public class IRResolver {

    private ContentResolver cr;
    private static int currentGroup=-1;
    private static int currentAccount=-1;
    private static final String TAG = "InvestRecord";

    private static final int CODE_GROUP = 0;
    private static final int CODE_ACCOUNT = 1;
    private static final int CODE_INFO_IO = 2;
    private static final int CODE_INFO_DAIRY = 3;
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
    private final List<Info_IO> IOs = new ArrayList<>();
    private final List<Info_Dairy> dairies = new ArrayList<>();
    private final List<CategoryMain> categoryMains = new ArrayList<>();
    private final List<CategorySub> categorySubs = new ArrayList<>();
    private final List<Card> cards = new ArrayList<>();
    private final List<Spend> spends = new ArrayList<>();
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private final List<SpendCard> spends_card = new ArrayList<>();
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private final List<SpendCash> spends_cash = new ArrayList<>();
    private final List<Income> incomes = new ArrayList<>();

    private static final String URI_GROUP = "content://watering.investrecord.provider/group";
    private static final String URI_ACCOUNT = "content://watering.investrecord.provider/account";
    private static final String URI_INFO_IO = "content://watering.investrecord.provider/info_io";
    private static final String URI_INFO_DAIRY = "content://watering.investrecord.provider/info_dairy";
    private static final String URI_CARD = "content://watering.investrecord.provider/card";
    private static final String URI_CATEGORY_MAIN = "content://watering.investrecord.provider/category_main";
    private static final String URI_CATEGORY_SUB = "content://watering.investrecord.provider/category_sub";
    private static final String URI_INCOME = "content://watering.investrecord.provider/income";
    private static final String URI_SPEND = "content://watering.investrecord.provider/spend";
    private static final String URI_SPEND_CARD = "content://watering.investrecord.provider/spend_card";
    private static final String URI_SPEND_CASH = "content://watering.investrecord.provider/spend_cash";
    private static final String URI_JOIN = "content://watering.investrecord.provider/join";
    private static final String URI_TABLE = "content://watering.investrecord.provider/table";

    public void getContentResolver(ContentResolver cr) {
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
    public List<Info_Dairy> getInfoDaires(int id_account) {
        String[] selectionArgs = new String[] {String.valueOf(id_account)};

        dairies.clear();
        getData(CODE_INFO_DAIRY, URI_INFO_DAIRY,"id_account=?",selectionArgs,"date DESC");
        return dairies;
    }
    public List<CategoryMain> getCategoryMains(int kind) {
        categoryMains.clear();
        String selection = null;
        String[] selectionArgs = null;

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
    public Info_IO getInfoIO(int id_account, String date) {
        Cursor c;
        Info_IO io = new Info_IO();

        String where = "id_account=? and date=?";
        String[] selectionArgs = new String[]{String.valueOf(id_account),date};

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
        io.setIncome(c.getInt(c.getColumnIndex("income")));
        io.setSpendCash(c.getInt(c.getColumnIndex("spend_cash")));
        io.setSpendCard(c.getInt(c.getColumnIndex("spend_card")));

        c.close();

        return io;
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

    private Info_IO getInfoIO(int id) {
        Cursor c;
        Info_IO io = new Info_IO();

        String where = "_id=?";
        String[] selectionArgs = new String[]{String.valueOf(id)};

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
        io.setIncome(c.getInt(c.getColumnIndex("income")));
        io.setSpendCash(c.getInt(c.getColumnIndex("spend_cash")));
        io.setSpendCard(c.getInt(c.getColumnIndex("spend_card")));

        c.close();

        return io;
    }

    public Info_Dairy getLastInfoDairy(int id_account) {
        String[] selectionArgs = new String[] {String.valueOf(id_account)};

        dairies.clear();
        getData(CODE_INFO_DAIRY, URI_INFO_DAIRY, "id_account=?",selectionArgs,"date DESC");
        if(dairies.isEmpty()) return null;
        else return dairies.get(0);
    }
    public Info_Dairy getLastInfoDairy(int id_account, String date) {
        String selection = "id_account=? and date<=?";
        String[] selectionArgs = new String[] {String.valueOf(id_account),date};

        dairies.clear();
        getData(CODE_INFO_DAIRY, URI_INFO_DAIRY, selection,selectionArgs,"date DESC");
        if(dairies.isEmpty()) return null;
        else return dairies.get(0);
    }
    public Info_IO getLastInfoIO(int id_account, String date) {
        String selection = "id_account=? and date<=?";
        String[] selectionArgs = new String[] {String.valueOf(id_account),date};

        IOs.clear();
        getData(CODE_INFO_IO, URI_INFO_IO, selection,selectionArgs,"date DESC");
        if(IOs.isEmpty()) return null;
        else return IOs.get(0);
    }
    public String getFirstDate() {
        IOs.clear();
        getData(CODE_INFO_IO, URI_INFO_IO, null, null, "date ASC");
        if(IOs.isEmpty()) return null;
        else return IOs.get(0).getDate();
    }
    public String getLastSpendCode(String date) {
        String selection = "date_use=?";
        String[] selectionArgs = new String[] {date};

        spends.clear();
        getData(CODE_SPEND, URI_SPEND, selection,selectionArgs,"_id DESC");
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

        if(currentGroup == -1) return;

        ContentValues cv = new ContentValues();

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
    public void insertInfoIO(int id_account, String date, int input, int output, int income, int spend_cash, int spend_card, int evaluation) {

        if(currentGroup == -1 || currentAccount == -1) return;

        ContentValues cv = new ContentValues();

        cv.put("id_account",id_account);
        cv.put("date", date);
        cv.put("input",input);
        cv.put("output",output);
        cv.put("income",income);
        cv.put("spend_cash",spend_cash);
        cv.put("spend_card",spend_card);
        cv.put("evaluation",evaluation);

        try {
            cr.insert(Uri.parse(URI_INFO_IO), cv);
            modifyInfoDiary(0,id_account, date);
        } catch (Exception e) {
            Log.e(TAG,"DB 추가 error");
        }
    }
    public void insertInfoIO(Info_IO io) {

        if(currentGroup == -1 || currentAccount == -1) return;

        ContentValues cv = new ContentValues();

        cv.put("id_account",io.getAccount());
        cv.put("date",io.getDate());
        cv.put("input",io.getInput());
        cv.put("output",io.getOutput());
        cv.put("income",io.getIncome());
        cv.put("spend_cash",io.getSpendCash());
        cv.put("spend_card",io.getSpendCard());
        cv.put("evaluation",io.getEvaluation());

        try {
            cr.insert(Uri.parse(URI_INFO_IO), cv);
            modifyInfoDiary(0,io.getAccount(),io.getDate());
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
        if(card == null)
        {
            Log.i(TAG,"No card");
            return;
        }

        Spend spend = getSpend(code);
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

        String date = spend.getDate();
        int id_account = card.getAccount();

        Info_IO io = getInfoIO(id_account,date);
        Info_IO io_latest = getLastInfoIO(id_account,date);

        int evaluation = 0;

        // io_latest가 없으면 0
        if(io_latest != null) evaluation = io_latest.getEvaluation();
        // evaluation에 해당일 input, output값 반영
        if(io != null) evaluation = evaluation - io.getOutput() + io.getInput();
        // evaluation에 해당일 spendcash, spendcard, income 반영
        evaluation = evaluation - getSpendsCashSum(date,id_account) - getSpendsCardSum(date,id_account) + getIncomeSum(date,id_account);

        int sum = getSpendsCardSum(date, id_account);

        if(io != null) {
            io.setSpendCard(sum);
            io.setEvaluation(evaluation);
            updateInfoIO(io);
        }
        else {
            try {
                insertInfoIO(id_account, date, 0, 0, 0, 0, sum, evaluation);
            } catch (Exception e) {
                Log.e(TAG,"DB insert error");
            }
        }
    }
    public void insertSpendCash(String code, int id_account) {
        ContentValues cv = new ContentValues();
        String date = null;

        Spend spend = getSpend(code);
        if(spend != null) date = spend.getDate();

        cv.put("spend_code",code);
        cv.put("id_account",id_account);

        cr.insert(Uri.parse(URI_SPEND_CASH),cv);

        Info_IO io = getInfoIO(id_account,date);
        Info_IO io_latest = getLastInfoIO(id_account,date);

        int evaluation = 0;

        // io_latest가 없으면 0
        if(io_latest != null) evaluation = io_latest.getEvaluation();
        // evaluation에 해당일 input, output값 반영
        if(io != null) evaluation = evaluation - io.getOutput() + io.getInput();
        // evaluation에 해당일 spendcash, spendcard, income 반영
        evaluation = evaluation - getSpendsCashSum(date,id_account) - getSpendsCardSum(date,id_account) + getIncomeSum(date,id_account);

        int sum = getSpendsCashSum(date,id_account);

        if(io != null) {
            io.setSpendCash(sum);
            io.setEvaluation(evaluation);
            updateInfoIO(io);
        }
        else insertInfoIO(id_account, date,0,0,0,sum,0,evaluation);
    }
    public void insertIncome(String details, String date, int id_account, int id_category_sub, int amount) {
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

        int sum = getIncomeSum(date, id_account);
        Info_IO io = getInfoIO(id_account,date);
        Info_IO io_latest = getLastInfoIO(id_account,date);

        int evaluation = 0;

        // io_latest가 없으면 0
        if(io_latest != null) evaluation = io_latest.getEvaluation();
        // evaluation에 해당일 input, output값 반영
        if(io != null) evaluation = evaluation - io.getOutput() + io.getInput();
        // evaluation에 해당일 spendcash, spendcard, income 반영
        evaluation = evaluation - getSpendsCashSum(date,id_account) - getSpendsCardSum(date,id_account) + getIncomeSum(date,id_account);


        if(io != null) {
            io.setIncome(sum);
            io.setEvaluation(evaluation);
            updateInfoIO(io);
        }
        else {
            try {
                insertInfoIO(id_account, date, 0, 0, sum, 0, 0, evaluation);
            } catch (Exception e) {
                Log.e(TAG,"DB insert error");
            }
        }
    }

    private void insertInfoDairy(int id_account, String date, int principal, double rate) {

        if(currentGroup == -1 || currentAccount == -1) return;

        ContentValues cv = new ContentValues();

        cv.put("id_account", id_account);
        cv.put("date", date);
        cv.put("principal", principal);
        cv.put("rate",rate);

        try {
            cr.insert(Uri.parse(URI_INFO_DAIRY), cv);
        } catch (Exception e) {
            Log.e(TAG,"DB insert error");
        }
    }

    public void deleteAll() {
        deleteGroup(null,null);
        deleteAccount(null, null);
        deleteInfoIO(null, null);
        deleteCategoryMain(null,null);
        deleteCategorySub(null,null);
        deleteCard(null,null);
        deleteSpend(null,null);
        deleteSpendCash(null,null);
        deleteSpendCard(null,null);
        deleteIncome(null,null);
        deleteInfoDairy();
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
        if(spendCard == null) {
            Log.i(TAG,"No spendCard");
            return;
        }

        Card card = getCard(spendCard.getCard());
        if(card == null) {
            Log.i(TAG,"No card");
            return;
        }

        Spend spend = getSpend(spendCard.getCode());
        if(spend == null) {
            Log.i(TAG,"No spend");
            return;
        }

        cr.delete(Uri.parse(URI_SPEND_CARD),where,args);

        String date = spend.getDate();
        int id_account = card.getAccount();
        int sum = getSpendsCardSum(date, id_account);

        Info_IO io = getInfoIO(id_account,date);
        Info_IO io_latest = getLastInfoIO(id_account,date);

        int evaluation = 0;

        // io_latest가 없으면 0
        if(io_latest != null) evaluation = io_latest.getEvaluation();
        // evaluation에 해당일 input, output값 반영
        if(io != null) evaluation = evaluation - io.getOutput() + io.getInput();
        // evaluation에 해당일 spendcash, spendcard, income 반영
        evaluation = evaluation - getSpendsCashSum(date,id_account) - getSpendsCardSum(date,id_account) + getIncomeSum(date,id_account);

        if(io != null) {
            io.setSpendCard(sum);
            io.setEvaluation(evaluation);
            updateInfoIO(io);
        }
        else {
            try {
                insertInfoIO(id_account, date, 0, 0, 0, 0, sum, evaluation);
            } catch (Exception e) {
                Log.e(TAG,"DB insert error");
            }
        }
    }
    public void deleteSpendCash(String where, String[] args) {

        SpendCash spendCash = getSpendCash(args[0]);
        if(spendCash == null) {
            Log.i(TAG,"No spendCash");
            return;
        }

        Spend spend = getSpend(spendCash.getCode());
        if(spend == null) {
            Log.i(TAG,"No spend");
            return;
        }

        cr.delete(Uri.parse(URI_SPEND_CASH),where,args);

        String date = spend.getDate();
        int id_account = spendCash.getAccount();
        int sum = getSpendsCashSum(date,id_account);

        Info_IO io = getInfoIO(id_account,date);
        Info_IO io_latest = getLastInfoIO(id_account,date);

        int evaluation = 0;

        // io_latest가 없으면 0
        if(io_latest != null) evaluation = io_latest.getEvaluation();
        // evaluation에 해당일 input, output값 반영
        if(io != null) evaluation = evaluation - io.getOutput() + io.getInput();
        // evaluation에 해당일 spendcash, spendcard, income 반영
        evaluation = evaluation - getSpendsCashSum(date,id_account) - getSpendsCardSum(date,id_account) + getIncomeSum(date,id_account);

        if(io != null) {
            io.setSpendCash(sum);
            io.setEvaluation(evaluation);
            updateInfoIO(io);
        }
        else insertInfoIO(id_account, date,0,0,0,sum,0,evaluation);
    }
    public void deleteIncome(String where, String[] args) {
        Income income = getIncome(Integer.valueOf(args[0]));

        if(income == null) return;

        String date = income.getDate();
        int id_account = income.getAccount();

        cr.delete(Uri.parse(URI_INCOME),where,args);

        int sum = getIncomeSum(date, id_account);
        Info_IO io = getInfoIO(id_account,date);
        Info_IO io_latest = getLastInfoIO(id_account,date);

        int evaluation = 0;

        // io_latest가 없으면 0
        if(io_latest != null) evaluation = io_latest.getEvaluation();
        // evaluation에 해당일 input, output값 반영
        if(io != null) evaluation = evaluation - io.getOutput() + io.getInput();
        // evaluation에 해당일 spendcash, spendcard, income 반영
        evaluation = evaluation - getSpendsCashSum(date,id_account) - getSpendsCardSum(date,id_account) + getIncomeSum(date,id_account);

        if(io != null) {
            io.setIncome(sum);
            io.setEvaluation(evaluation);
            updateInfoIO(io);
        }
        else {
            try {
                insertInfoIO(id_account, date, 0, 0, sum, 0, 0, evaluation);
            } catch (Exception e) {
                Log.e(TAG,"DB insert error");
            }
        }
    }
    public void deleteSpend(String where, String[] args) {
        cr.delete(Uri.parse(URI_SPEND),where,args);
    }

    private void deleteInfoIO(String where, String[] args) {
        Info_IO io = getInfoIO(Integer.valueOf(args[0]));

        if(io == null) {
            Log.i(TAG,"The DB is not exist");
            return;
        }

        int id_account = io.getAccount();
        String date = io.getDate();

        try {
            cr.delete(Uri.parse(URI_INFO_IO), where, args);
            modifyInfoDiary(1, id_account, date);
        } catch (Exception e) {
            Log.e(TAG, "DB delete Error");
        }
    }
    private void deleteInfoDairy() {
        cr.delete(Uri.parse(URI_INFO_DAIRY), null, null);
    }

    public void updateGroup(Group group) {
        ContentValues cv = new ContentValues();
        String where = "_id";
        String[] selectionArgs = new String[] {String.valueOf(group.getId())};

        cv.put("name",group.getName());

        try {
            cr.update(Uri.parse(URI_GROUP), cv, where, selectionArgs);
        } catch (Exception e) {
            Log.e(TAG,"DB update error");
        }
    }
    public void updateAccount(int id, String institute, String account, String description) {
        ContentValues cv = new ContentValues();
        String where = "_id";
        String[] selectionArgs = new String[] {String.valueOf(id)};

        cv.put("institute",institute);
        cv.put("number",account);
        cv.put("description",description);

        try {
            cr.update(Uri.parse(URI_ACCOUNT), cv, where, selectionArgs);
        } catch (Exception e) {
            Log.e(TAG,"DB update error");
        }
    }
    public void updateInfoIO(Info_IO io) {
        ContentValues cv = new ContentValues();
        String where = "_id";
        String[] selectionArgs = new String[] {String.valueOf(io.getId())};

        cv.put("id_account",io.getAccount());
        cv.put("date", io.getDate());
        cv.put("input",io.getInput());
        cv.put("output",io.getOutput());
        cv.put("income",io.getIncome());
        cv.put("spend_cash",io.getSpendCash());
        cv.put("spend_card",io.getSpendCard());
        cv.put("evaluation",io.getEvaluation());

        try {
            cr.update(Uri.parse(URI_INFO_IO), cv, where, selectionArgs);
            modifyInfoDiary(1, io.getAccount(), io.getDate());
        } catch (Exception e) {
            Log.e(TAG,"DB update error");
        }
    }
    public void updateCategoryMain(int id, String name) {
        ContentValues cv = new ContentValues();
        String where = "_id";
        String[] selectionArgs = new String[]{String.valueOf(id)};

        cv.put("name", name);

        try {
            cr.update(Uri.parse(URI_CATEGORY_MAIN), cv, where, selectionArgs);
        } catch (Exception e) {
            Log.e(TAG, "DB update error");
        }
    }
    public void updateCategorySub(int id, String name, int main_id) {
        ContentValues cv = new ContentValues();
        String where = "_id";
        String[] selectionArgs = new String[] {String.valueOf(id)};

        cv.put("name",name);
        cv.put("main_id",main_id);

        try {
            cr.update(Uri.parse(URI_GROUP), cv, where, selectionArgs);
        } catch (Exception e) {
            Log.e(TAG,"DB update error");
        }
    }
    public void updateCard(Card card) {
        ContentValues cv = new ContentValues();
        String where = "_id";
        String[] selectionArgs = new String[] {String.valueOf(card.getId())};

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

        String where = "_id";
        String[] selectionArgs = new String[] {String.valueOf(id)};

        Card card = getCard(id_card);
        if(card == null) {
            Log.i(TAG,"No card");
            return;
        }

        Spend spend = getSpend(code);
        if(spend == null) {
            Log.i(TAG,"No spend");
            return;
        }

        ContentValues cv = new ContentValues();

        cv.put("spend_code",code);
        cv.put("id_card",id_card);

        try {
            cr.update(Uri.parse(URI_SPEND_CARD), cv, where, selectionArgs);
        } catch (Exception e) {
            Log.e(TAG,"DB update error");
        }

        String date = spend.getDate();
        int id_account = card.getAccount();
        Info_IO io = getInfoIO(id_account,date);
        Info_IO io_latest = getLastInfoIO(id_account,date);

        int evaluation = 0;

        // io_latest가 없으면 0
        if(io_latest != null) evaluation = io_latest.getEvaluation();
        // evaluation에 해당일 input, output값 반영
        if(io != null) evaluation = evaluation - io.getOutput() + io.getInput();
        // evaluation에 해당일 spendcash, spendcard, income 반영
        evaluation = evaluation - getSpendsCashSum(date,id_account) - getSpendsCardSum(date,id_account) + getIncomeSum(date,id_account);

        int sum = getSpendsCardSum(date, id_account);
        if(io != null) {
            io.setSpendCard(sum);
            io.setEvaluation(evaluation);
            updateInfoIO(io);
        }
        else {
            try {
                insertInfoIO(id_account, date, 0, 0, 0, 0, sum, evaluation);
            } catch (Exception e) {
                Log.e(TAG, "DB update error");
            }
        }
    }
    public void updateSpendCash(int id, String code, int id_account) {
        ContentValues cv = new ContentValues();
        String where = "_id";
        String[] selectionArgs = new String[] {String.valueOf(id)};
        String date = null;

        Spend spend = getSpend(code);
        if(spend != null) date = spend.getDate();

        cv.put("spend_code",code);
        cv.put("id_account",id_account);

        try {
            cr.update(Uri.parse(URI_SPEND_CASH), cv, where, selectionArgs);
        } catch (Exception e) {
            Log.e(TAG,"DB update error");
        }

        Info_IO io = getInfoIO(id_account,date);
        Info_IO io_latest = getLastInfoIO(id_account,date);

        int evaluation = 0;

        // io_latest가 없으면 0
        if(io_latest != null) evaluation = io_latest.getEvaluation();
        // evaluation에 해당일 input, output값 반영
        if(io != null) evaluation = evaluation - io.getOutput() + io.getInput();
        // evaluation에 해당일 spendcash, spendcard, income 반영
        evaluation = evaluation - getSpendsCashSum(date,id_account) - getSpendsCardSum(date,id_account) + getIncomeSum(date,id_account);

        int sum = getSpendsCashSum(date, id_account);
        if(io != null) {
            io.setSpendCash(sum);
            io.setEvaluation(evaluation);
            updateInfoIO(io);
        }
        else {
            try {
                insertInfoIO(id_account, date, 0, 0, 0, sum, 0, evaluation);
            } catch (Exception e) {
                Log.e(TAG, "DB update error");
            }
        }
    }
    public void updateIncome(int id, String details, String date, int id_account, int id_category_sub, int amount) {
        ContentValues cv = new ContentValues();
        String where = "_id";
        String[] selectionArgs = new String[] {String.valueOf(id)};

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

        int sum = getIncomeSum(date, id_account);
        Info_IO io = getInfoIO(id_account, date);
        Info_IO io_latest = getLastInfoIO(id_account,date);

        int evaluation = 0;

        // io_latest가 없으면 0
        if(io_latest != null) evaluation = io_latest.getEvaluation();
        // evaluation에 해당일 input, output값 반영
        if(io != null) evaluation = evaluation - io.getOutput() + io.getInput();
        // evaluation에 해당일 spendcash, spendcard, income 반영
        evaluation = evaluation - getSpendsCashSum(date,id_account) - getSpendsCardSum(date,id_account) + getIncomeSum(date,id_account);

        if(io != null) {
            io.setIncome(sum);
            io.setEvaluation(evaluation);
            updateInfoIO(io);
        }
        else {
            try {
                insertInfoIO(id_account, date, 0, 0, sum, 0, 0, evaluation);
            } catch (Exception e) {
                Log.e(TAG,"DB insert error");
            }
        }
    }

    private void updateInfoDairy(int id, int id_account, String date, int principal, double rate) {
        ContentValues cv = new ContentValues();
        String where = "_id";
        String[] selectionArgs = new String[] {String.valueOf(id)};

        cv.put("id_account", id_account);
        cv.put("date", date);
        cv.put("principal",principal);
        cv.put("rate",String.format(Locale.getDefault(),"%.2f",rate));

        try {
            cr.update(Uri.parse(URI_INFO_DAIRY), cv, where, selectionArgs);
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
            assert cursor != null;
            cursor.close();
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
                    account.setDescription(cursor.getString(3));
                    account.setGroup(cursor.getInt(4));

                    accounts.add(account);
                    break;
                case CODE_INFO_IO:
                    Info_IO io = new Info_IO();

                    io.setId(cursor.getInt(0));
                    io.setDate(cursor.getString(1));
                    io.setInput(cursor.getInt(2));
                    io.setOutput(cursor.getInt(3));
                    io.setEvaluation(cursor.getInt(4));
                    io.setSpendCash(cursor.getInt(5));
                    io.setSpendCard(cursor.getInt(6));
                    io.setIncome(cursor.getInt(7));
                    io.setAccount(cursor.getInt(8));

                    IOs.add(io);
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
        Cursor c;

        // table이 존재하지 않으면
        if(checkDBTable("tbl_spend_cash") == 0) return 0;

        String where = "A.date_use=? AND B.id_account=?";
        String[] selectionArgs = {date,String.valueOf(id_account)};
        String[] select = {"total(A.amount) "
                + "FROM tbl_spend AS A "
                + "LEFT JOIN tbl_spend_cash AS B "
                + "ON A.spend_code=B.spend_code"};

        c = cr.query(Uri.parse(URI_JOIN), select, where, selectionArgs, null);

        assert c != null;
        c.moveToNext();

        int sum = c.getInt(0);
        c.close();

        return sum;
    }
    public int getSpendsCardSum(String date, int id_account) {
        Cursor c;

        // table이 존재하지 않으면
        if(checkDBTable("tbl_spend_card") == 0) return 0;

        String where = "A.date_use=? AND C.id_account=?";
        String[] selectionArgs = {date,String.valueOf(id_account)};
        String[] select = {"total(A.amount) "
                + "FROM tbl_spend AS A "
                + "JOIN tbl_spend_card AS B "
                + "ON A.spend_code=B.spend_code "
                + "JOIN tbl_card AS C "
                + "ON B.id_card=C._id"};

        c = cr.query(Uri.parse(URI_JOIN), select, where, selectionArgs, null);

        assert c != null;
        c.moveToNext();

        int sum = c.getInt(0);
        c.close();

        return sum;
    }
    public int getIncomeSum(String date, int id_account) {
        Cursor c;

        // table이 존재하지 않으면
        if(checkDBTable("tbl_income") == 0) return 0;

        String where = "date=? AND id_account=?";
        String[] select = {"total(amount) AS SUM"};
        String[] selectionArgs = {date,String.valueOf(id_account)};

        c = cr.query(Uri.parse(URI_INCOME), select, where, selectionArgs, null);

        assert c != null;
        c.moveToNext();

        int sum = c.getInt(0);
        c.close();

        return sum;
    }
    public int getSpendMonth() {
        Cursor c;

        // table이 존재하지 않으면
        if(checkDBTable("tbl_spend") == 0) return 0;

        String where = "date_use BETWEEN date('now','start of month') AND date('now')";
        String[] select = {"total(amount) AS SUM"};

        c = cr.query(Uri.parse(URI_SPEND), select, where, null, null);

        assert c != null;
        c.moveToNext();

        int sum = c.getInt(0);
        c.close();

        return sum;
    }
    public int getIncomeMonth() {
        Cursor c;

        // table이 존재하지 않으면
        if(checkDBTable("tbl_income") == 0) return 0;

        String where = "date BETWEEN date('now','start of month') AND date('now')";
        String[] select = {"total(amount) AS SUM"};

        c = cr.query(Uri.parse(URI_INCOME), select, where, null, null);

        assert c != null;
        c.moveToNext();

        int sum = c.getInt(0);
        c.close();

        return sum;
    }

    private int getSum(String uri, int id_account, String[] column, String selectedDate) {
        Cursor c;

        String[] select = {"total(" + column[0] + ") AS SUM"};
        String where = "date<=? AND id_account=?";
        String[] selectionArgs = new String[]{selectedDate,String.valueOf(id_account)};

        c = cr.query(Uri.parse(uri), select, where, selectionArgs, null);
        assert c != null;
        c.moveToNext();

        int sum = c.getInt(0);
        c.close();

        return sum;
    }

    private void modifyInfoDiary(int select, int id_account, String selectedDate) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String txtDate;
        int evaluation = 0;
        int index = 0;
        Info_IO io;

        if(select == 0) {
            io = getInfoIO(id_account, selectedDate);

            if(io != null) evaluation = io.getEvaluation();
            calInfoDairy(select,0,id_account,selectedDate,evaluation);
            select = 1;
        }

        List<Info_Dairy> daires = getInfoDaires(id_account);
        if(daires.isEmpty()) {
            Log.i(TAG,"No dairy");
            return;
        }

        try {
            @SuppressWarnings("UnusedAssignment")
            Date date = df.parse(daires.get(index).getDate());

            do {
                txtDate = daires.get(index).getDate();
                date = df.parse(txtDate);

                io = getInfoIO(id_account,txtDate);

                if(io != null) evaluation = io.getEvaluation();

                calInfoDairy(select,daires.get(index).getId(),id_account,txtDate,evaluation);
                index++;

            } while(df.parse(selectedDate).compareTo(date) < 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void calInfoDairy(int select, int id, int id_account, String date, int evaluation) {
        int sum_in, sum_out, sum_spend_card, sum_spend_cash, sum_income, principal;
        double rate = 0;

        sum_in = getSum(URI_INFO_IO,id_account,new String[]{"input"},date);
        sum_income = getSum(URI_INFO_IO,id_account,new String[]{"income"},date);
        sum_out = getSum(URI_INFO_IO,id_account,new String[]{"output"},date);
        sum_spend_card = getSum(URI_INFO_IO,id_account,new String[]{"spend_card"},date);
        sum_spend_cash = getSum(URI_INFO_IO,id_account,new String[]{"spend_cash"},date);

        principal = sum_in + sum_income - sum_out - sum_spend_cash - sum_spend_card;

        if(principal != 0 && evaluation != 0) rate = (double)evaluation / (double)principal * 100 - 100;

        switch(select) {
            case 0:
                insertInfoDairy(id_account, date, principal, rate);
                break;
            case 1:
                updateInfoDairy(id, id_account, date, principal, rate);
                break;
        }
    }
    private int checkDBTable(String name) {
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
}
