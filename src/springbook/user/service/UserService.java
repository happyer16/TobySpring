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
