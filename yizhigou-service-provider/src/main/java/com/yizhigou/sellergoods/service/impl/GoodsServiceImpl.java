package com.yizhigou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.yizhigou.Extra.GoodsExt;
import com.yizhigou.entity.PageResult;
import com.yizhigou.mapper.*;
import com.yizhigou.pojo.TbItem;
import com.yizhigou.sellergoods.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    TbGoodsDescMapper tbGoodsDescMapper;

    @Autowired
    TbGoodsMapper tbGoodsMapper;

    @Autowired
    TbItemMapper tbItemMapper;

    @Autowired
    TbItemCatMapper tbItemCatMapper;

    @Autowired
    TbBrandMapper tbBrandMapper;

    @Autowired
    TbSellerMapper sellerMapper;

    @Override
    public List<GoodsExt> findAll() {
        return null;
    }

    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        return null;
    }

    @Override
    public void add(GoodsExt goods) {

        goods.getTbGoods().setAuditStatus("0");//设置未申请状态
        tbGoodsMapper.insert(goods.getTbGoods());
        goods.getTbGoodsDesc().setGoodsId(goods.getTbGoods().getId());//设置ID
        tbGoodsDescMapper.insert(goods.getTbGoodsDesc());//插入商品扩展数据


        //判断 是否开启了SKU 如果没有则只有一条默认信息
        List<TbItem> itemList = goods.getItemList();
        if ("1".equals(goods.getTbGoods().getIsEnableSpec())) {//开启了sku

            for (TbItem item : itemList) {
                //设置标题
                String tittle = goods.getTbGoods().getGoodsName();
                Map specItems = (Map) JSON.parse(item.getSpec());
                for (Object o : specItems.values()) {

                    tittle += " " + o;
                }
                item.setTitle(tittle);

                //补全其他数据
                setItemPropertitis(goods, item);


                tbItemMapper.insert(item);

            }

        } else {//没有启用分类

            //new一个新的item
            TbItem item = new TbItem();

            //设置标题
            String tittle = goods.getTbGoods().getGoodsName();
            item.setTitle(tittle);
            //补全价格
            item.setPrice(goods.getTbGoods().getPrice());
            item.setNum(9999);

            //补全其他数据
            setItemPropertitis(goods, item);

            tbItemMapper.insert(item);
        }
    }

    private void setItemPropertitis(GoodsExt goods, TbItem item) {
        //设置图片
        List<Map> imageMaps = JSON.parseArray(goods.getTbGoodsDesc().getItemImages(), Map.class);

        if (imageMaps != null && imageMaps.size() > 0){
            item.setImage((String) imageMaps.get(0).get("url"));
        }

        //设置品牌
        item.setBrand(tbBrandMapper.selectByPrimaryKey(goods.getTbGoods().getBrandId()).getName());

        //设置卖家所属ID
        item.setSellerId(goods.getTbGoods().getSellerId());
        //设置卖家名称
        item.setSeller(sellerMapper.selectByPrimaryKey(goods.getTbGoods().getSellerId()).getNickName());

        //设置分类
        item.setCategoryid(goods.getTbGoods().getCategory3Id());
        item.setCategory(tbItemCatMapper.selectByPrimaryKey(goods.getTbGoods().getBrandId()).getName());

        //设置上商品关联ID
        item.setGoodsId(goods.getTbGoods().getId());

        //设置时间
        item.setCreateTime(new Date());
        item.setUpdateTime(new Date());
    }

    @Override
    public void update(GoodsExt goods) {

    }

    @Override
    public GoodsExt findOne(Long id) {
        return null;
    }


    @Override
    public void delete(Long[] ids) {

    }

    @Override
    public PageResult findPage(GoodsExt goods, int pageNum, int pageSize) {
        return null;
    }
}
