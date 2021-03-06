package com.example.watering.investrecord;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.AsyncTask;
import android.os.Environment;
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
import android.view.Menu;
import android.view.MenuItem;
import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.watering.investrecord.data.Account;
import com.example.watering.investrecord.data.Group;
import com.example.watering.investrecord.fragment.*;
import com.example.watering.investrecord.info.InfoDairyForeign;
import com.example.watering.investrecord.info.InfoIOForeign;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static android.os.Environment.DIRECTORY_DOCUMENTS;

public class MainActivity extends AppCompatActivity {

    public FragmentSub1 fragmentSub1;
    public FragmentSub2 fragmentSub2;
    public FragmentSub3 fragmentSub3;
    public final IRResolver ir = new IRResolver();

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private DrawerLayout drawerLayout;

    private ArrayAdapter<String> groupAdapter;
    private final List<String> grouplists = new ArrayList<>();
    private List<Group> groups = new ArrayList<>();

    private DriveClient mDriveClient;
    private DriveResourceClient mDriveResourceClient;
    private DriveId mCurrentDriveId;
    private DriveContents mDriveContents;

    private static final String TAG = "InvestRecord";

    private static final int REQUEST_CODE_SIGN_IN_UP = 0;
    private static final int REQUEST_CODE_SIGN_IN_DOWN = 1;
    private static final int REQUEST_CODE_CREATOR = 2;
    private static final int REQUEST_CODE_OPENER = 3;

    public interface Callback {
        void update();
    }
    public interface ExchangeTask {
        void finish(Double[] exchange);
    }

    private Callback m_callbackSub3;
    private static ExchangeTask exchangeTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ir.getContentResolver(getContentResolver());

