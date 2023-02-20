package com.c103.dolbom.user.common;

public interface JwtProperties {

    String SECRET_KEY = "asdfa1!r1rf#6zcwmo&1072%4r624rfja;awef24rq][.vrkafr!*ja$2j#via;erfmcqjedzdfq$";
    long ACCESS_EXP_TIME = 1000 * 60 * 60 * 5; // 5시간

    long REFRESH_EXP_TIME = 1000 * 60 * 60 * 30; // 30시간
    String ACCESS_HEADER_STRING = "access-token";
    String REFRESH_HEADER_STRING = "refresh-token";
    String TOKEN_HEADER_PREFIX = "Bearer ";

}
