
package com.android.test;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class MainActivity extends AppCompatActivity {
    public final static String TAG = MainActivity.class.getSimpleName();

    private static final int MESSAGE_ONE = 1;
    private static final int MESSAGE_TWO = MESSAGE_ONE + 1;
    private static final int MESSAGE_THREE = MESSAGE_TWO + 1;
    private static final int MESSAGE_FOUR = MESSAGE_THREE + 1;
    private static final int MESSAGE_FIVE = MESSAGE_FOUR + 1;


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.e(TAG, "============ " + msg.what);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(MainActivity.class.getSimpleName());
        setSupportActionBar(toolbar);


        Message msg = handler.obtainMessage();
//        msg.what = MESSAGE_ONE;
//        handler.sendMessage(msg);
//
//        msg = handler.obtainMessage();
//        msg.what = MESSAGE_TWO;
//        handler.sendMessage(msg);
//
//        msg = handler.obtainMessage();
//        msg.what = MESSAGE_THREE;
//        handler.sendMessage(msg);
//
//        msg = handler.obtainMessage();
//        msg.what = MESSAGE_FOUR;
//        msg.setAsynchronous(true);
//
        MessageQueue messageQueue = Looper.myQueue();
        Object result = PrivateUtil.invoke(messageQueue, "postSyncBarrier");


        /*Object result = */PrivateUtil.invoke(messageQueue, "postSyncBarrier", new Class[]{long.class},
                new Object[]{SystemClock.uptimeMillis() + 15000l});

//        int token = -1;
//        if (result != null) {
//            token = (int) result;
//        }

//        Log.e(TAG, "========== postSyncBarrier " + token);
//
//        msg = handler.obtainMessage();
//        msg.what = MESSAGE_ONE;
//        handler.sendMessage(msg);


        msg = handler.obtainMessage();
        msg.what = MESSAGE_FIVE;
//        msg.setAsynchronous(true);
        handler.sendMessageDelayed(msg, 10000);

        handler.sendEmptyMessageDelayed(MESSAGE_TWO, 5000);

        handler.sendEmptyMessageDelayed(MESSAGE_TWO, 5000);

        handler.sendEmptyMessageAtTime(MESSAGE_FOUR, SystemClock.uptimeMillis() + 20000);


        handler.removeMessages(MESSAGE_TWO);


//        msg = handler.obtainMessage();
//        msg.what = MESSAGE_ONE;
//        handler.sendMessage(msg);

//        msg = handler.obtainMessage();
//        msg.what = MESSAGE_THREE;
//        msg.setAsynchronous(true);
//        handler.sendMessageDelayed(msg, 10000);

        test();

        messageQueue.addIdleHandler(new MessageQueue.IdleHandler() {
            /**
             * 返回值boolean 意思是needKeep
             * true，表示要保留保留， 代表不移除这个idleHandler，可以反复执行
             * false代表执行完毕之后就移除这个idleHandler, 也就是只执行一次
             */
            @Override
            public boolean queueIdle() {
                Log.e(TAG, "-------------->queueIdle  主线程空闲了");
                return true;
            }
        });


//        PrivateUtil.invoke(messageQueue, "removeSyncBarrier", new Class[]{int.class},
//                new Object[]{token});
//
//        msg = handler.obtainMessage();
//        msg.what = MESSAGE_ONE;
//        msg.setAsynchronous(true);
//        handler.sendMessageDelayed(msg, 2000);


        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                return false;
            }
        });

    }

    private void test(){}

    class MyThread extends Thread {
        public Handler mHandler;

        public void run() {
            Looper.prepare();
            mHandler = new Handler() {
                public void handleMessage(Message msg) {
                /* 处理接收到的消息 */
                }
            };
            Looper.loop();
        }
    }

    private void getMethods(Class clz) {
        Method method[] = clz.getMethods();
        for (int i = 0; i < method.length; ++i) {
            method[i].setAccessible(true);
            Class<?> returnType = method[i].getReturnType();
            Class<?> para[] = method[i].getParameterTypes();
            int temp = method[i].getModifiers();
            System.out.print(Modifier.toString(temp) + " ");
            System.out.print(returnType.getName() + "  ");
            System.out.print(method[i].getName() + " ");
            System.out.print("(");
            for (int j = 0; j < para.length; ++j) {
                System.out.print(para[j].getName() + " " + "arg" + j);
                if (j < para.length - 1) {
                    System.out.print(",");
                }
            }
            Class<?> exce[] = method[i].getExceptionTypes();
            if (exce.length > 0) {
                System.out.print(") throws ");
                for (int k = 0; k < exce.length; ++k) {
                    System.out.print(exce[k].getName() + " ");
                    if (k < exce.length - 1) {
                        System.out.print(",");
                    }
                }
            } else {
                System.out.print(")");
            }
            System.out.println();
        }
    }
}
