package com.study.login.repository;

import com.study.login.model.Point;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * RedisRepository 코드 예제를 위한 레파지토리
 */

@Repository
public interface PointRepository extends CrudRepository<Point, String> {
}
