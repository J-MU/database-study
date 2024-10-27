package project.io.app.core.user.domain;

import lombok.Getter;

@Getter
public class MonthlyNewUserStatistics {

    private final String incomingDate;
    private final long incomingUserCount;

    public MonthlyNewUserStatistics(String incomingDate, long incomingUserCount) {
        this.incomingDate = incomingDate;
        this.incomingUserCount = incomingUserCount;
    }
}
