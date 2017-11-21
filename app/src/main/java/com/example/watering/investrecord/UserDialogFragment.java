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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by watering on 17. 11. 2.
 */

public class UserDialogFragment extends DialogFragment {

    private int type;
    private UserListener listener;
    private List<Group> groups = new ArrayList<>();
    private List<String> lists = new ArrayList<>();
    private EditText edit, txtInput, txtOutput, txtEvaluation;
    private String select, selectedDate;
    private MainActivity mActivity;
    private IRResolver ir;

    public static UserDialogFragment newInstance(int type, UserListener listener) {
        UserDialogFragment fragment = new UserDialogFragment();
        fragment.listener = listener;
        fragment.type = type;
        return fragment;
    }

    public interface UserListener {
        void onWorkComplete(String name);
        void onDeleteAll();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view;
        ArrayAdapter<String> adapter;
        ListView list;
        Button btn;

        mActivity = (MainActivity) getActivity();
        ir = mActivity.ir;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = mActivity.getLayoutInflater();

        switch (type) {
            case 0: //dialog_addgroup
                view = inflater.inflate(R.layout.dialog_addgroup, null);
                edit = (EditText) view.findViewById(R.id.edit_groupname_add);
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
                return builder.create();

            case 1: //dialog_editgroup
                adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, lists);

                view = inflater.inflate(R.layout.dialog_editgroup, null);

                list = (ListView) view.findViewById(R.id.listGroup_edit);
                list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                list.setAdapter(adapter);
                list.setOnItemClickListener(mItemClickListener);

                edit = (EditText) view.findViewById(R.id.edit_groupname_edit);
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
                return builder.create();

            case 2: //dialog_delgroup
                adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, lists);

                view = inflater.inflate(R.layout.dialog_delgroup, null);

                list = (ListView) view.findViewById(R.id.listGroup_del);
                list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                list.setAdapter(adapter);
                list.setOnItemClickListener(mItemClickListener);

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
                return builder.create();

            case 3: //dialog_setting
                view = inflater.inflate(R.layout.dialog_setting, null);
                btn = (Button)view.findViewById(R.id.button_delete_all);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onDeleteAll();
                    }
                });

                builder.setView(view).setTitle("설정");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onWorkComplete("");
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                return builder.create();

            case 4: //dialog_inout
                view = inflater.inflate(R.layout.dialog_inout, null);

                txtInput = (EditText) view.findViewById(R.id.editText_input);
                txtOutput = (EditText) view.findViewById(R.id.editText_output);
                txtEvaluation = (EditText) view.findViewById(R.id.editText_evaluation);

                Info_IO io = ir.getInfoIO(String.valueOf(ir.getCurrentAccount()),selectedDate);
                if(io == null) {
                    txtInput.setText("");
                    txtOutput.setText("");
                    txtEvaluation.setText("");
                }
                else {
                    txtInput.setText(String.valueOf(io.getInput()));
                    txtOutput.setText(String.valueOf(io.getOutput()));
                    txtEvaluation.setText(String.valueOf(io.getEvaluation()));
                }

                view.findViewById(R.id.button_regist_frag3).setOnClickListener(mClickListener);
                view.findViewById(R.id.button_edit_frag3).setOnClickListener(mClickListener);
                view.findViewById(R.id.button_delete_frag3).setOnClickListener(mClickListener);

                builder.setView(view).setTitle("입출금 입력");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onWorkComplete("");
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                return builder.create();

            default:
                return null;
        }
    }

    public void initData(List<Group> lists) {
        this.groups = lists;
        for(int i=0; i<groups.size(); i++) {
            this.lists.add(groups.get(i).getName());
        }
    }

    AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            select = groups.get(position).getName();
        }
    };

    public void setSelectedDate(String selectedDate) {
        this.selectedDate = selectedDate;
    }

    Button.OnClickListener mClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            int input = 0, output = 0, evaluation = 0;

            String in = txtInput.getText().toString();
            String out = txtOutput.getText().toString();
            String eval = txtEvaluation.getText().toString();

            if(!in.isEmpty()) input = Integer.parseInt(in);
            else input = 0;
            if(!out.isEmpty()) output = Integer.parseInt(out);
            else output = 0;
            if(!eval.isEmpty()) evaluation = Integer.parseInt(eval);
            else evaluation = 0;

            switch(v.getId()) {
                case R.id.button_regist_frag3:
                    ir.insertInfoIO(selectedDate,input,output,evaluation);
                    calInfoDairy(0,evaluation);
                    mActivity.Callback3to2();
                    break;
                case R.id.button_edit_frag3:
                    ir.updateInfoIO(selectedDate,input,output,evaluation);
                    calInfoDairy(1,evaluation);
                    mActivity.Callback3to2();
                    break;
                case R.id.button_delete_frag3:
                    break;
            }
        }
    };

    private void calInfoDairy(int select, int evaluation) {
        int sum_in, sum_out, principal;
        double rate = 0;

        sum_in = ir.getSum(new String[]{"input"},selectedDate);
        sum_out = ir.getSum(new String[]{"output"},selectedDate);
        principal = sum_in - sum_out;
        if(principal !=0 && evaluation !=0) rate = (double)evaluation / (double)principal * 100 - 100;

        switch(select) {
            case 0:
                ir.insertInfoDairy(selectedDate,principal,rate);
                break;
            case 1:
                ir.updateInfoDairy(selectedDate,principal,rate);
                break;
        }
    }
}
