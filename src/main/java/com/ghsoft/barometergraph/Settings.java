package com.ghsoft.barometergraph;

import android.content.Context;
import android.content.SharedPreferences;

import com.ghsoft.barometergraph.data.ISettingsProvider;

/**
 * Created by brian on 8/4/15.
 */
public class Settings implements ISettingsProvider {
    SharedPreferences mPrefs;

    public Settings(Context context) {
        mPrefs = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
    }

    @Override
    public String getUnit() {
        return mPrefs.getString("setting_unit", "psi");
    }

    @Override
    public int getAverageSize() {
        return mPrefs.getInt("setting_average_size", 10);
    }

    @Override
    public void setUnit(String val) {
        mPrefs.edit().putString("setting_unit", val).apply();
    }

    @Override
    public void setAverageSize(int val) {
        mPrefs.edit().putInt("setting_average_size", val).apply();
    }
}
