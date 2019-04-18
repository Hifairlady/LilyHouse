package com.example.lilyhouse;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.lilyhouse.fragments.MainPageFragment;
import com.example.lilyhouse.scenes.DetailActivity;

public class MainActivity extends AppCompatActivity {

    private MainPageFragment fragment;
    private int curSortOrder = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar mToolbar = findViewById(R.id.toolbar);
        if (getSupportActionBar() == null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

        fragment = MainPageFragment.newInstance(null, null);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_fragment_container, fragment);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.search_menu_item:
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                startActivity(intent);
                break;

            case R.id.filter_menu_item:
                break;

            case R.id.sort_menu_item:
                curSortOrder = (curSortOrder == 0 ? 1 : 0);
                item.setIcon((curSortOrder == 0 ? R.drawable.ic_heat_desc : R.drawable.ic_date_desc));
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
