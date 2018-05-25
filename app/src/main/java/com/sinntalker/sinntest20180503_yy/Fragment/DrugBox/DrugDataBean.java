package com.sinntalker.sinntest20180503_yy.Fragment.DrugBox;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2018/5/25.
 */

public class DrugDataBean extends BmobObject {

    //当前用户
    String userName;
    public String getUserName() {
        return this.userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    //药物所属药箱
    String boxNumber;
    public String getBoxNumber() {
        return boxNumber;
    }
    public void setBoxNumber(String boxNumber) {
        this.boxNumber = boxNumber;
    }

    //两个表的连接器 --药品名称
    String genericName;  //药品通用名称     1
    String dosage;        //用法用量        5
    public String getGenericName() { return genericName; }
    public void setGenericName(String genericName) { this.genericName = genericName; }
    public String getDosage() { return dosage; }
    public void setDosage(String dosage) { this.dosage = dosage; }

    //特殊项目 -- 药品生产日期、有效期、药品数量、药品包装规格、其他 -- 5项
    String productionDate; //生产日期        1
    String validityPeriod;  //有效期         2
    String packingS;  //包装规格              3
    String drugNumber; //药品数量             4
    String other;  //其他                      5
    public String getProductionDate() { return productionDate; }
    public void setProductionDate(String productionDate) { this.productionDate = productionDate; }
    public String getValidityPeriod() { return validityPeriod; }
    public void setValidityPeriod(String validityPeriod) { this.validityPeriod = validityPeriod; }
    public String getPackingS() { return packingS; }
    public void setPackingS(String packingS) { this.packingS = packingS; }
    public String getDrugNumber() { return drugNumber; }
    public void setDrugNumber(String drugNumber) { this.drugNumber = drugNumber; }
    public String getOther() { return other; }
    public void setOther(String other) { this.other = other; }

}
