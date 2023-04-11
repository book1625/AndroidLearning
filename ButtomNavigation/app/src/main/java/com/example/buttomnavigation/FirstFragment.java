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

public class FirstFragment extends Fragment {

    private FirstViewModel mViewModel;

    private ImageView imageView;

    public static FirstFragment newInstance() {
        return new FirstFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);
        imageView = view.findViewById(R.id.imageView);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(FirstViewModel.class);

        //初始憶中的位置
        imageView.setRotation(mViewModel.getRotation());

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(imageView, "rotation", 0, 0);
        objectAnimator.setDuration(500);
        imageView.setOnClickListener(view1 -> {
            if (objectAnimator.isRunning()) return; //避免連點發生，因為新進來的動畫會取代原本存在的動畫
            mViewModel.setRotation(mViewModel.getRotation() + 90);
            objectAnimator.setFloatValues(imageView.getRotation(), imageView.getRotation() + 90);
            objectAnimator.start();
        });
    }
}