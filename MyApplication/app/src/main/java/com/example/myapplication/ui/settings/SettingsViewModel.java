package com.example.myapplication.ui.settings;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SettingsViewModel extends ViewModel {

    private MutableLiveData<String> ip;
    private MutableLiveData<Integer> port;

    public SettingsViewModel() {
        ip = new MutableLiveData<>();
        port = new MutableLiveData<>();
        ip.setValue("old_ip");
        port.setValue(5555);
    }

    public MutableLiveData<String> getIP() { return ip; }

    public LiveData<Integer> getPort() {
        return port;
    }

    public void set_ip(MutableLiveData<String> i){
        ip=i;
    }
}