package com.yizhigou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yizhigou.Extra.SpecificationExt;
import com.yizhigou.entity.PageResult;
import com.yizhigou.entity.Result;
import com.yizhigou.sellergoods.service.SpecificationService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("specification")
public class SpecificationController {

    @Reference
    SpecificationService specificationService;


    @RequestMapping("search")
    public PageResult findPage(@RequestBody SpecificationExt specificationExt, int page, int rows) {

        PageResult pageResult = specificationService.findPage(specificationExt, page, rows);
        return pageResult;
    }

    @RequestMapping("add")
    public Result add(@RequestBody SpecificationExt specificationExt) {

        try {

            Result result = specificationService.add(specificationExt);
            return result;
        } catch (Exception e) {

            e.printStackTrace();
            return new Result(false, "添加失败：" + e.getMessage());
        }

    }

    @RequestMapping("update")
    public Result update(@RequestBody SpecificationExt specificationExt) {

        try {

            Result result = specificationService.update(specificationExt);
            return result;
        } catch (Exception e) {

            e.printStackTrace();
            return new Result(false, "修改失败：" + e.getMessage());
        }

    }

    @RequestMapping("findOne")
    public SpecificationExt findOne(int id){

        return specificationService.findOne(id);
    }

    @RequestMapping("delete")
    public Result delete(Long[] ids) {

        try {

            specificationService.delete(ids);
            return new Result(true, "修改成功");
        } catch (Exception e) {

            e.printStackTrace();
            return new Result(false, "修改失败：" + e.getMessage());
        }
    }

}
