package com.ll.exam.qsl.user.repository;

import com.ll.exam.qsl.user.entity.SiteUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserRepositoryCustom {
    SiteUser getQslUser(Long id);

    Long getQslCount();

    SiteUser getOldestUser();

    List<SiteUser> getQslUsersOrderByIdAsc();

    List<SiteUser> searchQsl(String username);

    Page<SiteUser> searchQsl(String userlike, Pageable pageable);
}
