package com.example.pawan.whatsAppcleaner.tabs.Documents;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toolbar;

import com.example.pawan.whatsAppcleaner.R;

import java.util.ArrayList;
import java.util.List;

public class AndroidTabLayoutActivity_doc extends AppCompatActivity {


    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab);

//
//        TabHost tabHost = getTabHost();
//        tabHost.getTabWidget().setStripEnabled(false);
//        tabLayout = findViewById(R.id.tabLayout);
//
//        TabHost.TabSpec recived_doc = tabHost.newTabSpec("Photos");
//        recived_doc.setIndicator(Html.fromHtml("<small>Recieved Files</small>"));
//
//        Intent recvd = new Intent(AndroidTabLayoutActivity_doc.this, Doc_rec.class);
//        recived_doc.setContent(recvd);
//
//
//        TabHost.TabSpec sent_doc = tabHost.newTabSpec("Sent");
//        sent_doc.setIndicator(Html.fromHtml("<font color = '#C103A9F4'>Sent Files</font>"));
//
//        Intent snt = new Intent(AndroidTabLayoutActivity_doc.this, Doc_sent.class);
//        sent_doc.setContent(snt);
//
//        tabHost.addTab(recived_doc);
//        tabHost.addTab(sent_doc);
//    }

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSelectedTabIndicatorHeight(0);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new RecFragment(), "Recieved Files");
        adapter.addFragment(new SentFragment(), "Sent Files");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}


