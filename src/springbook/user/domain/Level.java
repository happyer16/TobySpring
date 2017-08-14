package springbook.user.domain;

/**
 * Level enum을 사용하는 이유 1) DB에는 결국 int가 들어가지만, 겉으로는 Level 타입의 오브젝트 이기 때문에 안전하게 사용 가능 ( 1000이 들어온 경우
 * compile 에러를 내줌 )
 * 
 * @author Tmax
 *
 */
public enum Level {
  BASIC(1), SILVER(2), GOLD(3);

  private final int value;

  Level(int value) {
    this.value = value;
  }

  public int intValue() {
    return value;
  }

  public static Level valueOf(int value) {
    switch (value) {
      case 1:
        return BASIC;
      case 2:
        return SILVER;
      case 3:
        return GOLD;
      default:
        throw new AssertionError("Unknown value : " + value);
    }
  }
}
