package org.horoyami.contestassistant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by victor on 2/27/18.
 *
 */

public class RoundListAdapter extends BaseAdapter {

    private final ArrayList<Task> data;
    private final Context context;
    private final CharSequence[] actions;

    public RoundListAdapter(ArrayList<Task> data, Context context) {
        this.data = data;
        this.context = context;
        this.actions = context.getResources().getTextArray(R.array.actions);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.task_item, viewGroup, false);

        final TextView name = (TextView) view.findViewById(R.id.task_name);
        final TextView action = (TextView) view.findViewById(R.id.task_action);
        final ImageView menu = (ImageView) view.findViewById(R.id.task_menu);
        final LinearLayout lay = (LinearLayout) view.findViewById(R.id.lay_item);

        name.setText(String.format("%s %s", context.getResources().getText(R.string.problem), String.valueOf((char)('A' + i))));
        action.setText(actions[data.get(i).getStep()]);
        action.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                addAction(data.get(i).getStep(), i, lay);
                data.get(i).incStep();
                action.setText(actions[data.get(i).getStep()]);
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenu(view, i, lay);
            }
        });

        for(int j = 0; j < data.get(i).sizeStep(); j++) {
            updateLay(data.get(i).getTimeStep(j), lay, data.get(i).getActStep(j));
        }

        return view;
    }

    private void updateLay(int time, LinearLayout lay, int i) {
        final int h = time / 60;
        final int m = time % 60;
        final CharSequence d = actions[i];

        TextView newAct = new TextView(context);
        newAct.setText(String.format("\u00B7 %02d:%02d %s", h, m, d));
        newAct.setPadding(70, 0 ,0,0);
        lay.addView(newAct, lay.getChildCount() - 1);
    }

    private void addAction(int k, int i, LinearLayout lay) {
        final int time = RoundActivity.getTime();

        final Task item = data.get(i);
        item.add(time, k);

        updateLay(time, lay, k);
    }

    private void showMenu(View view,final int i,final LinearLayout lay) {
        final PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.inflate(R.menu.popupmenu);

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int k = -1;
                switch (menuItem.getItemId()) {
                    case R.id.menu1: k = 0; break;
                    case R.id.menu2: k = 1; break;
                    case R.id.menu3: k = 2; break;
                    case R.id.menu4: k = 3; break;
                    case R.id.menu5: k = 4; break;
                    case R.id.menu6: k = 5; break;
                    case R.id.menu7: k = 6; break;
                }
                if (k == -1)
                    return false;
                addAction(k, i, lay);
                return true;
            }
        });

        popupMenu.show();
    }

}
