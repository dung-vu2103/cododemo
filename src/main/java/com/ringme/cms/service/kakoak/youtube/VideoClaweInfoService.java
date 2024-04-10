package com.ringme.cms.service.kakoak.youtube;

import com.ringme.cms.dto.kakoak.video_clawer_infoDto.Video_clawer_infoDto;
import com.ringme.cms.model.kakoak.videoclawerinfo.VideoClawerInfo;
import org.springframework.data.domain.Page;

import java.util.List;

public interface VideoClaweInfoService {
    Video_clawer_infoDto processSearch(String type);
    Page<VideoClawerInfo> getAll(Video_clawer_infoDto videoClawerInfoDto, int pageNO, int pageSize);
    VideoClawerInfo findById(Integer id);
    void delete(Integer id);
    void save(VideoClawerInfo videoClawerInfo);
}
