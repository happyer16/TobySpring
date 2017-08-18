package springbook.user.service;

import springbook.user.domain.User;

/**
 * 업그레이드 정책을 유연하게 변경하고 싶은 경우? ( 연말 이벤트나 홍보기간 중에 변경 할 수가 있음 )
 * 
 * 이럴 떄 마다 UserService를 변경한다면 번거롭고 위험한 방법임
 * 
 * => UserService에서 분리해서 DI를 이용
 * 
 * @author Tmax
 *
 */
public interface UserLevelUpgradePolicy {
  boolean canUpgradeLevel(User user);

  void upgradeLevel(User user);
}
