package com.yizhigou.sellergoods.service;

import com.yizhigou.Extra.GoodsExt;
import com.yizhigou.entity.PageResult;
import com.yizhigou.pojo.TbGoods;

import java.util.List;
/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface GoodsService {

	/**
	 * 返回全部列表
	 * @return
	 */
	public List<GoodsExt> findAll();
	
	/**
	 * 增加
	*/
	public void add(GoodsExt goods);
	
	
	/**
	 * 修改
	 */
	public void update(GoodsExt goods);
	

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	public GoodsExt findOne(Long id);
	
	
	/**
	 * 批量删除
	 * @param ids
	 */
	public void delete(Long[] ids);

	/**
	 * 分页
	 * @param pageNum 当前页 码
	 * @param pageSize 每页记录数
	 * @param goods 条件
	 * @return
	 */
	public PageResult findPage(TbGoods goods, int pageNum, int pageSize);

	/**
	 * 批量修改状态
	 * @param ids
	 * @param status
	 */
	public void updateStatus(Long []ids,String status);
	
}
