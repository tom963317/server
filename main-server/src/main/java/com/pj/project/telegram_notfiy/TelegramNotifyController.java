package com.pj.project.telegram_notfiy;

import com.pj.utils.sg.AjaxJson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/telegramNotify/")
public class TelegramNotifyController {
    @Autowired
    private TelegramNotfiyService service;

    @RequestMapping({"getTelegramNotify"})
    public AjaxJson getTelegramNotify() {
        TelegramNotfiy notify = service.getNotify();
        return AjaxJson.getSuccessData(notify);
    }
    @RequestMapping({"updateTelegramNotify"})
    public AjaxJson updateTelegramNotify(@RequestBody TelegramNotfiy notify) {
        service.updateTelegramNotfiy(notify);
        return AjaxJson.getSuccess();
    }
}
