package com.example.watering.investrecord;

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by watering on 17. 11. 2.
 */

@SuppressWarnings("ALL")
public class UserDialogFragment extends DialogFragment {
    private MainActivity mActivity;
    private IRResolver ir;
    private LayoutInflater inflater;
    private AlertDialog.Builder builder;
    private View view;

    private ArrayAdapter<String> adapter1, adapter2, adapter3, adapter4;
    private List<String> lists1 = new ArrayList<>();
    private List<String> lists2 = new ArrayList<>();
    private List<String> lists3 = new ArrayList<>();
    private List<String> lists4 = new ArrayList<>();
    private List<Group> groups = new ArrayList<>();
    private List<Account> accounts = new ArrayList<>();
    private List<CategoryMain> categoryMains = new ArrayList<>();
    private List<CategorySub> categorySubs = new ArrayList<>();
    private List<Card> cards = new ArrayList<>();

    private String selectedDate;
    private int type_dialog, type_spend, i_u, schedule = 0;
    private int selectedMainId, selectedSubId, selectedCardId, selectedAccountId;
    private int id_income, id_spend, id_spend_schedule, id_spend_cash, id_spend_card;
    private String selectedId = "",selectedCode = "";
    private UserListener listener;

    public interface UserListener {
        void onWorkComplete(String str);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mActivity = (MainActivity) getActivity();
        ir = mActivity.ir;

        builder = new AlertDialog.Builder(getActivity());
        inflater = mActivity.getLayoutInflater();

        switch (type_dialog) {
            case 0:
                dialogInout();
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
            case R.id.navigation_item_setting:
                dialogSetting();
                break;
            case R.id.editText_frag5_date:
            case R.id.editText_frag6_date:
            case R.id.editText_dlg_spend_date:
                dialogDate();
                break;
            case R.id.floating_frag5:
                dialogSpend();
                break;
            case R.id.floating_frag6:
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

    public void initData(List<Group> lists) {
        this.groups = lists;
        for(int i=0; i<groups.size(); i++) {
            this.lists1.add(groups.get(i).getName());
        }
    }
    public void initId(String id) {
        this.selectedId = id;
    }
    public void setSelectedDate(String selectedDate) {
        this.selectedDate = selectedDate;
    }

    private void dialogSpend() {
        view = inflater.inflate(R.layout.dialog_spend, null);

        final EditText editText_date = view.findViewById(R.id.editText_dlg_spend_date);
        final EditText editText_details = view.findViewById(R.id.editText_dlg_spend_details);
        final EditText editText_amount = view.findViewById(R.id.editText_dlg_spend_amount);
        Spinner spinner_category_main = view.findViewById(R.id.spinner_dlg_spend_category_main);
        Spinner spinner_category_sub = view.findViewById(R.id.spinner_dlg_spend_category_sub);
        Spinner spinner_approval_1 = view.findViewById(R.id.spinner_dlg_spend_approval_1);
        final Spinner spinner_approval_2 = view.findViewById(R.id.spinner_dlg_spend_approval_2);
        final CheckBox checkBox = view.findViewById(R.id.checkbox_dlg_spend_schedule);
        final EditText editText_date_schedule = view.findViewById(R.id.editText_dlg_spend_date_schedule);
        int position = 0;
        Spend spend = new Spend();

        if(selectedId.isEmpty()) editText_date.setText(selectedDate);
        else {
            spend = ir.getSpend(selectedId);

            id_spend = spend.getId();
            selectedCode = spend.getCode();
            editText_date.setText(spend.getDate());
            editText_amount.setText(String.valueOf(spend.getAmount()));
            editText_details.setText(spend.getDetails());
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
                dialog.show(getFragmentManager(), "dialog");
            }
        });

