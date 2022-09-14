package com.ll.exam.qsl;

import com.ll.exam.qsl.user.entity.SiteUser;
import com.ll.exam.qsl.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

@SpringBootTest
@ActiveProfiles("test") //test 모드 활성화, 이 클래스에 정의된 Bean 들은 테스트모드에서만 활성화
@Transactional // 테스트케이스에서 발생한 결과를 실제 DB에 반영하지 않음
class UserRepositoryTests {

	@Autowired
	private UserRepository userRepository;

	@Test
	@DisplayName("회원 생성")
	void t1() {
		SiteUser siteUser3 = SiteUser.builder()
				.username("user3")
				.password("{noop}1234")
				.email("user3@test.com")
				.build();

		SiteUser siteUser4 = SiteUser.builder()
				.username("user4")
				.password("{noop}1234")
				.email("user4@test.com")
				.build();

		userRepository.saveAll(Arrays.asList(siteUser3, siteUser4));
	}

	@Test
	@DisplayName("1번 회원을 Qsl로 가져오기")
	void t2() {
		SiteUser siteUser1 = new SiteUser(null, "user1", "{noop}1234", "user1@test.com");
		SiteUser siteUser2 = new SiteUser(null, "user2", "{noop}1234", "user2@test.com");

		userRepository.saveAll(Arrays.asList(siteUser1, siteUser2));

		SiteUser u1 = userRepository.getQslUser(1L);

		assertThat(u1.getId()).isEqualTo(1L);
		assertThat(u1.getUsername()).isEqualTo("user1");
		assertThat(u1.getEmail()).isEqualTo("user1@test.com");
		assertThat(u1.getPassword()).isEqualTo("{noop}1234");
	}

	@Test
	@DisplayName("모든 회원 수")
	void t4() {
		long count = userRepository.getQslCount();
		System.out.println(count);
		assertThat(count).isGreaterThan(0);
	}

}
