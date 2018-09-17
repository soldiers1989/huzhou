package com.huzhou.gjj.utils;


import com.huzhou.gjj.AppApplication;
import com.huzhou.gjj.bean.Number;
import com.huzhou.gjj.bean.User;
import org.xutils.DbManager;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.List;


/**
 * Toast统一管理类
 */
public class DbUtils {

    public static User getUserData() {
        DbManager db = x.getDb(((AppApplication) x.app().getApplicationContext()).getDaoConfig());
        User user = null;
        SharedPreferencesUtil utils = new SharedPreferencesUtil(x.app());
        try {
            WhereBuilder b = WhereBuilder.b();
            b.and("personAcctNo", "=", utils.read(Const.GJJ_DATA)); //构造修改的条件
            user = db.selector(User.class).where(b).findFirst();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return user;

    }

    public static Number getNumberData() {
        DbManager db = x.getDb(((AppApplication) x.app().getApplicationContext()).getDaoConfig());
        Number number = null;
        try {
            number = db.findFirst(Number.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return number;
    }

    public static String FindNumberData(String code, String dictType) {
        Number first;
        DbManager db = x.getDb(((AppApplication) x.app().getApplicationContext()).getDaoConfig());
        WhereBuilder b = WhereBuilder.b();
        b.and("code", "=", code); //构造修改的条件
        b.and("dictType", "=", dictType);
        try {
            first = db.selector(Number.class).where(b).findFirst();//findAll()：查询所有结果
        } catch (DbException e) {
            return null;
        }
        return first.getName();
    }

    public static List<Number> FindAllData(String dictType) {
        List<Number> first;
        DbManager db = x.getDb(((AppApplication) x.app().getApplicationContext()).getDaoConfig());
        WhereBuilder b = WhereBuilder.b();
        b.and("dictType", "=", dictType);
        try {
            first = db.selector(Number.class).where(b).findAll();//findAll()：查询所有结果
        } catch (DbException e) {
            return null;
        }
        return first;
    }
}
