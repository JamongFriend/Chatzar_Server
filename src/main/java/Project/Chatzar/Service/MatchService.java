package Project.Chatzar.Service;

import Project.Chatzar.repository.MatchPreferenceRepository;
import Project.Chatzar.repository.MatchRepository;
import Project.Chatzar.repository.MatchRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MatchService {
    private final MatchRepository matchRepository;
    private final MatchPreferenceRepository matchPreferenceRepository;
    private final MatchRequestRepository matchRequestRepository;


}
