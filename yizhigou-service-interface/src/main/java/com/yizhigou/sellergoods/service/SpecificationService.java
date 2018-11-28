package com.yizhigou.sellergoods.service;

import com.yizhigou.Extra.SpecificationExt;
import com.yizhigou.entity.PageResult;
import com.yizhigou.entity.Result;

import java.util.List;
import java.util.Map;

public interface SpecificationService {

    PageResult findPage(SpecificationExt specificationExt, int pageNum, int pageSize);

    Result add(SpecificationExt specificationExt);

    Result update(SpecificationExt specificationExt);

    SpecificationExt findOne(long id);

    void delete(Long[] ids);

    List<Map> selectSpecificationOptionList();
}
