package ru.practicum.log;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public class Log {
    public static void setRequestLog(String requester, HttpServletRequest request) {
        log.info(String.format("%-11s", requester) + String.format("%-10s", "(" + request.getMethod() + ")")
                + request.getRequestURL() + (request.getQueryString() == null ? "" : "?" + request.getQueryString()));
    }
}