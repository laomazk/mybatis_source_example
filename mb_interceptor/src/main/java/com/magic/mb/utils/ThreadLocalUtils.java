package com.magic.mb.utils;

import com.magic.mb.bean.Page;

public class ThreadLocalUtils {
    private static final ThreadLocal<Page> tl = new ThreadLocal<>();

    public static void set(Page page) {
        tl.set(page);
    }

    public static Page get() {
        return tl.get();
    }

    public static void remove() {
        tl.remove();
    }

    public static Page getAndRemove() {
        Page page = tl.get();
        tl.remove();
        return page;
    }
}
