package com.spinai.sakgamnono.config;

import com.spinai.sakgamnono.jwt.JwtUtil;
import com.spinai.sakgamnono.websocket.JwtChannelInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;



@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    // jwtUtil 생성자
    private final JwtUtil jwtUtil;
    public WebSocketConfig(JwtUtil jwtUtil) {
        // jwtUtil 주입
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 프론트엔드에서 연결할 endpoint
        registry.addEndpoint("/ws/chat")
        .setAllowedOriginPatterns("*")
        .withSockJS();
    }
    
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 클라이언트가 subscribe할 때 사용할 prefix
        config.enableSimpleBroker("/topic", "/queue");
        // 클라이언트에서 메시지를 보낼 때 사용할 prefix
        config.setApplicationDestinationPrefixes("/app");
    }

    // InboundChannel에 JwtChannelInterceptor 등록
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new JwtChannelInterceptor(jwtUtil));
    }
    
}
