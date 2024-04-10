package com.ringme.cms.model.kakoak.videoclawerinfo;

import com.ringme.cms.model.sys.EntityBaseKakoak;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "video_crawler_info")
public class VideoClawerInfo extends EntityBaseKakoak implements Serializable {
    private static final long serialVersionUID = -297553281792804396L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_video_info")
    private Integer id_video_info;
    @Column(name = "type")
    private String type;
    @Column(name = "url")
    private String url;
    @Column(name = "active")
    private Integer active;
    @Column(name = "msisdn")
    private String msisdn;
    @Column(name = "categoryId")
    private Integer categoryId;
    @Column(name = "status")
    private Integer status;
    @Column(name = "title")
    private String title;
    @Column(name = "total_video")
    private Integer total_video;
    @Column(name = "current_video")
    private Integer current_video;
    @Column(name = "channel_id")
    private Integer channel_id;
    @Column(name = "created_at")
    private Date created_at;
    @Column(name = "media_path")
    private Date media_path;

}
