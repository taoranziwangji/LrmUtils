package com.vdolrm.lrmutils.LogUtils;

import android.util.Log;

public class MyLog {
    private static final String TAG = "lrmutils";
    public static final boolean DEBUG = true;


    private MyLog() {
    }

    public static void d(String msg) {
        d(TAG, msg);
    }


    public static void e(String msg) {
        e(TAG, msg);
    }


    public static void i(String tag, String msg) {
        if (DEBUG) {
            Log.i(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void v(String tag, String msg) {
        if (DEBUG) {
            Log.v(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (DEBUG) {
            Log.e(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (DEBUG) {
            Log.w(tag, msg);
        }
    }

    /**
     * 打印当前类、方法、行数、文件名等信息
     */
    public static String printBaseInfo() {
        if (DEBUG) {
            StringBuffer strBuffer = new StringBuffer();
            StackTraceElement[] stackTrace = new Throwable().getStackTrace();

            strBuffer.append("--- class:").append(stackTrace[1].getClassName())
                    .append("; method:").append(stackTrace[1].getMethodName())
                    .append("; number:").append(stackTrace[1].getLineNumber())
                    .append("; fileName:").append(stackTrace[1].getFileName())
                    .append("  ");

            //println( strBuffer.toString( ) );
            return strBuffer.toString();
        }
        return "";
    }

    /**
     * 打印当前文件名、方法、行数等信息
     */
    public static String printSimpleBaseInfo() {
        if (DEBUG) {
            StringBuffer strBuffer = new StringBuffer();
            StackTraceElement[] stackTrace = new Throwable().getStackTrace();

            strBuffer.append("  ---|fileName: ").append(stackTrace[1].getFileName())
                    .append(" ; method: ").append(stackTrace[1].getMethodName())
                    .append(" ; number: ").append(stackTrace[1].getLineNumber())
                    .append("|---  ");

            //println( strBuffer.toString( ) );
            return strBuffer.toString();
        }
        return "";
    }

    /**
     * 打印当前行数、文件名等信息
     */
    public static String printFileNameAndLinerNumber() {
        if (DEBUG) {
            StringBuffer strBuffer = new StringBuffer();
            StackTraceElement[] stackTrace = new Throwable().getStackTrace();

            strBuffer.append("; fileName:").append(stackTrace[1].getFileName())
                    .append("; number:").append(stackTrace[1].getLineNumber());

            // println( strBuffer.toString( ) );
            return strBuffer.toString();
        }
        return "";
    }

    /**
     * 打印当前行数
     */
    public static int printLineNumber() {
        if (DEBUG) {
            StringBuffer strBuffer = new StringBuffer();
            StackTraceElement[] stackTrace = new Throwable().getStackTrace();

            strBuffer.append("; number:").append(stackTrace[1].getLineNumber());

            //println( strBuffer.toString( ) );
            return stackTrace[1].getLineNumber();
        } else {
            return 0;
        }
    }

    /**
     * 打印当前方法名
     */
    public static String printMethodName() {
        if (DEBUG) {
            StringBuffer strBuffer = new StringBuffer();
            StackTraceElement[] stackTrace = new Throwable().getStackTrace();

            strBuffer.append("; number:").append(stackTrace[1].getMethodName());

            //println( strBuffer.toString( ) );
            return strBuffer.toString();
        }
        return "";
    }

    /**
     * 打印当前行数、文件名等信息
     */
    public static String printFileNameAndLinerNumber(String printInfo) {
        if (null == printInfo || !DEBUG) {
            return "";
        }
        StringBuffer strBuffer = new StringBuffer();
        StackTraceElement[] stackTrace = new Throwable().getStackTrace();

        strBuffer.append("; fileName:").append(stackTrace[1].getFileName())
                .append("; number:").append(stackTrace[1].getLineNumber()).append("\n")
                .append((null != printInfo) ? printInfo : "");

        //println( strBuffer.toString( ) );
        return strBuffer.toString();
    }


}

