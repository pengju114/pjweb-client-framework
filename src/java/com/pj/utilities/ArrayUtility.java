/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.pj.utilities;

/**
 *
 * @author PENGJU
 * 时间:2012-7-15 13:40:54
 */
public class ArrayUtility {
    public static String join(Object[] array,String separator){
        StringBuilder sb=new StringBuilder();
        if (array!=null) {
            for (Object object : array) {
                sb.append(object).append(separator);
            }
            
            if (array.length>0) {
                sb.delete(sb.length()-separator.length(), sb.length());
            }
        }
        return  sb.toString();
    }
    
    public static String join(Object val,int count ,String separator){
        StringBuilder sb=new StringBuilder();
        for (int i = 0; i <count; i++) {
            sb.append(val).append(separator);
        }
        if (count>0) {
            sb.delete(sb.length()-separator.length(), sb.length());
        }
        return  sb.toString();
    }
}
