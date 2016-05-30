package com.monkeyk.sos.web;

import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Shengzhao Li
 */
public abstract class WebUtils {


    private static ThreadLocal<String> ipThreadLocal = new ThreadLocal<>();


    public static void setIp(String ip) {
        ipThreadLocal.set(ip);
    }

    public static String getIp() {
        return ipThreadLocal.get();
    }

    //private
    private WebUtils() {
    }


    /**
     * Retrieve client ip address
     *
     * @param request HttpServletRequest
     * @return IP
     */
    public static String retrieveClientIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (isUnAvailableIp(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (isUnAvailableIp(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (isUnAvailableIp(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    private static boolean isUnAvailableIp(String ip) {
        return StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip);
    }

}