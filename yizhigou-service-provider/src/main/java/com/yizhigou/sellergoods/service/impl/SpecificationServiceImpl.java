package com.yizhigou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yizhigou.Extra.SpecificationExt;
import com.yizhigou.entity.PageResult;
import com.yizhigou.entity.Result;
import com.yizhigou.mapper.TbSpecificationMapper;
import com.yizhigou.mapper.TbSpecificationOptionMapper;
import com.yizhigou.pojo.TbSpecification;
import com.yizhigou.pojo.TbSpecificationExample;
import com.yizhigou.pojo.TbSpecificationOption;
import com.yizhigou.pojo.TbSpecificationOptionExample;
import com.yizhigou.sellergoods.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

@Service
public class SpecificationServiceImpl implements SpecificationService {

    @Autowired
    TbSpecificationMapper tbSpecificationMapper;

    @Autowired
    TbSpecificationOptionMapper tbSpecificationOptionMapper;


    @Override
    public PageResult findPage(SpecificationExt specificationExt, int pageNum, int pageSize) {

        //设置起始页和每页个数
        PageHelper.startPage(pageNum, pageSize);

        TbSpecificationExample specificationExample = new TbSpecificationExample();
        TbSpecificationExample.Criteria criteria = specificationExample.createCriteria();
        if (specificationExt != null && specificationExt.getTbSpecification() != null
                && !StringUtils.isEmpty(specificationExt.getTbSpecification().getSpecName())) {

            criteria.andSpecNameLike("%" + specificationExt.getTbSpecification().getSpecName() + "%");
        }

        //获得页数
        Page<TbSpecification> page = (Page<TbSpecification>) tbSpecificationMapper.selectByExample(specificationExample);
        //返回封装好的数据
        PageResult pageResult = new PageResult(page.getTotal(), page.getResult());
        return pageResult;
    }

    @Override
    public Result add(SpecificationExt specificationExt) {

        tbSpecificationMapper.insert(specificationExt.getTbSpecification());

        for (TbSpecificationOption tbSpecificationOption : specificationExt.getTbSpecificationOptionList()) {

            tbSpecificationOption.setSpecId(specificationExt.getTbSpecification().getId());
            tbSpecificationOptionMapper.insertSelective(tbSpecificationOption);
        }

        return new Result(true, "成功");
    }

    @Override
    public Result update(SpecificationExt specificationExt) {

        //增加规格
        tbSpecificationMapper.updateByPrimaryKeySelective(specificationExt.getTbSpecification());

        //删除原来的规格项
        TbSpecificationOptionExample tbSpecificationOptionExample = new TbSpecificationOptionExample();
        TbSpecificationOptionExample.Criteria criteria = tbSpecificationOptionExample.createCriteria();
        criteria.andSpecIdEqualTo(specificationExt.getTbSpecification().getId());
        tbSpecificationOptionMapper.deleteByExample(tbSpecificationOptionExample);

        //增加新的规格项
        for (TbSpecificationOption tbSpecificationOption : specificationExt.getTbSpecificationOptionList()) {

            tbSpecificationOption.setSpecId(specificationExt.getTbSpecification().getId());
            tbSpecificationOptionMapper.insertSelective(tbSpecificationOption);
        }

        return new Result(true, "成功");
    }

    @Override
    public SpecificationExt findOne(long id) {

        //查找当前的规格
        TbSpecification tbSpecification = tbSpecificationMapper.selectByPrimaryKey(id);

        //查询对应的规格项
        TbSpecificationOptionExample tbSpecificationOptionExample = new TbSpecificationOptionExample();
        TbSpecificationOptionExample.Criteria criteria = tbSpecificationOptionExample.createCriteria();
        criteria.andSpecIdEqualTo(id);
        List<TbSpecificationOption> tbSpecificationOptions = tbSpecificationOptionMapper.selectByExample(tbSpecificationOptionExample);


        return new SpecificationExt(tbSpecification, tbSpecificationOptions);
    }

    @Override
    public void delete(Long[] ids) {

        for (Long id : ids) {

            tbSpecificationMapper.deleteByPrimaryKey(id);

            TbSpecificationOptionExample tbSpecificationOptionExample = new TbSpecificationOptionExample();
            TbSpecificationOptionExample.Criteria criteria = tbSpecificationOptionExample.createCriteria();
            criteria.andSpecIdEqualTo(id);
            tbSpecificationOptionMapper.deleteByExample(tbSpecificationOptionExample);
        }
    }

    @Override
    public List<Map> selectSpecificationOptionList() {

        List<Map> maps = tbSpecificationMapper.selectSpecificationOptionList();
        return maps;
    }
}
