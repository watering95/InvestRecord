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

    private ArrayAdapter<String> adapter1, adapter2;
    private List<Group> groups = new ArrayList<>();
    private List<String> lists1 = new ArrayList<>();
    private List<String> lists2 = new ArrayList<>();
    private List<CategoryMain> categoryMains = new ArrayList<>();
    private List<CategorySub> categorySubs = new ArrayList<>();
    private List<Card> cards = new ArrayList<>();

    private String selectedDate;
    private int type, i_u, selectedMainId, selectedSubId, selectedId;
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

        switch (type) {
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
        fragment.type = type;
        return fragment;
    }

    public void initData(List<Group> lists) {
        this.groups = lists;
        for(int i=0; i<groups.size(); i++) {
            this.lists1.add(groups.get(i).getName());
        }
    }
    public void setSelectedDate(String selectedDate) {
        this.selectedDate = selectedDate;
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
        TextView txtDate = view.findViewById(R.id.textView_dlg_inout_date);

        if(ir.getCurrentAccount() < 0) {
            Toast.makeText(mActivity.getApplicationContext(),R.string.toast_no_account,Toast.LENGTH_SHORT).show();
            return;
        }

        Info_IO io = ir.getInfoIO(ir.getCurrentAccount(),selectedDate);

        DecimalFormat df = new DecimalFormat("#,###");

        txtDate.setText(selectedDate);

        if(io == null) {
            i_u = 0;
            txtInput.setText("");
            txtOutput.setText("");
            txtEvaluation.setText("");
        }
        else {
            i_u = 1;
            txtInput.setText(df.format(io.getInput()));
            txtOutput.setText(df.format(io.getOutput()));
            txtEvaluation.setText(df.format(io.getEvaluation()));
        }

        builder.setView(view).setTitle("입출금 입력");
        builder.setPositiveButton("등록", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int input=0, output=0, evaluation=0;
                String in, out, eval;

                DecimalFormat df = new DecimalFormat("#,###");

                in = txtInput.getText().toString();
                out = txtOutput.getText().toString();
                eval = txtEvaluation.getText().toString();

                if(in.isEmpty()) in = "0";
                if(out.isEmpty()) out = "0";
                if(eval.isEmpty()) eval = "0";

                try {
                    input = df.parse(in).intValue();
                    output = df.parse(out).intValue();
                    evaluation = df.parse(eval).intValue();
                } catch (ParseException e) {

                }

                Info_IO io = ir.getInfoIO(ir.getCurrentAccount(),selectedDate);

                switch (i_u) {
                    case 0:
                        ir.insertInfoIO(selectedDate,input,output,evaluation);
                        break;
                    case 1:
                        ir.updateInfoIO(io.getId(),io.getDate(),input,output,evaluation);
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
    private void dialogSpend() {
        view = inflater.inflate(R.layout.dialog_spend, null);

        builder.setView(view).setTitle("지출 입력");
        builder.setPositiveButton("완료",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
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

        builder.setView(view).setTitle("수입 입력");
        builder.setPositiveButton("완료",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.onWorkComplete(null);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

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

        updateCategoryMainList();

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
                        updateCategoryMainList();
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

        updateCategoryMainList();
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

                updateCategoryMainList();
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

        updateCategoryMainList();
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
                updateCategoryMainList();
                updateCategorySubList();
                adapter1.notifyDataSetChanged();
                adapter2.notifyDataSetChanged();
            }
        });
        button_sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ir.deleteCategorySub("_id",new String[] {String.valueOf(selectedSubId)});
                updateCategoryMainList();
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

        adapter1 = new ArrayAdapter<>(getActivity(),R.layout.support_simple_spinner_dropdown_item,lists1);
        spinner.setAdapter(adapter1);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (cards.size() > 0) {
                    Card card = cards.get(i);
                    selectedId = card.getId();
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

                ir.updateCard(selectedId,name,num,com,Integer.parseInt(date));
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
                    selectedId = card.getId();
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
                ir.deleteCard("_id", new String[] {String.valueOf(selectedId)});
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

    private void updateCategoryMainList() {
        lists1.clear();
        categoryMains = ir.getCategoryMains();

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
    private void updateCardList() {
        lists1.clear();
        cards = ir.getCards();

        if(cards.isEmpty()) return;

        for (int i = 0; i < cards.size(); i++) {
            lists1.add(cards.get(i).getName());
        }
    }
}
