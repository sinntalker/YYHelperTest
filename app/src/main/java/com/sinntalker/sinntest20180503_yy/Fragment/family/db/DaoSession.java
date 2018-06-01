package com.sinntalker.sinntest20180503_yy.Fragment.family.db;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import com.sinntalker.sinntest20180503_yy.Fragment.family.db.NewFriend;
import com.sinntalker.sinntest20180503_yy.Fragment.family.db.NewFriendDao;

/**
 * Created by Administrator on 2018/5/31.
 */

public class DaoSession extends AbstractDaoSession {

    private final DaoConfig newFriendDaoConfig;

    private final NewFriendDao newFriendDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        newFriendDaoConfig = daoConfigMap.get(NewFriendDao.class).clone();
        newFriendDaoConfig.initIdentityScope(type);

        newFriendDao = new NewFriendDao(newFriendDaoConfig, this);

        registerDao(NewFriend.class, newFriendDao);
    }

    public void clear() {
        newFriendDaoConfig.getIdentityScope().clear();
    }

    public NewFriendDao getNewFriendDao() {
        return newFriendDao;
    }

}
