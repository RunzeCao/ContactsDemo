package com.metagem.contactsdemo;

import android.content.Context;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;



public class PrefUtils {
    /**
     * ItemBean对象保存
     */
    private static final String PRF_ITEM_BEAN = "prf.item.bean";

    public static void saveItemBean(Context context, String str) {
        Log.d(PRF_ITEM_BEAN, "saveItemBean: 22222222222222");
        PreferencesUtils.getInstance(context).setString(PRF_ITEM_BEAN, str);
    }

    public static String getItemBean(Context context) {
        return PreferencesUtils.getInstance(context).getString(PRF_ITEM_BEAN, "");
    }

    //对象转换字符串
    public static String obj2String(Object obj) {
        ByteArrayOutputStream bos = null;
        ObjectOutputStream oos = null;
        try {
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            String str = bos.toString("ISO-8859-1");
            str = java.net.URLEncoder.encode(str, "UTF-8");
            return str;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //换字符串转对象
    public static Object str2Object(String str) {
        ByteArrayInputStream bis = null;
        ObjectInputStream ois = null;
        try {
            String redStr = java.net.URLDecoder.decode(str, "UTF-8");
            bis = new ByteArrayInputStream(redStr.getBytes("ISO-8859-1"));
            ois = new ObjectInputStream(bis);
            return ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
                if (bis != null) {
                    bis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
