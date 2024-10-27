package project.io.app.core.user.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import project.io.app.core.order.domain.Order;
import project.io.app.core.order.domain.OrderItem;
import project.io.app.core.user.domain.Gender;
import project.io.app.core.user.domain.MonthlyNewUserStatistics;
import project.io.app.core.user.domain.MonthlyNewUserStatisticsByGender;
import project.io.app.core.user.domain.User;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserReadDaoTest {

    @Autowired
    UserReadDao userReadDao;

    @Autowired
    UserWriteDao userWriteDao;

    @BeforeEach
    void setUp() {
        userWriteDao.deleteAllUsers();
    }

    @DisplayName("1번 문제")
    @Test
    void getMonthlyNewUserCountsByGenderDescending() {
        User dummyUser1 = generateDummyUserWithCreatedAtAndGender(2024, 8, Gender.MAN);
        User dummyUser2 = generateDummyUserWithCreatedAtAndGender(2024, 8, Gender.WOMAN);
        User dummyUser3 = generateDummyUserWithCreatedAtAndGender(2024, 8, Gender.WOMAN);
        User dummyUser4 = generateDummyUserWithCreatedAtAndGender(2024, 9, Gender.MAN);
        User dummyUser5 = generateDummyUserWithCreatedAtAndGender(2024, 9, Gender.MAN);
        User dummyUser6 = generateDummyUserWithCreatedAtAndGender(2024, 9, Gender.MAN);
        User dummyUser7 = generateDummyUserWithCreatedAtAndGender(1999, 1, Gender.MAN);

        userWriteDao.insertUser(dummyUser1);
        userWriteDao.insertUser(dummyUser2);
        userWriteDao.insertUser(dummyUser3);
        userWriteDao.insertUser(dummyUser4);
        userWriteDao.insertUser(dummyUser5);
        userWriteDao.insertUser(dummyUser6);
        userWriteDao.insertUser(dummyUser7);

        List<MonthlyNewUserStatisticsByGender> monthlyNewUserCounts = userReadDao.getMonthlyNewUserCountsDescendingByGender();

        assertThat(monthlyNewUserCounts).hasSize(3);
        assertThat(monthlyNewUserCounts)
                .extracting(MonthlyNewUserStatisticsByGender::getIncomingDate,
                        MonthlyNewUserStatisticsByGender::getGender,
                        MonthlyNewUserStatisticsByGender::getIncomingUserCount)
                .containsExactly(
                        Tuple.tuple("2024-09", Gender.MAN, 3L),
                        Tuple.tuple("2024-08", Gender.WOMAN, 2L),
                        Tuple.tuple("2024-08", Gender.MAN, 1L)
                );
    }

    @DisplayName("1-1번 문제")
    @Test
    void getMonthlyNewUserCountsDescending() {
        User dummyUser1 = generateDummyUserWithCreatedAt(2024,7);
        User dummyUser2 = generateDummyUserWithCreatedAt(2024,8);
        User dummyUser3 = generateDummyUserWithCreatedAt(2024,8);
        User dummyUser4 = generateDummyUserWithCreatedAt(2024,9);
        User dummyUser5 = generateDummyUserWithCreatedAt(2024,9);
        User dummyUser6 = generateDummyUserWithCreatedAt(2024,9);
        User dummyUser7 = generateDummyUserWithCreatedAt(1999,1);

        userWriteDao.insertUser(dummyUser1);
        userWriteDao.insertUser(dummyUser2);
        userWriteDao.insertUser(dummyUser3);
        userWriteDao.insertUser(dummyUser4);
        userWriteDao.insertUser(dummyUser5);
        userWriteDao.insertUser(dummyUser6);
        userWriteDao.insertUser(dummyUser7);

        List<MonthlyNewUserStatistics> monthlyNewUserCounts = userReadDao.getMonthlyNewUserCountsDescending();

        assertThat(monthlyNewUserCounts).hasSize(3);
        assertThat(monthlyNewUserCounts)
                .extracting(MonthlyNewUserStatistics::getIncomingDate, MonthlyNewUserStatistics::getIncomingUserCount)
                .containsExactly(
                        Tuple.tuple("2024-09", 3L),
                        Tuple.tuple("2024-08", 2L),
                        Tuple.tuple("2024-07", 1L)
                );
    }

    @DisplayName("2번 문제")
    @Test
    void findActiveUsers() {
        User dummyUser = generateDummyUserWithCreatedAt(2024,7);
        long savedUserId = userWriteDao.insertUser(dummyUser);

        Order order = Order.builder()
                .userId(savedUserId)
                .totalAmount(new BigDecimal(100000))
                .build();

        OrderItem.builder()
                .productId(1L)
                .qty(2)
                .build();

        OrderItem.builder()
                .productId(1L)
                .qty(2)
                .build();

        List<MonthlyNewUserStatistics> monthlyNewUserCounts = userReadDao.getMonthlyNewUserCountsDescending();

        assertThat(monthlyNewUserCounts).hasSize(3);
        assertThat(monthlyNewUserCounts)
                .extracting(MonthlyNewUserStatistics::getIncomingDate, MonthlyNewUserStatistics::getIncomingUserCount)
                .containsExactly(
                        Tuple.tuple("2024-09", 3L),
                        Tuple.tuple("2024-08", 2L),
                        Tuple.tuple("2024-07", 1L)
                );
    }

    @DisplayName("userId로 특정 유저를 찾아온다.")
    @Test
    void findById() {
        User dummyUser = generateDummyUser();
        long savedUserId = userWriteDao.insertUser(dummyUser);

        Optional<User> userOptional = userReadDao.findById(savedUserId);

        assertTrue(userOptional.isPresent());
        User user = userOptional.get();
        assertThat(user.getId()).isEqualTo(savedUserId);
    }

    private User generateDummyUserWithCreatedAtAndGender(int year, int month, Gender gender) {
        return generateDummyUser(LocalDateTime.of(year, month, 1, 0, 0), gender);
    }

    private User generateDummyUserWithCreatedAt(int year, int month) {
        return generateDummyUser(LocalDateTime.of(year, month, 1, 0, 0), Gender.MAN);
    }

    private User generateDummyUser() {
        return generateDummyUser(null, Gender.MAN);
    }

    private User generateDummyUser(LocalDateTime createdAt, Gender gender) {
        return User.builder()
                .name("testName")
                .nickname("testNickName")
                .gender(gender)
                .countryId(1L)
                .now(createdAt)
                .build();
    }
}