package com.yizhigou.sellergoods.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.yizhigou.Extra.GoodsExt;
import com.yizhigou.entity.PageResult;
import com.yizhigou.mapper.*;
import com.yizhigou.pojo.*;
import com.yizhigou.sellergoods.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
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
    public PageResult findPage(TbGoods goods, int pageNum, int pageSize) {

        PageHelper.startPage(pageNum, pageSize);

        TbGoodsExample example=new TbGoodsExample();
        TbGoodsExample.Criteria criteria = example.createCriteria();
        //商家只能查询自己的商品
        if(goods!=null){
            if(goods.getSellerId()!=null && goods.getSellerId().length()>0){
                criteria.andSellerIdEqualTo(goods.getSellerId());
            }
            if(goods.getGoodsName()!=null && goods.getGoodsName().length()>0){
                criteria.andGoodsNameLike("%"+goods.getGoodsName()+"%");
            }
            if(goods.getAuditStatus()!=null && goods.getAuditStatus().length()>0){
                criteria.andAuditStatusLike("%"+goods.getAuditStatus()+"%");
            }
            if(goods.getIsMarketable()!=null && goods.getIsMarketable().length()>0){
                criteria.andIsMarketableLike("%"+goods.getIsMarketable()+"%");
            }
            if(goods.getCaption()!=null && goods.getCaption().length()>0){
                criteria.andCaptionLike("%"+goods.getCaption()+"%");
            }
            if(goods.getSmallPic()!=null && goods.getSmallPic().length()>0){
                criteria.andSmallPicLike("%"+goods.getSmallPic()+"%");
            }
            if(goods.getIsEnableSpec()!=null && goods.getIsEnableSpec().length()>0){
                criteria.andIsEnableSpecLike("%"+goods.getIsEnableSpec()+"%");
            }
            if(goods.getIsDelete()!=null && goods.getIsDelete().length()>0){
                criteria.andIsDeleteLike("%"+goods.getIsDelete()+"%");
            }

        }

        Page<TbGoods> page= (Page<TbGoods>)tbGoodsMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void updateStatus(Long[] ids, String status) {

        for(Long id:ids){
            TbGoods goods = tbGoodsMapper.selectByPrimaryKey(id);
            goods.setAuditStatus(status);
            tbGoodsMapper.updateByPrimaryKey(goods);
        }
    }

    @Override
    public void add(GoodsExt goods) {

        goods.getTbGoods().setAuditStatus("0");//设置未申请状态
        tbGoodsMapper.insert(goods.getTbGoods());
        goods.getTbGoodsDesc().setGoodsId(goods.getTbGoods().getId());//设置ID
        tbGoodsDescMapper.insert(goods.getTbGoodsDesc());//插入商品扩展数据


        //判断 是否开启了SKU 如果没有则只有一条默认信息
        addAllItemList(goods);
    }

    private void addAllItemList(GoodsExt goods) {
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

        tbGoodsMapper.updateByPrimaryKey(goods.getTbGoods());
        tbGoodsDescMapper.updateByPrimaryKey(goods.getTbGoodsDesc());

        //删除item中原来的数据
        List<TbItem> itemList = goods.getItemList();
        for (TbItem tbItem : itemList) {
            tbItemMapper.deleteByPrimaryKey(tbItem.getId());
        }

        //重新添加新的数据
        addAllItemList(goods);
    }

    @Override
    public GoodsExt findOne(Long id) {

        TbGoods tbGoods = tbGoodsMapper.selectByPrimaryKey(id);
        TbGoodsDesc tbGoodsDesc = tbGoodsDescMapper.selectByPrimaryKey(id);

        TbItemExample tbItemExample = new TbItemExample();
        tbItemExample.createCriteria().andGoodsIdEqualTo(id);
        List<TbItem> tbItems = tbItemMapper.selectByExample(tbItemExample);

        GoodsExt goodsExt = new GoodsExt();
        goodsExt.setTbGoods(tbGoods);
        goodsExt.setTbGoodsDesc(tbGoodsDesc);
        goodsExt.setItemList(tbItems);
        return goodsExt;
    }


    @Override
    public void delete(Long[] ids) {

    }
}
