package com.ll.exam.qsl;

import com.ll.exam.qsl.user.entity.SiteUser;
import com.ll.exam.qsl.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

//	@Test
//	@DisplayName("1번 회원을 Qsl로 가져오기")
//	void t2() {
//		SiteUser siteUser1 = new SiteUser(null, "user1", "{noop}1234", "user1@test.com");
//		SiteUser siteUser2 = new SiteUser(null, "user2", "{noop}1234", "user2@test.com");
//
//		userRepository.saveAll(Arrays.asList(siteUser1, siteUser2));
//
//		SiteUser u1 = userRepository.getQslUser(1L);
//
//		assertThat(u1.getId()).isEqualTo(1L);
//		assertThat(u1.getUsername()).isEqualTo("user1");
//		assertThat(u1.getEmail()).isEqualTo("user1@test.com");
//		assertThat(u1.getPassword()).isEqualTo("{noop}1234");
//	}

	@Test
	@DisplayName("모든 회원 수")
	void t4() {
		long count = userRepository.getQslCount();
		System.out.println(count);
		assertThat(count).isGreaterThan(0);
	}

	@Test
	@DisplayName("가장 오래된 회원 1명")
	void t5() {
		SiteUser oldestUser = userRepository.getOldestUser();
		System.out.println(oldestUser.getId());
		assertThat(oldestUser.getId()).isGreaterThan(0);
	}

	@Test
	@DisplayName("전체회원, 오래된 순")
	void t6() {
		List<SiteUser> users = userRepository.getQslUsersOrderByIdAsc();

		SiteUser u1 = users.get(0);

		assertThat(u1.getId()).isEqualTo(1L);
		assertThat(u1.getUsername()).isEqualTo("user1");
		assertThat(u1.getEmail()).isEqualTo("user1@test.com");
		assertThat(u1.getPassword()).isEqualTo("{noop}1234");

		SiteUser u2 = users.get(1);

		assertThat(u2.getId()).isEqualTo(2L);
		assertThat(u2.getUsername()).isEqualTo("user2");
		assertThat(u2.getEmail()).isEqualTo("user2@test.com");
		assertThat(u2.getPassword()).isEqualTo("{noop}1234");
	}

	@Test
	@DisplayName("검색, List 리턴")
	void t7() {
		// 검색대상 : username, email
		// user1 로 검색
		List<SiteUser> users = userRepository.searchQsl("user1");

		assertThat(users.size()).isEqualTo(1);

		SiteUser u = users.get(0);

		assertThat(u.getId()).isEqualTo(1L);
		assertThat(u.getUsername()).isEqualTo("user1");
		assertThat(u.getEmail()).isEqualTo("user1@test.com");
		assertThat(u.getPassword()).isEqualTo("{noop}1234");

		// user2 로 검색
		users = userRepository.searchQsl("user2");

		assertThat(users.size()).isEqualTo(1);

		u = users.get(0);

		assertThat(u.getId()).isEqualTo(2L);
		assertThat(u.getUsername()).isEqualTo("user2");
		assertThat(u.getEmail()).isEqualTo("user2@test.com");
		assertThat(u.getPassword()).isEqualTo("{noop}1234");
	}

	@Test
	@DisplayName("검색, Page 리턴, id DESC, pageSize=1, page=0")
	void t8() {
		long totalCount = userRepository.count();
		int pageSize = 1; // 한 페이지에 보여줄 아이템 개수
		int totalPages = (int) Math.ceil(totalCount / (double) pageSize);
		int page = 1;
		String kw = "user";

		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.asc("id"));
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by(sorts)); // 한 페이지에 10까지 가능
		Page<SiteUser> usersPage = userRepository.searchQsl(kw, pageable);

		assertThat(usersPage.getTotalPages()).isEqualTo(totalPages);
		assertThat(usersPage.getNumber()).isEqualTo(page);
		assertThat(usersPage.getSize()).isEqualTo(pageSize);

		List<SiteUser> users = usersPage.get().toList();

		assertThat(users.size()).isEqualTo(pageSize);

		SiteUser u = users.get(0);

		assertThat(u.getId()).isEqualTo(2L);
		assertThat(u.getUsername()).isEqualTo("user2");
		assertThat(u.getEmail()).isEqualTo("user2@test.com");
		assertThat(u.getPassword()).isEqualTo("{noop}1234");
		// 검색어 : user1
		// 한 페이지에 나올 수 있는 아이템 수 : 1개
		// 현재 페이지 : 1
		// 정렬 : id 역순

		// 내용 가져오는 SQL
        /*
        SELECT site_user.*
        FROM site_user
        WHERE site_user.username LIKE '%user%'
        OR site_user.email LIKE '%user%'
        ORDER BY site_user.id ASC
        LIMIT 1, 1
         */

		// 전체 개수 계산하는 SQL
        /*
        SELECT COUNT(*)
        FROM site_user
        WHERE site_user.username LIKE '%user%'
        OR site_user.email LIKE '%user%'
         */
	}

	@Test
	@DisplayName("검색, Page 리턴, id DESC, pageSize=1, page=0")
	void t9() {
		long totalCount = userRepository.count();
		int pageSize = 1; // 한 페이지에 보여줄 아이템 개수
		int totalPages = (int) Math.ceil(totalCount / (double) pageSize);
		int page = 1;
		String kw = "user";

		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("id"));
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by(sorts)); // 한 페이지에 10까지 가능
		Page<SiteUser> usersPage = userRepository.searchQsl(kw, pageable);

		assertThat(usersPage.getTotalPages()).isEqualTo(totalPages);
		assertThat(usersPage.getNumber()).isEqualTo(page);
		assertThat(usersPage.getSize()).isEqualTo(pageSize);

		List<SiteUser> users = usersPage.get().toList();

		assertThat(users.size()).isEqualTo(pageSize);

		SiteUser u = users.get(0);

		assertThat(u.getId()).isEqualTo(1L);
		assertThat(u.getUsername()).isEqualTo("user1");
		assertThat(u.getEmail()).isEqualTo("user1@test.com");
		assertThat(u.getPassword()).isEqualTo("{noop}1234");
	}

	@Test
	@DisplayName("회원에게 관심사를 등록할 수 있다.")
	void t10() {
		SiteUser u2 = userRepository.getQslUser(2L);

		u2.addInterestKeywordContent("축구");
		u2.addInterestKeywordContent("롤");
		u2.addInterestKeywordContent("헬스");
		u2.addInterestKeywordContent("헬스"); // 중복등록은 무시

		userRepository.save(u2);
		// 엔티티클래스 : InterestKeyword(interest_keyword 테이블)
		// 중간테이블도 생성되어야 함, 힌트 : @ManyToMany
		// interest_keyword 테이블에 축구, 롤, 헬스에 해당하는 row 3개 생성
	}
}