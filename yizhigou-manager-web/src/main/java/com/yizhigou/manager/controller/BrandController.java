package com.yizhigou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yizhigou.entity.PageResult;
import com.yizhigou.entity.Result;
import com.yizhigou.pojo.TbBrand;
import com.yizhigou.sellergoods.service.BrandService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("brand")
public class BrandController {

    @Reference
    BrandService brandService;

    @RequestMapping("getAllBrands")
    public List<TbBrand> getAllBrands() {

        List<TbBrand> allBrands = brandService.getAllBrands();
        return allBrands;
    }

    @RequestMapping("search")
    public PageResult findPage(@RequestBody TbBrand tbBrand, int page, int rows) {

        PageResult pageResult = brandService.findPage(tbBrand, page, rows);
        return pageResult;
    }

    @RequestMapping("add")
    public Result add(@RequestBody TbBrand brand) {

        try {

            Result result = brandService.add(brand);
            return result;
        } catch (Exception e) {

            e.printStackTrace();
            return new Result(false, "添加失败：" + e.getMessage());
        }

    }

    @RequestMapping("update")
    public Result update(@RequestBody TbBrand brand) {

        try {

            Result result = brandService.update(brand);
            return result;
        } catch (Exception e) {

            e.printStackTrace();
            return new Result(false, "修改失败：" + e.getMessage());
        }

    }

    @RequestMapping("findOne")
    public TbBrand findOne(int id){

        return brandService.findOne(id);
    }

    @RequestMapping("delete")
    public Result delete(Long[] ids) {


        try {

            brandService.delete(ids);
            return new Result(true, "修改成功");
        } catch (Exception e) {

            e.printStackTrace();
            return new Result(false, "修改失败：" + e.getMessage());
        }
    }

}
