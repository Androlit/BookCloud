/*
 * Copyright (C) 2017 Book Cloud
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.androlit.bookcloud.view.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androlit.bookcloud.R;
import com.androlit.bookcloud.view.adapters.HomePagerAdapter;
import com.androlit.bookcloud.view.fragment.AvailableBookListFragment;
import com.androlit.bookcloud.view.fragment.SearchBookListFragment;
import com.androlit.bookcloud.view.navigator.Navigator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    List<Fragment> mFragmentListOfViewpager;
    private FirebaseAuth mAuth;
    private Button mSignIn;
    private TextView mTvEmail;
    private TextView mTvFullName;
    // viewpager
    private ViewPager mHomePager;
    private HomePagerAdapter mHomePagerAdapter;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, HomeActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        // home pager view initializing
        mFragmentListOfViewpager = new ArrayList<>();


        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            searchAvailableBooks(query);
        }else{
            mFragmentListOfViewpager.add(new AvailableBookListFragment());
            mHomePagerAdapter = new HomePagerAdapter(getSupportFragmentManager(), mFragmentListOfViewpager);
            mHomePager = (ViewPager) findViewById(R.id.home_pager);
            mHomePager.setAdapter(mHomePagerAdapter);
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAuth.getCurrentUser() != null)
                    Navigator.navigateToAddBook(HomeActivity.this);
                else
                    Toast.makeText(HomeActivity.this, "Loging to add book", Toast.LENGTH_SHORT).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        setNavigationView();

        mAuth = FirebaseAuth.getInstance();
    }


    // search books
    private void searchAvailableBooks(String query) {
//        remember:
//        https://developer.android.com/guide/topics/search/search-dialog.html
//        If your data is stored online, then the perceived search performance might
//        be inhibited by the user's data connection. You might want to display
//        a spinning progress wheel until your search returns. See android.net
//        for a reference of network APIs and Creating a Progress Dialog
//        for information about a to display a progress wheel.
        SearchBookListFragment fragment = new SearchBookListFragment();
        fragment.setQuery(query);
        mFragmentListOfViewpager.add(fragment);
        mHomePagerAdapter = new HomePagerAdapter(getSupportFragmentManager(), mFragmentListOfViewpager);
        mHomePager = (ViewPager) findViewById(R.id.home_pager);
        mHomePager.setAdapter(mHomePagerAdapter);
    }

    private void setNavigationView() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View navHeader = navigationView.getHeaderView(0);
        mSignIn = (Button) navHeader.findViewById(R.id.btn_log_reg);
        mSignIn.setOnClickListener(this);
        mTvEmail = (TextView) navHeader.findViewById(R.id.text_user_identity);
        mTvFullName = (TextView) navHeader.findViewById(R.id.text_user_full_name);
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkIfUserSignedIn();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the options menu from XML
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem menuItem = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_sign_out) {
            signOutUser();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.action_my_books) {

        } else if (id == R.id.action_messages) {

        } else if (id == R.id.action_history) {

        } else if (id == R.id.action_manage_profile) {

        } else if (id == R.id.action_settings) {

        } else if (id == R.id.action_about) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {
        final int id = view.getId();

        switch (id) {
            case R.id.btn_log_reg:
                Navigator.navigateToAuth(this);
                break;
            default:
                break;
        }
    }

    private void checkIfUserSignedIn() {
        if (mAuth.getCurrentUser() != null) {
            updateNavHeader(true);
        }
    }

    private void signOutUser() {
        mAuth.signOut();
        updateNavHeader(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Navigator.AUTH_REQUEST && resultCode == RESULT_OK) {
            checkIfUserSignedIn();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void updateNavHeader(boolean signedIn) {
        if (mSignIn.getVisibility() == View.VISIBLE && signedIn) {
            mSignIn.setVisibility(View.GONE);
            FirebaseUser user = mAuth.getCurrentUser();

            assert user != null;
            if (user.getEmail() != null) {
                mTvEmail.setText(mAuth.getCurrentUser().getEmail());
            }
            mTvEmail.setVisibility(View.VISIBLE);

            if (mAuth.getCurrentUser().getDisplayName() != null)
                mTvFullName.setText(mAuth.getCurrentUser().getDisplayName());
            mTvFullName.setVisibility(View.VISIBLE);
        } else if (!signedIn) {
            mTvEmail.setVisibility(View.GONE);
            mTvFullName.setVisibility(View.GONE);
            mSignIn.setVisibility(View.VISIBLE);
        }
    }
}
