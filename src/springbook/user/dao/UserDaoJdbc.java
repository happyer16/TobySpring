package springbook.user.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import springbook.user.domain.Level;
import springbook.user.domain.User;

public class UserDaoJdbc implements UserDao {
  private RowMapper<User> userMapper = new RowMapper<User>() {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
      User user = new User();
      user.setId(rs.getString("id"));
      user.setName(rs.getString("name"));
      user.setPassword(rs.getString("password"));
      user.setLevel(Level.valueOf(rs.getInt("level")));
      user.setLogin(rs.getInt("login"));
      user.setRecommend(rs.getInt("recommend"));
      return user;
    }

  };

  /**
   * Connection 사용방법 1) 미리 생성돼서 트랜젝션 동기화 저장소에등록된 DB 컨넥션이 없는 경우 자체적으로 생성 2) 있는 경우 그대로 사용
   */
  private JdbcTemplate jdbcTemplate;

  public void setDataSource(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
  }

  @Override
  public void add(final User user) {
    this.jdbcTemplate.update(
        "insert into users(id, name, password,level,login,recommend) values(?,?,?,?,?,?)",
        user.getId(), user.getName(), user.getPassword(), user.getLevel().intValue(),
        user.getLogin(), user.getRecommend());
  }

  @Override
  public User get(String id) {
    return this.jdbcTemplate.queryForObject("select * from users where id = ?", new Object[] {id},
        userMapper);
  }

  @Override
  public void deleteAll() {
    this.jdbcTemplate.update("delete from users");
  }

  @Override
  public int getCount() {
    return this.jdbcTemplate.queryForInt("select count(*) from users");
  }

  @Override
  public List<User> getAll() {
    return this.jdbcTemplate.query("select * from users order by id", userMapper);
  }

  @Override
  public void update(User user) {
    this.jdbcTemplate.update(
        "update users set name=?, password=?, level=?, login=?, " + "recommend=? where id=?",
        user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLogin(),
        user.getRecommend(), user.getId());

  }
}
