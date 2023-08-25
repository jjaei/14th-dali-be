package com.dali.dali.domain.community.service;

import com.dali.dali.domain.community.dto.CommunityDto;
import com.dali.dali.domain.community.entity.AMPM;
import com.dali.dali.domain.community.entity.Community;
import com.dali.dali.domain.community.entity.Gender;
import com.dali.dali.domain.community.entity.Time;
import com.dali.dali.domain.community.repository.CommunityRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class CommunityService {
    private final CommunityRepository communityRepository;

    public Community createPost(CommunityDto communityDto) {

        // 로그인 검증 추후 추가

        Community community = Community.builder()
                .title(communityDto.getTitle())
                .content(communityDto.getContent())
                .gender(communityDto.getGender())
                .ampm(communityDto.getAmpm())
                .time(communityDto.getTime())
                .userCount(communityDto.getUserCount())
                .regDate(LocalDateTime.now())
                .build();

        return communityRepository.save(community);
    }

    public Community getPost(Long id) {
        return communityRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글이 없습니다.")
        );
    }

    public Page<Community> getPosts(Pageable pageable, AMPM ampm, Time time, Gender gender) {
        if (ampm == null && time == null && gender == null) {
            return communityRepository.findAll(pageable);
        } else if (gender != null) {
            return communityRepository.findByGender(gender, pageable);
        } else if (time != null) {
            return communityRepository.findByTime(time, pageable);
        } else if (ampm != null) {
            return communityRepository.findByAmpm(ampm, pageable);
        }
        return communityRepository.findByGenderAndTimeAndAmpm(gender, time, ampm, pageable);
    }
}
