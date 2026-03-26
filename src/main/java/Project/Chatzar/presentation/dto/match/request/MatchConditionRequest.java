package Project.Chatzar.presentation.dto.match.request;

import Project.Chatzar.Domain.match.MatchCondition;

public record MatchConditionRequest(
        String genderPreference,
        Integer minAge,
        Integer maxAge,
        String topic,
        String region
) {
    public MatchCondition toCondition() {
        return new MatchCondition(genderPreference, minAge, maxAge, topic, region);
    }

    public static MatchConditionRequest fromCondition(MatchCondition condition) {
        return new MatchConditionRequest(
                condition.getGenderPreference(),
                condition.getMinAge(),
                condition.getMaxAge(),
                condition.getTopic(),
                condition.getRegion()
        );
    }
}
