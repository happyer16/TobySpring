package springbook.user.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

public interface StatementStrategy {
  PreparedStatement makePreparedStatement(Connection c) throws SQLException;


  public static void test() throws SQLException {
    DataSource dataSource = null;
    Connection c = dataSource.getConnection();

    c.setAutoCommit(false); // 트랜잭션 시작

    try { // 트랜잭션 하나의 작업
      PreparedStatement st1 = c.prepareStatement("update users ... ");
      st1.executeUpdate();

      PreparedStatement st2 = c.prepareStatement("delete users ... ");
      st2.executeUpdate();

      c.commit();// 트랜잭션 커밋
    } catch (Exception e) {
      c.rollback();
    }

    c.close();
  }
}
