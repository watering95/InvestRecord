package com.example.watering.investrecord;

import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.drive.CreateFileActivityOptions;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveClient;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.DriveResourceClient;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.OpenFileActivityOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public FragmentMain fragmentMain;
    public FragmentSub fragmentSub;

    private ViewPager mMainViewPager;

    public final IRResolver ir = new IRResolver();

    private DriveClient mDriveClient;
    private DriveResourceClient mDriveResourceClient;
    private DriveId mCurrentDriveId;
    private DriveContents mDriveContents;

    private static final String TAG = "InvestRecord";
    private static final int REQUEST_CODE_SIGN_IN_UP = 0;
    private static final int REQUEST_CODE_SIGN_IN_DOWN = 1;
    private static final int REQUEST_CODE_CREATOR = 2;
    private static final int REQUEST_CODE_OPENER = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initLayout();
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

    private void initLayout() {
        Toolbar mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        final ToggleButton tb = findViewById(R.id.button_title);
        tb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tb.isChecked()) {

                }
                else {

                }
            }
        });

        mMainViewPager = findViewById(R.id.main_viewpager);
        MainTabPagerAdapter mMainPagerAdapter = new MainTabPagerAdapter(getSupportFragmentManager());
        mMainViewPager.setAdapter(mMainPagerAdapter);

        fragmentMain = (FragmentMain) mMainPagerAdapter.getItem(0);
        fragmentSub = (FragmentSub) mMainPagerAdapter.getItem(1);
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
                fragmentMain.updateGroupSpinner();
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
                fragmentMain.updateGroupSpinner();
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
                fragmentMain.updateGroupSpinner();
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
                switch(name) {
                    case "del":
                        deleteDBFile();
                        break;
                    case "backup":
                        signIn(REQUEST_CODE_SIGN_IN_UP);
                        break;
                    case "restore":
                        signIn(REQUEST_CODE_SIGN_IN_DOWN);
                        break;
                }
            }

            @Override
            public void onDeleteAll() {
                ir.deleteAll();
                fragmentMain.updateGroupSpinner();
                fragmentMain.updateAccountSpinner();
            }
        });

        dialog.show(getFragmentManager(), "dialog");
    }

    private void signIn(int code) {
        Log.i(TAG, "Start sign in");
        GoogleSignInClient mGoogleSignInClient = buildGoogleSignInClient();
        startActivityForResult(mGoogleSignInClient.getSignInIntent(), code);
    }
    private void saveFileToDrive() {
        Log.i(TAG, "Load DB File");

        final FileInputStream fileInputStream;

        try {
            String file = getFilesDir().toString();
            file = file.substring(0,file.length()-5) + "databases/InvestRecord.db";
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            Log.i(TAG, "DB File not existed.");
            Toast.makeText(this,R.string.toast_db_error,Toast.LENGTH_SHORT).show();
            return;
        }

        mDriveResourceClient
        .createContents()
        .continueWithTask(
            new Continuation<DriveContents, Task<Void>>() {
                @Override
                public Task<Void> then(@NonNull Task<DriveContents> task) throws Exception {
                    return createFileIntentSender(task.getResult(), fileInputStream);
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
            fragmentMain.updateGroupSpinner();
            fragmentMain.updateAccountSpinner();
            Toast.makeText(this,R.string.toast_db_del_ok,Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this,R.string.toast_db_del_error,Toast.LENGTH_SHORT).show();
        }
    }
    private void downloadFileFromDrive() {
        Log.i(TAG, "Open Drive file.");

        List<String> mimeList = new ArrayList<>();
        mimeList.clear();
        mimeList.add("application/x-sqlite3");

        final OpenFileActivityOptions openFileActivityOptions =
            new OpenFileActivityOptions.Builder()
                .setMimeType(mimeList)
                .build();

        mDriveClient.newOpenFileActivityIntentSender(openFileActivityOptions)
            .addOnSuccessListener(new OnSuccessListener<IntentSender>() {
                @Override
                public void onSuccess(IntentSender intentSender) {
                    try {
                        startIntentSenderForResult(intentSender, REQUEST_CODE_OPENER,null,0,0,0);
                    } catch(IntentSender.SendIntentException e) {
                        Log.w(TAG,"Unable to send intent", e);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Unable to create OpenFileActivityIntent.", e);
            }
        });
    }
    private void loadCurrentFile() {
        Log.d(TAG, "Retrieving...");
        final DriveFile file = mCurrentDriveId.asDriveFile();

        mDriveResourceClient.getMetadata(file)
            .continueWithTask(new Continuation<Metadata, Task<DriveContents>>() {
                @Override
                public Task<DriveContents> then(@NonNull Task<Metadata> task) {
                    if(task.isSuccessful()) {
                        return mDriveResourceClient.openFile(file, DriveFile.MODE_READ_ONLY);
                    } else {
                        return Tasks.forException(task.getException());
                    }
                }
            }).addOnSuccessListener(new OnSuccessListener<DriveContents>() {
                @Override
                public void onSuccess(DriveContents driveContents) {
                    mDriveContents = driveContents;
                    refreshDBFromCurrentFile();
                }
            }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Unable to retrieve file metadata and contents.", e);
            }
        });
    }
    private void refreshDBFromCurrentFile() {
        Log.d(TAG, "Refreshing...");
        if(mCurrentDriveId == null) {

            return;
        }

        try {
            String file = getFilesDir().toString();
            file = file.substring(0,file.length()-5) + "databases/InvestRecord.db";
            final FileOutputStream dbFileOutputStream = new FileOutputStream(file);

            InputStream inputStream = mDriveContents.getInputStream();

            byte[] writeBuffer = new byte[1024];

            try {
                while(inputStream.read(writeBuffer,0,writeBuffer.length) != -1) {
                    dbFileOutputStream.write(writeBuffer);
                }
            } catch(IOException e_io) {
                Log.i(TAG, "Be Failed to restore the DB File.");
                Toast.makeText(this,R.string.toast_db_restore_error,Toast.LENGTH_SHORT).show();
            }

        } catch (FileNotFoundException e) {
            Log.i(TAG, "DB File not existed.");
            Toast.makeText(this,R.string.toast_db_error,Toast.LENGTH_SHORT).show();
            return;
        }
        fragmentMain.initDataBase();
        Toast.makeText(this,R.string.toast_db_restore_ok,Toast.LENGTH_SHORT).show();
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
            case REQUEST_CODE_SIGN_IN_UP:
            case REQUEST_CODE_SIGN_IN_DOWN:
                Log.i(TAG, "Sign in request code");
                if(resultCode == RESULT_OK) {
                    Log.i(TAG, "Singed in successfully.");
                    mDriveClient = Drive.getDriveClient(this, GoogleSignIn.getLastSignedInAccount(this));
                    mDriveResourceClient = Drive.getDriveResourceClient(this, GoogleSignIn.getLastSignedInAccount(this));
                    if(requestCode == REQUEST_CODE_SIGN_IN_UP) saveFileToDrive();
                    else if(requestCode == REQUEST_CODE_SIGN_IN_DOWN) downloadFileFromDrive();
                }
                break;
            case REQUEST_CODE_CREATOR:
                Log.i(TAG, "creator request code");
                if(resultCode == RESULT_OK) {
                    Log.i(TAG, "REQUEST_CODE_CREATOR succeded.");
                    Toast.makeText(this,R.string.toast_backup,Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this,R.string.toast_backup_error,Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_CODE_OPENER:
                if( resultCode == RESULT_OK) {
                    mCurrentDriveId = data.getParcelableExtra(OpenFileActivityOptions.EXTRA_RESPONSE_DRIVE_ID);
                    loadCurrentFile();
                }
                break;
            default:
                super.onActivityResult(requestCode,resultCode,data);
        }
    }
}