package com.ringme.cms.model.kakoak.videocrawleritem;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Entity
@ToString
@Table(name = "video_crawler_item")
public class VideoCrawerItem implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_video_item")
    private Integer id_video_item;
    @Column(name = "url_video_item")
    private String url_video_item;
    @Column(name = "title")
    private String title;
    @Column(name = "download_time")
    private Date download_time;
    @Column(name = "video_crawler_info_id")
    private Integer video_crawler_info_id;
    @Column(name = "status")
    private Integer status;
    @Column(name = "media_path")
    private String media_path;
    @Column(name = "id_video_info")
    private Integer id_video_info;
    @Column(name = "id_string")
    private String id_string;


}
