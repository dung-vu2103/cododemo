package com.ringme.cms.service.kakoak.video_crawler_item;

import com.ringme.cms.model.kakoak.videocrawleritem.VideoCrawerItem;
import com.ringme.cms.model.kakoakcms.Book;
import org.springframework.data.domain.Page;

import java.util.List;

public interface VideoCrawlerItemService {
    Page<VideoCrawerItem> get(Integer id, int pageNo, int pageSize);
    VideoCrawerItem findById(Integer id);
    void save(VideoCrawerItem videoCrawerItem);
    void delete(Integer id);
    List<String> getId();
    List<String> getUrl();
}
