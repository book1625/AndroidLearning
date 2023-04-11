package com.example.buttomnavigation;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.telephony.BarringInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.time.temporal.ValueRange;

public class FourthFragment extends Fragment {

    private TabLayout pagerTab;
    private ViewPager2 viewPager;

    public FourthFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fourth, container, false);

        pagerTab = view.findViewById(R.id.viewPagerTab);
        viewPager = view.findViewById(R.id.viewPager);

        viewPager.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                switch (position) {
                    case 0:
                        return new FirstFragment();
                    case 1:
                        return new SecondFragment();
                    case 2:
                        return new ThirdFragment();
                    default:
                        return null;
                }
            }

            @Override
            public int getItemCount() {
                return 3;
            }
        });

        TabLayoutMediator mediator = new TabLayoutMediator(pagerTab, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("旋轉");
                    break;
                case 1:
                    tab.setText("縮放");
                    break;
                case 2:
                    tab.setText("平移");
                    break;
                default:
                    break;
            }
        });

        mediator.attach();

        return view;
    }
}