package com.example.healthapp.Response;

import com.google.gson.annotations.SerializedName;

public class ThongTinThoiTietResponse {
    @SerializedName("location")
    private Location location;

    @SerializedName("astronomy")
    private Astronomy astronomy;

    public Location getLocation() {
        return location;
    }

    public Astronomy getAstronomy() {
        return astronomy;
    }

    public static class Location {
        private String name;
        private String region;
        private String country;
        private double lat;
        private double lon;
        @SerializedName("tz_id")
        private String timezoneId;
        @SerializedName("localtime_epoch")
        private long localtimeEpoch;
        private String localtime;

        public Location(String name, String region, String country, double lat, double lon, String timezoneId, long localtimeEpoch, String localtime) {
            this.name = name;
            this.region = region;
            this.country = country;
            this.lat = lat;
            this.lon = lon;
            this.timezoneId = timezoneId;
            this.localtimeEpoch = localtimeEpoch;
            this.localtime = localtime;
        }

        public Location() {
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

        public String getTimezoneId() {
            return timezoneId;
        }

        public void setTimezoneId(String timezoneId) {
            this.timezoneId = timezoneId;
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

    public static class Astronomy {
        private Astro astro;

        public Astro getAstro() {
            return astro;
        }

        public static class Astro {
            private String sunrise;
            private String sunset;
            private String moonrise;
            private String moonset;
            @SerializedName("moon_phase")
            private String moonPhase;
            @SerializedName("moon_illumination")
            private int moonIllumination;
            @SerializedName("is_moon_up")
            private int isMoonUp;
            @SerializedName("is_sun_up")
            private int isSunUp;

            public Astro() {
            }

            public Astro(String sunrise, String sunset, String moonrise, String moonset, String moonPhase, int moonIllumination, int isMoonUp, int isSunUp) {
                this.sunrise = sunrise;
                this.sunset = sunset;
                this.moonrise = moonrise;
                this.moonset = moonset;
                this.moonPhase = moonPhase;
                this.moonIllumination = moonIllumination;
                this.isMoonUp = isMoonUp;
                this.isSunUp = isSunUp;
            }

            public String getSunrise() {
                return sunrise;
            }

            public void setSunrise(String sunrise) {
                this.sunrise = sunrise;
            }

            public String getSunset() {
                return sunset;
            }

            public void setSunset(String sunset) {
                this.sunset = sunset;
            }

            public String getMoonrise() {
                return moonrise;
            }

            public void setMoonrise(String moonrise) {
                this.moonrise = moonrise;
            }

            public String getMoonset() {
                return moonset;
            }

            public void setMoonset(String moonset) {
                this.moonset = moonset;
            }

            public String getMoonPhase() {
                return moonPhase;
            }

            public void setMoonPhase(String moonPhase) {
                this.moonPhase = moonPhase;
            }

            public int getMoonIllumination() {
                return moonIllumination;
            }

            public void setMoonIllumination(int moonIllumination) {
                this.moonIllumination = moonIllumination;
            }

            public int getIsMoonUp() {
                return isMoonUp;
            }

            public void setIsMoonUp(int isMoonUp) {
                this.isMoonUp = isMoonUp;
            }

            public int getIsSunUp() {
                return isSunUp;
            }

            public void setIsSunUp(int isSunUp) {
                this.isSunUp = isSunUp;
            }
            // Constructors, getters, and setters
        }
    }
}