        updateCategoryMainList(1);
        if(categoryMains.size() > 0) {
            if(selectedId.isEmpty()) {
                selectedMainId = categoryMains.get(0).getId();
                position = 0;
            }
            else {
                selectedMainId = ir.getCategorySub(String.valueOf(spend.getCategory())).getCategoryMain();
                position = findCategoryMain(selectedMainId);
            }
        }
        adapter1 = new ArrayAdapter<>(getActivity(),R.layout.support_simple_spinner_dropdown_item, lists1);
        spinner_category_main.setAdapter(adapter1);
        spinner_category_main.setSelection(position);
        spinner_category_main.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedMainId = categoryMains.get(i).getId();
                updateCategorySubList();
                adapter2.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        updateCategorySubList();
        if (categorySubs.size() > 0) {
            if(selectedId.isEmpty()) {
                selectedSubId = categorySubs.get(0).getId();
                position = 0;
            }
            else {
                selectedSubId = spend.getCategory();
                position = findCategorySub(selectedSubId);
            }
        }
        adapter2 = new ArrayAdapter<>(getActivity(),R.layout.support_simple_spinner_dropdown_item, lists2);
        spinner_category_sub.setAdapter(adapter2);
        spinner_category_sub.setSelection(position);
        spinner_category_sub.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedSubId = categorySubs.get(i).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        List<String> lists_approval = new ArrayList<>();
        lists_approval.clear();
        lists_approval.add("현금");
        lists_approval.add("카드");

        if(selectedId.isEmpty()) {
            position = 0;
            type_spend = 1;
        }
        else {
            if(selectedCode.charAt(0) == '1') {
                position = 0;
                type_spend = 1;
            }
            else {
                position = 1;
                type_spend = 2;
            }
        }

