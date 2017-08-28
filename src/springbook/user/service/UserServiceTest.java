package springbook.user.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static springbook.user.service.UserServiceImpl.MIN_LOGCOUNT_FOR_SILVER;
import static springbook.user.service.UserServiceImpl.MIN_RECOMMEND_FOR_GOLD;

import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;

import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;
import springbook.user.service.UserServiceTest.TestUserService.TestUserServiceException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/test-applicationContext.xml")
public class UserServiceTest {

  // Test 대상인 UserService 빈을 제공받을 수 있도록 @Autowired가 붙은 인스턴스 변수를 선언해 줌
  @Autowired
  UserService userService;
  @Autowired
  DataSource dataSource;
  @Autowired
  PlatformTransactionManager transactionManager;

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
  public void upgradeLevels() throws Exception {
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

  static class TestUserService extends UserServiceImpl {
    private String id;

    private TestUserService(String id) {
      this.id = id;
    }

    @Override
    protected void upgradeLevel(User user) {
      if (user.getId().equals(this.id)) // 지정된 id의 User 오브젝트가 발견되면 예외를 던져서 작업을 강제로 중단시킨다.
        throw new TestUserServiceException();
      super.upgradeLevel(user);
    }

    static class TestUserServiceException extends RuntimeException {

    }
  }

  /**
   * 레벨 업그레이드를 시도하다가 중간에 예외가 발생한 경우, 원래 상태로 돌아가는지 테스트
   * 
   * @throws Exception
   */
  @Test
  public void upgradeAllOrNothing() throws Exception {
    TestUserService testUserService = new TestUserService(users.get(3).getId());
    testUserService.setUserDao(this.userDao); // 특별한 목적으로만 사용하는 것이니, 번거롭게 스프링 빈으로 등록할 필요 없이 수동으로 DI
                                              // 해줌

    
    UserServiceTx txUserService=new UserServiceTx();
    txUserService.setTransactionManager(transactionManager);
    txUserService.setUserService(testUserService);
    
    userDao.deleteAll();

    for (User user : users)
      userDao.add(user);
    try {
      testUserService.upgradeLevels();
      fail("TestUserServiceException expected"); // 중간에 예외를 안 던저주면 문제가 있는거임
    } catch (TestUserServiceException e) { // 예외를 잡아서 어떤 작업을 진행

    }

    checkLevelUpgraded(users.get(1), false); // Test는 실패하게 됨 ( upgradeLevel()은 하나의 트랜잭션이 아니기 때문에 )
  }

  private void checkLevelUpgraded(User user, boolean upgraded) {
    User userUpdate = userDao.get(user.getId());
    if (upgraded)
      assertThat(userUpdate.getLevel(), is(user.getLevel().nextLevel()));
    else
      assertThat(userUpdate.getLevel(), is(user.getLevel()));

  }


}
