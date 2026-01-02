package com.earthguard.earthquake.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UsgsEarthquakeResponse {

    private String type;

    private Metadata metadata;

    private List<Feature> features;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Metadata {
        private Long generated;
        private String url;
        private String title;
        private Integer status;
        private String api;
        private Integer count;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Feature {
        private String type;
        private Properties properties;
        private Geometry geometry;
        private String id;

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Properties {
            private Double mag;
            private String place;
            private Long time;
            private Long updated;
            private Integer tz;
            private String url;
            private String detail;
            private Integer felt;
            private Double cdi;
            private Double mmi;
            private String alert;
            private String status;
            private Integer tsunami;
            private Integer sig;
            private String net;
            private String code;
            private String ids;
            private String sources;
            private String types;
            private Integer nst;
            private Double dmin;
            private Double rms;
            private Double gap;
            private String magType;
            private String type;
            private String title;
        }

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Geometry {
            private String type;
            private List<Double> coordinates; // [longitude, latitude, depth]
        }
    }
}