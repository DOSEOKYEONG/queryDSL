package com.ll.exam.qsl.user.entity;

import com.ll.exam.qsl.interestkeywod.entity.InterestKeyword;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SiteUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    @Column(unique = true)
    private String email;

    @ManyToMany(cascade = CascadeType.ALL)
    @Builder.Default
    private Set<InterestKeyword> interestKeywordSet = new HashSet<>();


    public void addInterestKeywordContent(String keyword) {
        InterestKeyword interestKeyword = new InterestKeyword();
        interestKeyword.setContent(keyword);

        interestKeywordSet.add(interestKeyword);

//        interestKeywordSet.add(new InterestKeyword(keyword));
    }
}
