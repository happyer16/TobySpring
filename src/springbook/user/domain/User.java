package springbook.user.domain;

/**
 * DAO ( Data Access Object ) : DB를 사용해 데이터를 조회하거나 조작하는 기능을 전담하도록 만든 오브젝트를 만든다.
 * 
 * @author Tmax
 *
 */
public class User {
  String id;
  String name;
  String password;

  private Level level;
  private int login;
  private int recommend;

  public User(String id, String name, String password, Level level, int login, int recommend) {
    super();
    this.id = id;
    this.name = name;
    this.password = password;
    this.level = level;
    this.login = login;
    this.recommend = recommend;
  }

  public User() {}

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public int getRecommend() {
    return recommend;
  }

  public void setRecommend(int recommend) {
    this.recommend = recommend;
  }

  public int getLogin() {
    return login;
  }

  public void setLogin(int login) {
    this.login = login;
  }

  public Level getLevel() {
    return level;
  }

  public void setLevel(Level level) {
    this.level = level;
  }

}
