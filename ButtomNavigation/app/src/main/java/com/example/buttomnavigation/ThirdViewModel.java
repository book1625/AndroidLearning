package com.example.buttomnavigation;

import androidx.lifecycle.ViewModel;

public class ThirdViewModel extends ViewModel {

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    private float x = 100;
}