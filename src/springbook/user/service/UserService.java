package springbook.user.service;

import java.util.List;

import springbook.user.dao.UserDao;
import springbook.user.domain.Level;
import springbook.user.domain.User;

public class UserService {
  private UserDao userDao;

  public void setUserDao(UserDao userDao) {
    this.userDao = userDao;
  }

  public void upgradeLevels() {
    List<User> users = userDao.getAll();
    for (User user : users) {
      if (canUpgradeLevel(user))
        upgradeLevel(user);
    }
  }

  private boolean canUpgradeLevel(User user) {
    Level currentLevel = user.getLevel();

    switch (currentLevel) {
      case BASIC:
        return (user.getLogin() >= 50);
      case SILVER:
        return (user.getRecommend() >= 30);
      case GOLD:
        return false;
      default:
        throw new IllegalArgumentException("Unknown level : " + currentLevel);
    }
  }

  /**
   * upgradeLevel을 따로 두면 좋은 점?
   * 
   * 만약 나중에 레벨이 오른 걸 사용자에게 알려줘야 하는 로직을 추가할 경우,
   * 
   * 어디를 수정해야 할 지 쉽게 찾을 수 있음.
   * 
   * @param user
   */
  private void upgradeLevel(User user) {

    /**
     * user로 upgradeLevel을 옮긴 이유 : 사용자 정보를 담고 있는 단순한 자바빈이지만 내부 정보를 다루는 기능이 있을 수 있다. UserService가 일일이
     * User의 레벨을 수정하는 것보다는 정보를 변경하라고 User에게 요청하는 것이 나음
     */
    user.upgradeLevel();
    userDao.update(user);

    // 기존 코드의 문제점 : 다음 레벨도 알아야 되고... Gold 예외 처리도 없고.... 레벨이 많아진다면 if문도 많아진다.
    // if (user.getLevel() == Level.BASIC)
    // user.setLevel(Level.SILVER);
    // else if (user.getLevel() == Level.SILVER)
    // user.setLevel(Level.GOLD);
    // userDao.update(user);

  }

  public void upgradeLevelsOld() {
    List<User> users = userDao.getAll();

    for (User user : users) {
      Boolean changed = false; // 레벨 변화 체크 플래그

      // BAISC 레벨 업그레이드 작업
      if (user.getLevel() == Level.BASIC && user.getLogin() >= 50) {
        user.setLevel(Level.SILVER);
        changed = true;
      }

      /**
       * 해당 코드의 문제점
       * 
       * 1) if,else if,else 읽기가 불편하다.
       * 
       * 1. 현재 레벨 파악 2. 업그레이드 조건을 담는 로직 3. 업그레이드를 위한 작업 4. update를 위한 플래그
       * 
       * 위와 같이 관련이 있어 보이지만 성격이 다른것들이 섞여 있음
       * 
       * 
       * 2) Level이 추가된다면?
       * 
       * enum도 수정하고 if문은 추가된 만큼 늘어난다. -> 메소드는 점점 복잡해짐
       * 
       * 3) 현재 레벨과 업그레이드 조건을 동시에 비교하고 있다.
       */
      // SILVER 레벨 업그레이드 작업
      else if (user.getLevel() == Level.SILVER && user.getRecommend() >= 30) {
        user.setLevel(Level.GOLD);
        changed = true;
      }

      else if (user.getLevel() == Level.GOLD)
        changed = false;
      else
        changed = false;

      if (changed)
        userDao.update(user);
    }
  }

  public void add(User user) {
    if (user.getLevel() == null)
      user.setLevel(Level.BASIC);
    userDao.add(user);
  }
}
