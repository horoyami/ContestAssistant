package org.horoyami.contestassistant;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by victor on 3/3/18.
 */

public class StatusActivity extends Activity {

    private ArrayList<Context> cons = new ArrayList<>();
    private ArrayList<String> names = new ArrayList<>();
    private boolean isSync = false;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.status_activity);

        cons = (ArrayList<Context>) getIntent().getSerializableExtra(StartActivity.EXTRA_CONS);
        ListView list = (ListView) findViewById(R.id.cons);

        for(Context it : cons) {
            String name = it.getName();
            if (name.equals(""))
                name = getResources().getString(R.string.noname);
            names.add(name);
        }

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names);
        list.setAdapter(adapter);
    }

    protected void onClickSync(View view) {
        isSync = true;
        names.clear();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        if (isSync)
            setResult(RESULT_OK);
        else
            setResult(RESULT_CANCELED);
        finish();
    }
}
