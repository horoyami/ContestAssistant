package org.horoyami.contestassistant;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by victor on 2/27/18.
 */


public class StartActivity extends Activity {

    private final int SETTING = 23;
    private final int STATUS = 24;
    public static final String EXTRA_CONS = "org.horoyami.contest.round_activity.cons";
    public static final String EXTRA_DOWN = "org.horoyami.contest.round_activity.down";
    private ArrayList<Context> cons = new ArrayList<>();
    private boolean isDownload = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);

        file = new File(this.getCacheDir(), basa);

        if (savedInstanceState != null) {
            cons = (ArrayList<Context>) savedInstanceState.getSerializable(EXTRA_CONS);
            isDownload = savedInstanceState.getBoolean(EXTRA_DOWN);
        }

        if (!isDownload)
            read();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putBoolean(EXTRA_DOWN, isDownload);
        savedInstanceState.putSerializable(EXTRA_CONS, cons);
    }

    protected void onClickStartContest(View view) {
        Intent intent = new Intent(this, SettingContestActivity.class);
        startActivityForResult(intent, SETTING);
    }

    protected void onClickStatus(View view) {
        Intent intent = new Intent(this, StatusActivity.class);
        intent.putExtra(EXTRA_CONS, cons);
        startActivityForResult(intent, STATUS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SETTING) {
            if (resultCode == RESULT_OK) {

                final String name = data.getStringExtra(SettingContestActivity.EXTRA_CONTEST_NAME);
                final int allTime = data.getIntExtra(SettingContestActivity.EXTRA_CONTEST_TIME, 0);
                final ArrayList<Task> tasks = (ArrayList<Task>)  data.getSerializableExtra(RoundActivity.EXTRA_TASKS);

                cons.add(new Context(name, tasks, allTime));
                write();
            }
        }
        if (requestCode == STATUS) {
            if (resultCode == RESULT_OK) {
                cons.clear();
                write();
            }
        }
    }

    private static final String basa = "data.db";
    private File file;

    private void read() {

        try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {

            cons = (ArrayList<Context>) in.readObject();

        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            Toast.makeText(this, getResources().getText(R.string.errorIOR), Toast.LENGTH_SHORT).show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void write() {
        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {

            out.writeObject(cons);

        } catch (IOException e) {
            Toast.makeText(this, getResources().getText(R.string.errorIOW), Toast.LENGTH_SHORT).show();
        }
    }
}
