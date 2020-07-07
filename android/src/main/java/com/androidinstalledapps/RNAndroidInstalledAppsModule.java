
package com.androidinstalledapps;

import android.app.UiModeManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.helper.Utility;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class RNAndroidInstalledAppsModule extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;

  public RNAndroidInstalledAppsModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return "RNAndroidInstalledApps";
  }

  @ReactMethod
  public void getApps(Promise promise) {
    try {
      PackageManager pm = this.reactContext.getPackageManager();
      List<PackageInfo> pList = pm.getInstalledPackages(0);
      WritableArray list = Arguments.createArray();
      for (int i = 0; i < pList.size(); i++) {
        PackageInfo packageInfo = pList.get(i);
        WritableMap appInfo = Arguments.createMap();

        appInfo.putString("packageName", packageInfo.packageName);
        appInfo.putString("versionName", packageInfo.versionName);
        appInfo.putDouble("versionCode", packageInfo.versionCode);
        appInfo.putDouble("firstInstallTime", (packageInfo.firstInstallTime));
        appInfo.putDouble("lastUpdateTime", (packageInfo.lastUpdateTime));
        appInfo.putString("appName", ((String) packageInfo.applicationInfo.loadLabel(pm)).trim());

        Drawable icon = pm.getApplicationIcon(packageInfo.applicationInfo);
        appInfo.putString("icon", Utility.convert(icon));

        String apkDir = packageInfo.applicationInfo.publicSourceDir;
        appInfo.putString("apkDir", apkDir);

        File file = new File(apkDir);
        double size = file.length();
        appInfo.putDouble("size", size);

        if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
          appInfo.putString("type", "nonsystem");
        }
        else {
          appInfo.putString("type", "system");
        }

        list.pushMap(appInfo);
      }
      promise.resolve(list);
    } catch (Exception ex) {
      promise.reject(ex);
    }
  }

  @ReactMethod
  public void getNonSystemApps(Promise promise) {
    try {
      PackageManager pm = this.reactContext.getPackageManager();
      List<PackageInfo> pList = pm.getInstalledPackages(0);
      WritableArray list = Arguments.createArray();
      for (int i = 0; i < pList.size(); i++) {
        PackageInfo packageInfo = pList.get(i);
        WritableMap appInfo = Arguments.createMap();

        if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
          appInfo.putString("packageName", packageInfo.packageName);
          appInfo.putString("versionName", packageInfo.versionName);
          appInfo.putDouble("versionCode", packageInfo.versionCode);
          appInfo.putDouble("firstInstallTime", (packageInfo.firstInstallTime));
          appInfo.putDouble("lastUpdateTime", (packageInfo.lastUpdateTime));
          appInfo.putString("appName", ((String) packageInfo.applicationInfo.loadLabel(pm)).trim());

          Drawable icon = pm.getApplicationIcon(packageInfo.applicationInfo);
          appInfo.putString("icon", Utility.convert(icon));

          String apkDir = packageInfo.applicationInfo.publicSourceDir;
          appInfo.putString("apkDir", apkDir);

          File file = new File(apkDir);
          double size = file.length();
          appInfo.putDouble("size", size);

          list.pushMap(appInfo);
        }
      }
      promise.resolve(list);
    } catch (Exception ex) {
      promise.reject(ex);
    }

  }

  @ReactMethod
  public void getSystemApps(Promise promise) {
    try {
      PackageManager pm = this.reactContext.getPackageManager();
      List<PackageInfo> pList = pm.getInstalledPackages(0);
      WritableArray list = Arguments.createArray();
      for (int i = 0; i < pList.size(); i++) {
        PackageInfo packageInfo = pList.get(i);
        WritableMap appInfo = Arguments.createMap();

        if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
          appInfo.putString("packageName", packageInfo.packageName);
          appInfo.putString("versionName", packageInfo.versionName);
          appInfo.putDouble("versionCode", packageInfo.versionCode);
          appInfo.putDouble("firstInstallTime", (packageInfo.firstInstallTime));
          appInfo.putDouble("lastUpdateTime", (packageInfo.lastUpdateTime));
          appInfo.putString("appName", ((String) packageInfo.applicationInfo.loadLabel(pm)).trim());

          Drawable icon = pm.getApplicationIcon(packageInfo.applicationInfo);
          appInfo.putString("icon", Utility.convert(icon));

          String apkDir = packageInfo.applicationInfo.publicSourceDir;
          appInfo.putString("apkDir", apkDir);

          File file = new File(apkDir);
          double size = file.length();
          appInfo.putDouble("size", size);

          list.pushMap(appInfo);
        }
      }
      promise.resolve(list);
    } catch (Exception ex) {
      promise.reject(ex);
    }

  }

  @ReactMethod
  public void getConfig(Promise promise) {
    Configuration conf = getReactApplicationContext().getResources().getConfiguration();

    TimeZone tz = TimeZone.getDefault();

    WritableMap timeZoneDisplayNameMap = new WritableNativeMap();
    timeZoneDisplayNameMap.putString("long", tz.getDisplayName());
    timeZoneDisplayNameMap.putString("short", tz.getDisplayName(tz.inDaylightTime(new Date()), TimeZone.SHORT));

    WritableMap timeZoneMap = new WritableNativeMap();
    timeZoneMap.putMap("displayName", timeZoneDisplayNameMap);
    timeZoneMap.putString("ID", tz.getID());
    timeZoneMap.putInt("offset", tz.getOffset(new Date().getTime()));

    WritableMap localizationMap = new WritableNativeMap();
    Locale localization = conf.locale;
    localizationMap.putMap("timeZone", timeZoneMap);
    localizationMap.putString("language", localization.getLanguage());
    localizationMap.putString("country", localization.getCountry());
    localizationMap.putString("locale", localization.toString());
    localizationMap.putString("displayCountry", localization.getDisplayCountry());
    localizationMap.putString("displayName", localization.getDisplayName());
    localizationMap.putString("displayLanguage", localization.getDisplayLanguage());
    localizationMap.putBoolean("is24HourFormat", DateFormat.is24HourFormat(getReactApplicationContext()));


    /**
     * Get ISO 3166-1 alpha-2 country code for this device (or null if not available)
     * @param context Context reference to get the TelephonyManager instance from
     * @return country code or null
     */
    final TelephonyManager tm = (TelephonyManager) getReactApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
    String simCountry = tm.getSimCountryIso();
    if (simCountry != null && simCountry.length() == 2) { // SIM country code is available
      simCountry = simCountry.toUpperCase(Locale.US);
    }
    String networkCountry = tm.getNetworkCountryIso();
    if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
      if (networkCountry != null && networkCountry.length() == 2) { // network country code is available
        networkCountry = networkCountry.toUpperCase(Locale.US);
      }
    }

    localizationMap.putString("simCountry", simCountry);
    localizationMap.putString("networkCountry", networkCountry);
    WritableMap result = new WritableNativeMap();
    result.putMap("localization", localizationMap);
    if (Build.VERSION.SDK_INT > 16) {
      result.putInt("densityDpi", conf.densityDpi);
    } else {
      DisplayMetrics metrics = getReactApplicationContext().getResources().getDisplayMetrics();
      int densityDpi = (int)(metrics.density * 160f);
      result.putInt("densityDpi", densityDpi);
    }
    result.putDouble("fontScale", conf.fontScale);

    String hardKeyboardHidden = null;
    if (conf.hardKeyboardHidden == 0) {
      hardKeyboardHidden = "undefined";
    } else if (conf.hardKeyboardHidden == 1) {
      hardKeyboardHidden = "no";
    } else {
      hardKeyboardHidden = "yes";
    }
    result.putString("hardKeyboardHidden", hardKeyboardHidden);

    String keyboardHidden = null;
    if (conf.keyboardHidden == 0) {
      keyboardHidden = "undefined";
    } else if (conf.keyboardHidden == 1) {
      keyboardHidden = "no";
    } else {
      keyboardHidden = "yes";
    }
    result.putString("keyboardHidden", keyboardHidden);

    String keyboard = null;
    if (conf.keyboard == 0) {
      keyboard = "undefined";
    } else if (conf.keyboard == 1) {
      keyboard = "nokeys";
    } else if (conf.keyboard == 2) {
      keyboard = "qwerty";
    } else {
      keyboard = "12key";
    }
    result.putString("keyboard", keyboard);

    String orientation = null;
    if (conf.orientation == 0) {
      orientation = "undefined";
    } else if (conf.orientation == 1) {
      orientation = "portrait";
    } else if (conf.orientation == 2) {
      orientation = "landscape";
    } else {
      orientation = "square";
    }
    result.putString("orientation", orientation);

    result.putInt("screenHeightDp", conf.screenHeightDp);
    result.putInt("screenWidthDp", conf.screenWidthDp);

    int screenLayoutInt = getReactApplicationContext().getResources().getConfiguration().screenLayout;
    screenLayoutInt &= Configuration.SCREENLAYOUT_SIZE_MASK;
    String screenLayout = null;
    if (screenLayoutInt == Configuration.SCREENLAYOUT_SIZE_SMALL) {
      screenLayout = "small";
    } else if (screenLayoutInt == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
      screenLayout = "normal";
    } else if (screenLayoutInt == Configuration.SCREENLAYOUT_SIZE_LARGE) {
      screenLayout = "large";
    } else if (screenLayoutInt == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
      screenLayout = "xlarge";
    } else {
      screenLayout = "undefined";
    }
    result.putString("screenLayout", screenLayout);

    result.putInt("smallestScreenWidthDp", conf.smallestScreenWidthDp);

    UiModeManager uiModeManager = (UiModeManager) getReactApplicationContext().getSystemService(Context.UI_MODE_SERVICE);
    int uiModeTypeInt = uiModeManager.getCurrentModeType();
    String uiModeType = null;
    if (uiModeTypeInt ==  Configuration.UI_MODE_TYPE_UNDEFINED) {
      uiModeType = "undefined";
    } else if (uiModeTypeInt == Configuration.UI_MODE_TYPE_NORMAL) {
      uiModeType = "normal";
    } else if (uiModeTypeInt == Configuration.UI_MODE_TYPE_DESK) {
      uiModeType = "desk";
    } else if (uiModeTypeInt == Configuration.UI_MODE_TYPE_CAR) {
      uiModeType = "car";
    } else if (uiModeTypeInt == Configuration.UI_MODE_TYPE_TELEVISION) {
      uiModeType = "television";
    } else if (uiModeTypeInt == Configuration.UI_MODE_TYPE_APPLIANCE) {
      uiModeType = "appliance";
    } else if (uiModeTypeInt == Configuration.UI_MODE_TYPE_WATCH) {
      uiModeType = "watch";
    }
    result.putString("uiModeType", uiModeType);

    promise.resolve(result);
  }

}
