package Project.Chatzar.config.webSocket;

import Project.Chatzar.Domain.auth.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;

@RequiredArgsConstructor
public class StompAuthInterceptor implements ChannelInterceptor {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor == null) return message;

        if(StompCommand.CONNECT.equals(accessor.getCommand())){
            String authHeader = accessor.getFirstNativeHeader("Authorization");

            if(authHeader == null || !authHeader.startsWith("Bearer ")){
                throw new IllegalArgumentException("Authorization 헤더가 없습니다.");
            }

            String token = authHeader.substring(7);

            if(!jwtTokenProvider.isValid(token)){
                throw new IllegalArgumentException("유효하지 않은 JWT입니다.");
            }
            Long memberId = jwtTokenProvider.getMemberId(token);
            accessor.setUser(new StompPrincipal(memberId));
        }

        return message;
    }
}
