package com.example.watering.investrecord;

import android.content.Intent;
import android.content.IntentSender;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.graphics.Color;
import android.widget.Toast;

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

    public FragmentSub1 fragmentSub1;
    public FragmentSub2 fragmentSub2;
    public final IRResolver ir = new IRResolver();

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private DrawerLayout drawerLayout;

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
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initLayout() {
        final Toolbar mToolbar = findViewById(R.id.toolbar_main);
        mToolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);

        fragmentSub1 = new FragmentSub1();
        fragmentSub2 = new FragmentSub2();

        fragmentManager = getSupportFragmentManager();

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.frame_main, fragmentSub1).commit();

        drawerLayout = findViewById(R.id.drawer_main_layout);
        NavigationView navigationView = findViewById(R.id.navigation_main);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                drawerLayout.closeDrawers();

                switch(item.getItemId()) {
                    case R.id.navigation_item_sub1:
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frame_main, fragmentSub1).commit();
                        mToolbar.setTitle(R.string.title1);
                        break;
                    case R.id.navigation_item_sub2:
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frame_main, fragmentSub2).commit();
                        mToolbar.setTitle(R.string.title2);
                        break;
                    case R.id.navigation_item_setting:
                        settingDialog();
                        break;
                }

                return true;
            }
        });
    }

    public String getToday() {
        Calendar today = Calendar.getInstance();

        return String.format(Locale.getDefault(), "%04d-%02d-%02d", today.get(Calendar.YEAR),today.get(Calendar.MONTH)+1,today.get(Calendar.DATE));
    }
    public String dateChange(String date, int amount) {
        Calendar calendar = Calendar.getInstance();
        String year = date.substring(0,4);
        String month = date.substring(5,7);
        String day = date.substring(8,10);

        calendar.set(Integer.parseInt(year),Integer.parseInt(month),Integer.parseInt(day));
        calendar.add(Calendar.DATE,amount);

        return String.format(Locale.getDefault(),"%d-%02d-%02d",calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DATE));
    }

    public void inoutDialog(String selectedDate) {
        UserDialogFragment dialog = UserDialogFragment.newInstance(1, new UserDialogFragment.UserListener() {
            @Override
            public void onWorkComplete(String name) {

            }
        });

        dialog.setSelectedDate(selectedDate);
        dialog.show(fragmentManager, "dialog");
    }
    private void settingDialog() {
        UserDialogFragment dialog = UserDialogFragment.newInstance(R.id.navigation_item_setting, new UserDialogFragment.UserListener() {
            @Override
            public void onWorkComplete(String name) {
                switch(name) {
                    case "delall":
                        ir.deleteAll();
                        fragmentSub1.updateGroupSpinner();
                        fragmentSub1.updateAccountSpinner();
                        break;
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
        });

        dialog.show(fragmentManager, "dialog");
    }

    private void deleteDBFile() {
        String file = getFilesDir().toString();
        file = file.substring(0,file.length()-5) + "databases/InvestRecord.db";

        File dbFile = new File(file);
        try {
            //noinspection ResultOfMethodCallIgnored
            dbFile.delete();
            fragmentSub1.updateGroupSpinner();
            fragmentSub1.updateAccountSpinner();
            Toast.makeText(this,R.string.toast_db_del_ok,Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this,R.string.toast_db_del_error,Toast.LENGTH_SHORT).show();
        }
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
                            //noinspection ConstantConditions
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
        fragmentSub1.initDataBase();
        Toast.makeText(this,R.string.toast_db_restore_ok,Toast.LENGTH_SHORT).show();
    }

    private void signIn(int code) {
        Log.i(TAG, "Start sign in");
        GoogleSignInClient mGoogleSignInClient = buildGoogleSignInClient();
        startActivityForResult(mGoogleSignInClient.getSignInIntent(), code);
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
                    //noinspection ConstantConditions
                    mDriveClient = Drive.getDriveClient(this, GoogleSignIn.getLastSignedInAccount(this));
                    //noinspection ConstantConditions
                    mDriveResourceClient = Drive.getDriveResourceClient(this, GoogleSignIn.getLastSignedInAccount(this));
                    if(requestCode == REQUEST_CODE_SIGN_IN_UP) saveFileToDrive();
                    else downloadFileFromDrive();
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