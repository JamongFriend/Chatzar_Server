package Project.Chatzar.presentation.dto.match.request;

import Project.Chatzar.Domain.match.MatchCondition;

public record MatchConditionRequest(
        String gender,
        Integer minAge,
        Integer maxAge,
        String topic
) {
    public MatchCondition toCondition() {
        return new MatchCondition( gender, minAge, maxAge, topic);
    }
}
