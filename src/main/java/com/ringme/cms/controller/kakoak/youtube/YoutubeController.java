package com.ringme.cms.controller.kakoak.youtube;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ringme.cms.dto.kakoak.video_clawer_infoDto.Video_clawer_infoDto;
import com.ringme.cms.dto.kakoak.videocrawleritemDto.VideoCrawlerItemDto;
import com.ringme.cms.dto.kakoakcms.sticker.StickerDto;
import com.ringme.cms.model.kakoak.videoclawerinfo.VideoClawerInfo;
import com.ringme.cms.model.kakoak.videocrawleritem.VideoCrawerItem;
import com.ringme.cms.model.kakoakcms.sticker.Sticker;
import com.ringme.cms.service.kakoak.video_crawler_item.VideoCrawlerItemService;
import com.ringme.cms.service.kakoak.youtube.Validation;
import com.ringme.cms.service.kakoak.youtube.VideoClaweInfoService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.PatternSyntaxException;

@Log4j2
@Controller
@RequestMapping("/youtube")
public class YoutubeController {
    @Autowired
    VideoClaweInfoService youtubeService;
    @Autowired
    VideoCrawlerItemService videoCrawlerItemService;
    @Autowired
    private MessageSource messageSource;

    @GetMapping(value = {"/get", "/get/{page}"})
    private String index(@PathVariable(required = false) Integer page,
                         @RequestParam(name = "pageSize", required = false) Integer pageSize,
                         @RequestParam(value = "type", required = false) String type
            , Model model) {
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }

        Video_clawer_infoDto video_clawer_infoDto = youtubeService.processSearch(type);

