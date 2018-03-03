package org.horoyami.contestassistant;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Pair;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by victor on 2/27/18.
 */

public class RoundActivity extends Activity {

    private final String EXTRA_IS_CREATED = "org.horoyami.contest.round_activity.is_created";
    private final String EXTRA_ALL_TIME = "org.horoyami.contest.round_activity.all_time";
    private final String EXTRA_PASSED_TIME = "org.horoyami.contest.round_activity.passed_mode";
    private final String EXTRA_TIME_MODE = "org.horoyami.contest.round_activity.time_mode";
    public static final String EXTRA_TASKS = "org.horoyami.contest.round_activity.tasks";

    private enum TIME_MODE {UP, DOWN}

    private boolean isCreated = false;
    private int allTimeContest;
    private static int passedTime = 0;
    private TIME_MODE timeMode = TIME_MODE.DOWN;
    private ArrayList<Task> tasks = new ArrayList<>();

    private Timer timerPerMin;
    private Timer timerEndCon;
    private TextView timerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.round_activity);

        if (savedInstanceState != null) {

            isCreated = savedInstanceState.getBoolean(EXTRA_IS_CREATED, false);
            allTimeContest = savedInstanceState.getInt(EXTRA_ALL_TIME, 0);
            passedTime = savedInstanceState.getInt(EXTRA_PASSED_TIME, 0);
            timeMode = (TIME_MODE) savedInstanceState.getSerializable(EXTRA_TIME_MODE);
            tasks = (ArrayList<Task>) savedInstanceState.getSerializable(EXTRA_TASKS);
            ListView list = (ListView) findViewById(R.id.list_tasks);
            list.setAdapter(new RoundListAdapter(tasks, this));
            timerView = (TextView) findViewById(R.id.timer);
            timerView.setText(generateTime());
            timerPerMin = new Timer();
            timerEndCon = new Timer();
            setTimers();
        }

        if (!isCreated) {
            init();
        }
    }

    public static int getTime() {
        return passedTime;
    }

    private void init() {
        ListView list = (ListView) findViewById(R.id.list_tasks);
        int problems = getIntent().getIntExtra(SettingContestActivity.EXTRA_CONTEST_PROBLEM, 0);
        allTimeContest = getIntent().getIntExtra(SettingContestActivity.EXTRA_CONTEST_TIME, 0);
        tasks.clear();
        for(int i = 0; i < problems; i++) {
            tasks.add(new Task());
        }
        list.setAdapter(new RoundListAdapter(tasks, this));

        timerView = (TextView) findViewById(R.id.timer);
        timerView.setText(generateTime());
        passedTime = 0;
        timerEndCon = new Timer();
        timerPerMin = new Timer();
        setTimers();

        isCreated = true;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putBoolean(EXTRA_IS_CREATED, isCreated);
        savedInstanceState.putInt(EXTRA_ALL_TIME, allTimeContest);
        savedInstanceState.putInt(EXTRA_PASSED_TIME, passedTime);
        savedInstanceState.putSerializable(EXTRA_TIME_MODE, timeMode);
        savedInstanceState.putSerializable(EXTRA_TASKS, tasks);
    }

    protected void onClickTime(View view) {
        if (timeMode == TIME_MODE.UP) {
            timeMode = TIME_MODE.DOWN;
        } else {
            timeMode = TIME_MODE.UP;
        }
        timerView.setText(generateTime());
    }

    private class Time {
        public int h, m;
    }

    private Time generatorTime(Integer time, int color) {
        Time t = new Time();
        t.h = time / 60;
        t.m = time % 60;
        timerView.setTextColor(getResources().getColor(color));
        return t;
    }

    private String generateTime() {

        Time a = new Time();

        if (timeMode == TIME_MODE.DOWN) {
            a = generatorTime(allTimeContest - passedTime, R.color.timer_reverse);
        }
        if (timeMode == TIME_MODE.UP) {
            a = generatorTime(passedTime, R.color.timer);
        }

        if (a.m == -1) a.m++;
        return String.format("%d:%02d", a.h, a.m);
    }

    class updateTimer extends TimerTask {

        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    passedTime++;
                    timerView.setText(generateTime());
                }
            });
        }
    }

    class endTimer extends TimerTask {

        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    timerPerMin.cancel();
                    AlertDialog.Builder builder = new AlertDialog.Builder(RoundActivity.this);
                    builder.setTitle(R.string.end_title)
                            .setMessage(R.string.end_mes)
                            .setCancelable(false)
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finishContest();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });
        }
    }

    private final int minToMili = 1000;

    private void setTimers() {
        timerPerMin.schedule(new updateTimer(), 0, minToMili);
        timerEndCon.schedule(new endTimer(), (((allTimeContest-passedTime)*minToMili)));
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder quitDialog = new AlertDialog.Builder(RoundActivity.this);
        quitDialog.setTitle(R.string.end_title2);

        quitDialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishContest();
            }
        });

        quitDialog.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        quitDialog.show();
    }

    private void finishContest() {
        timerPerMin.cancel();
        timerEndCon.cancel();
        Intent ans = new Intent();
        ans.putExtra(EXTRA_TASKS, tasks);
        setResult(RESULT_OK, ans);
        finish();
    }
}