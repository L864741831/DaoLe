package com.tck.daole.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.telephony.PhoneNumberUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    /**
     * 判断字符串是否为空
     */
    public static boolean isSpace(String str) {
        if (str == null || "".equals(str.trim())) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否是一个合法的手机号
     */
    public static boolean isPhone(String number){
        String pattern = "^1(3[0-9]|4[5,7]|5[0-9]|7[0,1,2,3,4,5,6,7,8,9]|8[0-9])\\d{8}$";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(number);
        return  m.matches();
    }

    /**
     * 根据身份证号输出年龄
     */
    public static int IdNOToAge(String IdNO) {
        int leh = IdNO.length();
        String dates = "";
        if (leh == 18) {
            int se = Integer.valueOf(IdNO.substring(leh - 1)) % 2;
            dates = IdNO.substring(6, 10);
            SimpleDateFormat df = new SimpleDateFormat("yyyy");
            String year = df.format(new Date());
            int u = Integer.parseInt(year) - Integer.parseInt(dates);
            return u;
        } else {
            dates = IdNO.substring(6, 8);
            return Integer.parseInt(dates);
        }
    }

    /**
     * 调起系统发短信功能
     */
    public static void doSendSMSTo(Activity activity, String phoneNumber, String message) {
        if (PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber)) {
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phoneNumber));
            intent.putExtra("sms_body", message);
            activity.startActivity(intent);
        }
    }
}
