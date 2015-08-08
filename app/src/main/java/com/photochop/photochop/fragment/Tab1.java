package com.photochop.photochop.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.photochop.photochop.R;
import com.photochop.photochop.base.BaseActivity;
import com.photochop.photochop.base.BaseFragment;

/**
 * Created by Vaughn on 8/8/15.
 */
public class Tab1 extends BaseFragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_tab2, container, false);
        return v;
    }
}
