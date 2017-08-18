package springbook.user.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static springbook.user.service.UserService.MIN_LOGCOUNT_FOR_SILVER;
import static springbook.user.service.UserService.MIN_RECOMMEND_FOR_GOLD;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/test-applicationContext.xml")
public class UserServiceTest {

  // Test 대상인 UserService 빈을 제공받을 수 있도록 @Autowired가 붙은 인스턴스 변수를 선언해 줌
  @Autowired
  UserService userService;

  @Autowired
  UserDao userDao;

  List<User> users;

  @Before
  public void setUp() {
    users = Arrays.asList(
        new User("gyumee", "박성철", "springno1", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER - 1, 0),
        new User("gyumee2", "박성4철", "springno61", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0),
        new User("gyumee3", "박성5철", "springno71", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD - 1),
        new User("leegw700", "이길원", "springno2", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD),
        new User("bumjin", "박범진", "springno3", Level.GOLD, 100, 40));
  }

  @Test
  public void upgradeLevels() {
    userDao.deleteAll();

    for (User user : users)
      userDao.add(user);

    userService.upgradeLevels();

    checkLevel(users.get(0), Level.BASIC);
    checkLevel(users.get(1), Level.SILVER);
    checkLevel(users.get(2), Level.SILVER);
    checkLevel(users.get(3), Level.GOLD);
    checkLevel(users.get(4), Level.GOLD);
  }

  private void checkLevel(User user, Level expectedLevel) {
    User userUpdate = userDao.get(user.getId());
    assertThat(userUpdate.getLevel(), is(expectedLevel));
  }

  @Test
  public void bean() {
    assertThat(this.userService, is(notNullValue()));
  }

  @Test
  public void add() {
    userDao.deleteAll();

    User userWithLevel = users.get(4); // GOLD 레벨 ( 레벨을 초기화하지 않아야 함 )
    User userWithoutLevel = users.get(0); // BASIC 레벨
    userWithoutLevel.setLevel(null);

    userService.add(userWithLevel);
    userService.add(userWithoutLevel);

    User userWithLevelRead = userDao.get(userWithLevel.getId());
    User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());

    assertThat(userWithLevelRead.getLevel(), is(userWithLevel.getLevel()));
    assertThat(userWithoutLevelRead.getLevel(), is(userWithoutLevel.getLevel()));


  }
}
