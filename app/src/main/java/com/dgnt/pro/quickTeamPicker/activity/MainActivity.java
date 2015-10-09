package com.dgnt.pro.quickTeamPicker.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.dgnt.pro.quickTeamPicker.R;
import com.dgnt.pro.quickTeamPicker.holder.Person;
import com.dgnt.pro.quickTeamPicker.holder.Team;
import com.dgnt.pro.quickTeamPicker.util.TeamPicker;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);


        List<Person> personList = new ArrayList<>();
        personList.add(new Person("Andrew",4));
        personList.add(new Person("Andrew2",3));
        personList.add(new Person("Andrew3",2));
        personList.add(new Person("Andrew4",1));

        List<Team> teams = TeamPicker.pickTeams(personList, 2, true);


        final MainItem[] mainItems = new MainItem[]{
                new MainItem(getResources().getString(R.string.quickStart), R.drawable.place_holder),
                new MainItem(getResources().getString(R.string.fullStart), R.drawable.place_holder),
                new MainItem(getResources().getString(R.string.manage), R.drawable.place_holder),
                new MainItem(getResources().getString(R.string.load), R.drawable.place_holder)
        };
        ArrayAdapter<MainItem> adapter = new ArrayAdapter<MainItem>(this, R.layout.main_list_item, mainItems) {
            @Override
            public View getView(int position, View convertView,
                                ViewGroup parent) {

                View view = convertView;

                if (view == null) {
                    LayoutInflater vi;
                    vi = LayoutInflater.from(getContext());
                    view = vi.inflate(R.layout.main_list_item, null);
                }

                TextView textView = (TextView) view.findViewById(R.id.mainItem_tv);
                ImageView imageView = (ImageView) view.findViewById(R.id.mainItem_iv);

                textView.setText(getItem(position).getTitle());
                imageView.setImageResource(getItem(position).getImageId());

                return view;
            }
        };
        final GridView main_gridView = (GridView) findViewById(R.id.main_gridView);
        main_gridView.setAdapter(adapter);
        main_gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
//                        startActivity(new Intent(getApplicationContext(), ChooseTemplateActivity.class));

                        break;
                    case 1:
                    default:
//                        startActivity(new Intent(getApplicationContext(), ChooseContractActivity.class));

                        break;
                }


            }
        });

        ViewTreeObserver vto = main_gridView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                main_gridView.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                int maxHeight = 0;
                final int size = main_gridView.getChildCount();
                for (int i = 0; i < size; i++) {
                    ViewGroup gridChild = (ViewGroup) main_gridView.getChildAt(i);
                    maxHeight = gridChild.getHeight() > maxHeight ? gridChild.getHeight() : maxHeight;
                }
                ViewGroup.LayoutParams layoutParams = main_gridView.getLayoutParams();
                layoutParams.height = maxHeight*2;
                main_gridView.setLayoutParams(layoutParams);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    private static class MainItem {
        private String title;
        private int imageId;

        public MainItem(final String title, final int imageId) {
            this.title = title;
            this.imageId = imageId;
        }

        public String getTitle() {
            return title;
        }

        public int getImageId() {
            return imageId;
        }
    }
}
