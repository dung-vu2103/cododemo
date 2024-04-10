package com.ringme.cms.service.kakoak.video_crawler_item;

import com.ringme.cms.model.kakoak.videocrawleritem.VideoCrawerItem;
import com.ringme.cms.repository.kakoak.video_crawler_item.VideoCrawlerItemRepository;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VideoCrawlerItemServiceimp implements VideoCrawlerItemService {
    @Autowired
    VideoCrawlerItemRepository videoCrawlerItemRepository;
    @Override
    public Page<VideoCrawerItem> get(Integer video_crawler_info_id, int pageNo, int pageSize) {
        Pageable pageable= PageRequest.of(pageNo-1,pageSize);
        return videoCrawlerItemRepository.get(video_crawler_info_id,pageable);
    }

    @Override
    public VideoCrawerItem findById(Integer id) {
        return videoCrawlerItemRepository.findById(id).orElse(null);
    }

    @Override
    public void save(VideoCrawerItem videoCrawerItem) {
        videoCrawlerItemRepository.save(videoCrawerItem);

    }

    @Override
    public void delete(Integer id) {
    videoCrawlerItemRepository.deleteById(id);
    }

    @Override
    public List<String> getId() {
        return videoCrawlerItemRepository.getAll();
    }

    @Override
    public List<String> getUrl() {
        return videoCrawlerItemRepository.getUrl();
    }
}
