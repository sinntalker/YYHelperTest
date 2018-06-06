package com.sinntalker.sinntest20180503_yy.Fragment.user.about;

import com.sinntalker.sinntest20180503_yy.AllUserBean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2018/6/4.
 */

public class FeedbackBean extends BmobObject {

    private AllUserBean user;

    public AllUserBean getUser() {
        return user;
    }
    public void setUser(AllUserBean user) {
        this.user = user;
    }

    private String submitTime;

    public String getSubmitTime() {
        return submitTime;
    }
    public void setSubmitTime(String submitTime) {
        this.submitTime = submitTime;
    }

    private String feedbackContext;

    public String getFeedbackContext() {
        return feedbackContext;
    }
    public void setFeedbackContext(String feedbackContext) {
        this.feedbackContext = feedbackContext;
    }

}
