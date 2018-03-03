package org.horoyami.contestassistant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by victor on 2/27/18.
 */

public class SettingContestActivity extends Activity {

    public static final String EXTRA_CONTEST_PROBLEM = "org.horoyami.contest.number_problems";
    public static final String EXTRA_CONTEST_TIME = "org.horoyami.contest.time";
    public static final String EXTRA_CONTEST_NAME = "org.horoyami.contest.name";

    public static final int CONTEST_REQUEST = 32;

    private EditText problems;
    private EditText time;
    private EditText name;

    int i_problems;
    int i_time;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_new_contest_activity);

        problems = (EditText) findViewById(R.id.setting_contest_number_problems);
        time = (EditText) findViewById(R.id.setting_contest_time);
        name = (EditText) findViewById(R.id.setting_contest_name);
    }

    private int parseEditText(EditText t) {
        int x;
        try {
            String s = t.getText().toString();

            int k = 0;
            while(k < s.length() && s.charAt(k) == '0')
                k++;
            s = s.substring(k);

            if (s.length() > 4)
                x = -1;
            else
                x = Integer.parseInt(t.getText().toString());
        } catch (NumberFormatException e) {
            x = 0;
        }
        return x;
    }

    void makeMassageError(int mes) {
        Toast.makeText(getApplicationContext(), getResources().getText(mes), Toast.LENGTH_SHORT).show();
    }

    protected void onClickStart(View view) {

        i_problems = parseEditText(problems);
        i_time = parseEditText(time);

        if (i_problems == 0) {
            makeMassageError(R.string.enter_error_problem_zero);
            return;
        }
        if (i_problems == -1 || i_problems > 26) {
            makeMassageError(R.string.enter_error_problem_more);
            return;
        }
        if (i_time == 0) {
            makeMassageError(R.string.enter_error_time_zero);
            return;
        }
        if (i_time == -1) {
            makeMassageError(R.string.enter_error_time_more);
            return;
        }

        Intent intent = new Intent(this, RoundActivity.class);
        intent.putExtra(EXTRA_CONTEST_PROBLEM, i_problems);
        intent.putExtra(EXTRA_CONTEST_TIME, i_time);
        startActivityForResult(intent, CONTEST_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CONTEST_REQUEST) {
            if (resultCode == RESULT_OK) {
                data.putExtra(EXTRA_CONTEST_TIME, i_time);
                data.putExtra(EXTRA_CONTEST_NAME, name.getText().toString());
                setResult(RESULT_OK, data);
                Log.e("LOL", "WAH!");
                finish();
            }
        }
    }

}
