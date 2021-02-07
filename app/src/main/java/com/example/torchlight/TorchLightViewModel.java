package com.example.torchlight;

import android.util.Log;

import androidx.lifecycle.ViewModel;

public class TorchLightViewModel extends ViewModel {

    private Boolean isTorchOn = false;
    private int toggleImage;


    public void setIsTorchOn(Boolean torchOn) {
        isTorchOn = torchOn;

    }

    public Boolean getIsTorchOn() {
        return isTorchOn;
    }

    public void setToggleImage(int toggleImage) {
        this.toggleImage = toggleImage;
    }

    public int getToggleImage() {
        return toggleImage;
    }
}
