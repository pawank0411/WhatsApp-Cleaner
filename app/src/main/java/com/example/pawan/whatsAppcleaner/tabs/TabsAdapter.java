package com.example.pawan.whatsAppcleaner.tabs;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.pawan.whatsAppcleaner.DataHolder;

public class TabsAdapter extends FragmentStatePagerAdapter {
    private String receivedPath;
    private String sentPath;
    private String category;

    TabsAdapter(FragmentManager fm, String category, String receivedPath, String sentPath) {
        super(fm);
        this.category = category;
        this.receivedPath = receivedPath;
        this.sentPath = sentPath;
    }

    @Override
    public Fragment getItem(int i) {
        switch (category) {
            default:
            case DataHolder.IMAGE:
                switch (i) {
                    default:
                    case 0:
                        return FilesFragment.newInstance(DataHolder.IMAGE, receivedPath);
                    case 1:
                        return FilesFragment.newInstance(DataHolder.IMAGE, sentPath);
                }
            case DataHolder.DOCUMENT:
                switch (i) {
                    default:
                    case 0:
                        return FilesFragment.newInstance(DataHolder.DOCUMENT, receivedPath);
                    case 1:
                        return FilesFragment.newInstance(DataHolder.DOCUMENT, sentPath);
                }
            case DataHolder.VIDEO:
                switch (i) {
                    default:
                    case 0:
                        return FilesFragment.newInstance(DataHolder.VIDEO, receivedPath);
                    case 1:
                        return FilesFragment.newInstance(DataHolder.VIDEO, sentPath);
                }
            case DataHolder.AUDIO:
                switch (i) {
                    default:
                    case 0:
                        return FilesFragment.newInstance(DataHolder.AUDIO, receivedPath);
                    case 1:
                        return FilesFragment.newInstance(DataHolder.AUDIO, sentPath);
                }
            case DataHolder.GIF:
                switch (i) {
                    default:
                    case 0:
                        return FilesFragment.newInstance(DataHolder.GIF, receivedPath);
                    case 1:
                        return FilesFragment.newInstance(DataHolder.GIF, sentPath);
                }
            case DataHolder.WALLPAPER:
                switch (i) {
                    default:
                    case 0:
                        return FilesFragment.newInstance(DataHolder.WALLPAPER, receivedPath);
                    case 1:
                        return FilesFragment.newInstance(DataHolder.WALLPAPER, sentPath);
                }
            case DataHolder.VOICE:
                switch (i){
                    default:
                    case 0:
                        return FilesFragment.newInstance(DataHolder.VOICE, receivedPath);
                    case 1:
                        return FilesFragment.newInstance(DataHolder.VOICE, sentPath);
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
                return "Recieved Files";
            case 1:
                return "Sent Files";
            default:
                return "";
        }
    }
}
