package com.example.watering.investrecord.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.watering.investrecord.data.*;
import com.example.watering.investrecord.*;
import com.example.watering.investrecord.info.*;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by watering on 17. 11. 2.
 */

@SuppressWarnings("DefaultFileTemplate")
public class UserDialogFragment extends DialogFragment {
    private MainActivity mActivity;
    private IRResolver ir;
    private LayoutInflater inflater;
    private AlertDialog.Builder builder;
    private View view;
    private UserListener listener;

    private ArrayAdapter<String> adapter1;
    private ArrayAdapter<String> adapter_category_main, adapter_category_sub;
    private ArrayAdapter<String> adapter_account, adapter_card;
    private ArrayAdapter<String> adapter_account_from, adapter_account_to;
    private ArrayAdapter<String> adapter_approval_2;

    private final List<String> lists_group = new ArrayList<>();
    private final List<String> lists_category_main = new ArrayList<>();
    private final List<String> lists_category_sub = new ArrayList<>();
    private final List<String> lists_account = new ArrayList<>();
    private final List<String> lists_card = new ArrayList<>();
    private final List<String> lists_select = new ArrayList<>();

    private List<Group> groups = new ArrayList<>();
    private List<Account> accounts = new ArrayList<>();
    private List<Account> accounts_from = new ArrayList<>();
    private List<Account> accounts_to = new ArrayList<>();
    private List<CategoryMain> categoryMains = new ArrayList<>();
    private List<CategorySub> categorySubs = new ArrayList<>();
    private List<Card> cards = new ArrayList<>();

    private boolean exist;

    private int selectedMainId, selectedSubId, selectedCardId, selectedAccountId = -1;
    private int selectedAccountIdFrom, selectedAccountIdTo;
    private int selectedGroupIdFrom, selectedGroupIdTo;
    private int id_income, id_spend, id_spend_cash, id_spend_card;
    private int selectedId = -1;
    private int type_dialog, type_spend;
    private final int schedule = 0;
    private static final int KIND_ALL = 0, KIND_SPEND = 1, KIND_INCOME = 2;
    private static final int TYPE_CASH = 1, TYPE_CARD = 2;

    private String selectedDate;
    private String selectedSpendCode = "";
    private static final String TAG = "InvestRecord";

    private final DecimalFormat df = new DecimalFormat("#,###");

    public interface UserListener {
        void onWorkComplete(String str);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mActivity = (MainActivity) getActivity();
        assert mActivity != null;
        ir = mActivity.ir;

        builder = new AlertDialog.Builder(getActivity());
        inflater = mActivity.getLayoutInflater();

        switch (type_dialog) {
            case 0:
                dialogSelect();
                break;
            case 1:
                dialogInoutKRW();
                break;
            case 2:
                dialogTransfer();
                break;
            case 3:
                dialogInoutForeign();
                break;
            case R.id.menu_sub1_addGroup:
                dialogGroupAdd();
                break;
            case R.id.menu_sub1_editGroup:
                dialogGroupEdit();
                break;
            case R.id.menu_sub1_delGroup:
                dialogGroupDel();
                break;
            case R.id.navigation_item_DB_manage:
                dialogDBManage();
                break;
            case R.id.editText_frag3_date:
            case R.id.editText_frag4_date:
            case R.id.editText_dlg_spend_date:
                dialogDate();
                break;
            case R.id.floating_frag3:
                dialogSpend();
                break;
            case R.id.floating_frag4:
                dialogIncome();
                break;
            case R.id.menu_sub2_category_add:
                dialogCategoryAdd();
                break;
            case R.id.menu_sub2_category_edit:
                dialogCategoryEdit();
                break;
            case R.id.menu_sub2_category_del:
                dialogCategoryDel();
                break;
            case R.id.menu_sub2_card_add:
                dialogCardAdd();
                break;
            case R.id.menu_sub2_card_edit:
                dialogCardEdit();
                break;
            case R.id.menu_sub2_card_del:
                dialogCardDel();
                break;
        }
        return builder.create();
    }
    public static UserDialogFragment newInstance(int type, UserListener listener) {
        UserDialogFragment fragment = new UserDialogFragment();
        fragment.listener = listener;
        fragment.type_dialog = type;
        return fragment;
    }

    public void initId(int id) {
        this.selectedId = id;
    }
    public void initCode(String code) {
        this.selectedSpendCode = code;
    }
    public void setSelectedDate(String selectedDate) {
        this.selectedDate = selectedDate;
    }

