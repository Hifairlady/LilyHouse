package com.example.lilyhouse;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.lilyhouse.fragments.FilterDialogFragment;
import com.example.lilyhouse.fragments.MainPageFragment;
import com.example.lilyhouse.scenes.DetailActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "==========MainActivity";

    private MainPageFragment fragment;
    private FilterDialogFragment filterDialogFragment;
    private int curSortOrder = 0;
    private int[] requestCodes = new int[6];
    private MenuItem menuItem;

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
            fragment.filterApplyAction(newRequestCodes);
            SharedPreferences sharedPreferences = getSharedPreferences("REQUEST_CODES", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.putInt("SUBJECT_CODE", newRequestCodes[0]);
            editor.putInt("GROUP_CODE", newRequestCodes[1]);
            editor.putInt("STATUS_CODE", newRequestCodes[2]);
            editor.putInt("REGION_CODE", newRequestCodes[3]);
            editor.putInt("SORT_CODE", newRequestCodes[4]);
            editor.putInt("PAGE_CODE", newRequestCodes[5]);
            editor.apply();
            Log.d(TAG, "onFilterApply: request codes saved!");
        }
    };

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
        getMenuInflater().inflate(R.menu.main_menu, menu);
        menuItem = menu.findItem(R.id.sort_menu_item);
        menuItem.setIcon(curSortOrder == 0 ? R.drawable.ic_heat_desc : R.drawable.ic_date_desc);
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
                filterDialogFragment = FilterDialogFragment.newInstance(requestCodes);
                filterDialogFragment.setOnFilterApplyListener(mOnFilterApplyListener);
                filterDialogFragment.show(getSupportFragmentManager(), "FilterDialogFragment");
                break;

            case R.id.sort_menu_item:
                curSortOrder = (curSortOrder == 0 ? 1 : 0);
                item.setIcon((curSortOrder == 0 ? R.drawable.ic_date_desc : R.drawable.ic_heat_desc));
                fragment.switchSortOrder(curSortOrder);
                break;

            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        filterDialogFragment.dismiss();
    }
}
