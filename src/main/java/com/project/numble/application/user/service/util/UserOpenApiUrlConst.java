package com.project.numble.application.user.service.util;

public final class UserOpenApiUrlConst {

    public static final String GOOGLE_GEOLOCATION_COORDINATES_URL
            = "https://www.googleapis.com/geolocation/v1/geolocate?key=";
    public static final String KAKAO_LOCAL_COORDINATES_ADDRESS_URL
        = "https://dapi.kakao.com/v2/local/geo/coord2regioncode.json?x=";
    public static final String KAKAO_LOCAL_SEARCH_ADDRESS_URL
        = "https://dapi.kakao.com/v2/local/search/address.json?query=";

    private UserOpenApiUrlConst() {
    }
}
