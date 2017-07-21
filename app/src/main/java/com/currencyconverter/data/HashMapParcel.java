package com.currencyconverter.data;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;


public class HashMapParcel implements Parcelable {

    private HashMap<String, String> map;
    private Cursor cursor;

    /**
     * Getter Methods
     */
    public HashMap<String, String> getMap() {
        return map;
    }

    public Cursor getCursor() {
        return cursor;
    }

    public String get(String key) {
        return map.get(key);
    }

    /**
     * Setter Methods
     */

    public void put(String key, String value) {
        map.put(key, value);
    }

    public void setMap(HashMap<String, String> map) {
        this.map = map;
    }

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(map.size());
        for (String s : map.keySet()) {
            dest.writeString(s);
            dest.writeString(map.get(s));
        }
    }

    public void readFromParcel(Parcel in) {
        int count = in.readInt();
        for (int i = 0; i < count; i++) {
            map.put(in.readString(), in.readString());
        }
    }

    public static final Creator<HashMapParcel> CREATOR = new Creator<HashMapParcel>() {
        @Override
        public HashMapParcel createFromParcel(Parcel in) {
            return new HashMapParcel(in);
        }

        @Override
        public HashMapParcel[] newArray(int size) {
            return new HashMapParcel[size];
        }
    };

    public HashMapParcel() {
        map = new HashMap<>();
    }

    public HashMapParcel(Parcel in) {
        map = new HashMap<>();
        readFromParcel(in);
    }
}