        Page<VideoClawerInfo> oblectPage = youtubeService.getAll(video_clawer_infoDto, page, pageSize);
        List<VideoClawerInfo> videoClawerInfos = oblectPage.toList();
        model.addAttribute("currentPage", page);
        model.addAttribute("type", type);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", oblectPage.getTotalPages());
        model.addAttribute("totalItems", oblectPage.getTotalElements());
        model.addAttribute("videoClawerInfos", videoClawerInfos);
        return "youtube/index";
    }

    @GetMapping("/view/{id}")
    public String detail(@PathVariable(name = "id") Integer id, Model model) {
        VideoClawerInfo object = youtubeService.findById(id);
        model.addAttribute("model", object);
        return "youtube/index::view_detail";
    }

    @GetMapping(value = {"/delete", "/delete/{page}"})
    public String delete(@PathVariable(required = false) Integer page,
                         @RequestParam(name = "pageSize", required = false) Integer pageSize,
                         @RequestParam(name = "id", required = false) Integer id,
                         RedirectAttributes redirectAttributes) {
        if (page == null)
            page = 1;
        if (pageSize == null)
            pageSize = 10;
        try {
            youtubeService.delete(id);
            redirectAttributes.addFlashAttribute("success", messageSource.getMessage("title.delete.success", null, LocaleContextHolder.getLocale()));
        } catch (Exception e) {
            log.error("ERROR" + e.getMessage(), e);
        }
        return "redirect:/youtube/get/" + page + "?pageSize=" + pageSize;
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("model") Video_clawer_infoDto dto, Errors error, RedirectAttributes redirectAttributes) {
        log.info("---SAVE DTO---|" + dto);
        if (!error.hasErrors()) {
            VideoClawerInfo object = new VideoClawerInfo();
            if (dto.getId_video_info() == null) {
                redirectAttributes.addFlashAttribute("success", messageSource.getMessage("title.create.success", null, LocaleContextHolder.getLocale()));
            } else {
                object = youtubeService.findById(dto.getId_video_info());
                redirectAttributes.addFlashAttribute("success", messageSource.getMessage("title.update.success", null, LocaleContextHolder.getLocale()));
            }
            object.setType(dto.getType());
            object.setUrl(dto.getUrl());
            object.setMsisdn(dto.getMsisdn());
            object.setCategoryId(dto.getCategoryId());
            object.setTotal_video(dto.getTotal_video());
            youtubeService.save(object);
        } else {
            log.error("ERROR|Save|" + error);
            if (dto.getId_video_info() == null)
                return "redirect:/youtube/create";
            else
                return "redirect:/youtube/update/" + dto.getId_video_info();
        }
        if (dto.getId_video_info() == null)
            redirectAttributes.addFlashAttribute("success", messageSource.getMessage("title.create.success", null, LocaleContextHolder.getLocale()));
        else
            redirectAttributes.addFlashAttribute("success", messageSource.getMessage("title.update.success", null, LocaleContextHolder.getLocale()));
        return "redirect:/youtube/get";
    }

    @GetMapping("/create")
    public String create(Model model) throws NoSuchMessageException {
        VideoClawerInfo dto = new VideoClawerInfo();
        model.addAttribute("model", dto);
        model.addAttribute("title", messageSource.getMessage("title.sticker.create", null, LocaleContextHolder.getLocale()));
        return "youtube/form";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable(name = "id") Integer id, Model model) {
        VideoClawerInfo dto = youtubeService.findById(id);
        model.addAttribute("model", dto);
        model.addAttribute("title", messageSource.getMessage("title.sticker.update", null, LocaleContextHolder.getLocale()));
        return "youtube/form";
    }


    @GetMapping("/save1")
    public String download(@RequestParam("url") String url, @RequestParam("video_crawler_info_id") Integer video_crawler_info_id,
                        @RequestParam("total_video") Integer total_video,RedirectAttributes redirectAttributes) {
        if (url.contains("https://www.youtube.com/watch?v=")) {
            boolean check = false;
            List<String> checkUrl = videoCrawlerItemService.getUrl();
            for (String u : checkUrl) {
                if (u.contains(url)) {
                    check = true;
                    break;
                }
            }
            if (!check) {
                String commandTemplate = "D:/luu/yt-dlp.exe -o D:/luu/%(title)s.%(ext)s --sleep-interval 60 " + url; //--sleep-interval 360
                try {
                    Process proc = Runtime.getRuntime().exec(commandTemplate);

                    BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                    List<String> lines = new ArrayList<>();
                    String s;
                    while ((s = reader.readLine()) != null) {
                        lines.add(s);
                    }

                    log.info("mediaaa" + lines);
                    String mediaPath = "";
                    for (String path : lines) {
                        if (path.contains("Destination: ")) {
                            mediaPath = path;
                        }
                    }
                    VideoCrawerItem videoCrawler = new VideoCrawerItem();
                    log.info("1111117 ");
                    videoCrawler.setDownload_time(new Date());
                    videoCrawler.setUrl_video_item(url);
                    videoCrawler.setId_video_info(video_crawler_info_id);
                    int count1 = mediaPath.indexOf(":");
                    videoCrawler.setMedia_path(mediaPath.substring(count1 + 1).trim());
                    log.info("wwwwwwww " + mediaPath.substring(count1 + 1).trim());
                    videoCrawler.setStatus(2);
                    int count2 = mediaPath.lastIndexOf("\\");
                    videoCrawler.setTitle(Validation.validateFileName(mediaPath.substring(count2 + 1).trim()));
                    videoCrawler.setVideo_crawler_info_id(video_crawler_info_id);
                    log.info("11111167890-98765432");
                    videoCrawlerItemService.save(videoCrawler);
                } catch (Exception e) {
                    log.info("FALSE : " + e);

                }
            }

        } else {
            String commandTemplate = "D:/luu/yt-dlp.exe --skip-download --flat-playlist --dump-json --playlist-start 1 --playlist-end "+total_video+"  " + url; //--sleep-interval 360

            try {
                Process proc = Runtime.getRuntime().exec(commandTemplate);
                log.info("11111111122");
                BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                log.info("111111111111111");
                List<String> lines = new ArrayList<>();
                String s;
                while ((s = reader.readLine()) != null) {
                    lines.add(s);
                    log.info("11111131111111111111111111");
                }
                log.info("1111113" +lines );
                List<String> idVideo = new ArrayList<>(); // find ID video
                ObjectMapper objectMapper = new ObjectMapper();
                for (String line : lines) {
                    JsonNode jsonNode = objectMapper.readTree(line);
                    String id = jsonNode.get("id").asText();
                    log.info("idddddd" + id);
                    idVideo.add(id);
                }
                log.info("IdVide|DATA|" + idVideo);
                List<String> checkId = videoCrawlerItemService.getId();

                for (String id : idVideo) {
                    if (checkId.contains(id)) {
                        continue;
                    } else {
                        String commandTemplate1 = "D:/luu/yt-dlp.exe -o D:/luu/%(title)s.%(ext)s --sleep-interval 60 %SOURCE_PATH%"; //--sleep-interval 360
                        String command1 = commandTemplate1.replace("%SOURCE_PATH%", "https://www.youtube.com/watch?v=" + id);
                        Process proc1 = Runtime.getRuntime().exec(command1);

                        log.info("1111115");
                        BufferedReader reader1 = new BufferedReader(new InputStreamReader(proc1.getInputStream()));
                        List<String> line1s = new ArrayList<>();
                        String l;
                        while ((l = reader1.readLine()) != null) {
                            line1s.add(l);
                        }
                        log.info("w" + line1s);
                        String mediaPath = ""; //Find String contains [download] Destination:
                        for (String path : line1s) {
                            if (path.contains("[download] D:\\luu\\")) {
                                mediaPath = path;
                            } else if (path.contains("Destination: ")) {
                                mediaPath = path;
                            }
                        }
                        log.info("media" + mediaPath);
                        log.info("1111116 ");
                        VideoCrawerItem videoCrawler = new VideoCrawerItem();
                        log.info("1111117 ");
                        videoCrawler.setDownload_time(new Date());
                        videoCrawler.setId_video_info(video_crawler_info_id);
                        int count1 = mediaPath.indexOf(":");
                        videoCrawler.setMedia_path(mediaPath.substring(count1 + 1).trim());
                        log.info("wwwwwwww " + mediaPath.substring(count1 + 1).trim());
                        videoCrawler.setStatus(2);
                        int count2 = mediaPath.lastIndexOf("\\");
                        videoCrawler.setTitle(Validation.validateFileName(mediaPath.substring(count2 + 1).trim()));
                        videoCrawler.setUrl_video_item("https://www.youtube.com/watch?v=" + id);
                        videoCrawler.setId_string(id);
                        videoCrawler.setVideo_crawler_info_id(video_crawler_info_id);
                        log.info("11111155555 ");
                        log.info("11111167890-98765432");

                        videoCrawlerItemService.save(videoCrawler);
                    }
                }
            } catch (Exception e) {
                log.error("error" + e.getMessage(), e);
            }
        }
        redirectAttributes.addFlashAttribute("success", messageSource.getMessage("success.download.create", null, LocaleContextHolder.getLocale()));
        return "redirect:/youtube/get";
    }
}
