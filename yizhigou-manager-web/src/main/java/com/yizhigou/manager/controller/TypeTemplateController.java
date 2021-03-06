package com.yizhigou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yizhigou.entity.PageResult;
import com.yizhigou.entity.Result;
import com.yizhigou.pojo.TbTypeTemplate;
import com.yizhigou.sellergoods.service.BrandService;
import com.yizhigou.sellergoods.service.SpecificationService;
import com.yizhigou.sellergoods.service.TypeTemplateService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/typeTemplate")
public class TypeTemplateController {

	@Reference
	private TypeTemplateService typeTemplateService;

	@Reference
	private BrandService brandService;

	@Reference
	private SpecificationService specificationService;


	@RequestMapping("findAll")
	public List<TbTypeTemplate> findAll(){ return typeTemplateService.findAll();}

	/**
	 * 增加
	 * @param typeTemplate
	 * @return
	 */
	@RequestMapping("/add")
	public Result add(@RequestBody TbTypeTemplate typeTemplate){
		try {
			typeTemplateService.add(typeTemplate);
			return new Result(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "增加失败");
		}
	}
	
	/**
	 * 修改
	 * @param typeTemplate
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody TbTypeTemplate typeTemplate){
		try {
			typeTemplateService.update(typeTemplate);
			return new Result(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "修改失败");
		}
	}	
	
	/**
	 * 获取实体
	 * @param id
	 * @return
	 */
	@RequestMapping("/findOne")
	public TbTypeTemplate findOne(Long id){
		return typeTemplateService.findOne(id);		
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public Result delete(Long [] ids){
		try {
			typeTemplateService.delete(ids);
			return new Result(true, "删除成功"); 
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "删除失败");
		}
	}
	
		/**
	 * 查询+分页
	 * @param typeTemplate
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbTypeTemplate typeTemplate, int page, int rows  ){
		return typeTemplateService.findPage(typeTemplate, page, rows);		
	}

	@RequestMapping("selectBrandOptionList")
	public List<Map> selectBrandOptionList() {

		List<Map> maps = brandService.selectBrandOptionList();
        //System.out.println(maps);
		return maps;
	}

	@RequestMapping("selectSpecificationOptionList")
	public List<Map> selectSpecificationOptionList(){

		List<Map> maps = specificationService.selectSpecificationOptionList();
        //System.out.println(maps);
		return maps;
	}
}
