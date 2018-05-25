package com.sinntalker.sinntest20180503_yy.Fragment.DrugBox;

/**
 * Created by Administrator on 2018/5/25.
 */

public class DrugCommonDataBean extends DrugDataBean {

    //通用项目 -- 药品名称、药品性状、成份、适应症、用法用量、不良反应、禁忌、注意事项
    // -- 药物相互作用、临床试验、毒理研究、批准文号、生产企业、药物分类 -- 14项
//    String genericName;  //药品通用名称     1
    String traits;   //药品性状             2
    String ingredients; //药品成份         3
    String indications;  //适应症          4
//    String dosage;        //用法用量        5
    String adverseReactions;  //不良反应   6
    String taboo;  //禁忌                   7
    String precautions;  //注意事项        8
    String interaction; //药物相互作用     9
    String clinicalTrials;  //临床试验    10
    String tResearch; //毒理研究           11
    String approvalNumber; //批准文号     12
    String manufacturer; //生产企业       13
    String classification; //药物分类     14
//    public String getGenericName() { return genericName; }
//    public void setGenericName(String genericName) { this.genericName = genericName; }
    public String getTraits() { return traits; }
    public void setTraits(String traits) { this.traits = traits; }
    public String getIngredients() { return ingredients; }
    public void setIngredients(String ingredients) { this.ingredients = ingredients; }
    public String getIndications() { return indications; }
    public void setIndications(String indications) { this.indications = indications; }
//    public String getDosage() { return dosage; }
//    public void setDosage(String dosage) { this.dosage = dosage; }
    public String getAdverseReactions() { return adverseReactions; }
    public void setAdverseReactions(String adverseReactions) { this.adverseReactions = adverseReactions; }
    public String getTaboo() { return taboo; }
    public void setTaboo(String taboo) { this.taboo = taboo; }
    public String getPrecautions() { return precautions; }
    public void setPrecautions(String precautions) { this.precautions = precautions; }
    public String getInteraction() { return interaction; }
    public void setInteraction(String interaction) { this.interaction = interaction; }
    public String getClinicalTrials() { return clinicalTrials; }
    public void setClinicalTrials(String clinicalTrials) { this.clinicalTrials = clinicalTrials; }
    public String getTResearch() { return tResearch; }
    public void setTResearch(String tResearch) { this.tResearch = tResearch; }
    public String getApprovalNumber() { return approvalNumber; }
    public void setApprovalNumber(String approvalNumber) { this.approvalNumber = approvalNumber; }
    public String getManufacturer() { return manufacturer; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }
    public String getClassification() { return classification; }
    public void setClassification(String classification) { this.classification = classification; }

}
