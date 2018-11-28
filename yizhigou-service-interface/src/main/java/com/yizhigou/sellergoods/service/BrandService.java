package com.yizhigou.sellergoods.service;

import com.yizhigou.entity.PageResult;
import com.yizhigou.entity.Result;
import com.yizhigou.pojo.TbBrand;

import java.util.List;
import java.util.Map;

public interface BrandService {

    List<TbBrand> getAllBrands();

    PageResult findPage(TbBrand tbBrand, int pageNum, int pageSize);

    Result add(TbBrand tbBrand);

    Result update(TbBrand tbBrand);

    TbBrand findOne(long id);

    void delete(Long[] ids);

    List<Map> selectBrandOptionList();
}
