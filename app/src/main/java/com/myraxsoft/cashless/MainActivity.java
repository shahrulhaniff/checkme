package com.myraxsoft.cashless;

import android.content.res.Configuration;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.myraxsoft.cashless.Adapter.CustomExpandableListAdapter;
import com.myraxsoft.cashless.Helper.FragmentNavigationManager;
import com.myraxsoft.cashless.Interface.NavigationManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;
    private String[] items;
    private ExpandableListView expandableListView;
    private ExpandableListAdapter adapter;
    private List<String> lstTitle;
    private Map<String,List<String>> lstChild;
    private NavigationManager navigationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init view
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();
        expandableListView = (ExpandableListView)findViewById(R.id.navList);
        navigationManager = FragmentNavigationManager.getmInstance(this);

        initItems();
        View listHeaderView = getLayoutInflater().inflate(R.layout.nav_header, null, false);
        expandableListView.addHeaderView(listHeaderView);

        gendata();
        addDrawersItem();
        setupDrawer();

        if (savedInstanceState == null)
            selectFirstItemAsDefault();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Cashless");

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void selectFirstItemAsDefault() {
        if (navigationManager != null)
        {
            String firstItem = lstTitle.get(0);
            navigationManager.showFragment(firstItem);
            getSupportActionBar().setTitle(firstItem);
        }
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open, R.string.close)
        {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //getSupportActionBar().setTitle("Cashelusse");
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                //getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu();
            }
        };
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void addDrawersItem() {
        adapter = new CustomExpandableListAdapter(this,lstTitle,lstChild);
        expandableListView.setAdapter(adapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                getSupportActionBar().setTitle(lstTitle.get(groupPosition).toString()); //Set title for toolbar when open menu
            }

        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                getSupportActionBar().setTitle("Cashhhhlesssa");
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                //change fragment when click on item
                String selectedItem = ((List)(lstChild.get(lstTitle.get(groupPosition))))
                        .get(childPosition).toString();
                getSupportActionBar().setTitle(selectedItem);

                if(items[0].equals(lstTitle.get(groupPosition)))
                    navigationManager.showFragment(selectedItem);
                else
                    throw new IllegalArgumentException("Not supported fragment");

                mDrawerLayout.closeDrawer(GravityCompat.START);

                return false;
            }
        });
    }

    private void gendata() {
        List<String> title = Arrays.asList("Android Programming","Master Programming","Apple Code");
        List<String> childitem = Arrays.asList( "Beginner","Medium","Professional","Advance");

        lstChild = new TreeMap<>();
        lstChild.put(title.get(0),childitem);
        lstChild.put(title.get(1),childitem);
        lstChild.put(title.get(2),childitem);

        lstTitle = new ArrayList<>(lstChild.keySet());
    }

    private void initItems() {
        items = new String[]{"Android Programming","Master Programming","Apple Code"};
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (mDrawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }
}
