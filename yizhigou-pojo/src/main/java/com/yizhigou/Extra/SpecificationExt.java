package com.yizhigou.Extra;

import com.yizhigou.pojo.TbSpecification;
import com.yizhigou.pojo.TbSpecificationOption;

import java.io.Serializable;
import java.util.List;

/**
 * 这个类封装了Specification和SpecificationOptionList
 */
public class SpecificationExt implements Serializable {

    private TbSpecification tbSpecification;

    private List<TbSpecificationOption> tbSpecificationOptionList;

    public TbSpecification getTbSpecification() {
        return tbSpecification;
    }

    public void setTbSpecification(TbSpecification tbSpecification) {
        this.tbSpecification = tbSpecification;
    }

    public List<TbSpecificationOption> getTbSpecificationOptionList() {
        return tbSpecificationOptionList;
    }

    public void setTbSpecificationOptionList(List<TbSpecificationOption> tbSpecificationOptionList) {
        this.tbSpecificationOptionList = tbSpecificationOptionList;
    }

    public SpecificationExt() {

    }

    public SpecificationExt(TbSpecification tbSpecification, List<TbSpecificationOption> tbSpecificationOptionList) {

        this.tbSpecification = tbSpecification;
        this.tbSpecificationOptionList = tbSpecificationOptionList;
    }
}
