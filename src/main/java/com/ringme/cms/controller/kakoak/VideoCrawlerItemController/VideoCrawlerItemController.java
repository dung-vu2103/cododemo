package com.ringme.cms.controller.kakoak.VideoCrawlerItemController;

import com.ringme.cms.model.kakoak.videoclawerinfo.VideoClawerInfo;
import com.ringme.cms.model.kakoak.videocrawleritem.VideoCrawerItem;
import com.ringme.cms.service.kakoak.video_crawler_item.VideoCrawlerItemService;
import com.ringme.cms.service.kakoak.youtube.VideoClaweInfoService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
@Log4j2
@Controller
@RequestMapping("/video")
public class VideoCrawlerItemController {
    @Autowired
    VideoClaweInfoService videoClaweInfoService;
    @Autowired
    VideoCrawlerItemService videoCrawlerItemService;
    @Autowired
    private MessageSource messageSource;
    @GetMapping(value = {"/index", "/index/{page}"})
    public String getAllPage(@PathVariable(required = false) Integer page,
                             @RequestParam(name = "pageSize", required = false) Integer pageSize,
                             @RequestParam(name = "id_video_info") Integer id_video_info, Model model) {
        if(page == null)
            page = 1;
        if(pageSize == null)
            pageSize = 10;
        Page<VideoCrawerItem> objectPage = videoCrawlerItemService.get(id_video_info, page, pageSize);
        VideoClawerInfo user1 = videoClaweInfoService.findById(id_video_info);
        model.addAttribute("currentPage", page);
        model.addAttribute("id_video_info", id_video_info);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", objectPage.getTotalPages());
        model.addAttribute("totalItems", objectPage.getTotalElements());
        model.addAttribute("models", objectPage.toList());
        model.addAttribute("user1", user1);
        model.addAttribute("title", messageSource.getMessage("title.stickerItem", null, LocaleContextHolder.getLocale()));
        return "video/index";
    }
    @GetMapping("/create")
    public String create(@RequestParam(name = "id_video_info") Integer id_video_info, Model model) {
        VideoCrawerItem dto = new VideoCrawerItem();
        dto.setId_video_item(id_video_info);
        model.addAttribute("model", dto);
        model.addAttribute("title", messageSource.getMessage("title.sticker-item.create", null, LocaleContextHolder.getLocale()));
        return "video/form";
    }
//    @GetMapping("/update/{id}")
//    public String update(@PathVariable(name = "id") Integer id, Model model) {
//        VideoCrawerItem dto = videoCrawlerItemService.findById(id);
//        model.addAttribute("model", dto);
//        model.addAttribute("title", messageSource.getMessage("title.sticker-item.update", null, LocaleContextHolder.getLocale()));
//        return "video/form";
//    }
    @GetMapping(value = {"/delete", "/delete/{page}"})
    public String delete(@PathVariable(required = false) Integer page,
                         @RequestParam(name = "pageSize", required = false) Integer pageSize,
                         @RequestParam(name = "id_video_item", required = false) Integer id_video_item,
                         @RequestParam(name = "id_video_info", required = false) Integer id_video_info,
                         RedirectAttributes redirectAttributes) {
        if(page == null)
            page = 1;
        if(pageSize == null)
            pageSize = 10;
        log.info("iddd" +id_video_item);
        videoCrawlerItemService.delete(id_video_item);
        redirectAttributes.addFlashAttribute("success", messageSource.getMessage("title.delete.success", null, LocaleContextHolder.getLocale()));
        return "redirect:/video/index/" +page + "?pageSize=" + pageSize + "&id_video_info=" + id_video_info ;
    }
}