    @SuppressLint("InflateParams")
    private void dialogSpend() {
        final ArrayAdapter<String> adapter_approval_1;

        List<String> lists_approval = new ArrayList<>();
        Spend spend = new Spend();
        SpendCash spendCash;
        SpendCard spendCard;
        int position = 0, id_category;

        view = inflater.inflate(R.layout.dialog_spend, null);

        final EditText editText_date = view.findViewById(R.id.editText_dlg_spend_date);
        final EditText editText_details = view.findViewById(R.id.editText_dlg_spend_details);
        final EditText editText_amount = view.findViewById(R.id.editText_dlg_spend_amount);
        final Spinner spinner_approval_2 = view.findViewById(R.id.spinner_dlg_spend_approval_2);

        Spinner spinner_category_main = view.findViewById(R.id.spinner_dlg_spend_category_main);
        Spinner spinner_category_sub = view.findViewById(R.id.spinner_dlg_spend_category_sub);
        Spinner spinner_approval_1 = view.findViewById(R.id.spinner_dlg_spend_approval_1);

        // Dialog 초기화
        if(selectedSpendCode.isEmpty()) editText_date.setText(selectedDate);
        else {
            spend = ir.getSpend(selectedSpendCode);

            if(spend != null) {
                id_spend = spend.getId();
                editText_date.setText(spend.getDate());
                editText_amount.setText(df.format(spend.getAmount()));
                editText_details.setText(spend.getDetails());
            }
        }

        //날짜 변경 시
        editText_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserDialogFragment dialog = UserDialogFragment.newInstance(R.id.editText_dlg_spend_date, new UserDialogFragment.UserListener() {
                    @Override
                    public void onWorkComplete(String date) {
                        editText_date.setText(date);
                        selectedDate = date;
                    }
                });
                dialog.setSelectedDate(selectedDate);
                //noinspection ConstantConditions
                dialog.show(getFragmentManager(), "dialog");
            }
        });


        // 메인 카테고리 선택
        updateCategoryMainList(KIND_SPEND);
        if(categoryMains.size() > 0) {
            if(selectedSpendCode.isEmpty()) {
                if(categoryMains.size() > 0) selectedMainId = categoryMains.get(0).getId();
                position = 0;
            }
            else {
                id_category = -1;
                if(spend != null) id_category = spend.getCategory();
                CategorySub categorySub = ir.getCategorySub(id_category);
                if(categorySub != null) selectedMainId = categorySub.getCategoryMain();
                position = findCategoryMain(selectedMainId);
            }
        }

        //noinspection ConstantConditions
        adapter_category_main = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item, lists_category_main);
        spinner_category_main.setAdapter(adapter_category_main);
        if(!adapter_category_main.isEmpty()) spinner_category_main.setSelection(position);
        spinner_category_main.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedMainId = categoryMains.get(i).getId();
                updateCategorySubList();
                adapter_category_sub.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // Sub 카테고리 선택
        updateCategorySubList();
        if (categorySubs.size() > 0) {
            if(selectedSpendCode.isEmpty()) {
                if(categorySubs.size() > 0) selectedSubId = categorySubs.get(0).getId();
                position = 0;
            }
            else {
                if(spend != null) selectedSubId = spend.getCategory();
                position = findCategorySub(selectedSubId);
            }
        }
        //noinspection ConstantConditions
        adapter_category_sub = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item, lists_category_sub);
        spinner_category_sub.setAdapter(adapter_category_sub);
        if(!adapter_category_sub.isEmpty()) spinner_category_sub.setSelection(position);
        spinner_category_sub.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(categorySubs.size() > 0) selectedSubId = categorySubs.get(i).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // 현금, 카드 선택
        lists_approval.clear();
        lists_approval.add(getString(R.string.cash));
        lists_approval.add(getString(R.string.card));

        if(selectedSpendCode.isEmpty()) {
            position = 0;
            type_spend = TYPE_CASH;
        }
        else {
            if(selectedSpendCode.charAt(0) == '1') {
                position = 0;
                type_spend = TYPE_CASH;
            }
            else {
                position = 1;
                type_spend = TYPE_CARD;
            }
        }

        adapter_approval_1 = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item,lists_approval);
        spinner_approval_1.setAdapter(adapter_approval_1);
        if(!adapter_approval_1.isEmpty()) spinner_approval_1.setSelection(position);

        spinner_approval_1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int position = 0;
                type_spend = i+1;
                switch(type_spend) {
                    case 1:
                        adapter_approval_2 = adapter_account;
                        position = findAccount(selectedAccountId);
                        break;
                    case 2:
                        adapter_approval_2 = adapter_card;
                        position = findCard(selectedCardId);
                        break;
                }
                spinner_approval_2.setAdapter(adapter_approval_2);
                spinner_approval_2.setSelection(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                type_spend = 0;
            }
        });

        // 현금 선택 시 계좌 List
        updateAccountList(ir.getCurrentGroup());
        if(type_spend == TYPE_CASH) {
            if(selectedSpendCode.isEmpty()) {
                if(accounts.size() > 0) selectedAccountId = accounts.get(0).getId();
                position = 0;
            }
            else {
                spendCash = ir.getSpendCash(selectedSpendCode);
                if(spendCash != null) {
                    selectedAccountId = spendCash.getAccount();
                    id_spend_cash = spendCash.getId();
                }
                position = findAccount(selectedAccountId);
            }
        }
        //noinspection ConstantConditions
        adapter_account = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item, lists_account);

        // 카드 선택 시 카드 List
        updateCardList();
        if(type_spend == TYPE_CARD) {
            if (selectedSpendCode.isEmpty()) {
                if(cards.size() > 0) selectedCardId = cards.get(0).getId();
                position = 0;
            } else {
                spendCard = ir.getSpendCard(selectedSpendCode);
                if(spendCard != null) {
                    selectedCardId = spendCard.getCard();
                    id_spend_card = spendCard.getId();
                }
                position = findCard(selectedCardId);
            }
        }
        //noinspection ConstantConditions
        adapter_card = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item, lists_card);

        switch (type_spend) {
            case TYPE_CASH:
                adapter_approval_2 = adapter_account;
                break;
            case TYPE_CARD:
                adapter_approval_2 = adapter_card;
                break;
        }

        spinner_approval_2.setAdapter(adapter_approval_2);
        spinner_approval_2.setSelection(position);
        spinner_approval_2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch(type_spend) {
                    case 1:
                        selectedAccountId = accounts.get(i).getId();
                        break;
                    case 2:
                        selectedCardId = accounts.get(i).getId();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        builder.setView(view).setTitle(getString(R.string.input_spend));
        builder.setPositiveButton(getString(R.string.finish),new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int amount = 0, lastCode = 0;
                String newCode, details = editText_details.getText().toString();

                selectedDate = editText_date.getText().toString();

                List<Spend> spends = ir.getSpends(selectedDate);
                Calendar date = mActivity.strToCalendar(selectedDate);

                try {
                    amount = df.parse(editText_amount.getText().toString()).intValue();
                } catch (Exception e) {
                    Log.e(TAG, "parse error");
                }

                // spend 항목이 삭제된 경우 등 코드 중복 방지
                if(spends.size() > 0) lastCode = Integer.parseInt(ir.getLastSpendCode(selectedDate).substring(10)) + 1;
                newCode = String.format(Locale.getDefault(), "%d%d%04d%02d%02d%02d", type_spend, schedule, date.get(Calendar.YEAR), date.get(Calendar.MONTH) + 1, date.get(Calendar.DAY_OF_MONTH), lastCode);

                ir.insertSpend(newCode, details, selectedDate, amount, selectedSubId);
                // 현금 지출인지 카드 지출인지
                switch (type_spend) {
                    case TYPE_CASH:
                        // 현금 지출을 새로 입력하는 경우
                        if (selectedSpendCode.isEmpty()) ir.insertSpendCash(newCode, selectedAccountId);
                        else {
                            // 카드에서 현금으로 변경한 경우
                            if(selectedSpendCode.charAt(0) != '1') {
                                ir.deleteSpendCard("spend_code",new String[] {selectedSpendCode});
                                ir.insertSpendCash(newCode, selectedAccountId);
                            }
                            // 현금인데 계좌가 변경된 경우
                            else if(ir.getSpendCash(selectedSpendCode).getAccount() != selectedAccountId) {
                                ir.deleteSpendCash("spend_code",new String[] {selectedSpendCode});
                                ir.insertSpendCash(newCode, selectedAccountId);
                            }
                            else {
                                ir.updateSpendCash(id_spend_cash, newCode, selectedAccountId);
                            }
                        }
                        break;
                    case TYPE_CARD:
                        // 카드 지출을 새로 입력하는 경우
                        if (selectedSpendCode.isEmpty()) ir.insertSpendCard(newCode, selectedCardId);
                        else {
                            // 현금에서 카드로 변경한 경우
                            if(selectedSpendCode.charAt(0) != '2') {
                                ir.deleteSpendCash("spend_code",new String[] {selectedSpendCode});
                                ir.insertSpendCard(newCode, selectedCardId);
                            }
                            // 카드가 변경된 경우
                            else if(ir.getSpendCard(selectedSpendCode).getCard() != selectedCardId) {
                                ir.deleteSpendCard("spend_code", new String[] {selectedSpendCode});
                                ir.insertSpendCard(newCode, selectedCardId);
                            }
                            else {
                                ir.updateSpendCard(id_spend_card, newCode, selectedCardId);
                            }
                        }
                        break;
                }
                ir.deleteSpend("_id",new String[] {String.valueOf(id_spend)});
                mActivity.fragmentSub1.callUpdateFrag1();
                mActivity.fragmentSub1.callUpdateFrag2();
                mActivity.fragmentSub2.updateFragSub2();
                mActivity.fragmentSub2.callUpdateFrag3();
                mActivity.fragmentSub2.callUpdateFrag5();
            }
        });
        builder.setNegativeButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (type_spend) {
                    case TYPE_CASH:
                        ir.deleteSpendCash("spend_code",new String[] {selectedSpendCode});
                        break;
                    case TYPE_CARD:
                        ir.deleteSpendCard("spend_code",new String[] {selectedSpendCode});
                        break;
                }
                ir.deleteSpend("_id",new String[] {String.valueOf(id_spend)});
                mActivity.fragmentSub1.callUpdateFrag1();
                mActivity.fragmentSub1.callUpdateFrag2();
                mActivity.fragmentSub2.updateFragSub2();
                mActivity.fragmentSub2.callUpdateFrag3();
                mActivity.fragmentSub2.callUpdateFrag5();
            }
        });
        builder.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }
    @SuppressLint("InflateParams")
    private void dialogIncome() {
        int position = 0, id_category = -1;
        Income in, income = new Income();
        CategorySub categorySub;

        view = inflater.inflate(R.layout.dialog_income, null);

        final EditText editText_date = view.findViewById(R.id.editText_dlg_income_date);
        final EditText editText_details = view.findViewById(R.id.editText_dlg_income_details);
        final EditText editText_amount = view.findViewById(R.id.editText_dlg_income_amount);

        Spinner spinner_category_main = view.findViewById(R.id.spinner_dlg_income_category_main);
        Spinner spinner_category_sub = view.findViewById(R.id.spinner_dlg_income_category_sub);
        Spinner spinner_account = view.findViewById(R.id.spinner_dlg_income_account);

        if(selectedId < 0) editText_date.setText(selectedDate);
        else {
            income = ir.getIncome(selectedId);

            if(income != null) {
                id_income = income.getId();
                editText_date.setText(income.getDate());
                editText_amount.setText(df.format(income.getAmount()));
                editText_details.setText(income.getDetails());
            }
        }

        editText_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserDialogFragment dialog = UserDialogFragment.newInstance(R.id.editText_dlg_spend_date, new UserDialogFragment.UserListener() {
                    @Override
                    public void onWorkComplete(String date) {
                        editText_date.setText(date);
                        selectedDate = date;
                    }
                });
                dialog.setSelectedDate(selectedDate);
                //noinspection ConstantConditions
                dialog.show(getFragmentManager(), "dialog");
            }
        });

        updateCategoryMainList(KIND_INCOME);
        if(categoryMains.size() > 0) {
            if (selectedId < 0) {
                if(categoryMains.size() > 0) selectedMainId = categoryMains.get(0).getId();
                position = 0;
            } else {
                if(income != null) id_category = income.getCategory();
                categorySub = ir.getCategorySub(id_category);
                if(categorySub != null) selectedMainId = categorySub.getCategoryMain();
                position = findCategoryMain(selectedMainId);
            }
        }
        //noinspection ConstantConditions
        adapter_category_main = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item, lists_category_main);
        spinner_category_main.setAdapter(adapter_category_main);
        if(!adapter_category_main.isEmpty()) spinner_category_main.setSelection(position);
        spinner_category_main.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedMainId = categoryMains.get(i).getId();
                updateCategorySubList();
                adapter_category_sub.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        updateCategorySubList();
        if (categorySubs.size() > 0) {
            if(selectedId < 0) {
                if(categorySubs.size() > 0) selectedSubId = categorySubs.get(0).getId();
                position = 0;
            }
            else {
                if(income != null) selectedSubId = income.getCategory();
                position = findCategorySub(selectedSubId);
            }
        }
        //noinspection ConstantConditions
        adapter_category_sub = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item, lists_category_sub);
        spinner_category_sub.setAdapter(adapter_category_sub);
        if(!adapter_category_sub.isEmpty()) spinner_category_sub.setSelection(position);
        spinner_category_sub.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(categorySubs.size() > 0) selectedSubId = categorySubs.get(i).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        updateAccountList(ir.getCurrentGroup());
        if(selectedId < 0) {
            if(accounts.size() > 0) selectedAccountId = accounts.get(0).getId();
            position = 0;
        }
        else {
            in = ir.getIncome(selectedId);
            if(income != null) selectedAccountId = in.getAccount();
            position = findAccount(selectedAccountId);
        }
        //noinspection ConstantConditions
        adapter_account = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item, lists_account);
        spinner_account.setAdapter(adapter_account);
        if(!adapter_account.isEmpty()) spinner_account.setSelection(position);
        spinner_account.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedAccountId = accounts.get(i).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        builder.setView(view).setTitle(getString(R.string.input_income));
        builder.setPositiveButton(getString(R.string.finish),new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String details = editText_details.getText().toString();
                int amount = 0;
                try {
                    amount = df.parse(editText_amount.getText().toString()).intValue();
                } catch (Exception e) {
                    Log.e(TAG, "Parse error");
                }
                selectedDate = editText_date.getText().toString();

                if(selectedId < 0) ir.insertIncome(details,selectedDate,selectedAccountId,selectedSubId,amount);
                else ir.updateIncome(id_income,details,selectedDate,selectedAccountId,selectedSubId,amount);
                mActivity.fragmentSub1.callUpdateFrag1();
                mActivity.fragmentSub1.callUpdateFrag2();
                mActivity.fragmentSub2.updateFragSub2();
                mActivity.fragmentSub2.callUpdateFrag4();
                mActivity.fragmentSub2.callUpdateFrag5();
                listener.onWorkComplete(null);
            }
        });
        builder.setNegativeButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ir.deleteIncome("_id",new String[] {String.valueOf(id_income)});
                mActivity.fragmentSub1.callUpdateFrag1();
                mActivity.fragmentSub1.callUpdateFrag2();
                mActivity.fragmentSub2.updateFragSub2();
                mActivity.fragmentSub2.callUpdateFrag4();
                mActivity.fragmentSub2.callUpdateFrag5();
            }
        });
        builder.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }

    @SuppressLint("InflateParams")
    private void dialogDBManage() {
        view = inflater.inflate(R.layout.dialog_listview, null);

        ListView listView = view.findViewById(R.id.listView_dlg_listView);

        lists_select.clear();
        lists_select.add(getString(R.string.delete_all));
        lists_select.add(getString(R.string.DB_delete));
        lists_select.add(getString(R.string.DB_backup));
        lists_select.add(getString(R.string.DB_restore));
        lists_select.add(getString(R.string.DB_save));

        //noinspection ConstantConditions
        adapter1 = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, lists_select);

        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setAdapter(adapter1);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str = null;
                switch(position) {
                    case 0:
                        str = "delall";
                        break;
                    case 1:
                        str = "del";
                        break;
                    case 2:
                        str = "backup";
                        break;
                    case 3:
                        str = "restore";
                        break;
                    case 4:
                        str = "save";
                        break;
                }
                listener.onWorkComplete(str);
            }
        });

        builder.setView(view).setTitle(getString(R.string.setting));
        builder.setPositiveButton(getString(R.string.finish), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onWorkComplete("");
            }
        });
    }
    @SuppressLint("InflateParams")
    private void dialogSelect() {
        view = inflater.inflate(R.layout.dialog_listview, null);

        ListView listView = view.findViewById(R.id.listView_dlg_listView);

        lists_select.clear();
        lists_select.add(getString(R.string.input_inout_krw));
        lists_select.add(getString(R.string.input_inout_foreign));
        lists_select.add(getString(R.string.transfer));

        //noinspection ConstantConditions
        adapter1 = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, lists_select);

        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setAdapter(adapter1);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position) {
                    case 0:
                        UserDialogFragment dialog = UserDialogFragment.newInstance(1, new UserDialogFragment.UserListener() {
                            @Override
                            public void onWorkComplete(String date) {
                            }
                        });
                        dialog.setSelectedDate(selectedDate);
                        //noinspection ConstantConditions
                        dialog.show(getFragmentManager(), "dialog");
                        break;
                    case 1:
                        dialog = UserDialogFragment.newInstance(3, new UserDialogFragment.UserListener() {
                            @Override
                            public void onWorkComplete(String date) {
                            }
                        });
                        dialog.setSelectedDate(selectedDate);
                        //noinspection ConstantConditions
                        dialog.show(getFragmentManager(), "dialog");
                        break;
                    case 2:
                        dialog = UserDialogFragment.newInstance(2, new UserDialogFragment.UserListener() {
                            @Override
                            public void onWorkComplete(String date) {
                            }
                        });
                        dialog.setSelectedDate(selectedDate);
                        //noinspection ConstantConditions
                        dialog.show(getFragmentManager(), "dialog");
                        break;
                }
                listener.onWorkComplete(null);
            }
        });

        builder.setView(view).setTitle(getString(R.string.select));
        builder.setPositiveButton(getString(R.string.finish), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onWorkComplete("");
            }
        });
    }
    @SuppressLint("InflateParams")
    private void dialogInoutKRW() {
        view = inflater.inflate(R.layout.dialog_inout_krw, null);

        final EditText txtInput = view.findViewById(R.id.editText_dlg_inout_krw_input);
        final EditText txtOutput = view.findViewById(R.id.editText_dlg_inout_krw_output);
        final EditText txtEvaluation = view.findViewById(R.id.editText_dlg_inout_krw_evaluation);
        final EditText txtIncome = view.findViewById(R.id.editText_dlg_inout_krw_income);
        final EditText txtSpend = view.findViewById(R.id.editText_dlg_inout_krw_spend);
        final EditText txtPrincipal = view.findViewById(R.id.editText_dlg_inout_krw_principal);
        TextView txtDate = view.findViewById(R.id.textView_dlg_inout_krw_date);

        final int id_account = ir.getCurrentAccount();

        txtIncome.setEnabled(false);
        txtSpend.setEnabled(false);
        txtPrincipal.setEnabled(false);
        if(ir.getCurrentAccount() < 0) {
            Toast.makeText(mActivity.getApplicationContext(),R.string.toast_no_account,Toast.LENGTH_SHORT).show();
            return;
        }

        InfoIOKRW io_krw = ir.getInfoIOKRW(id_account,selectedDate);
        InfoIOKRW io_krw_latest = ir.getLastInfoIOKRW(id_account,selectedDate);

        int evaluation;

        //선택한 날짜의 값이 있으면 가져오고 없으면 최근값을 가져온다.
        if(io_krw != null) evaluation = io_krw.getEvaluation();
        else if(io_krw_latest != null) evaluation = io_krw_latest.getEvaluation();
        else evaluation = 0;

        DecimalFormat df = new DecimalFormat("#,###");

        txtDate.setText(selectedDate);

        //선택한 날짜의 입력값이 없으면 평가액만 최근값으로 반영한다.
        if(io_krw == null) {
            exist = false;
            txtInput.setText("0");
            txtOutput.setText("0");
            txtEvaluation.setText(String.valueOf(evaluation));
            txtIncome.setText("0");
            txtSpend.setText("0");
        }
        else {
            exist = true;
            txtInput.setText(df.format(io_krw.getInput()));
            txtOutput.setText(df.format(io_krw.getOutput()));
            txtEvaluation.setText(df.format(evaluation));
            txtIncome.setText(df.format(io_krw.getIncome()));
            txtSpend.setText(df.format(io_krw.getSpendCard()+io_krw.getSpendCash()));
        }

        InfoDairyKRW dairy_krw = ir.getLastInfoDairyKRW(id_account, selectedDate);
        if(dairy_krw != null) {
            txtPrincipal.setText(df.format(dairy_krw.getPrincipal()));
        }
        else {
            txtPrincipal.setText("0");
        }

        builder.setView(view).setTitle(getString(R.string.input_inout_krw));
        builder.setPositiveButton(getString(R.string.regist), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String str_in, str_out, str_eval;

                DecimalFormat df = new DecimalFormat("#,###");

                str_in = txtInput.getText().toString();
                str_out = txtOutput.getText().toString();
                str_eval = txtEvaluation.getText().toString();

                if(str_in.isEmpty()) str_in = "0";
                if(str_out.isEmpty()) str_out = "0";

                InfoIOKRW io_krw = new InfoIOKRW();

                if(exist) {
                    io_krw = ir.getInfoIOKRW(id_account,selectedDate);
                    if(io_krw == null) {
                        Log.i(TAG, "No InfoIOKRW");
                        return;
                    }
                }

                int in = 0, out = 0, evaluation = 0;
                try {
                    in = df.parse(str_in).intValue();
                    out = df.parse(str_out).intValue();
                    if(str_eval.isEmpty()) {
                        evaluation = in - out;
                    }
                    else {
                        evaluation = df.parse(str_eval).intValue();
                    }
                } catch (ParseException e) {
                    Log.e(TAG,"Data Format Parse Error");
                }
                io_krw.setDate(selectedDate);
                io_krw.setInput(in);
                io_krw.setOutput(out);
                io_krw.setEvaluation(evaluation);
                io_krw.setAccount(id_account);

                if(!exist) ir.insertInfoIOKRW(io_krw);
                else ir.updateInfoIOKRW(io_krw);

                mActivity.fragmentSub1.callUpdateFrag1();
                mActivity.fragmentSub3.callUpdateFrag6();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
    }
    @SuppressLint("InflateParams")
    private void dialogInoutForeign() {
        int input_krw, output_krw;
        double input_foreign, output_foreign, evaluation;
        final int[] current_currency = {0};
        double exchangeRate_input = 0f;
        double exchangeRate_output = 0f;

        final DecimalFormat df = new DecimalFormat("#,###.##");
        ArrayList<String> lists_currency = new ArrayList<>();
        ArrayAdapter<String> adapter_currency;

        view = inflater.inflate(R.layout.dialog_inout_foreign, null);

        final TextView txtDate = view.findViewById(R.id.textView_dlg_inout_foreign_date);
        final Spinner spinnerCurrency = view.findViewById(R.id.spinner_dlg_inout_foreign_currency);
        final EditText txtInput = view.findViewById(R.id.editText_dlg_inout_foreign_input);
        final EditText txtInputExchangeRate = view.findViewById(R.id.editText_dlg_inout_foreign_input_exchange_rate);
        final EditText txtInputKRW = view.findViewById(R.id.editText_dlg_inout_foreign_input_krw);
        final EditText txtOutput = view.findViewById(R.id.editText_dlg_inout_foreign_output);
        final EditText txtOutputExchangeRate = view.findViewById(R.id.editText_dlg_inout_foreign_output_exchange_rate);
        final EditText txtOutputKRW = view.findViewById(R.id.editText_dlg_inout_foreign_output_krw);
        final EditText txtEvaluation = view.findViewById(R.id.editText_dlg_inout_foreign_evaluation);
        final EditText txtPrincipal = view.findViewById(R.id.editText_dlg_inout_foreign_principal);
        final EditText txtExchangeRate = view.findViewById(R.id.editText_dlg_inout_foreign_evaluation_rate);

        final Double[] exchangeRate = new Double[1];

        MainActivity.ExchangeTask exchangeTask = new MainActivity.ExchangeTask() {
            @Override
            public void finish(Double[] exchange) {
                if(exchange == null) {
                    Toast.makeText(getContext(),R.string.toast_exchange_error,Toast.LENGTH_SHORT).show();
                    return;
                }

                exchangeRate[0] = exchange[current_currency[0]];

                if(exchangeRate[0] == null) {
                    Toast.makeText(getContext(),R.string.toast_exchange_error,Toast.LENGTH_SHORT).show();
                    return;
                }

                txtExchangeRate.setText(df.format(exchangeRate[0]));
            }
        };

        mActivity.setExchangeTask(exchangeTask);
        mActivity.runExchangeTask(selectedDate);

        if(ir.getCurrentAccount() < 0) {
            Toast.makeText(mActivity.getApplicationContext(),R.string.toast_no_account,Toast.LENGTH_SHORT).show();
            return;
        }

        lists_currency.add("USD");
        lists_currency.add("CNY");
        lists_currency.add("EUR");
        lists_currency.add("JPY");
        lists_currency.add("HKD");

        txtInputExchangeRate.setEnabled(false);
        txtOutputExchangeRate.setEnabled(false);
        txtPrincipal.setEnabled(false);
        txtExchangeRate.setEnabled(false);

        adapter_currency = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item, lists_currency);
        spinnerCurrency.setAdapter(adapter_currency);

        spinnerCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                current_currency[0] = position;

                mActivity.runExchangeTask(selectedDate);

                int input_krw, output_krw;
                double input_foreign, output_foreign, evaluation;
                double exchangeRate_input = 0f, exchangeRate_output = 0f;

                int id_account = ir.getCurrentAccount();
                InfoIOForeign io_foreign = ir.getInfoIOForeign(id_account,current_currency[0],selectedDate);
                InfoIOForeign io_foreign_latest = ir.getLastInfoIOForeign(id_account,current_currency[0],selectedDate);
                InfoDairyForeign dairy_foreign_latest = ir.getLastInfoDairyForeign(id_account, current_currency[0], selectedDate);

                if(io_foreign != null) evaluation = io_foreign.getEvaluation();
                else if(io_foreign_latest != null) evaluation = io_foreign_latest.getEvaluation();
                else evaluation = 0;

                txtDate.setText(selectedDate);

                //선택한 날짜의 입력값이 없으면 평가액만 최근값으로 반영한다.
                if(io_foreign == null) {
                    exist = false;
                    txtInput.setText("0");
                    txtOutput.setText("0");
                    txtInputExchangeRate.setText("0");
                    txtOutputExchangeRate.setText("0");
                    txtInputKRW.setText("0");
                    txtOutputKRW.setText("0");
                    txtEvaluation.setText(String.valueOf(evaluation));
                }
                else {
                    exist = true;

                    input_foreign = io_foreign.getInput();
                    output_foreign = io_foreign.getOutput();
                    input_krw = io_foreign.getInput_krw();
                    output_krw = io_foreign.getOutput_krw();

                    txtInput.setText(df.format(input_foreign));
                    txtOutput.setText(df.format(output_foreign));
                    txtInputKRW.setText(df.format(input_krw));
                    txtOutputKRW.setText(df.format(output_krw));
                    txtEvaluation.setText(df.format(evaluation));

                    if(input_foreign != 0) exchangeRate_input = input_krw / input_foreign;
                    if(output_foreign != 0) exchangeRate_output = output_krw / output_foreign;
                    txtInputExchangeRate.setText(df.format(exchangeRate_input));
                    txtOutputExchangeRate.setText(df.format(exchangeRate_output));
                }

                if(dairy_foreign_latest == null) {
                    txtPrincipal.setText("0");
                }
                else {
                    txtPrincipal.setText(df.format(dairy_foreign_latest.getPrincipal()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        int id_account = ir.getCurrentAccount();
        final InfoIOForeign io_foreign = ir.getInfoIOForeign(id_account,current_currency[0],selectedDate);
        InfoDairyForeign dairy_foreign_latest = ir.getLastInfoDairyForeign(id_account, current_currency[0], selectedDate);

        //선택한 날짜의 값이 있으면 가져오고 없으면 최근값을 가져온다.
        if(io_foreign != null) evaluation = io_foreign.getEvaluation();
        else if(dairy_foreign_latest != null) {
            if(exchangeRate[0] != null){
                evaluation = (int) (dairy_foreign_latest.getPrincipal() * exchangeRate[0]);
            }
            else {
                InfoIOForeign io_foreign_latest = ir.getLastInfoIOForeign(id_account, current_currency[0],selectedDate);
                if(io_foreign_latest != null) {
                    evaluation = io_foreign_latest.getEvaluation();
                }
                else {
                    evaluation = 0;
                }
            }
        }
        else evaluation = 0;

        txtDate.setText(selectedDate);

        //선택한 날짜의 입력값이 없으면 평가액만 최근값으로 반영한다.
        if(io_foreign == null) {
            exist = false;
            txtInput.setText("0");
            txtOutput.setText("0");
            txtInputExchangeRate.setText("0");
            txtOutputExchangeRate.setText("0");
            txtInputKRW.setText("0");
            txtOutputKRW.setText("0");
            spinnerCurrency.setSelection(0);
            txtEvaluation.setText(String.valueOf(evaluation));
        }
        else {
            exist = true;

            input_foreign = io_foreign.getInput();
            output_foreign = io_foreign.getOutput();
            input_krw = io_foreign.getInput_krw();
            output_krw = io_foreign.getOutput_krw();

            txtInput.setText(df.format(input_foreign));
            txtOutput.setText(df.format(output_foreign));
            txtInputKRW.setText(df.format(input_krw));
            txtOutputKRW.setText(df.format(output_krw));
            spinnerCurrency.setSelection(io_foreign.getCurrency());
            txtEvaluation.setText(df.format(evaluation));

            if(input_foreign != 0) exchangeRate_input = input_krw / input_foreign;
            if(output_foreign != 0)exchangeRate_output = output_krw / output_foreign;
            txtInputExchangeRate.setText(df.format(exchangeRate_input));
            txtOutputExchangeRate.setText(df.format(exchangeRate_output));
        }

        if(dairy_foreign_latest == null) {
            txtPrincipal.setText("0");
        }
        else {
            txtPrincipal.setText(df.format(dairy_foreign_latest.getPrincipal()));
        }

        builder.setView(view).setTitle(getString(R.string.input_inout_foreign));
        builder.setPositiveButton(getString(R.string.regist), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String str_in, str_out, str_in_krw, str_out_krw, str_eval;

                DecimalFormat df = new DecimalFormat("#,###.##");

                str_in = txtInput.getText().toString();
                str_out = txtOutput.getText().toString();
                str_eval = txtEvaluation.getText().toString();
                str_in_krw = txtInputKRW.getText().toString();
                str_out_krw = txtOutputKRW.getText().toString();

                if (str_in.isEmpty()) str_in = "0";
                if (str_out.isEmpty()) str_out = "0";
                if (str_in_krw.isEmpty()) str_in_krw = "0";
                if (str_out_krw.isEmpty()) str_out_krw = "0";

                InfoIOForeign io_foreign = new InfoIOForeign();

                if (exist) {
                    io_foreign = ir.getInfoIOForeign(ir.getCurrentAccount(), current_currency[0], selectedDate);
                    if (io_foreign == null) {
                        Log.i(TAG, "No InfoIO Foreign");
                        return;
                    }
                }

                double in = 0f, out = 0f;
                int in_krw = 0, out_krw = 0, evaluation = 0;

                try {
                    in = df.parse(str_in).floatValue();
                    out = df.parse(str_out).floatValue();
                    in_krw = df.parse(str_in_krw).intValue();
                    out_krw = df.parse(str_out_krw).intValue();
                    if (str_eval.isEmpty()) {
                        evaluation = (int) ((in - out) * exchangeRate[0]);
                    } else {
                        evaluation = df.parse(str_eval).intValue();
                    }
                } catch (ParseException e) {
                    Log.e(TAG, "Data Format Parse Error");
                }

                io_foreign.setDate(selectedDate);
                io_foreign.setInput(in);
                io_foreign.setInput_krw(in_krw);
                io_foreign.setOutput(out);
                io_foreign.setOutput_krw(out_krw);
                io_foreign.setEvaluation(evaluation);
                io_foreign.setAccount(ir.getCurrentAccount());
                io_foreign.setCurrency(current_currency[0]);

                if (!exist) ir.insertInfoIOForeign(io_foreign);
                else ir.updateInfoIOForeign(io_foreign);

                mActivity.fragmentSub1.callUpdateFrag1();
                mActivity.fragmentSub3.callUpdateFrag6();
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
    }
    @SuppressLint("InflateParams")
    private void dialogDate() {
        final String[] date = new String[1];

        view = inflater.inflate(R.layout.dialog_date, null);

        DatePicker datePicker = view.findViewById(R.id.datePicker_dlg_date);

        datePicker.init(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar select = Calendar.getInstance();
                select.set(year, monthOfYear, dayOfMonth);

                if (Calendar.getInstance().before(select)) {
                    Toast.makeText(mActivity.getApplicationContext(), R.string.toast_date_error, Toast.LENGTH_SHORT).show();
                    return;
                }
                date[0] = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, monthOfYear + 1, dayOfMonth);

            }
        });
        builder.setView(view).setTitle(getString(R.string.select_date));
        builder.setPositiveButton(getString(R.string.select),new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.onWorkComplete(date[0]);
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
    }
    @SuppressLint("InflateParams")
    private void dialogTransfer() {
        view = inflater.inflate(R.layout.dialog_transfer, null);

        groups = ir.getGroups();
        if(!groups.isEmpty()) updateGroupList();

        Spinner spinnerGroupFrom = view.findViewById(R.id.spinner_dlg_transfer_from_group);
        Spinner spinnerGroupTo = view.findViewById(R.id.spinner_dlg_transfer_to_group);
        Spinner spinnerAccountFrom = view.findViewById(R.id.spinner_dlg_transfer_from_account);
        Spinner spinnerAccountTo = view.findViewById(R.id.spinner_dlg_transfer_to_account);
        final EditText editText_amount = view.findViewById(R.id.editText_dlg_transfer_amount);

        if(groups.size() > 0) {
            selectedGroupIdFrom = ir.getCurrentGroup();
            selectedGroupIdTo = ir.getCurrentGroup();
        }

        @SuppressWarnings("ConstantConditions")
        ArrayAdapter<String> adapter_group = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, lists_group);

        // 출금계좌그룹 선택
        spinnerGroupFrom.setAdapter(adapter_group);
        spinnerGroupFrom.setSelection(findGroup(selectedGroupIdFrom));
        spinnerGroupFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedGroupIdFrom = groups.get(position).getId();
                updateAccountList(selectedGroupIdFrom);
                accounts_from = accounts;
                adapter_account_from.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // 입금계좌그룹 선택
        spinnerGroupTo.setAdapter(adapter_group);
        spinnerGroupTo.setSelection(findGroup(selectedGroupIdTo));
        spinnerGroupTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedGroupIdTo = groups.get(position).getId();
                updateAccountList(selectedGroupIdTo);
                accounts_to = accounts;
                adapter_account_to.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // 출금계좌 선택
        updateAccountList(selectedGroupIdFrom);
        accounts_from = accounts;
        //noinspection ConstantConditions
        adapter_account_from = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item,lists_account);
        selectedAccountIdFrom = ir.getCurrentAccount();
        spinnerAccountFrom.setAdapter(adapter_account_from);
        spinnerAccountFrom.setSelection(findAccount(selectedAccountIdFrom));
        spinnerAccountFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedAccountIdFrom = accounts_from.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // 입금계좌 선택
        updateAccountList(selectedGroupIdTo);
        accounts_to = accounts;
        //noinspection ConstantConditions
        adapter_account_to = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item,lists_account);
        if(accounts.size() > 0) selectedAccountIdTo = accounts.get(0).getId();
        spinnerAccountTo.setAdapter(adapter_account_to);
        spinnerAccountTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedAccountIdTo = accounts_to.get(position).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        builder.setView(view).setTitle(getString(R.string.transfer));
        builder.setPositiveButton(getString(R.string.execute),new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int amount = Integer.parseInt(editText_amount.getText().toString());

                if(amount <= 0) {
                    Toast.makeText(getContext(),R.string.toast_no_amount,Toast.LENGTH_SHORT).show();
                    return;
                }

                // 출금계좌
                InfoIOKRW io = ir.getInfoIOKRW(selectedAccountIdFrom,selectedDate);
                InfoIOKRW io_latest = ir.getLastInfoIOKRW(selectedAccountIdFrom,selectedDate);

                int evaluation = 0;

                // io_latest가 없으면 0, 있으면 반영
                if(io_latest != null) evaluation = io_latest.getEvaluation();
                // evaluation에 해당일 input, output값 반영
                if(io != null) evaluation = evaluation - io.getOutput() + io.getInput();
                // evaluation에 해당일 spendcash, spendcard, income 반영
                evaluation = evaluation - ir.getSpendsCashSum(selectedDate,selectedAccountIdFrom) - ir.getSpendsCardSum(selectedDate,selectedAccountIdFrom) + ir.getIncomeSum(selectedDate,selectedAccountIdFrom);
                // 반영된 평가액에 출금액 반영
                evaluation = evaluation - amount;

                //noinspection ConstantConditions
                if(io != null) {
                    io.setOutput(io.getOutput() + amount);
                    io.setEvaluation(evaluation);
                    ir.updateInfoIOKRW(io);
                }
                else ir.insertInfoIOKRW(selectedAccountIdFrom, selectedDate,0,amount,0,0,0,evaluation);

                // 입금계좌
                io = ir.getInfoIOKRW(selectedAccountIdTo,selectedDate);
                io_latest = ir.getLastInfoIOKRW(selectedAccountIdTo,selectedDate);

                // io_latest가 없으면 0
                if(io_latest != null) evaluation = io_latest.getEvaluation();
                // evaluation에 해당일 input, output값 반영
                if(io != null) evaluation = evaluation - io.getOutput() + io.getInput();
                // evaluation에 해당일 spendcash, spendcard, income 반영
                evaluation = evaluation - ir.getSpendsCashSum(selectedDate,selectedAccountIdFrom) - ir.getSpendsCardSum(selectedDate,selectedAccountIdFrom) + ir.getIncomeSum(selectedDate,selectedAccountIdFrom);
                evaluation = evaluation + amount;

                //noinspection ConstantConditions
                if(io != null) {
                    io.setInput(io.getInput() + amount);
                    io.setEvaluation(evaluation);
                    ir.updateInfoIOKRW(io);
                }
                else ir.insertInfoIOKRW(selectedAccountIdTo,selectedDate,amount,0,0,0,0,evaluation);

                mActivity.fragmentSub1.callUpdateFrag1();
                mActivity.fragmentSub3.callUpdateFrag6();
                listener.onWorkComplete("");
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
    }

    @SuppressLint("InflateParams")
    private void dialogGroupAdd() {
        view = inflater.inflate(R.layout.dialog_group_add, null);

        final EditText edit = view.findViewById(R.id.editText_dlg_group_add);

        builder.setView(view).setTitle(getString(R.string.group_regist));
        builder.setPositiveButton(getString(R.string.execute), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = edit.getText().toString();
                if(!name.isEmpty()) ir.insertGroup(name);
                listener.onWorkComplete(null);
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }
    @SuppressLint("InflateParams")
    private void dialogGroupEdit() {
        view = inflater.inflate(R.layout.dialog_group_edit, null);

        final EditText edit = view.findViewById(R.id.editText_dlg_group_edit);

        ListView list = view.findViewById(R.id.listView_dlg_editGroup);

        groups = ir.getGroups();
        if(!groups.isEmpty()) updateGroupList();

        //noinspection ConstantConditions
        adapter1 = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_single_choice, lists_group);

        list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        list.setAdapter(adapter1);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String select = groups.get(position).getName();

                ir.setCurrentGroup(groups.get(position).getId());
                edit.setText(select);
                listener.onWorkComplete(null);
            }
        });

        builder.setView(view).setTitle(getString(R.string.group_edit));
        builder.setPositiveButton(getString(R.string.execute), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = edit.getText().toString();
                Group group = ir.getGroup(ir.getCurrentGroup());

                if(group == null) {
                    Log.i(TAG, "No group");
                    return;
                }

                if(!name.isEmpty()) {
                    group.setName(name);
                    ir.updateGroup(group);
                }
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }
    @SuppressLint("InflateParams")
    private void dialogGroupDel() {
        final String[] select = new String[1];

        view = inflater.inflate(R.layout.dialog_listview, null);

        ListView list = view.findViewById(R.id.listView_dlg_listView);

        groups = ir.getGroups();
        if(!groups.isEmpty()) updateGroupList();

        //noinspection ConstantConditions
        adapter1 = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_single_choice, lists_group);

        list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        list.setAdapter(adapter1);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                select[0] = groups.get(position).getName();
                ir.setCurrentGroup(groups.get(position).getId());
                listener.onWorkComplete(null);
            }
        });

        builder.setView(view).setTitle(getString(R.string.group_delete));
        builder.setPositiveButton(getString(R.string.execute), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ir.deleteGroup("name",new String[] {select[0]});
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }

    @SuppressLint("InflateParams")
    private void dialogCategoryAdd() {
        view = inflater.inflate(R.layout.dialog_category_add, null);

        final RadioGroup radioGroup1 = view.findViewById(R.id.radio_dlg_category_add_1);
        final RadioGroup radioGroup2 = view.findViewById(R.id.radio_dlg_category_add_2);
        final Spinner spinner = view.findViewById(R.id.spinner_dlg_category_add_main);
        final EditText editText = view.findViewById(R.id.editText_dlg_category_add_name);

        Button button = view.findViewById(R.id.button_dlg_category_add);

        updateCategoryMainList(KIND_SPEND);

        //noinspection ConstantConditions
        adapter_category_main = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item, lists_category_main);
        spinner.setAdapter(adapter_category_main);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedMainId = categoryMains.get(i).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.radiobtn_dlg_category_spend) {
                    updateCategoryMainList(KIND_SPEND);
                }
                else {
                    updateCategoryMainList(KIND_INCOME);
                }
                selectedMainId = categoryMains.get(0).getId();
                adapter_category_main.notifyDataSetChanged();
            }
        });
        if(radioGroup2.getCheckedRadioButtonId() == R.id.radiobtn_dlg_category_main) {
            spinner.setEnabled(false);
        }
        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i == R.id.radiobtn_dlg_category_main) {
                    spinner.setEnabled(false);
                }
                else {
                    spinner.setEnabled(true);
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name =editText.getText().toString();
                String kind = null;

                if(!name.isEmpty()) {
                    if (radioGroup2.getCheckedRadioButtonId() == R.id.radiobtn_dlg_category_main) {
                        switch (radioGroup1.getCheckedRadioButtonId()) {
                            case R.id.radiobtn_dlg_category_spend:
                                kind = "spend";
                                break;
                            case R.id.radiobtn_dlg_category_income:
                                kind = "income";
                                break;
                        }
                        ir.insertCategoryMain(name, kind);
                        updateCategoryMainList(KIND_ALL);
                        adapter_category_main.notifyDataSetChanged();
                    } else {
                        ir.insertCategorySub(name, selectedMainId);
                    }
                    Toast.makeText(getContext(),getString(R.string.category_add),Toast.LENGTH_SHORT).show();
                    editText.setText("");
               }
            }
        });
        builder.setView(view).setTitle(getString(R.string.category_add));
        builder.setPositiveButton(getString(R.string.finish),new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
    }
    @SuppressLint("InflateParams")
    private void dialogCategoryEdit() {
        CategoryMain categoryMain;
        CategorySub categorySub;

        view = inflater.inflate(R.layout.dialog_category_edit, null);

        final EditText editText_main = view.findViewById(R.id.editText_dlg_category_edit_main);
        final EditText editText_sub = view.findViewById(R.id.editText_dlg_category_edit_sub);

        Spinner spinner_main = view.findViewById(R.id.spinner_dlg_category_edit_main);
        Spinner spinner_sub = view.findViewById(R.id.spinner_dlg_category_edit_sub);

        Button button = view.findViewById(R.id.button_dlg_category_edit);

        updateCategoryMainList(KIND_ALL);
        if(categoryMains.size() > 0) {
            selectedMainId = categoryMains.get(0).getId();
            categoryMain = ir.getCategoryMain(selectedMainId);
            if(categoryMain != null) editText_main.setText(categoryMain.getName());
        }

        updateCategorySubList();
        if (categorySubs.size() > 0) {
            selectedSubId = categorySubs.get(0).getId();
            categorySub = ir.getCategorySub(selectedSubId);
            if(categorySub != null) editText_sub.setText(categorySub.getName());
        }

        //noinspection ConstantConditions
        adapter_category_main = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item, lists_category_main);
        //noinspection ConstantConditions
        adapter_category_sub = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item, lists_category_sub);
        spinner_main.setAdapter(adapter_category_main);
        spinner_sub.setAdapter(adapter_category_sub);

        spinner_main.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                CategoryMain categoryMain;
                CategorySub categorySub;

                if(categoryMains.size() > 0) {
                    selectedMainId = categoryMains.get(i).getId();
                    categoryMain = ir.getCategoryMain(selectedMainId);
                    if(categoryMain != null) editText_main.setText(categoryMain.getName());
                    updateCategorySubList();
                    adapter_category_sub.notifyDataSetChanged();
                    if(categorySubs.size() > 0) selectedSubId = categorySubs.get(0).getId();
                    categorySub = ir.getCategorySub(selectedSubId);
                    if(categorySub != null) editText_sub.setText(categorySub.getName());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner_sub.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                CategorySub categorySub;

                if (categorySubs.size() > 0) {
                    selectedSubId = categorySubs.get(i).getId();
                    categorySub = ir.getCategorySub(selectedSubId);
                    if(categorySub != null) editText_sub.setText(categorySub.getName());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name_main = editText_main.getText().toString();
                String name_sub = editText_sub.getText().toString();
                ir.updateCategoryMain(selectedMainId,name_main);
                ir.updateCategorySub(selectedSubId,name_sub,selectedMainId);

                updateCategoryMainList(KIND_ALL);
                updateCategorySubList();
                adapter_category_main.notifyDataSetChanged();
                adapter_category_sub.notifyDataSetChanged();
            }
        });
        builder.setView(view).setTitle(getString(R.string.category_edit));
        builder.setPositiveButton(getString(R.string.finish),new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
    }
    @SuppressLint("InflateParams")
    private void dialogCategoryDel() {
        view = inflater.inflate(R.layout.dialog_category_del, null);

        final Spinner spinner_main = view.findViewById(R.id.spinner_dlg_category_del_main);
        final Spinner spinner_sub = view.findViewById(R.id.spinner_dlg_category_del_sub);

        Button button_main = view.findViewById(R.id.button_dlg_category_del_main);
        Button button_sub = view.findViewById(R.id.button_dlg_category_del_sub);

        updateCategoryMainList(KIND_ALL);
        if(categoryMains.size() > 0) {
            selectedMainId = categoryMains.get(0).getId();
        }

        updateCategorySubList();
        if (categorySubs.size() > 0) {
            selectedSubId = categorySubs.get(0).getId();
        }

        //noinspection ConstantConditions
        adapter_category_main = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item, lists_category_main);
        //noinspection ConstantConditions
        adapter_category_sub = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item, lists_category_sub);
        spinner_main.setAdapter(adapter_category_main);
        spinner_sub.setAdapter(adapter_category_sub);

        spinner_main.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedMainId = categoryMains.get(i).getId();
                updateCategorySubList();
                adapter_category_sub.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner_sub.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(categorySubs.size() > 0) selectedSubId = categorySubs.get(i).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        button_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ir.deleteCategoryMain("_id",new String[] {String.valueOf(selectedMainId)});
                updateCategoryMainList(KIND_ALL);
                adapter_category_main.notifyDataSetChanged();
                spinner_main.setSelection(0);
                updateCategorySubList();
                adapter_category_sub.notifyDataSetChanged();
                spinner_sub.setSelection(0);
            }
        });
        button_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ir.deleteCategorySub("_id",new String[] {String.valueOf(selectedSubId)});
                updateCategoryMainList(KIND_ALL);
                adapter_category_main.notifyDataSetChanged();
                spinner_main.setSelection(0);
                updateCategorySubList();
                adapter_category_sub.notifyDataSetChanged();
                spinner_sub.setSelection(0);
            }
        });

        builder.setView(view).setTitle(getString(R.string.category_del));
        builder.setPositiveButton(getString(R.string.finish),new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.onWorkComplete("");
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
    }

    @SuppressLint("InflateParams")
    private void dialogCardAdd() {
        int position;

        view = inflater.inflate(R.layout.dialog_card_add, null);

        final EditText editText_name = view.findViewById(R.id.editText_dlg_card_add_name);
        final EditText editText_num = view.findViewById(R.id.editText_dlg_card_add_num);
        final EditText editText_com = view.findViewById(R.id.editText_dlg_card_add_com);
        final EditText editText_date = view.findViewById(R.id.editText_dlg_card_add_date);
        Spinner spinner_account = view.findViewById(R.id.spinner_dlg_card_add_account);
        updateAccountList(ir.getCurrentGroup());
        if(selectedAccountId < 0) {
            selectedAccountId = accounts.get(0).getId();
            position = 0;
        }
        else {
            position = findAccount(selectedAccountId);
        }
        //noinspection ConstantConditions
        adapter_account = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item, lists_account);
        spinner_account.setAdapter(adapter_account);
        if(!adapter_account.isEmpty()) spinner_account.setSelection(position);
        spinner_account.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedAccountId = accounts.get(i).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        builder.setView(view).setTitle(getString(R.string.card_add));
        builder.setPositiveButton(getString(R.string.finish),new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String name = editText_name.getText().toString();
                String com = editText_com.getText().toString();
                String num = editText_num.getText().toString();
                String date = editText_date.getText().toString();

                if(checkCategorySubName(name)) {
                    ir.insertCard(name, num, com, Integer.parseInt(date), selectedAccountId);
                }
                else Toast.makeText(getContext(),R.string.toast_same_name,Toast.LENGTH_SHORT).show();
                listener.onWorkComplete(null);
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
    }
    @SuppressLint("InflateParams")
    private void dialogCardEdit() {
        view = inflater.inflate(R.layout.dialog_card_edit, null);

        final EditText editText_name = view.findViewById(R.id.editText_dlg_card_edit_name);
        final EditText editText_num = view.findViewById(R.id.editText_dlg_card_edit_num);
        final EditText editText_com = view.findViewById(R.id.editText_dlg_card_edit_com);
        final EditText editText_date = view.findViewById(R.id.editText_dlg_card_edit_date);

        Spinner spinner_card = view.findViewById(R.id.spinner_dlg_card_edit_name);
        Spinner spinner_account = view.findViewById(R.id.spinner_dlg_card_edit_account);

        updateCardList();

        //noinspection ConstantConditions
        adapter_card = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item, lists_card);
        spinner_card.setAdapter(adapter_card);

        spinner_card.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (cards.size() > 0) {
                    Card card = cards.get(i);
                    selectedCardId = card.getId();
                    editText_name.setText(card.getName());
                    editText_com.setText(card.getCompany());
                    editText_num.setText(card.getNumber());
                    editText_date.setText(String.valueOf(card.getDrawDate()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        int position;
        updateAccountList(ir.getCurrentGroup());
        if(selectedAccountId < 0) {
            selectedAccountId = accounts.get(0).getId();
            position = 0;
        }
        else {
            position = findAccount(selectedAccountId);
        }
        //noinspection ConstantConditions
        adapter_account = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item, lists_account);
        spinner_account.setAdapter(adapter_account);
        if(!adapter_account.isEmpty())spinner_account.setSelection(position);
        spinner_account.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedAccountId = accounts.get(i).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        builder.setView(view).setTitle(getString(R.string.card_edit));
        builder.setPositiveButton(getString(R.string.finish),new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Card card = ir.getCard(selectedCardId);

                if(card == null) {
                    Log.i(TAG,"No card");
                    return;
                }

                card.setName(editText_name.getText().toString());
                card.setNumber(editText_num.getText().toString());
                card.setCompany(editText_com.getText().toString());
                card.setDrawDate(Integer.parseInt(editText_date.getText().toString()));
                card.setAccount(selectedAccountId);

                ir.updateCard(card);
                listener.onWorkComplete(null);
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
    }
    @SuppressLint("InflateParams")
    private void dialogCardDel() {
        view = inflater.inflate(R.layout.dialog_listview, null);

        ListView list = view.findViewById(R.id.listView_dlg_listView);

        updateCardList();

        //noinspection ConstantConditions
        adapter_card = new ArrayAdapter<>(getContext(),R.layout.support_simple_spinner_dropdown_item, lists_card);
        list.setAdapter(adapter_card);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedCardId = cards.get(position).getId();
            }
        });

        builder.setView(view).setTitle(getString(R.string.card_del));
        builder.setPositiveButton(getString(R.string.finish),new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ir.deleteCard("_id", new String[] {String.valueOf(selectedCardId)});
                listener.onWorkComplete("");
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
    }

    private void updateGroupList() {
        lists_group.clear();

        if(groups.isEmpty()) return;

        for (int i = 0, n = groups.size(); i < n; i++) {
            lists_group.add(groups.get(i).getName());
        }
    }
    private void updateCategoryMainList(int kind) {
        lists_category_main.clear();

        categoryMains = ir.getCategoryMains(kind);

        if(categoryMains.isEmpty()) {
            Log.i(TAG,"No Main category");
            return;
        }

        for (int i = 0, n = categoryMains.size(); i < n; i++) {
            lists_category_main.add(categoryMains.get(i).getName());
        }
    }
    private void updateCategorySubList() {
        lists_category_sub.clear();
        categorySubs = ir.getCategorySubs(selectedMainId);

        if(categorySubs.isEmpty()) {
            Log.i(TAG,"No Sub category");
            return;
        }

        for (int i = 0, n = categorySubs.size(); i < n; i++) {
            lists_category_sub.add(categorySubs.get(i).getName());
        }
    }
    private void updateAccountList(int id_group) {
        String str;
        Account account;

        lists_account.clear();
        accounts = ir.getAccounts(id_group);
        if(accounts.isEmpty()) {
            Log.i(TAG, "No account");
            return;
        }

        for (int i = 0, n = accounts.size(); i < n; i++) {
            account = accounts.get(i);
            str = account.getNumber() + " " + account.getInstitute() + " " + account.getDescription();
            lists_account.add(str);
        }
    }
    private void updateCardList() {
        lists_card.clear();
        cards = ir.getCards();
        if(cards.isEmpty()) {
            Log.i(TAG,"No card");
            return;
        }

        for (int i = 0, n = cards.size(); i < n; i++) {
            lists_card.add(cards.get(i).getName());
        }
    }

    private int findGroup(int id) {
        int result = -1;

        if(groups.isEmpty()) return -1;

        for (int i = 0, n = groups.size(); i < n; i++) {
            if(groups.get(i).getId() == id) result = i;
        }
        return result;
    }
    private int findCategoryMain(int id) {
        int result = -1;

        if(categoryMains.isEmpty()) return -1;

        for (int i = 0, n = categoryMains.size(); i < n; i++) {
            if(categoryMains.get(i).getId() == id) result = i;
        }
        return result;
    }
    private int findCategorySub(int id) {
        int result = -1;

        if(categorySubs.isEmpty()) return -1;

        for (int i = 0, n = categorySubs.size(); i < n; i++) {
            if(categorySubs.get(i).getId() == id) result = i;
        }
        return result;
    }
    private int findAccount(int id) {
        int result = -1;

        if(accounts.isEmpty()) return -1;

        for (int i = 0, n = accounts.size(); i < n; i++) {
            if(accounts.get(i).getId() == id) result = i;
        }
        return result;
    }
    private int findCard(int id) {
        int result = -1;

        if(cards.isEmpty()) return -1;

        for (int i = 0, n = cards.size(); i < n; i++) {
            if(cards.get(i).getId() == id) result = i;
        }
        return result;
    }

    private boolean checkCategorySubName(String name) {
        CategorySub categorySub;

        for (int i = 0, n = categorySubs.size(); i < n; i++) {
            categorySub = categorySubs.get(i);
            if(categorySub.getCategoryMain() == selectedMainId) {
                if (categorySub.getName().compareTo(name) == 0) return false;
            }
        }

        return true;
    }
}
