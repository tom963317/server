package com.pj.project.bin_card;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.pj.utils.so.*;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * Mapper: bin_card -- 
 * @author qzy 
 */

@Mapper
@Repository
public interface BinCardMapper extends BaseMapper <BinCard> {

	/**
	 * 查集合 - 根据条件（参数为空时代表忽略指定条件）
	 * @param so 参数集合 
	 * @return 数据列表 
	 */
	List<BinCard> getList(SoMap so);


}
