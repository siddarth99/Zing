package com.example.wcc;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

class CollectionAdapter extends FragmentStateAdapter {
    // When requested, this adapter returns a DemoObjectFragment,
    // representing an object in the collection.



    public CollectionAdapter(FragmentActivity fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position)
        {
            case 0:
                RequestsFragment requestsFragment=new RequestsFragment();
                return  requestsFragment;
            case 1:
                ChatFragment chatFragment=new ChatFragment();
                return chatFragment;
            case 2:
                Calls calls=new Calls();
                return calls;
            default:
                return null;
        }
    }




    @Override
    public int getItemCount() {
        return 3;
    }
}




