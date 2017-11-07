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
    private EditText edit;
    private String select;

    public static UserDialogFragment newInstance(int type, UserListener listener) {
        UserDialogFragment fragment = new UserDialogFragment();
        fragment.listener = listener;
        fragment.type = type;
        return fragment;
    }

    public interface UserListener {
        void onWorkComplete(String name);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view;

        switch (type) {
            case 0:
                view = inflater.inflate(R.layout.dialog_addgroup, null);
                edit = (EditText) view.findViewById(R.id.edit_groupname);
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
            case 1:
                ArrayAdapter<String> adapter;

                adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, lists);

                view = inflater.inflate(R.layout.dialog_delgroup, null);

                ListView list = (ListView) view.findViewById(R.id.listGroup);

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

            default:
                return builder.create();
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
}
