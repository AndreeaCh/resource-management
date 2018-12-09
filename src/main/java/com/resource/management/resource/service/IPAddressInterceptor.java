package com.resource.management.resource.service;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

public class IPAddressInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(final ServerHttpRequest request,
                                   final ServerHttpResponse response,
                                   final WebSocketHandler wsHandler,
                                   final Map<String, Object> attributes) throws Exception {
        attributes.put("ip", request.getRemoteAddress());
        return true;
    }

    @Override
    public void afterHandshake(final ServerHttpRequest request,
                               final ServerHttpResponse response,
                               final WebSocketHandler wsHandler,
                               final @Nullable Exception exception) {
    }
}
