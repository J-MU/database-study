package project.io.app.core.user.domain;

import lombok.Getter;

@Getter
public class MonthlyNewUserStatisticsByGender extends MonthlyNewUserStatistics {

    private final Gender gender;

    public MonthlyNewUserStatisticsByGender(String incomingDate, long incomingUserCount, Gender gender) {
        super(incomingDate, incomingUserCount);
        this.gender = gender;
    }
}
