package com.gyouzhe.develop.balance;

/**
 * @author wanchuan
 * @version 0.0.1
 * @since 2021/10/31 23:41
 **/
public class DevelopLoadBalanceContextHolder {

    private static final ThreadLocal<String> holder = new InheritableThreadLocal<>();

    public static void setVal(String val) {
        holder.set(val);
    }

    public static String getVal() {
        return holder.get();
    }

    public static void clear() {
        holder.remove();
    }
}
