package com.pj.project4sp.admin;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.pj.utils.so.SoMap;
import org.apache.ibatis.annotations.Param;

/**
 * Mapper: 系统管理员表
 * @author kong
 */
@Mapper
public interface SpAdminMapper {


	/**
	 * 增 #{name}, #{password}, #{roleId} 
	 * @param obj
	 * @return
	 */
	int add(SpAdmin obj);

	/**
	 * 删 
	 * @param id
	 * @return
	 */
	int delete(long id);

	/**
	 * 改 
	 * @param obj
	 * @return
	 */
	int update(SpAdmin obj);

	/**
	 * 查 
	 * @param id
	 * @return
	 */
	SpAdmin getById(long id);

	/**
	 * 查  
	 * @param so
	 * @return
	 */
	List<SpAdmin> getList(SoMap so);

	/**
	 * 查询，根据name 
	 * @param name
	 * @return
	 */
	SpAdmin getByName(String name);
	
	/**
	 * 查询，根据 phone  
	 * @param phone
	 * @return
	 */
	SpAdmin getByPhone(String phone);


    void addDou(@Param("id") Long id, @Param("num")int num);

    void updateCheckNum(SpAdmin spAdmin);

    void resetCheckNum(int initCheck);

    SpAdmin findByOpenid(@Param("openid") String openid);

	void addCheck(@Param("openId")String openId, @Param("checkNum")Long checkNum);

	List<SpAdmin> getAccountAdminList();

    void updatePhone(@Param("id") Long id, @Param("phone") String phone);



	void updateAccountAdmin(@Param("id")Long id,@Param("roleIds") String roleIds,@Param("accountIds") String accountIds);

	List<SpAdmin> list();
}
