package com.pawan.files_cleaner.tabs;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.pawan.files_cleaner.DataHolder;

public class TabsAdapter_test extends FragmentStatePagerAdapter {
    private String cachePath;
    private String downloadPath;
    private String category;

    TabsAdapter_test(FragmentManager fm, String category, String cachePath, String downloadPath) {
        super(fm);
        this.category = category;
        this.cachePath = cachePath;
        this.downloadPath = downloadPath;
    }

    @NonNull
    @Override
    public Fragment getItem(int i) {
        switch (category) {
            default:
            case DataHolder.STATUS:
                switch (i) {
                    default:
                    case 0:
                        return FilesFragment_test.newInstance(DataHolder.STATUS, cachePath);
                    case 1:
                        return FilesFragment_test.newInstance(DataHolder.STATUS, downloadPath);
                }
        }

    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Cache Files";
            case 1:
                return "Download Files";
            default:
                return "";
        }
    }
}
