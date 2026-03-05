package Project.Chatzar.presentation.dto.friendship;

import jakarta.validation.constraints.NotBlank;

public record FriendSearchRequest(
        @NotBlank(message = "검색할 닉네임과 태그를 입력해주세요.")
        String searchName
) {
    public String getNickname() {
        return searchName.split("#")[0];
    }

    public String getTag() {
        String[] parts = searchName.split("#");
        return (parts.length > 1) ? parts[1] : "";
    }
}
