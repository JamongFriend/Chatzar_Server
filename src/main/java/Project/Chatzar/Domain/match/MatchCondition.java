package Project.Chatzar.Domain.match;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class MatchCondition {
    // 성별 선호
    private String genderPreference;

    // 나이 범위
    private Integer minAge;
    private Integer maxAge;

    // 지역 (선택)
    private String region;

    // 관심 주제 (선택)
    private String topic;

    protected MatchCondition() {}

    public MatchCondition(String genderPreference, Integer minAge, Integer maxAge, String topic, String region) {
        this.genderPreference = genderPreference;
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.topic = topic;
        this.region = region;
    }
}
