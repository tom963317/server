package com.pj.project.tb_card_info;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pj.project.bin_card.BinCard;
import com.pj.project.bin_card.BinCardMapper;
import com.pj.utils.so.SoMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pj.utils.sg.*;

/**
 * Service: tb_card_info --
 *
 * @author qzy
 */
@Service
public class TbCardInfoService extends ServiceImpl<TbCardInfoMapper, TbCardInfo> implements IService<TbCardInfo> {

    /**
     * 底层 Mapper 对象
     */
    @Autowired
    TbCardInfoMapper tbCardInfoMapper;


    /**
     * 查集合 - 根据条件（参数为空时代表忽略指定条件）
     */
    List<TbCardInfo> getList(SoMap so) {
        return tbCardInfoMapper.getList(so);
    }


    public TbCardInfo findByNo(String no) {
        QueryWrapper<TbCardInfo> ew = new QueryWrapper<>();
        ew.lambda().eq(TbCardInfo::getCarPrefix, no);
        List<TbCardInfo> list = list(ew);
        return list.isEmpty() ? null : list.get(0);
    }
}
