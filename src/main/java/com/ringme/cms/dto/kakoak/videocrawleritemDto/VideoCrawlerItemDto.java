package com.ringme.cms.dto.kakoak.videocrawleritemDto;

import lombok.Data;

import javax.persistence.Column;
import java.util.Date;
@Data
public class VideoCrawlerItemDto {
    private Integer id_video_item;

    private String url_video_item;

    private String title;

    private Date download_time;

    private Integer video_crawler_info_id;

    private String media_path;

    private Integer status;

    private Integer id_video_info;
    private String id_string;
}