        initLayout();
        initGroupSpinner();
        initDataBase();
        initExchangeRate();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_toolbar_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            default:
                UserDialogFragment dialog = UserDialogFragment.newInstance(item.getItemId(), new UserDialogFragment.UserListener() {
                    @Override
                    public void onWorkComplete(String name) {
                        updateGroupSpinner();
                    }
                });
                //noinspection ConstantConditions
                dialog.show(getSupportFragmentManager(), "dialog");
        }
        return super.onOptionsItemSelected(item);
    }

    public void setCallbackSub3(Callback callback) {
        this.m_callbackSub3 = callback;
    }
    public void inoutDialog(String selectedDate) {
        UserDialogFragment dialog = UserDialogFragment.newInstance(0, new UserDialogFragment.UserListener() {
            @Override
            public void onWorkComplete(String name) {

            }
        });

        dialog.setSelectedDate(selectedDate);
        dialog.show(fragmentManager, "dialog");
    }
    public String getToday() {
        Calendar today = Calendar.getInstance();

        return String.format(Locale.getDefault(), "%04d-%02d-%02d", today.get(Calendar.YEAR),today.get(Calendar.MONTH)+1,today.get(Calendar.DATE));
    }
    public String monthChange(String date, int amount) {
        Calendar calendar = strToCalendar(date);

        calendar.add(Calendar.MONTH, amount);

        if (Calendar.getInstance().before(calendar)) {
            Toast.makeText(getApplicationContext(), R.string.toast_date_error, Toast.LENGTH_SHORT).show();
            return date;
        }

        return String.format(Locale.getDefault(),"%d-%02d-%02d",calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.DATE));
    }
    public String dateChange(String date, int amount) {
        Calendar calendar = strToCalendar(date);

        calendar.add(Calendar.DATE,amount);

        if (Calendar.getInstance().before(calendar)) {
            Toast.makeText(getApplicationContext(), R.string.toast_date_error, Toast.LENGTH_SHORT).show();
            return date;
        }

        return String.format(Locale.getDefault(),"%d-%02d-%02d",calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.DATE));
    }
    public String calendarToStr(Calendar calendar) {
        return String.format(Locale.getDefault(), "%04d-%02d-%02d", calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.DATE));
    }
    public Calendar strToCalendar(String date) {
        Calendar calendar = Calendar.getInstance();

        if(date == null) return calendar;

        String year = date.substring(0,4);
        String month = date.substring(5,7);
        String day = date.substring(8,10);

        calendar.set(Integer.parseInt(year),Integer.parseInt(month)-1,Integer.parseInt(day));

        return calendar;
    }

    public void setExchangeTask(ExchangeTask exchangeTask) {
        MainActivity.exchangeTask = exchangeTask;
    }
    public void runExchangeTask(String date) {
        String authKey = "GJxPHU7IZSycH27FubZt3wvxIqKlnue1";
        String dataType = "AP01";
        String apiURL = "https://www.koreaexim.go.kr/site/program/financial/exchangeJSON?authkey=" + authKey + "&searchdate=" + date + "&data=" + dataType;

        new GetExchangeTask().execute(apiURL);
    }

    static class GetExchangeTask extends AsyncTask<String, Void, Double[]> {

        @Override
        protected Double[] doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                Double[] currency = new Double[5];

                DecimalFormat df = new DecimalFormat("#,###.##");

                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder res = new StringBuilder();

                while ((inputLine = br.readLine()) != null) {
                    res.append(inputLine);
                }
                br.close();

                JSONArray jsonArray = new JSONArray(res.toString());

                for (int i = 0, limit = jsonArray.length(); i < limit; i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String cur_unit = jsonObject.getString("cur_unit");
                    if (cur_unit.compareTo("USD") == 0) {
                        currency[0] = df.parse(jsonObject.getString("deal_bas_r")).doubleValue();
                    }
                    else if (cur_unit.compareTo("CNH") == 0) {
                        currency[1] = df.parse(jsonObject.getString("deal_bas_r")).doubleValue();
                    }
                    else if (cur_unit.compareTo("EUR") == 0) {
                        currency[2] = df.parse(jsonObject.getString("deal_bas_r")).doubleValue();
                    }
                    else if (cur_unit.compareTo("JPY(100)") == 0) {
                        currency[3] = df.parse(jsonObject.getString("deal_bas_r")).doubleValue();
                    }
                    else if (cur_unit.compareTo("HKD") == 0) {
                        currency[4] = df.parse(jsonObject.getString("deal_bas_r")).doubleValue();
                    }
                }
                con.disconnect();
                return currency;
            } catch (IOException | JSONException | ParseException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Double[] aDouble) {
            super.onPostExecute(aDouble);
            exchangeTask.finish(aDouble);
        }
    }

    private void initLayout() {
        final Toolbar mToolbar = findViewById(R.id.toolbar_main);
        mToolbar.setTitleTextColor(Color.parseColor("#ffffff"));

        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        actionBar.setDisplayHomeAsUpEnabled(true);

        fragmentSub1 = new FragmentSub1();
        fragmentSub2 = new FragmentSub2();
        fragmentSub3 = new FragmentSub3();

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
                    case R.id.navigation_item_sub3:
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frame_main, fragmentSub3).commit();
                        mToolbar.setTitle(R.string.title3);
                        break;
                    case R.id.navigation_item_DB_manage:
                        settingDialog();
                        break;
                }

                return true;
            }
        });
    }
    private void initGroupSpinner() {
        //noinspection ConstantConditions
        groupAdapter = new ArrayAdapter<>(getBaseContext(), R.layout.support_simple_spinner_dropdown_item, grouplists);

        Spinner mGroupSpinner = findViewById(R.id.spinner_group);
        mGroupSpinner.setAdapter(groupAdapter);
        mGroupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(groups.size() != 0 ) {
                    ir.setCurrentGroup(groups.get(position).getId());
                }
                fragmentSub1.callUpdateFrag1();
                fragmentSub1.callUpdateFrag2();
                callUpdateFragSub3();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void initDataBase() {
        updateGroupSpinner();
    }
    private void initExchangeRate() {
        final String today = getToday();

        // table이 존재하지 않으면
        if(ir.checkDBTable("tbl_Info_Dairy_Foreign") == 0) {
            return;
        }

        ExchangeTask mainExchangeTask = new ExchangeTask() {
            @Override
            public void finish(Double[] exchangeRate) {

                for(int i = 0, limit = 5; i < limit; i++) {
                    if(exchangeRate[i] == null) {
                        Toast.makeText(getApplicationContext(),R.string.toast_exchange_error,Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                InfoIOForeign[] info_io_foreign_today = new InfoIOForeign[5];

                for(int i_group = 0, limit_i = groups.size(); i_group < limit_i; i_group++) {

                    List<Account> accounts = ir.getAccounts(groups.get(i_group).getId());

                    for(int i_account = 0, limit_j = accounts.size(); i_account < limit_j; i_account++) {

                        int id_account = accounts.get(i_account).getId();

                        for(int i_currency = 0, limit_k = 5; i_currency < limit_k; i_currency++) {
                            InfoDairyForeign info_dairy_foreign_last = ir.getLastInfoDairyForeign(id_account, i_currency, today);
                            info_io_foreign_today[i_currency] = ir.getInfoIOForeign(id_account, i_currency, today);
                            int evaluation;

                            if(info_dairy_foreign_last != null) {
                                Double principal = info_dairy_foreign_last.getPrincipal();

                                if(principal >= 0) evaluation = (int) (principal * exchangeRate[i_currency]);
                                else continue;
                            }
                            else {
                                continue;
                            }

                            if(info_io_foreign_today[i_currency] == null) {
                                info_io_foreign_today[i_currency] = new InfoIOForeign();

                                info_io_foreign_today[i_currency].setCurrency(i_currency);
                                info_io_foreign_today[i_currency].setAccount(id_account);
                                info_io_foreign_today[i_currency].setDate(today);
                                info_io_foreign_today[i_currency].setEvaluation(evaluation);
                                ir.insertInfoIOForeign(info_io_foreign_today[i_currency]);
                            }
                            else {
//                                info_io_foreign_today[i_currency].setEvaluation(evaluation);
//                                ir.updateInfoIOForeign(info_io_foreign_today[i_currency]);
                            }
                        }
                    }
                }
            }
        };
        setExchangeTask(mainExchangeTask);
        runExchangeTask(today);
    }
    private void updateGroupSpinner() {
        updateGroupList();
        groupAdapter.notifyDataSetChanged();
        if(groups.isEmpty()) ir.setCurrentGroup(-1);
        else ir.setCurrentGroup(groups.get(0).getId());
    }
    private void updateGroupList() {
        grouplists.clear();
        groups = ir.getGroups();
        if(groups.isEmpty()) {
            Log.i(TAG, "No group");
            return;
        }

        for(int i = 0, n = groups.size(); i < n; i++) {
            grouplists.add(groups.get(i).getName());
        }
    }
    private void settingDialog() {
        UserDialogFragment dialog = UserDialogFragment.newInstance(R.id.navigation_item_DB_manage, new UserDialogFragment.UserListener() {
            @Override
            public void onWorkComplete(String name) {
                switch(name) {
                    case "delall":
                        ir.deleteAll();
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
                    case "save":
                        saveDBToLocal();
                        break;
                }
                updateGroupSpinner();
                fragmentSub1.callUpdateFrag1();
                fragmentSub1.callUpdateFrag2();
                callUpdateFragSub3();
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
        // DB 복원 후 해야 할 일 추가

        Toast.makeText(this,R.string.toast_db_restore_ok,Toast.LENGTH_SHORT).show();
    }
    private void callUpdateFragSub3() {
        if(m_callbackSub3 != null) {
            m_callbackSub3.update();
        }
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

        String filename = "InvestRecord_" + getToday() + ".db";

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
    private void saveDBToLocal() {
        try {
            File dataDir = Environment.getDataDirectory();
            File bkDir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);

            assert bkDir != null;
            if(bkDir.canWrite()) {
                String dbPath = "//data//com.example.watering.investrecord//databases//InvestRecord.db";
                String bkPath = "InvestRecord.db";

                File dataDB = new File(dataDir, dbPath);
                File bkDB = new File(bkDir, bkPath);

                if(dataDB.exists()) {
                    FileChannel src = new FileInputStream(dataDB).getChannel();
                    FileChannel dst = new FileOutputStream(bkDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();

                    Toast.makeText(getApplicationContext(),R.string.toast_save_ok,Toast.LENGTH_SHORT).show();
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),R.string.toast_save_error,Toast.LENGTH_SHORT).show();
        }
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