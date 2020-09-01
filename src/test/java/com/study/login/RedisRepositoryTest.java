package com.study.login;

import com.study.login.model.Point;
import com.study.login.repository.PointRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(
        properties = {
                "spring.redis.port : 6379"
        }
)
public class RedisRepositoryTest {

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    @Test
    @DisplayName("기본 등록 조회기능")
    void test() {
        String id = "johnxx1";
        LocalDateTime refreshTime = LocalDateTime.of(2020, 9, 1, 0, 0);

        Point point = Point.builder()
                .id(id)
                .amount(1000L)
                .refreshTime(refreshTime)
                .build();


        pointRepository.save(point);

        Point savedPoint = pointRepository.findById(id).get();

        assertThat(savedPoint.getAmount()).isEqualTo(1000L);
        assertThat(savedPoint.getRefreshTime()).isEqualTo(refreshTime);

    }
}
