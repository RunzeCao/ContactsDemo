package com.metagem.contactsdemo;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesUtils {
    private static String PRF_META_GEM = "meta.gem.pref";
    private static PreferencesUtils instance;
    private SharedPreferences.Editor ed;
    private SharedPreferences sp;

    private PreferencesUtils(Context context) {
        init(context);
    }

    public static PreferencesUtils getInstance(Context context) {
        if (instance == null) {
            instance = new PreferencesUtils(context);
        }
        return instance;
    }

    public void init(Context context) {
        if ((this.sp != null) && (this.ed != null)) {
            return;
        }
        try {
            this.sp = context.getSharedPreferences(PRF_META_GEM, Context.MODE_PRIVATE);
            this.ed = this.sp.edit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Float
    public Float getFloat(String key, Float value) {
        if (this.ed == null) {
            return 0f;
        }
        return this.sp.getFloat(key, value);
    }

    public boolean setFloat(String key, Float value) {
        if (this.ed == null) {
            return false;
        }
        this.ed.putFloat(key, value);
        return this.ed.commit();
    }

    //Boolean
    public boolean getBoolean(String key, boolean value) {
        if (this.ed == null) {
            return false;
        }
        return this.sp.getBoolean(key, value);
    }

    public boolean setBoolean(String key, boolean value) {
        if (this.ed == null) {
            return false;
        }
        this.ed.putBoolean(key, value);
        return this.ed.commit();
    }

    //Int
    public int getInt(String key, int value) {
        if (this.ed == null) {
            return 0;
        }
        return this.sp.getInt(key, value);
    }

    public boolean setInt(String key, int value) {
        if (this.ed == null) {
            return false;
        }
        ed.putInt(key, value);
        return this.ed.commit();
    }

    //Long
    public long getLong(String key, long value) {
        if (this.ed == null) {
            return 0;
        }
        return this.sp.getLong(key, value);
    }

    public boolean setLong(String key, long value) {
        if (this.ed == null) {
            return false;
        }
        this.ed.putLong(key, value);
        return this.ed.commit();
    }

    //String
    public String getString(String key, String value) {
        if (this.ed == null) {
            return "";
        }
        return this.sp.getString(key, value);
    }

    public boolean setString(String key, String value) {
        if (this.ed == null) {
            return false;
        }
        this.ed.putString(key, value);
        return this.ed.commit();
    }
}