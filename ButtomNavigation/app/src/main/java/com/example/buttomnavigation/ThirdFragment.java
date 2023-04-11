package com.example.buttomnavigation;

import androidx.lifecycle.ViewModelProvider;

import android.animation.ObjectAnimator;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.Random;

public class ThirdFragment extends Fragment {

    private ThirdViewModel mViewModel;

    private ImageView imageView;

    public static ThirdFragment newInstance() {
        return new ThirdFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_third, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ThirdViewModel.class);
        imageView = view.findViewById(R.id.imageView);

        //初始
        imageView.setX(mViewModel.getX());

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(imageView, "x", 0, 0);
        objectAnimator.setDuration(500);
        imageView.setOnClickListener(view1 -> {
            if (objectAnimator.isRunning()) return; //避免連點發生，因為新進來的動畫會取代原本存在的動畫
            float dx = new Random().nextBoolean() ? 50 : -50; //隨機左右
            mViewModel.setX(mViewModel.getX() + dx);
            objectAnimator.setFloatValues(imageView.getX(), imageView.getX() + dx);
            objectAnimator.start();
        });
    }
}