package com.ringme.cms.repository.kakoak.video_crawler_item;

import com.ringme.cms.model.kakoak.videoclawerinfo.VideoClawerInfo;
import com.ringme.cms.model.kakoak.videocrawleritem.VideoCrawerItem;
import com.ringme.cms.model.kakoakcms.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoCrawlerItemRepository extends JpaRepository<VideoCrawerItem,Integer> {
    @Query(value = """
            select * from video_crawler_item where (:id_video_info is null or id_video_info=:id_video_info)
            """,
            countQuery= """
                    SELECT COUNT(*) FROM video_crawler_item where (:id_video_info is null or id_video_info=:id_video_info)
                    """,
            nativeQuery = true)
    Page<VideoCrawerItem> get(@Param("id_video_info") Integer id_video_info, Pageable pageable);
    @Query(value = """
            select id_string from kakoak.video_crawler_item 
            """, nativeQuery = true)
    List<String> getAll();
    @Query(value = """
            select url_video_item from kakoak.video_crawler_item 
            """, nativeQuery = true)
    List<String> getUrl();
}
