package com.example.tekwan.popularmovies.DataModel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tekwan on 7/29/2017.
 */

public class Trailer implements Parcelable {

    private String id;
    private String type;
    private String key;
    private String site;
    private String name;


    public Trailer(String id, String type, String key, String site, String name) {
        this.id = id;
        this.type = type;
        this.key = key;
        this.site = site;
        this.name = name;
    }
    private Trailer(Parcel in){
        this.id = in.readString();
        this.type = in.readString();
        this.key = in.readString();
        this.site = in.readString();
        this.name = in.readString();
    }


    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.type);
        dest.writeString(this.key);
        dest.writeString(this.site);
        dest.writeString(this.name);
    }
    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };

}
