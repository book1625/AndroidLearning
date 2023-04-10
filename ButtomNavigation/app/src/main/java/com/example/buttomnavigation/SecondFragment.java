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

public class SecondFragment extends Fragment {

    private SecondViewModel mViewModel;

    private ImageView imageView;

    public static SecondFragment newInstance() {
        return new SecondFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // TODO: Use the ViewModel
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SecondViewModel.class);
        imageView = view.findViewById(R.id.imageView);

        //初始
        imageView.setScaleX(mViewModel.getScale());
        imageView.setScaleY(mViewModel.getScale());

        ObjectAnimator objectAnimatorX = ObjectAnimator.ofFloat(imageView, "scaleX", 0, 0);
        ObjectAnimator objectAnimatorY = ObjectAnimator.ofFloat(imageView, "scaleY", 0, 0);
        objectAnimatorX.setDuration(500);
        objectAnimatorY.setDuration(500);
        imageView.setOnClickListener(view1 -> {
            if (objectAnimatorX.isRunning()) return; //避免連點發生，因為新進來的動畫會取代原本存在的動畫
            if (objectAnimatorY.isRunning()) return;
            mViewModel.setScale(mViewModel.getScale() + 0.1f);
            objectAnimatorX.setFloatValues(imageView.getScaleX() + 0.1f);
            objectAnimatorY.setFloatValues(imageView.getScaleY() + 0.1f);
            objectAnimatorX.start();
            objectAnimatorY.start();
        });
    }
}