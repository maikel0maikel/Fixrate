package com.example.leaktest.utils;

import android.util.Log;

public class LogUtils {
    private static boolean sIsLogEnabled = true;// 是否打开LOG

    private static String sApplicationTag = "LogUtils";// LOG默认TAG

    // private static final String TAG_CONTENT_PRINT = "%s:%s.%s:%d";

    private static StackTraceElement getCurrentStackTraceElement() {
        return Thread.currentThread().getStackTrace()[4];

    }

    // 打印LOG
    public static void trace() {
        if (sIsLogEnabled) {
            android.util.Log.d(sApplicationTag, getContent(getCurrentStackTraceElement()));
        }
    }

    // 获取LOG
    private static String getContent(StackTraceElement trace) {
        // <<<<<<< .mine
        // return "" + sApplicationTag + ":" + trace.getClassName() + ":"
        // + trace.getMethodName() + "" + trace.getLineNumber();
        // // return String.format(TAG_CONTENT_PRINT, sApplicationTag,
        // // trace.getClassName(), trace.getMethodName(),
        // trace.getLineNumber());
        return "at method " + trace.getMethodName() + "[ line " + trace.getLineNumber() + "]:";
        // return String.format(TAG_CONTENT_PRINT, sApplicationTag,
        // trace.getClassName(), trace.getMethodName(), trace.getLineNumber());
    }

    // 打印默认TAG的LOG
    public static void traceStack() {
        if (sIsLogEnabled) {
            traceStack(sApplicationTag, android.util.Log.ERROR);
        }
    }

    // 打印Log当前调用栈信息
    public static void traceStack(String tag, int priority) {

        if (sIsLogEnabled) {
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            android.util.Log.println(priority, tag, stackTrace[4].toString());
            StringBuilder str = new StringBuilder();
            String prevClass = null;
            for (int i = 5; i < stackTrace.length; i++) {
                String className = stackTrace[i].getFileName();
                int idx = className.indexOf(".java");
                if (idx >= 0) {
                    className = className.substring(0, idx);
                }
                if (prevClass == null || !prevClass.equals(className)) {

                    str.append(className.substring(0, idx));

                }
                prevClass = className;
                //str.append(".").append(stackTrace[i].getMethodName()).append(":").append(stackTrace[i].getLineNumber()).append("->");
            }
            android.util.Log.println(priority, tag, str.toString());
        }
    }

    // 指定TAG和指定内容的方法
    public static void d(String tag, String msg) {
        if (sIsLogEnabled) {
            Log.d(tag, getContent(getCurrentStackTraceElement()) + "  " + msg);
        }
    }

    // 默认TAG和制定内容的方法
    public static void d(String msg) {
        if (sIsLogEnabled) {
            // <<<<<<< .mine
            // Log.d(sApplicationTag, getContent(getCurrentStackTraceElement())
            // + ">" + msg);
            // =======
            Log.d(getCurrentStackTraceElement().getClassName(), getContent(getCurrentStackTraceElement()) + "  " + msg);
        }
    }

    // 提示信息
    public static void i(String tag, String msg) {
        if (sIsLogEnabled) {
            Log.i(getCurrentStackTraceElement().getClassName(), getContent(getCurrentStackTraceElement()) + "  " + msg);
        }
    }

    // 警告信息
    public static void w(String tag, String msg) {
        if (sIsLogEnabled) {
            Log.w(getCurrentStackTraceElement().getClassName(), getContent(getCurrentStackTraceElement()) + "  " + msg);
        }
    }

    // 错误信息
    public static void e(String tag, String msg) {
        if (sIsLogEnabled) {
            Log.e(getCurrentStackTraceElement().getClassName(), getContent(getCurrentStackTraceElement()) + "  " + msg);
        }
    }

    // 错误信息
    public static void v(String tag, String msg) {
        if (sIsLogEnabled) {
            Log.v(getCurrentStackTraceElement().getClassName(), getContent(getCurrentStackTraceElement()) + "  " + msg);
        }
    }

    // 蓝牙调试信息
    public static void bt(String tag, String msg) {
        if (sIsLogEnabled) {
            Log.e(getCurrentStackTraceElement().getClassName(), getContent(getCurrentStackTraceElement()) + "  " + msg);
        }
    }

    // 语音调试信息
    public static void vo(String tag, String msg) {
        if (sIsLogEnabled) {
            Log.e(getCurrentStackTraceElement().getClassName(), getContent(getCurrentStackTraceElement()) + "  " + msg);
        }
    }

    // 语音调试信息
    public static void test(String tag, String msg) {
        if (sIsLogEnabled) {
            Log.e(getCurrentStackTraceElement().getClassName(), getContent(getCurrentStackTraceElement()) + "  " + msg);
        }
    }

}
