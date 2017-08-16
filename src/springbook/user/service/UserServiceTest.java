package springbook.user.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/test-applicationContext.xml")
public class UserServiceTest {

  // Test 대상인 UserService 빈을 제공받을 수 있도록 @Autowired가 붙은 인스턴스 변수를 선언해 줌
  @Autowired
  UserService userService;

  @Test
  public void bean() {
    assertThat(this.userService, is(notNullValue()));
  }
}
