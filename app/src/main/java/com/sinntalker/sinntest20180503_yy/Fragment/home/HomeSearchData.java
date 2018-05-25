package com.sinntalker.sinntest20180503_yy.Fragment.home;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Table;
import com.litesuits.orm.db.enums.AssignType;

/**
 * Created by Administrator on 2018/5/25.
 */

@Table("HomeSearch")
public class HomeSearchData {

    // 指定自增，每个对象需要有一个主键
    @PrimaryKey(AssignType.AUTO_INCREMENT)
    private int id;

    //当前用户、药品名、药箱
    @Column("CurrentUser")
    private String currentUser;
    @Column("DrugName")
    private String drugName;
    @Column("DrugBoxNum")
    private String drugBoxNum;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getDrugBoxNum() {
        return drugBoxNum;
    }

    public void setDrugBoxNum(String drugBoxNum) {
        this.drugBoxNum = drugBoxNum;
    }

    @Override
    public String toString() {
        return "HomeSearchData{" +
                "id=" + id +
                ", CurrentUser='" + currentUser + '\'' +
                ", DrugName='" + drugName + '\'' +
                ", DrugBoxNum='" + drugBoxNum + '\'' +
                '}';
    }

}
