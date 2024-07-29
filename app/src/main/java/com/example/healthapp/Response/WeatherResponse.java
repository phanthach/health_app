package com.example.healthapp.Response;

import com.google.gson.annotations.SerializedName;

public class WeatherResponse {
    @SerializedName("location")
    private Location location;

    @SerializedName("current")
    private CurrentWeather current;

    public Location getLocation() {
        return location;
    }

    public CurrentWeather getCurrent() {
        return current;
    }

    public static class Location {
        private String name;
        private String region;
        private String country;
        private double lat;
        private double lon;
        @SerializedName("localtime_epoch")
        private long localtimeEpoch;
        private String localtime;

        public Location() {
        }

        public Location(String name, String region, String country, double lat, double lon, long localtimeEpoch, String localtime) {
            this.name = name;
            this.region = region;
            this.country = country;
            this.lat = lat;
            this.lon = lon;
            this.localtimeEpoch = localtimeEpoch;
            this.localtime = localtime;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLon() {
            return lon;
        }

        public void setLon(double lon) {
            this.lon = lon;
        }

        public long getLocaltimeEpoch() {
            return localtimeEpoch;
        }

        public void setLocaltimeEpoch(long localtimeEpoch) {
            this.localtimeEpoch = localtimeEpoch;
        }

        public String getLocaltime() {
            return localtime;
        }

        public void setLocaltime(String localtime) {
            this.localtime = localtime;
        }
    }

    public static class CurrentWeather {
        @SerializedName("last_updated")
        private String lastUpdated;
        @SerializedName("temp_c")
        private float tempC;
        private Condition condition;
        private float uv;
        private int humidity;
        private float wind_kph;

        public CurrentWeather() {
        }

        public CurrentWeather(String lastUpdated, float tempC, Condition condition, float uv, int humidity, float wind_kph) {
            this.lastUpdated = lastUpdated;
            this.tempC = tempC;
            this.condition = condition;
            this.uv = uv;
            this.humidity = humidity;
            this.wind_kph = wind_kph;
        }

        public int getHumidity() {
            return humidity;
        }

        public void setHumidity(int humidity) {
            this.humidity = humidity;
        }

        public float getWind_kph() {
            return wind_kph;
        }

        public void setWind_kph(float wind_kph) {
            this.wind_kph = wind_kph;
        }

        public String getLastUpdated() {
            return lastUpdated;
        }

        public void setLastUpdated(String lastUpdated) {
            this.lastUpdated = lastUpdated;
        }

        public float getTempC() {
            return tempC;
        }

        public void setTempC(float tempC) {
            this.tempC = tempC;
        }

        public Condition getCondition() {
            return condition;
        }

        public void setCondition(Condition condition) {
            this.condition = condition;
        }

        public float getUv() {
            return uv;
        }

        public void setUv(float uv) {
            this.uv = uv;
        }
    }

    public static class Condition {
        private String text;

        public Condition() {
        }

        public Condition(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