        ArrayAdapter<String> adapter_approval = new ArrayAdapter<>(getActivity(),R.layout.support_simple_spinner_dropdown_item,lists_approval);
        spinner_approval_1.setAdapter(adapter_approval);
        spinner_approval_1.setSelection(position);
        spinner_approval_1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                type_spend = i+1;
                switch(type_spend) {
                    case 1:
                        spinner_approval_2.setAdapter(adapter3);
                        break;
                    case 2:
                        spinner_approval_2.setAdapter(adapter4);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                type_spend = 0;
            }
        });

        if(selectedId.isEmpty()) {
            checkBox.setChecked(false);
            schedule = 0;
        }
        else {
            if(selectedCode.charAt(1) == '0') {
                checkBox.setChecked(false);
                schedule = 0;
            }
            else {
                checkBox.setChecked(true);
                schedule = 1;
            }
        }

        editText_date_schedule.setEnabled(false);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                editText_date_schedule.setEnabled(b);
                if(b) schedule = 1;
                else schedule = 0;
            }
        });

        updateAccountList();
        if(type_spend == 1) {
            if(selectedId.isEmpty()) {
                selectedAccountId = accounts.get(0).getId();
                position = 0;
            }
            else {
                selectedAccountId = ir.getSpendCash(selectedCode).getAccount();
                position = findAccount(selectedAccountId);
                if(schedule == 1) id_spend_schedule = ir.getSpendSchedule(selectedCode).getId();
                else if(schedule == 0) id_spend_cash = ir.getSpendCash(selectedCode).getId();
            }

        }
        adapter3 = new ArrayAdapter<String>(getActivity(),R.layout.support_simple_spinner_dropdown_item,lists3);

        updateCardList();
        if(type_spend == 2) {
            if (selectedId.isEmpty()) {
                selectedCardId = cards.get(0).getId();
                position = 0;
            } else {
                selectedCardId = ir.getSpendCard(selectedCode).getCard();
                position = findCard(selectedCardId);
                if(schedule == 1) id_spend_schedule = ir.getSpendSchedule(selectedCode).getId();
                else if(schedule == 0) id_spend_card = ir.getSpendCard(selectedCode).getId();
            }
        }
        adapter4 = new ArrayAdapter<String>(getActivity(),R.layout.support_simple_spinner_dropdown_item,lists4);

        switch(type_spend) {
            case 1:
                spinner_approval_2.setAdapter(adapter3);
                break;
            case 2:
                spinner_approval_2.setAdapter(adapter4);
                break;
        }
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

        builder.setView(view).setTitle("지출 입력");
        builder.setPositiveButton("완료",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String details = editText_details.getText().toString();
                int amount = Integer.parseInt(editText_amount.getText().toString());
                List<Spend> spends = ir.getSpends(selectedDate);
                Calendar today = Calendar.getInstance();
                String newCode = String.format(Locale.getDefault(),"%d%d%04d%02d%02d%02d",type_spend,schedule,today.get(Calendar.YEAR),today.get(Calendar.MONTH)+1,today.get(Calendar.DAY_OF_MONTH),spends.size());

                if(selectedId.isEmpty()) ir.insertSpend(newCode, details, selectedDate, amount, selectedSubId);
                else ir.updateSpend(id_spend, newCode, details, selectedDate, amount, selectedSubId);
                if(checkBox.isChecked()) {
                    String date = editText_date_schedule.getText().toString();
                    switch (type_spend) {
                        case 1:
                            if(selectedId.isEmpty()) ir.insertSpendSchedule(newCode, date, selectedAccountId, -1);
                            else ir.updateSpendSchedule(id_spend_schedule, newCode, date, selectedAccountId,-1);
                            break;
                        case 2:
                            if(selectedId.isEmpty()) ir.insertSpendSchedule(newCode, date, -1, selectedCardId);
                            else ir.updateSpendSchedule(id_spend_schedule, newCode, date,-1,selectedCardId);
                            break;
                    }
                }
                else {
                    switch(type_spend) {
                        case 1:
                            if(selectedId.isEmpty()) ir.insertSpendCash(newCode, selectedAccountId);
                            else ir.updateSpendCash(id_spend_cash, newCode, selectedAccountId);
                            break;
                        case 2:
                            if(selectedId.isEmpty()) ir.insertSpendCard(newCode, selectedCardId);
                            else ir.updateSpendCard(id_spend_card, newCode, selectedCardId);
                            break;
                    }
                }
                listener.onWorkComplete(null);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
    }
    private void dialogIncome() {
        view = inflater.inflate(R.layout.dialog_income, null);

        final EditText editText_date = view.findViewById(R.id.editText_dlg_income_date);
        final EditText editText_details = view.findViewById(R.id.editText_dlg_income_details);
        final EditText editText_amount = view.findViewById(R.id.editText_dlg_income_amount);
        Spinner spinner_category_main = view.findViewById(R.id.spinner_dlg_income_category_main);
        Spinner spinner_category_sub = view.findViewById(R.id.spinner_dlg_income_category_sub);
        Spinner spinner_account = view.findViewById(R.id.spinner_dlg_income_account);
        int position = 0;
        Income income = new Income();

        if(selectedId.isEmpty()) editText_date.setText(selectedDate);
        else {
            income = ir.getIncome(selectedId);

            id_income = income.getId();
            editText_date.setText(income.getDate());
            editText_amount.setText(String.valueOf(income.getAmount()));
            editText_details.setText(income.getDetails());
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
                dialog.show(getFragmentManager(), "dialog");
            }
        });

        updateCategoryMainList(2);
        if(categoryMains.size() > 0) {
            if (selectedId.isEmpty()) {
                selectedMainId = categoryMains.get(0).getId();
                position = 0;
            } else {
                selectedMainId = ir.getCategorySub(String.valueOf(income.getCategory())).getCategoryMain();
                position = findCategoryMain(selectedMainId);
            }
        }
        adapter1 = new ArrayAdapter<>(getActivity(),R.layout.support_simple_spinner_dropdown_item, lists1);
        spinner_category_main.setAdapter(adapter1);
        spinner_category_main.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedMainId = categoryMains.get(i).getId();
                updateCategorySubList();
                adapter2.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        updateCategorySubList();
        if (categorySubs.size() > 0) {
            if(selectedId.isEmpty()) {
                selectedSubId = categorySubs.get(0).getId();
                position = 0;
            }
            else {
                selectedSubId = income.getCategory();
                position = findCategorySub(selectedSubId);
            }
        }
        adapter2 = new ArrayAdapter<>(getActivity(),R.layout.support_simple_spinner_dropdown_item, lists2);
        spinner_category_sub.setAdapter(adapter2);
        spinner_category_sub.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedSubId = categorySubs.get(i).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        updateAccountList();
        if(selectedId.isEmpty()) {
            selectedAccountId = accounts.get(0).getId();
            position = 0;
        }
        else {
            selectedAccountId = ir.getIncome(selectedId).getAccount();
            position = findAccount(selectedAccountId);
        }
        adapter3 = new ArrayAdapter<String>(getActivity(),R.layout.support_simple_spinner_dropdown_item,lists3);
        spinner_account.setAdapter(adapter3);
        spinner_account.setSelection(position);
        spinner_account.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedAccountId = accounts.get(i).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        builder.setView(view).setTitle("수입 입력");
        builder.setPositiveButton("완료",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String details = editText_details.getText().toString();
                int amount = Integer.parseInt(editText_amount.getText().toString());

                if(selectedId.isEmpty()) ir.insertIncome(details,selectedDate,selectedAccountId,selectedSubId,amount);
                else ir.updateIncome(id_income,details,selectedDate,selectedAccountId,selectedSubId,amount);
                listener.onWorkComplete(null);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

    }

    private void dialogSetting() {
        view = inflater.inflate(R.layout.dialog_setting, null);

        Button btn_delete = view.findViewById(R.id.button_dlg_setting_delete_all);
        Button btn_delete_file = view.findViewById(R.id.button_dlg_setting_delete_db);
        Button btn_backup_file = view.findViewById(R.id.button_dlg_setting_backup_db);
        Button btn_restore_file = view.findViewById(R.id.button_dlg_setting_restore_db);

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onWorkComplete("delall");
            }
        });
        btn_delete_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onWorkComplete("del");
            }
        });
        btn_backup_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onWorkComplete("backup");
            }
        });
        btn_restore_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onWorkComplete("restore");
            }
        });

        builder.setView(view).setTitle("설정");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onWorkComplete("");
            }
        });
    }
    private void dialogInout() {
        view = inflater.inflate(R.layout.dialog_inout, null);

        final EditText txtInput = view.findViewById(R.id.editText_dlg_inout_input);
        final EditText txtOutput = view.findViewById(R.id.editText_dlg_inout_output);
        final EditText txtEvaluation = view.findViewById(R.id.editText_dlg_inout_evaluation);
        final EditText txtIncome = view.findViewById(R.id.editText_dlg_inout_income);
        final EditText txtSpend = view.findViewById(R.id.editText_dlg_inout_spend);
        TextView txtDate = view.findViewById(R.id.textView_dlg_inout_date);

        txtIncome.setEnabled(false);
        txtSpend.setEnabled(false);
        if(ir.getCurrentAccount() < 0) {
            Toast.makeText(mActivity.getApplicationContext(),R.string.toast_no_account,Toast.LENGTH_SHORT).show();
            return;
        }

        Info_IO io = ir.getInfoIO(ir.getCurrentAccount(),selectedDate);

        DecimalFormat df = new DecimalFormat("#,###");

        txtDate.setText(selectedDate);

        if(io == null) {
            i_u = 0;
            txtInput.setText("0");
            txtOutput.setText("0");
            txtEvaluation.setText("0");
            txtIncome.setText("0");
            txtSpend.setText("0");
        }
        else {
            i_u = 1;
            txtInput.setText(df.format(io.getInput()));
            txtOutput.setText(df.format(io.getOutput()));
            txtEvaluation.setText(df.format(io.getEvaluation()));
            txtIncome.setText(df.format(io.getIncome()));
            txtSpend.setText(df.format(io.getSpend()));
        }

        builder.setView(view).setTitle("입출금 입력");
        builder.setPositiveButton("등록", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int input=0, output=0, evaluation=0, income=0, spend=0;
                String in, out, eval, inc, spe;

                DecimalFormat df = new DecimalFormat("#,###");

                in = txtInput.getText().toString();
                out = txtOutput.getText().toString();
                eval = txtEvaluation.getText().toString();
                inc = txtIncome.getText().toString();
                spe = txtSpend.getText().toString();

                if(in.isEmpty()) in = "0";
                if(out.isEmpty()) out = "0";
                if(eval.isEmpty()) eval = "0";
                if(inc.isEmpty()) inc = "0";
                if(spe.isEmpty()) spe = "0";

                try {
                    input = df.parse(in).intValue();
                    output = df.parse(out).intValue();
                    evaluation = df.parse(eval).intValue();
                    income = df.parse(inc).intValue();
                    spend = df.parse(spe).intValue();
                } catch (ParseException e) {

                }

                Info_IO io = ir.getInfoIO(ir.getCurrentAccount(),selectedDate);

                switch (i_u) {
                    case 0:
                        ir.insertInfoIO(selectedDate,input,output,income,spend,evaluation);
                        break;
                    case 1:
                        ir.updateInfoIO(io.getId(),io.getDate(),input,output,income,spend,evaluation);
                        break;
                }
                modifyInfoDiary(i_u);
            }
        });
        builder.setNegativeButton("삭제", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ir.deleteInfoIO("date",new String[] {selectedDate});
                modifyInfoDiary(2);
            }
        });
        builder.setNeutralButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }
    private void dialogDate() {
        view = inflater.inflate(R.layout.dialog_date, null);

        DatePicker datePicker = view.findViewById(R.id.datePicker_dlg_date);
        final String[] date = new String[1];

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
        builder.setView(view).setTitle("날짜 선택");
        builder.setPositiveButton("선택",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.onWorkComplete(date[0]);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
    }

    private void dialogGroupAdd() {
        view = inflater.inflate(R.layout.dialog_group_add, null);

        final EditText edit = view.findViewById(R.id.editText_dlg_group_add);

        builder.setView(view).setTitle("그룹 추가");
        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = edit.getText().toString();
                if(!name.isEmpty()) ir.insertGroup(name);
                listener.onWorkComplete(null);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }
    private void dialogGroupEdit() {
        view = inflater.inflate(R.layout.dialog_group_edit, null);

        ListView list = view.findViewById(R.id.listView_dlg_editGroup);
        final EditText edit = view.findViewById(R.id.editText_dlg_group_edit);

        adapter1 = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_single_choice, lists1);

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

        builder.setView(view).setTitle("그룹 수정");
        builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = edit.getText().toString();
                if(!name.isEmpty()) ir.updateGroup(ir.getCurrentGroup(),name);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }
    private void dialogGroupDel() {
        view = inflater.inflate(R.layout.dialog_group_del, null);

        final String[] select = new String[1];
        ListView list = view.findViewById(R.id.listView_dlg_group_del);

        adapter1 = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_single_choice, lists1);

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

        builder.setView(view).setTitle("그룹 삭제");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ir.deleteGroup("name",new String[] {select[0]});
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }

    private void dialogCategoryAdd() {
        view = inflater.inflate(R.layout.dialog_category_add, null);

        final RadioGroup radioGroup1 = view.findViewById(R.id.radio_dlg_category_add_1);
        final RadioGroup radioGroup2 = view.findViewById(R.id.radio_dlg_category_add_2);
        final Spinner spinner = view.findViewById(R.id.spinner_dlg_category_add_main);
        final EditText editText = view.findViewById(R.id.editText_dlg_category_add_name);
        Button button = view.findViewById(R.id.button_dlg_category_add);

        updateCategoryMainList(0);

        adapter1 = new ArrayAdapter<>(getActivity(),R.layout.support_simple_spinner_dropdown_item, lists1);
        spinner.setAdapter(adapter1);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedMainId = categoryMains.get(i).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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
                String kind = "";
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
                        updateCategoryMainList(0);
                        adapter1.notifyDataSetChanged();
                    } else {
                        ir.insertCategorySub(name, selectedMainId);
                    }
                    Toast.makeText(getContext(),"카테고리 추가",Toast.LENGTH_SHORT).show();
                    editText.setText("");
               }
            }
        });
        builder.setView(view).setTitle("카테고리 추가");
        builder.setPositiveButton("완료",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
    }
    private void dialogCategoryEdit() {
        view = inflater.inflate(R.layout.dialog_category_edit, null);

        Spinner spinner_main = view.findViewById(R.id.spinner_dlg_category_edit_main);
        Spinner spinner_sub = view.findViewById(R.id.spinner_dlg_category_edit_sub);
        final EditText editText_main = view.findViewById(R.id.editText_dlg_category_edit_main);
        final EditText editText_sub = view.findViewById(R.id.editText_dlg_category_edit_sub);
        Button button = view.findViewById(R.id.button_dlg_category_edit);

        updateCategoryMainList(0);
        if(categoryMains.size() > 0) {
            selectedMainId = categoryMains.get(0).getId();
            editText_main.setText(ir.getCategoryMain(String.valueOf(selectedMainId)).getName());
        }

        updateCategorySubList();
        if (categorySubs.size() > 0) {
            selectedSubId = categorySubs.get(0).getId();
            editText_sub.setText(ir.getCategorySub(String.valueOf(selectedSubId)).getName());
        }

        adapter1 = new ArrayAdapter<>(getActivity(),R.layout.support_simple_spinner_dropdown_item,lists1);
        adapter2 = new ArrayAdapter<>(getActivity(),R.layout.support_simple_spinner_dropdown_item,lists2);
        spinner_main.setAdapter(adapter1);
        spinner_sub.setAdapter(adapter2);

        spinner_main.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(categoryMains.size() > 0) {
                    selectedMainId = categoryMains.get(i).getId();
                    editText_main.setText(ir.getCategoryMain(String.valueOf(selectedMainId)).getName());
                    updateCategorySubList();
                    adapter2.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner_sub.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (categorySubs.size() > 0) {
                    selectedSubId = categorySubs.get(i).getId();
                    editText_sub.setText(ir.getCategorySub(String.valueOf(selectedSubId)).getName());
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

                updateCategoryMainList(0);
                updateCategorySubList();
                adapter1.notifyDataSetChanged();
                adapter2.notifyDataSetChanged();
            }
        });
        builder.setView(view).setTitle("카테고리 편집");
        builder.setPositiveButton("완료",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
    }
    private void dialogCategoryDel() {
        view = inflater.inflate(R.layout.dialog_category_del, null);

        final Spinner spinner_main = view.findViewById(R.id.spinner_dlg_category_del_main);
        final Spinner spinner_sub = view.findViewById(R.id.spinner_dlg_category_del_sub);
        Button button_main = view.findViewById(R.id.button_dlg_category_del_main);
        Button button_sub = view.findViewById(R.id.button_dlg_category_del_sub);

        updateCategoryMainList(0);
        if(categoryMains.size() > 0) {
            selectedMainId = categoryMains.get(0).getId();
        }

        updateCategorySubList();
        if (categorySubs.size() > 0) {
            selectedSubId = categorySubs.get(0).getId();
        }

        adapter1 = new ArrayAdapter<>(getActivity(),R.layout.support_simple_spinner_dropdown_item,lists1);
        adapter2 = new ArrayAdapter<>(getActivity(),R.layout.support_simple_spinner_dropdown_item,lists2);
        spinner_main.setAdapter(adapter1);
        spinner_sub.setAdapter(adapter2);

        spinner_main.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedMainId = categoryMains.get(i).getId();
                updateCategorySubList();
                adapter2.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner_sub.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedSubId = categorySubs.get(i).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        button_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ir.deleteCategoryMain("_id",new String[] {String.valueOf(selectedMainId)});
                updateCategoryMainList(0);
                updateCategorySubList();
                adapter1.notifyDataSetChanged();
                adapter2.notifyDataSetChanged();
            }
        });
        button_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ir.deleteCategorySub("_id",new String[] {String.valueOf(selectedSubId)});
                updateCategoryMainList(0);
                updateCategorySubList();
                adapter1.notifyDataSetChanged();
                adapter2.notifyDataSetChanged();
            }
        });

        builder.setView(view).setTitle("카테고리 삭제");
        builder.setPositiveButton("완료",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.onWorkComplete("");
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
    }

    private void dialogCardAdd() {
        view = inflater.inflate(R.layout.dialog_card_add, null);

        final EditText editText_name = view.findViewById(R.id.editText_dlg_card_add_name);
        final EditText editText_num = view.findViewById(R.id.editText_dlg_card_add_num);
        final EditText editText_com = view.findViewById(R.id.editText_dlg_card_add_com);
        final EditText editText_date = view.findViewById(R.id.editText_dlg_card_add_date);

        builder.setView(view).setTitle("카드 추가");
        builder.setPositiveButton("완료",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String name = editText_name.getText().toString();
                String com = editText_com.getText().toString();
                String num = editText_num.getText().toString();
                String date = editText_date.getText().toString();

                ir.insertCard(name,num,com,Integer.parseInt(date));
                listener.onWorkComplete(null);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
    }
    private void dialogCardEdit() {
        view = inflater.inflate(R.layout.dialog_card_edit, null);

        final EditText editText_name = view.findViewById(R.id.editText_dlg_card_edit_name);
        final EditText editText_num = view.findViewById(R.id.editText_dlg_card_edit_num);
        final EditText editText_com = view.findViewById(R.id.editText_dlg_card_edit_com);
        final EditText editText_date = view.findViewById(R.id.editText_dlg_card_edit_date);

        Spinner spinner = view.findViewById(R.id.spinner_dlg_card_edit_name);

        updateCardList();

        adapter3 = new ArrayAdapter<>(getActivity(),R.layout.support_simple_spinner_dropdown_item,lists3);
        spinner.setAdapter(adapter3);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        builder.setView(view).setTitle("카드 편집");
        builder.setPositiveButton("완료",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String name = editText_name.getText().toString();
                String num = editText_num.getText().toString();
                String com = editText_com.getText().toString();
                String date = editText_date.getText().toString();

                ir.updateCard(selectedCardId,name,num,com,Integer.parseInt(date));
                listener.onWorkComplete(null);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
    }
    private void dialogCardDel() {
        view = inflater.inflate(R.layout.dialog_card_del, null);

        Spinner spinner = view.findViewById(R.id.spinner_dlg_card_del_name);

        updateCardList();

        adapter1 = new ArrayAdapter<>(getActivity(),R.layout.support_simple_spinner_dropdown_item,lists1);
        spinner.setAdapter(adapter1);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (cards.size() > 0) {
                    Card card = cards.get(i);
                    selectedCardId = card.getId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        builder.setView(view).setTitle("카드 삭제");
        builder.setPositiveButton("완료",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ir.deleteCard("_id", new String[] {String.valueOf(selectedCardId)});
                listener.onWorkComplete("");
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
    }

    private void modifyInfoDiary(int select) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String txtDate;
        int evaluation;
        int index = 0;

        if(select == 0) {
            evaluation = ir.getInfoIO(ir.getCurrentAccount(), selectedDate).getEvaluation();
            calInfoDairy(select,0,selectedDate,evaluation);
            select = 1;
        }

        List<Info_Dairy> daires = ir.getInfoDaires();

        try {
            Date date = df.parse(daires.get(index).getDate());

             do {
                 txtDate = daires.get(index).getDate();
                 date = df.parse(txtDate);
                 evaluation = ir.getInfoIO(ir.getCurrentAccount(),txtDate).getEvaluation();
                 calInfoDairy(select,daires.get(index).getId(),txtDate,evaluation);
                 index++;

             } while(df.parse(selectedDate).compareTo(date) < 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void calInfoDairy(int select, int id, String date, int evaluation) {
        int sum_in, sum_out, principal;
        double rate = 0;

        sum_in = ir.getSum(new String[]{"input"},date);
        sum_out = ir.getSum(new String[]{"output"},date);
        principal = sum_in - sum_out;
        if(principal != 0 && evaluation != 0) rate = (double)evaluation / (double)principal * 100 - 100;

        switch(select) {
            case 0:
                ir.insertInfoDairy(date,principal,rate);
                break;
            case 1:
                ir.updateInfoDairy(id,date,principal,rate);
                break;
        }
        mActivity.fragmentSub1.CallUpdate2();
    }

    private void updateCategoryMainList(int kind) {
        lists1.clear();

        categoryMains = ir.getCategoryMains(kind);

        if(categoryMains.isEmpty()) return;

        for (int i = 0; i < categoryMains.size(); i++) {
            lists1.add(categoryMains.get(i).getName());
        }
    }
    private void updateCategorySubList() {
        lists2.clear();
        categorySubs = ir.getCategorySubs(selectedMainId);

        if(categorySubs.isEmpty()) return;

        for (int i = 0; i < categorySubs.size(); i++) {
            lists2.add(categorySubs.get(i).getName());
        }
    }
    private void updateAccountList() {
        String str;
        Account account;

        lists3.clear();
        accounts = ir.getAccounts();

        if(accounts.isEmpty()) return;

        for (int i = 0; i < accounts.size(); i++) {
            account = accounts.get(i);
            str = account.getNumber() + " " + account.getInstitute() + " " + account.getDiscription();
            lists3.add(str);
        }
    }
    private void updateCardList() {
        lists4.clear();
        cards = ir.getCards();

        if(cards.isEmpty()) return;

        for (int i = 0; i < cards.size(); i++) {
            lists4.add(cards.get(i).getName());
        }
    }

    private int findCategoryMain(int id) {
        int result = -1;

        if(categoryMains.isEmpty()) return -1;

        for (int i = 0; i < categoryMains.size(); i++) {
            if(categoryMains.get(i).getId() == id) result = i;
        }
        return result;
    }
    private int findCategorySub(int id) {
        int result = -1;

        if(categorySubs.isEmpty()) return -1;

        for (int i = 0; i < categorySubs.size(); i++) {
            if(categorySubs.get(i).getId() == id) result = i;
        }
        return result;
    }
    private int findAccount(int id) {
        int result = -1;

        if(accounts.isEmpty()) return -1;

        for (int i = 0; i < accounts.size(); i++) {
            if(accounts.get(i).getId() == id) result = i;
        }
        return result;
    }
    private int findCard(int id) {
        int result = -1;

        if(cards.isEmpty()) return -1;

        for (int i = 0; i < cards.size(); i++) {
            if(cards.get(i).getId() == id) result = i;
        }
        return result;
    }
}
