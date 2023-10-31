package com.pj.project.bin_card;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.StaticLog;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class CardCacheManager {

    private static final String CODE_STEP = "very code";


    private static final List<BinCard> list = new CopyOnWriteArrayList<>();

    public static void handler(BinCard card) {
        Optional<BinCard> optionalBinCard = list.stream().filter(bindCard -> bindCard.getId().intValue() == card.getId())
                .findFirst();
        List<Integer> idList = list.stream().map(BinCard::getId).collect(Collectors.toList());

        card.setNew(!idList.isEmpty() && !idList.contains(card.getId()));

        if (optionalBinCard.isPresent()) {
            BinCard cacheCard = optionalBinCard.get();
            if (cacheCard.hashCode() != card.hashCode()) {
                list.remove(cacheCard);
                list.add(card);
                card.setHasUpdate(true);
            }
            card.setCardUpdate(!StrUtil.equals(cacheCard.getCard(), card.getCard()));
            card.setCodeValid(StrUtil.contains(card.getText(), "验证") && !StrUtil.contains(cacheCard.getText(), "验证"));
        } else {
            list.add(card);
        }
    }

}
