package Project.Chatzar.infrastructure.chatEvent;

import Project.Chatzar.Domain.chatEvent.ChatEvent;
import Project.Chatzar.Domain.chatEvent.ChatEventRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ChatEventRepositoryImpl implements ChatEventRepository {
    private final ChatEventJpaRepository chatEventJpaRepository;

    @Override
    public ChatEvent save(ChatEvent chatEvent) {
        return chatEventJpaRepository.save(chatEvent);
    }
}
