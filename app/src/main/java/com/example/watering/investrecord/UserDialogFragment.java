package com.example.watering.investrecord;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by watering on 17. 11. 2.
 */

@SuppressWarnings("ALL")
public class UserDialogFragment extends DialogFragment {
    private int type, i_u;
    private UserListener listener;
    private List<Group> groups = new ArrayList<>();
    private final List<String> lists = new ArrayList<>();
    private EditText edit, txtInput, txtOutput, txtEvaluation;
    private TextView txtDate;
    private String select, selectedDate;
    private MainActivity mActivity;
    private IRResolver ir;
    private View view;
    private ArrayAdapter<String> adapter;
    private ListView list;
    private LayoutInflater inflater;
    private AlertDialog.Builder builder;

    public interface UserListener {
        void onWorkComplete(String name);
        void onDeleteAll();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mActivity = (MainActivity) getActivity();
        ir = mActivity.ir;

        builder = new AlertDialog.Builder(getActivity());
        inflater = mActivity.getLayoutInflater();

        switch (type) {
            case 0:
                dialogAddGroup();
                break;
            case 1:
                dialogEditGroup();
                break;
            case 2:
                dialogDelGroup();
                break;
            case 3:
                dialogSetting();
                break;
            case 4:
                dialogInout();
                break;
            default:
                return null;
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
            this.lists.add(groups.get(i).getName());
        }
    }
    public void setSelectedDate(String selectedDate) {
        this.selectedDate = selectedDate;
    }

    private void dialogAddGroup() {
        view = inflater.inflate(R.layout.dialog_addgroup, null);
        edit = view.findViewById(R.id.edit_groupname_add);
        builder.setView(view).setTitle("그룹 추가");
        builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onWorkComplete(edit.getText().toString());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }
    private void dialogEditGroup() {
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_single_choice, lists);

        view = inflater.inflate(R.layout.dialog_editgroup, null);

        list = view.findViewById(R.id.listGroup_edit);
        list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                select = groups.get(position).getName();
                ir.setCurrentGroup(groups.get(position).getId());
                edit.setText(select);
            }
        });

        edit = view.findViewById(R.id.edit_groupname_edit);
        builder.setView(view).setTitle("그룹 수정");
        builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onWorkComplete(edit.getText().toString());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }
    private void dialogDelGroup() {
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_single_choice, lists);

        view = inflater.inflate(R.layout.dialog_delgroup, null);

        list = view.findViewById(R.id.listGroup_del);
        list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                select = groups.get(position).getName();
                ir.setCurrentGroup(groups.get(position).getId());
            }
        });

        builder.setView(view).setTitle("그룹 삭제");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onWorkComplete(select);
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
        Button btn_delete = view.findViewById(R.id.button_delete_all);
        Button btn_delete_file = view.findViewById(R.id.button_delete_db);
        Button btn_backup_file = view.findViewById(R.id.button_backup_db);
        Button btn_restore_file = view.findViewById(R.id.button_restore_db);

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDeleteAll();
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
        if(ir.getCurrentAccount() < 0) {
            Toast.makeText(mActivity.getApplicationContext(),R.string.toast_no_account,Toast.LENGTH_SHORT).show();
            return;
        }
        view = inflater.inflate(R.layout.dialog_inout, null);

        DecimalFormat df = new DecimalFormat("#,###");

        txtInput = view.findViewById(R.id.editText_input);
        txtOutput = view.findViewById(R.id.editText_output);
        txtEvaluation = view.findViewById(R.id.editText_evaluation);
        txtDate = view.findViewById(R.id.txtDate);

        txtDate.setText(selectedDate);

        Info_IO io = ir.getInfoIO(ir.getCurrentAccount(),selectedDate);

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
}
