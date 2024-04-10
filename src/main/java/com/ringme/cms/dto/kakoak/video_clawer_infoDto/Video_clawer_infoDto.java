package com.ringme.cms.dto.kakoak.video_clawer_infoDto;

import lombok.Data;

import java.util.Date;

@Data
public class Video_clawer_infoDto {

    private Integer id_video_info;

    private String type;

    private String url;

    private Integer active;

    private String msisdn;

    private Integer categoryId;

    private Integer status;

    private String title;

    private Integer total_video;

    private Integer current_video;

    private Integer channel_id;

    private Date created_at;
    private String media_path;
}

