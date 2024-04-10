package com.ringme.cms.repository.kakoak.youtube;

import com.ringme.cms.model.kakoak.videoclawerinfo.VideoClawerInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoClawerInfoRepository extends JpaRepository<VideoClawerInfo,Integer> {
    @Query(value = """
            SELECT * FROM video_crawler_info where (:type is null or type LIKE CONCAT('%', :type, '%'))
            """,
            countQuery = """
                    SELECT count(*) FROM video_crawler_info where (:type is null or type LIKE CONCAT('%', :type, '%'))  
                    """,
            nativeQuery = true)
    Page<VideoClawerInfo> get(@Param("type") String type, Pageable pageable);
}
