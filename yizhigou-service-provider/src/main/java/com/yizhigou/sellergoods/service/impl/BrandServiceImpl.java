package com.yizhigou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yizhigou.entity.PageResult;
import com.yizhigou.entity.Result;
import com.yizhigou.mapper.TbBrandMapper;
import com.yizhigou.pojo.TbBrand;
import com.yizhigou.pojo.TbBrandExample;
import com.yizhigou.sellergoods.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    TbBrandMapper tbBrandMapper;

    @Override
    public List<TbBrand> getAllBrands() {

        List<TbBrand> tbBrands = tbBrandMapper.selectByExample(new TbBrandExample());
        return tbBrands;
    }

    @Override
    public PageResult findPage(TbBrand tbBrand, int pageNum, int pageSize) {

        //设置起始页和每页个数
        PageHelper.startPage(pageNum, pageSize);
        //获得页数
        TbBrandExample brandExample = new TbBrandExample();
        TbBrandExample.Criteria criteria = brandExample.createCriteria();

        if (tbBrand != null){
            if (StringUtils.isEmpty(tbBrand.getFirstChar()) == false){

                criteria.andFirstCharEqualTo(tbBrand.getFirstChar());
            }

            if (StringUtils.isEmpty(tbBrand.getName()) == false) {

                criteria.andNameLike("%" + tbBrand.getName() + "%");
            }
        }

        Page<TbBrand> page = (Page<TbBrand>) tbBrandMapper.selectByExample(brandExample);
        //返回封装好的数据
        PageResult pageResult = new PageResult(page.getTotal(), page.getResult());
        return pageResult;
    }

    @Override
    public Result add(TbBrand tbBrand) {

        int row = tbBrandMapper.insert(tbBrand);

        if(row > 0){
            return new Result(true, "添加成功");
        }

        return new Result(false, "添加失败");
    }

    @Override
    public Result update(TbBrand tbBrand) {

        int row = tbBrandMapper.updateByPrimaryKey(tbBrand);

        if(row > 0){
            return new Result(true, "修改成功");
        }

        return new Result(false, "修改失败");
    }

    @Override
    public TbBrand findOne(long id) {

        return tbBrandMapper.selectByPrimaryKey(id);
    }

    @Override
    public void delete(Long[] ids) {

        //ids.forEach((id) -> tbBrandMapper.deleteByPrimaryKey(id));
        for (Long id : ids) {
            tbBrandMapper.deleteByPrimaryKey(id);
        }
    }

    @Override
    public List<Map> selectBrandOptionList() {

        List<Map> maps = tbBrandMapper.selectBrandOptionList();
        return maps;
    }
}
