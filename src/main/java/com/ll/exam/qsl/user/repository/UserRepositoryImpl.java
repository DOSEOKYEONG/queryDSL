package com.ll.exam.qsl.user.repository;

import com.ll.exam.qsl.user.entity.QSiteUser;
import com.ll.exam.qsl.user.entity.SiteUser;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static com.ll.exam.qsl.user.entity.QSiteUser.siteUser;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public SiteUser getQslUser(Long id) {

//        SELECT * FROM SITE_USER WHERE id = 1;

        return jpaQueryFactory
                .select(siteUser)
                .from(siteUser)
                .where(siteUser.id.eq(id))
                .fetchOne();
    }

    @Override
    public Long getQslCount() {
        return jpaQueryFactory
                .select(siteUser)
                .from(siteUser)
                .fetch()
                .stream().count();
    }

//    강사가 만든 버전
//    @Override
//    public long getQslCount() {
//        return jpaQueryFactory
//                .select(siteUser.count())
//                .from(siteUser)
//                .fetchOne();
//    }

}
