package com.nhnacademy.http.context;

// Singleton 패턴으로 Context를 제공합니다.
public class ContextHolder {
    private static final Context context = new ApplicationContext();

    public static synchronized ApplicationContext getApplicationContext() {
        return (ApplicationContext) context;
    }
}
