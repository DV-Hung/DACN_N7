package com.haui.bookinghotel.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestResponse<T> {
    private int statusCode;
    private String error;
    //message co the la String hoac Arraylist
    private Object message;
    private T data;

}
