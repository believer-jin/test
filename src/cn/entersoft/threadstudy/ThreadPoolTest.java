package cn.entersoft.threadstudy;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * java �̳߳�ѧϰ
 * @author dingsj
 * @since 2013-12-23
 */
public class ThreadPoolTest {
    /**
    * �̳߳صĸ���
    * jdk5�����ThreadPool�ĸ���
    * ֮���Բ����̳߳ص���Ҫԭ�����ڣ�
    * �߳�ʱ��=T1(����ʱ��)+T2(����ʱ��)+T3(����ʱ��)
    * �̳߳������һ���߳̿���������Ϊ��һ�����������ӣ��ʹﵽ�������Ч����
    * �ٺ٣������Ŷӵ�ÿһ����һ����
    *
    * �����־�̬�����̳߳�
    *
    * �̶��̳߳�
    * �����̳߳�
    * ��һ�̳߳� �������油�ķ�ʽ��
    * ���⣬���ж�ʱ�����̳߳� ��ʹ�ÿ��Բο�Timer��ʹ�ã�
    */

    public static void main(String[] args) {
        /**
        * ����ʵ��˵��
        * ��100������һ���ύ���̳߳�10���������̳߳أ�ÿ�εĴ������
        */


        //�̶����̳߳�
//        ExecutorService threadPool = Executors.newFixedThreadPool(3);            //Executor��һ�������࣬������һ�����������̵߳��̳߳�


        //������̳߳� ,�����������Զ������߳��������ͻ��� ���ͻ��Զ����ӵ�ʮ���߳�
//         ExecutorService threadPool = Executors.newCachedThreadPool();


        //�̳߳���ֻ��һ���̣߳������߳����˺���Զ���һ���̵߳������������������̳߳أ�
        ExecutorService threadPool = Executors.newSingleThreadExecutor();
        for (int i = 0;i<10;i++){
            final int finalI = i;
            threadPool.execute(new Runnable() {            //����ִ��
                @Override
                public void run() {
                    for(int j = 0;j<10;j++){
                        try {
                            Thread.sleep(20);                 //��������ۿ�
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println(Thread.currentThread().getName()+" loop of " + finalI +
                        " for task of "+ j);     //ĳ���߳����ڽ��еڼ���ѭ��
                    }
                }
            });
        }
        System.out.println("all of 10 tasks have committed");
//        threadPool.shutdown();


        //���ö�ʱ���񣬶�ʱ����������
        Executors.newScheduledThreadPool(3).scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("bombing");
            }
        },5,2, TimeUnit.SECONDS);   //5��֮��ը��ÿ��2��ըһ��  ��schedule����ʱ���񣩻���schedule֮��ʱ�����һ���ǵ�λ
    }
}

/**����
* �������߳̽ӿ���ExecutorService����Executor��һ�������Ľӿڡ�
* �̳߳صı��ģʽ�£��������ύ���������ӣ��ɳ�����������������൱�ڣ��Ƚ��֣����������ֹ��ʱ��ĵȴ��������ֺ����ϸ�ڷ��䡣
* �������ύ���̳߳صģ�����һ���߳�ֻ��ִ��һ�����񣬵��ǿ���ͬʱ���̳߳��ύ�������
* ���Ƕ��ڸ߶˵�ʹ�ã���Ҫ�Լ������̳߳�
*/
