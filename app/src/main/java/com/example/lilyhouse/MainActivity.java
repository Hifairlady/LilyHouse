package com.example.lilyhouse;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.lilyhouse.fragments.FilterDialogFragment;
import com.example.lilyhouse.fragments.MainPageFragment;
import com.example.lilyhouse.scenes.DetailActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "==========MainActivity";

    private MainPageFragment fragment;
    private int[] requestCodes = new int[6];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        initView();
    }

    private FilterDialogFragment.OnFilterApplyListener mOnFilterApplyListener = new FilterDialogFragment.OnFilterApplyListener() {
        @Override
        public void onFilterApply(int[] newRequestCodes) {
            requestCodes = newRequestCodes;
            fragment.filterApplyAction(requestCodes);
            saveRequestCodes(requestCodes);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveRequestCodes(requestCodes);
    }

    private void initData() {
//        int subjectCode = 1, groupCode = 0, statusCode = 0, regionCode = 1, sortCode = 0, pageCode = 0;
        SharedPreferences sharedPreferences = getSharedPreferences("REQUEST_CODES", MODE_PRIVATE);
        requestCodes[0] = sharedPreferences.getInt("SUBJECT_CODE", 0);
        requestCodes[1] = sharedPreferences.getInt("GROUP_CODE", 0);
        requestCodes[2] = sharedPreferences.getInt("STATUS_CODE", 0);
        requestCodes[3] = sharedPreferences.getInt("REGION_CODE", 0);
        requestCodes[4] = sharedPreferences.getInt("SORT_CODE", 0);
        requestCodes[5] = sharedPreferences.getInt("PAGE_CODE", 0);
    }

    private void initView() {
        Toolbar mToolbar = findViewById(R.id.toolbar);
        if (getSupportActionBar() == null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

        fragment = MainPageFragment.newInstance(requestCodes);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_fragment_container, fragment);
        transaction.commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_page_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.sort_menu_item);
        menuItem.setIcon((requestCodes[4] == 0 ? R.drawable.ic_heat_desc : R.drawable.ic_date_desc));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.search_menu_item:
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                startActivity(intent);
                break;

            case R.id.filter_menu_item:
                FilterDialogFragment filterDialogFragment = FilterDialogFragment.newInstance(requestCodes);
                filterDialogFragment.setOnFilterApplyListener(mOnFilterApplyListener);
                filterDialogFragment.show(getSupportFragmentManager(), "FilterDialogFragment");
                break;

            case R.id.sort_menu_item:
                requestCodes[4] = (requestCodes[4] == 0 ? 1 : 0);
                item.setIcon((requestCodes[4] == 0 ? R.drawable.ic_heat_desc : R.drawable.ic_date_desc));
                fragment.switchSortOrder(requestCodes[4]);
                break;

            default:
                break;
        }

        return true;
    }

    private void saveRequestCodes(int[] codes) {
        SharedPreferences sharedPreferences = getSharedPreferences("REQUEST_CODES", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putInt("SUBJECT_CODE", codes[0]);
        editor.putInt("GROUP_CODE", codes[1]);
        editor.putInt("STATUS_CODE", codes[2]);
        editor.putInt("REGION_CODE", codes[3]);
        editor.putInt("SORT_CODE", codes[4]);
        editor.putInt("PAGE_CODE", codes[5]);
        editor.apply();
    }

}
