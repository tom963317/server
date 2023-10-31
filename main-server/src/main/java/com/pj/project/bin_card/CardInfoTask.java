package com.pj.project.bin_card;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.pj.current.task.Task;
import com.pj.project.tb_card_info.TbCardInfo;
import com.pj.project.tb_card_info.TbCardInfoService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class CardInfoTask extends Task {
    private String carNo;

    public CardInfoTask(String id, String carNo, long delayInMilliseconds) {
        super(id, delayInMilliseconds);
        this.carNo = carNo;
    }

    @Override
    public void run() {
        log.info(" start to get car level:{}", carNo);
        TbCardInfoService tbCardInfoService = SpringUtil.getBean(TbCardInfoService.class);
        BinCardService binCardService = SpringUtil.getBean(BinCardService.class);
        String f = StrUtil.sub(carNo, 0, 6);
        TbCardInfo info = tbCardInfoService.findByNo(f);
        if (info != null) {
            BinCard binCard = binCardService.findByCardNo(carNo);
            String infoStr = info.getInfo();
            binCard.setInfo(infoStr).setCardType(info.getCardType())
                    .setRemark(info.getRemark())
                    .setDebit(info.getDebit())
                    .setCardLevel(info.getCardLevel());
            binCardService.updateById(binCard);
        }
    }
}
