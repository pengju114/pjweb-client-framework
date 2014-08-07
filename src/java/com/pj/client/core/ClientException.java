/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.pj.client.core;

/**
 * 这种错误是抛出发到客户端
 * 可以显示给用户看的
 * @author PENGJU
 * email:pengju114@163.com
 * 时间:2012-9-20 10:48:28
 */
public class ClientException extends Exception{
    public static final int REQUEST_ERROR=-10;
    public static final int REQUEST_OK=0;
    public static final int REQUEST_NOT_LOGIN=-1;
    public static final int REQUEST_INVALID_PARAMETER=-2;
    public static final int REQUEST_ILLEGAL_ACCESS=-4;
    
    private int errorCode;

    public ClientException(int errorCode) {
        super();
        this.errorCode=errorCode;
    }

    public ClientException(int errorCode,String message) {
        super(message);
        this.errorCode=errorCode;
    }

    public ClientException(int errorCode,String message, Throwable cause) {
        super(message, cause);
        this.errorCode=errorCode;
    }


    public ClientException(int errorCode,Throwable cause) {
        super(cause);
        this.errorCode=errorCode;
    }
    
    public int getErrorCode(){
        return errorCode;
    }
}
