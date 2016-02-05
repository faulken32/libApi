/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.infinity.tools;

import java.text.SimpleDateFormat;

/**
 *
 * @author t311372
 */
public class DateTools {
    

    public static String getDateNow() {

        java.util.Date date = new java.util.Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        return simpleDateFormat.format(date);

    }

}
