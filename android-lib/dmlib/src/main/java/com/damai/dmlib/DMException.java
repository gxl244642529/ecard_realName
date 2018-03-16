package com.damai.dmlib;

public class DMException extends Exception {


    public DMException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
        // TODO Auto-generated constructor stub
    }

    public DMException(String detailMessage) {
        super(detailMessage);
        // TODO Auto-generated constructor stub
    }

    public DMException(Throwable throwable) {
        super(throwable);
        // TODO Auto-generated constructor stub
    }

    /**
     *
     */
    private static final long serialVersionUID = 6209282583205662830L;

}
