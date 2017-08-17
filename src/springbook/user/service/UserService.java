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
