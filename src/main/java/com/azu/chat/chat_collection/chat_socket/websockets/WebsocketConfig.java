package com.azu.chat.chat_collection.chat_socket.websockets;


import com.azu.hospital.all_user_sevices.user_utils.base_user_service.BaseUserDao;
import com.azu.hospital.chat_collection.chat_socket.redis.Publisher;
import com.azu.hospital.chat_collection.chat_socket.redis.Subscriber;
import com.azu.hospital.chat_collection.general_chat.dao.GeneralChatDao;
import com.azu.hospital.security.newsecurity.config.JwtService;
import com.azu.hospital.utils.Generic.GenericBaseService;
import jakarta.servlet.http.HttpServletRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Configuration
@EnableWebSocket
public class WebsocketConfig extends GenericBaseService implements WebSocketConfigurer {

    private final WebSocketSessionManager webSocketSessionManager;
    private final Publisher publisher;

    private final Subscriber subscriber;

    private final JwtService jwtService;
    private final HttpServletRequest request;

    private final BaseUserDao baseUserDao;

    private final GeneralChatDao chatDao;

    @Autowired
    public WebsocketConfig(
            WebSocketSessionManager webSocketSessionManager,
            Publisher publisher,
            Subscriber subscriber,
            JwtService jwtService,
            HttpServletRequest request,
            BaseUserDao baseUserDao,
           @Qualifier("GeneralChatDataAccessJpa") GeneralChatDao chatDao
    ) {
        super(jwtService, request);
        this.webSocketSessionManager = webSocketSessionManager;
        this.publisher = publisher;
        this.subscriber = subscriber;
        this.jwtService = jwtService;
        this.request = request;
        this.baseUserDao = baseUserDao;
        this.chatDao = chatDao;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new SocketTextHandler(
                                this.webSocketSessionManager,
                                this.publisher,
                                this.subscriber, jwtService
                        ),
                        "/users",
                        "/users/chat",
                        "/users/chat/*"
                ).addHandler(
                        new SocketGeneralChatHandler(
                                this.webSocketSessionManager ,
                                this.publisher ,
                                this.subscriber,
                                jwtService,
                                chatDao) ,
                "/general-chat"
                )
                .addInterceptors(getParameterInterceptors()).setAllowedOriginPatterns("*");
    }


    @Bean
    public HandshakeInterceptor getParameterInterceptors() {
        return new HandshakeInterceptor() {

            @Override
            public boolean beforeHandshake(@NotNull ServerHttpRequest request, @NotNull ServerHttpResponse response, @NotNull WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
                attributes.put(WebSocketHelper.userIdKey, authId().toString());
                return true;
            }

            @Override
            public void afterHandshake(@NotNull ServerHttpRequest request, @NotNull ServerHttpResponse response, @NotNull WebSocketHandler wsHandler, Exception exception) {



            }
        };
    }
}

