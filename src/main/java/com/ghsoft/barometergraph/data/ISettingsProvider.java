package com.ghsoft.barometergraph.data;

/**
 * Created by brian on 8/4/15.
 */
public interface ISettingsProvider {
    String getUnit();
    int getAverageSize();

    void setUnit(String unit);
    void setAverageSize(int size);

}
