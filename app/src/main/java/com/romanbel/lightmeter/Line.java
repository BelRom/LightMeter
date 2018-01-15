package com.romanbel.lightmeter;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by roman on 12.01.18.
 */

public class Line implements Parcelable, Serializable {
    private String lux;
    private String date;

    public Line(String lux, String date) {
        this.lux = lux;
        this.date = date;
    }

    protected Line(Parcel in) {
        lux = in.readString();
        date = in.readString();
    }

    public static final Creator<Line> CREATOR = new Creator<Line>() {
        @Override
        public Line createFromParcel(Parcel in) {
            return new Line(in);
        }

        @Override
        public Line[] newArray(int size) {
            return new Line[size];
        }
    };

    public String getLux() {
        return lux;
    }

    public void setLux(String lux) {
        this.lux = lux;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(lux);
        parcel.writeString(date);
    }
}
