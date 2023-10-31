package com.pj.project.tb_card_info;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.text.csv.CsvData;
import cn.hutool.core.text.csv.CsvReader;
import cn.hutool.core.text.csv.CsvUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.sax.handler.RowHandler;
import com.pj.project.bin_card.BinCard;
import com.pj.project.bin_card.BinCardService;
import com.pj.utils.so.SoMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.pj.utils.sg.*;
import com.pj.project4sp.SP;

import com.pj.current.satoken.StpUserUtil;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;


/**
 * Controller: tb_card_info --
 *
 * @author qzy
 */
@RestController
@RequestMapping("/TbCardInfo/")
public class TbCardInfoController {

    /**
     * 底层 Service 对象
     */
    @Autowired
    TbCardInfoService tbCardInfoService;
    @Resource
    private BinCardService binCardService;

    /**
     * 查集合 - 根据条件（参数为空时代表忽略指定条件）
     */
    @RequestMapping("getList")
    public AjaxJson getList() {
        SoMap so = SoMap.getRequestSoMap();
        List<TbCardInfo> list = tbCardInfoService.getList(so.startPage());
        return AjaxJson.getPageData(so.getDataCount(), list);
    }

    @RequestMapping("delete")
    public AjaxJson delete(String id) {
        StpUtil.checkLogin();
        TbCardInfo info = tbCardInfoService.getById(id);
        if (info != null) {
            info.setDeleteFlag(0);
            tbCardInfoService.updateById(info);
        }
        return AjaxJson.getSuccess();
    }

    @RequestMapping("update")
    public AjaxJson update(TbCardInfo tbCardInfo) {
        StpUtil.checkLogin();
        TbCardInfo info = tbCardInfoService.getById(tbCardInfo.getId());
        if (info != null) {
            tbCardInfoService.updateById(tbCardInfo);
            List<BinCard> cardList = binCardService.findByCardNoPrefix(tbCardInfo.getCarPrefix());
            cardList.parallelStream().forEach(binCard -> binCard.setRemark(tbCardInfo.getRemark()));
            binCardService.updateBatchById(cardList);
        }
        return AjaxJson.getSuccess();
    }

    @RequestMapping("upload")
    public AjaxJson upload(MultipartFile file) {
        try (InputStream is = file.getInputStream()) {
            //   List<CardCSVDTO> cardCSVDTOS = ExcelUtil.getReader(is).readAll(CardCSVDTO.class);
            ExcelUtil.readBySax(is, 0, createRowHandler());
        } catch (Exception e) {
            e.printStackTrace();
        }


        return AjaxJson.getSuccess();
    }

    List<TbCardInfo> list = new ArrayList<>();

    private RowHandler createRowHandler() {
        return new RowHandler() {
            @Override
            public void handle(int sheetIndex, long rowIndex, List<Object> rowlist) {
                String cardStr = rowlist.get(0).toString();
                String cardType = rowlist.get(1).toString();
                String DEBIT = rowlist.get(2).toString();
                String cardLevel = rowlist.get(3).toString();
                TbCardInfo info = new TbCardInfo();
                info.setCarPrefix(cardStr).setCardType(cardType).setDebit(DEBIT).setCardLevel(cardLevel);
//                tbCardInfoService.save(info);
                list.add(info);
                if (list.size() == 2000) {
                    tbCardInfoService.saveBatch(list);
                    list.clear();
                }
                Console.log("[{}] [{}] {}", sheetIndex, rowIndex, rowlist);
            }
        };
    }

}
