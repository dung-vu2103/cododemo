package com.ringme.cms.service.kakoak.youtube;

import com.ringme.cms.common.Helper;
import com.ringme.cms.dto.kakoak.video_clawer_infoDto.Video_clawer_infoDto;
import com.ringme.cms.model.kakoak.videoclawerinfo.VideoClawerInfo;
import com.ringme.cms.repository.kakoak.youtube.VideoClawerInfoRepository;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Log4j2
@Transactional(value = "kakoakTransactionManager")
public class VideoClawerInfoServiceimp implements VideoClaweInfoService {
    @Autowired
    VideoClawerInfoRepository youtubeRepository;
    @Override
    public Video_clawer_infoDto processSearch(String type) {
       Video_clawer_infoDto videoClawerInfoDto=new Video_clawer_infoDto();
       videoClawerInfoDto.setType(Helper.processStringSearch(type));
       return videoClawerInfoDto;
    }

    @Override
    public Page<VideoClawerInfo> getAll(Video_clawer_infoDto videoClawerInfoDto, int pageNO, int pageSize) {
        Pageable pageable = PageRequest.of(pageNO - 1, pageSize);

        return youtubeRepository.get(videoClawerInfoDto.getType(),pageable);
    }

    @Override
    public VideoClawerInfo findById(Integer id) {
        return youtubeRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(Integer id) {
        youtubeRepository.deleteById(id);
    }

    @Override
    public void save(VideoClawerInfo videoClawerInfo) {
        youtubeRepository.save(videoClawerInfo);
    }
}
