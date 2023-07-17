package net.hafiz.mymap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Maklumat {

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("hazardType")
    @Expose
    public String hazardType;
    @SerializedName("hazardDate")
    @Expose
    public String hazardDate;
    @SerializedName("hazardReporter")
    @Expose
    public String hazardReporter;
    @SerializedName("hazardTime")
    @Expose
    public String hazardTime;
    @SerializedName("hazardLocation")
    @Expose
    public String hazardLocation;
    @SerializedName("lat")
    @Expose
    public String lat;
    @SerializedName("lng")
    @Expose
    public String lng;

}
