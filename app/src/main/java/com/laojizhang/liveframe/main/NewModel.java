package com.laojizhang.liveframe.main;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;

import com.laojizhang.lifelibrary.model.BaseLifeModel;
import com.laojizhang.lifelibrary.utils.LogUtils;
import com.laojizhang.liveframe.base.LiveApplication;
import com.laojizhang.liveframe.db.table.User;

import java.util.ArrayList;
import java.util.List;

/**
 * 文件名称： NewModel
 * 作   者： guomaojian
 * 创建日期： 2017/05/27-14:42
 * 文件描述：
 * <p>
 */

public class NewModel extends BaseLifeModel<String> {

    private int start = 0;
    private List<Integer> mIntegers = new ArrayList<>();
    private Runnable mCountDownRunnable = new Runnable() {
        @Override
        public void run() {
            start++;
            LogUtils.i("AAA", String.valueOf(start));
            LogUtils.i("AAA", mIntegers.toString());
            getLiveData().setValue(String.valueOf(start));
            getHandler().postDelayed(this, 1000);
        }
    };

    public NewModel(Application application) {
        super(application);
        mIntegers.add(1);
        mIntegers.add(2);
        mIntegers.add(3);
        mIntegers.add(4);
        mIntegers.add(5);
        getHandler().postDelayed(mCountDownRunnable, 2000);
    }

    @Override
    public void onStop() {
        super.onStop();
        for (int i = 0, size = mIntegers.size(); i < size; i++) {
            mIntegers.set(i, mIntegers.get(i) + 1);
        }
        LogUtils.i("AAA", "STOP : " + mIntegers.toString());
    }

    @Override
    protected void onFinishInit() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                User user = new User();
                user.setNick_name("nick_name" + Math.random());
                user.setLast_name("last_name" + Math.random());
                LiveApplication.getDatabase().userDao().addUser(user);

                List<User> users = LiveApplication.getDatabase().userDao().listUsers();
                if (users != null) {
                    for (final User it : users) {
                        getHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                LogUtils.i(TAG, it.toString());
                            }
                        });
                    }
                }
            }
        }).start();
    }

    public MutableLiveData<ArrayList<Integer>> bindListData() {
        return bindLiveData(new ArrayList<Integer>());
    }
}
