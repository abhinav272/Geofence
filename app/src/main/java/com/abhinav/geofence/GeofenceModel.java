package com.abhinav.geofence;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by abhinav.sharma on 10/4/2016.
 */
public class GeofenceModel implements Parcelable {
    private double latitude;
    private double longitude;
    private float radius;
    private String id;

    public GeofenceModel(double latitude, double longitude, float radius, String id) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.radius = radius;
        this.id = id;
    }

    public static class List extends ArrayList<GeofenceModel>{}

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
        dest.writeFloat(this.radius);
        dest.writeString(this.id);
    }

    public GeofenceModel() {
    }

    protected GeofenceModel(Parcel in) {
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        this.radius = in.readFloat();
        this.id = in.readString();
    }

    public static final Creator<GeofenceModel> CREATOR = new Creator<GeofenceModel>() {
        @Override
        public GeofenceModel createFromParcel(Parcel source) {
            return new GeofenceModel(source);
        }

        @Override
        public GeofenceModel[] newArray(int size) {
            return new GeofenceModel[size];
        }
    };
}
