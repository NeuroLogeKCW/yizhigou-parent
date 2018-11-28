package com.yizhigou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yizhigou.entity.PageResult;
import com.yizhigou.entity.Result;
import com.yizhigou.mapper.TbItemCatMapper;
import com.yizhigou.pojo.TbItemCat;
import com.yizhigou.pojo.TbItemCatExample;
import com.yizhigou.sellergoods.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 服务实现层
 *
 * @author Administrator
 */
@Service
public class ItemCatServiceImpl implements ItemCatService  {

    @Autowired
    private TbItemCatMapper itemCatMapper;

    /**
     * 查询全部
     */
    @Override
    public List<TbItemCat> findAll() {
        return itemCatMapper.selectByExample(null);
    }

    /**
     * 按分页查询
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbItemCat> page = (Page<TbItemCat>) itemCatMapper.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 增加
     */
    @Override
    public void add(TbItemCat itemCat) {
        itemCatMapper.insert(itemCat);
    }


    /**
     * 修改
     */
    @Override
    public void update(TbItemCat itemCat) {
        itemCatMapper.updateByPrimaryKey(itemCat);
    }

    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    @Override
    public TbItemCat findOne(Long id) {
        return itemCatMapper.selectByPrimaryKey(id);
    }

    /**
     * 批量删除
     */
    @Override
    public Result delete(Long[] ids) {
        for (Long id : ids) {
            ////判断这个自子下面还有没有别的属性
            TbItemCatExample tbItemCatExample = new TbItemCatExample();
            tbItemCatExample.createCriteria().andParentIdEqualTo(id);

            int count = itemCatMapper.countByExample(tbItemCatExample);

            if (count > 0) {
                return new Result(false, "部分删除失败，请检查当前项是否有子集");
            } else {
                //正常  可以删除
                itemCatMapper.deleteByPrimaryKey(id);
            }


            //itemCatMapper.deleteByPrimaryKey(id);


        }
        return new Result(true, "删除成功");
    }


    @Override
    public PageResult findPage(TbItemCat itemCat, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        TbItemCatExample example = new TbItemCatExample();
        TbItemCatExample.Criteria criteria = example.createCriteria();

        if (itemCat != null) {
            if (itemCat.getName() != null && itemCat.getName().length() > 0) {
                criteria.andNameLike("%" + itemCat.getName() + "%");
            }

        }

        Page<TbItemCat> page = (Page<TbItemCat>) itemCatMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public List<TbItemCat> getItemCatByParentId(Long parentId) {

        TbItemCatExample tbItemCatExample = new TbItemCatExample();
        tbItemCatExample.createCriteria().andParentIdEqualTo(parentId);
        List<TbItemCat> tbItemCats = itemCatMapper.selectByExample(tbItemCatExample);
        return tbItemCats;
    }

    @Override
    public TbItemCat getItemCatById(Long id) {

        return itemCatMapper.selectByPrimaryKey(id);
    }

}
