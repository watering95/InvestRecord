package com.example.watering.investrecord;

import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.drive.CreateFileActivityOptions;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveClient;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveResourceClient;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private ArrayAdapter<String> groupAdapter;
    private ArrayAdapter<String> accountAdapter;
    private final List<String> grouplists = new ArrayList<>();
    private final List<String> accountlists = new ArrayList<>();
    private List<Group> groups = new ArrayList<>();
    private List<Account> accounts = new ArrayList<>();
    public final IRResolver ir = new IRResolver();

    private DriveClient mDriveClient;
    private DriveResourceClient mDriveResourceClient;

    private static final String TAG = "InvestRecord";
    private static final int REQUEST_CODE_SIGN_IN = 0;
    private static final int REQUEST_CODE_CREATOR = 1;
    private FileInputStream mDBFile;

    interface Callback {
        void updateList();
    }

    private Callback m_callback1,m_callback2,m_callback3,m_callback4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initLayout();
        initGroupSpinner();
        initAccountSpinner();
        initDataBase();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_addGroup:
                addGroupDialog();
                break;
            case R.id.menu_editGroup:
                editGroupDialog();
                break;
            case R.id.menu_delGroup:
                delGroupDialog();
                break;
            case R.id.menu_setting:
                settingDialog();
                break;
        }
        return true;
    }

    public void setCallback1(Callback callback) {
        this.m_callback1 = callback;
    }
    public void setCallback2(Callback callback) {
        this.m_callback2 = callback;
    }
    public void setCallback3(Callback callback) {
        this.m_callback3 = callback;
    }
    public void setCallback4(Callback callback) {
        this.m_callback4 = callback;
    }

    private void CallUpdate1() {
        if(m_callback1 != null) {
            m_callback1.updateList();
        }
    }
    public void CallUpdate2() {
        if(m_callback2 != null) {
            m_callback2.updateList();
        }
    }
    private void CallUpdate3() {
        if(m_callback3 != null) {
            m_callback3.updateList();
        }
    }
    private void CallUpdate4() {
        if(m_callback4 != null) {
            m_callback4.updateList();
        }
    }

    private void initLayout() {
        Toolbar mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        setSupportActionBar(mToolbar);

        TabLayout mTabLayout = findViewById(R.id.main_tab);
        mTabLayout.setTabTextColors(Color.parseColor("#ffffff"),Color.parseColor("#00ff00"));
        mTabLayout.addTab(mTabLayout.newTab().setText("통합자산"));
        mTabLayout.addTab(mTabLayout.newTab().setText("계좌별이력"));
        mTabLayout.addTab(mTabLayout.newTab().setText("입출금입력"));
        mTabLayout.addTab(mTabLayout.newTab().setText("계좌관리"));
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        mViewPager = findViewById(R.id.main_viewpager);
        MainTabPagerAdapter mPagerAdapter = new MainTabPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    private void initDataBase() {
        ir.getContentResolver(getContentResolver());
        updateGroupSpinner();
        updateAccountSpinner();
    }
    private void initGroupSpinner() {
        groupAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, grouplists);

        Spinner mGroupSpinner = findViewById(R.id.spinner_group);
        mGroupSpinner.setAdapter(groupAdapter);
        mGroupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(groups.size() != 0 ) {
                    ir.setCurrentGroup(groups.get(position).getId());
                }
                updateAccountSpinner();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void initAccountSpinner() {
        accountAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, accountlists);
        Spinner mAccountSpinner = findViewById(R.id.spinner_account);
        mAccountSpinner.setAdapter(accountAdapter);
        mAccountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ir.setCurrentAccount(accounts.get(position).getId());

                CallUpdate1();
                CallUpdate2();
                CallUpdate3();
                CallUpdate4();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void inoutDialog(String selectedDate) {
        UserDialogFragment dialog = UserDialogFragment.newInstance(4, new UserDialogFragment.UserListener() {
            @Override
            public void onWorkComplete(String name) {

            }

            @Override
            public void onDeleteAll() {

            }
        });

        dialog.setSelectedDate(selectedDate);
        dialog.show(getFragmentManager(), "dialog");
    }
    private void addGroupDialog() {
        UserDialogFragment dialog = UserDialogFragment.newInstance(0, new UserDialogFragment.UserListener() {
            @Override
            public void onWorkComplete(String name) {
                if(!name.isEmpty()) ir.insertGroup(name);
                updateGroupSpinner();
            }

            @Override
            public void onDeleteAll() {

            }
        });
        dialog.show(getFragmentManager(), "dialog");
    }
    private void editGroupDialog() {
        UserDialogFragment dialog = UserDialogFragment.newInstance(1, new UserDialogFragment.UserListener() {
            @Override
            public void onWorkComplete(String name) {
                if(!name.isEmpty()) ir.updateGroup(ir.getCurrentGroup(),name);
                updateGroupSpinner();
            }

            @Override
            public void onDeleteAll() {

            }

        });
        dialog.initData(ir.getGroups());
        dialog.show(getFragmentManager(), "dialog");
    }
    private void delGroupDialog() {
        UserDialogFragment dialog = UserDialogFragment.newInstance(2, new UserDialogFragment.UserListener() {
            @Override
            public void onWorkComplete(String name) {
                ir.deleteGroup("name",new String[] {name});
                updateGroupSpinner();
            }

            @Override
            public void onDeleteAll() {

            }
        });
        dialog.initData(ir.getGroups());
        dialog.show(getFragmentManager(), "dialog");
    }
    private void settingDialog() {
        UserDialogFragment dialog = UserDialogFragment.newInstance(3, new UserDialogFragment.UserListener() {
            @Override
            public void onWorkComplete(String name) {
                if(name.equals("del")) {
                    deleteDBFile();
                }
                else if(name.equals("backup")) {
                    signIn();
                }
            }

            @Override
            public void onDeleteAll() {
                ir.deleteAll();
                updateGroupSpinner();
                updateAccountSpinner();
            }
        });

        dialog.show(getFragmentManager(), "dialog");
    }

    private void updateGroupList() {
        grouplists.clear();
        groups = ir.getGroups();

        if(groups.isEmpty()) return;

        for(int i = 0; i < groups.size(); i++) {
            grouplists.add(groups.get(i).getName());
        }
    }
    private void updateAccountList() {
        String str;
        Account account;

        accountlists.clear();
        accounts = ir.getAccounts();

        if(accounts.isEmpty()) return;

        for (int i = 0; i < accounts.size(); i++) {
            account = accounts.get(i);
            str = account.getNumber() + " " + account.getInstitute() + " " + account.getDiscription();
            accountlists.add(str);
        }
    }
    private void updateGroupSpinner() {
        updateGroupList();
        groupAdapter.notifyDataSetChanged();
        if(groups.isEmpty()) ir.setCurrentGroup(-1);
        else ir.setCurrentGroup(groups.get(0).getId());
    }
    public void updateAccountSpinner() {
        updateAccountList();
        accountAdapter.notifyDataSetChanged();
        if(accounts.isEmpty()) ir.setCurrentAccount(-1);
        else ir.setCurrentAccount(accounts.get(0).getId());

        CallUpdate1();
        CallUpdate2();
        CallUpdate3();
        CallUpdate4();
    }

    private void signIn() {
        Log.i(TAG, "Start sign in");
        GoogleSignInClient mGoogleSignInClient = buildGoogleSignInClient();
        startActivityForResult(mGoogleSignInClient.getSignInIntent(), REQUEST_CODE_SIGN_IN);
    }
    private void saveFileToDrive() {
        Log.i(TAG, "Load DB File");
        final FileInputStream file = mDBFile;

        mDriveResourceClient
        .createContents()
        .continueWithTask(
            new Continuation<DriveContents, Task<Void>>() {
                @Override
                public Task<Void> then(@NonNull Task<DriveContents> task) throws Exception {
                    return createFileIntentSender(task.getResult(), file);
                }
            }
        )
        .addOnFailureListener(
            new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "Failed to create new contents.", e);
                }
            }
        );
    }
    private void deleteDBFile() {
        String file = getFilesDir().toString();
        file = file.substring(0,file.length()-5) + "databases/InvestRecord.db";

        File dbFile = new File(file);
        try {
            dbFile.delete();
        } catch (Exception e) {

        }
    }

    private GoogleSignInClient buildGoogleSignInClient() {
        GoogleSignInOptions signInOptions =
            new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(Drive.SCOPE_FILE)
                .build();
        return GoogleSignIn.getClient(this,signInOptions);
    }
    private Task<Void> createFileIntentSender(DriveContents driveContents, FileInputStream file) {
        Log.i(TAG, "New contents created.");
        OutputStream outputStream = driveContents.getOutputStream();

        byte[] readBuffer = new byte[1024];

        try {
            while(file.read(readBuffer,0,readBuffer.length) != -1) {
                outputStream.write(readBuffer);
            }
        } catch (IOException e) {
            Log.w(TAG, "Unable to write file contents.", e);
        }

        Calendar today = Calendar.getInstance();
        String filename = String.format(Locale.getDefault(),"InvestRecord_%d%02d%02d.db",today.get(Calendar.YEAR),today.get(Calendar.MONTH),today.get(Calendar.DATE));

        MetadataChangeSet metadataChangeSet =
            new MetadataChangeSet.Builder()
                .setMimeType("application/x-sqlite3")
                .setTitle(filename)
                .build();

        CreateFileActivityOptions createFileActivityOptions =
            new CreateFileActivityOptions.Builder()
                .setInitialMetadata(metadataChangeSet)
                .setInitialDriveContents(driveContents)
                .build();

        return mDriveClient
            .newCreateFileActivityIntentSender(createFileActivityOptions)
            .continueWith(
               new Continuation<IntentSender, Void>() {
                   @Override
                   public Void then(@NonNull Task<IntentSender> task) throws Exception {
                       startIntentSenderForResult(task.getResult(), REQUEST_CODE_CREATOR, null, 0, 0, 0);
                       return null;
                   }
               }
            );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_SIGN_IN:
                Log.i(TAG, "Sign in request code");
                if(resultCode == RESULT_OK) {
                    Log.i(TAG, "Singed in successfully.");
                    mDriveClient = Drive.getDriveClient(this, GoogleSignIn.getLastSignedInAccount(this));
                    mDriveResourceClient = Drive.getDriveResourceClient(this, GoogleSignIn.getLastSignedInAccount(this));

                    try {
                        String file = getFilesDir().toString();
                        file = file.substring(0,file.length()-5) + "databases/InvestRecord.db";
                        mDBFile = new FileInputStream(file);
                    } catch (FileNotFoundException e) {
                        Log.i(TAG, "DB File not existed.");
                        return;
                    }

                    saveFileToDrive();
                }
                break;
            case REQUEST_CODE_CREATOR:
                Log.i(TAG, "creator request code");
                if(resultCode == RESULT_OK) {
                    Log.i(TAG, "REQUEST_CODE_CREATOR succeded.");
                }
                break;
        }
    }
}