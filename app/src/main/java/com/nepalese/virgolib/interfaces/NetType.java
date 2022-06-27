package com.nepalese.virgolib.interfaces;

/**
 * Created by Administrator on 2022/6/27.
 * Usage:
 */

public enum NetType {
    NETWORK_NO("No network"),
    NETWORK_ETHERNET("ETHERNET"),
    NETWORK_WIFI("WiFi"),
    NETWORK_2G("2G"),
    NETWORK_3G("3G"),
    NETWORK_4G("4G"),
    NETWORK_5G("5G"),
    NETWORK_UNKNOWN("Unknown");

    private String info;

    NetType(String info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return "NetType{" +
                "info='" + info + '\'' +
                '}';
    }
}
