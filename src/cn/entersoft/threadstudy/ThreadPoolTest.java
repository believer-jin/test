package cn.entersoft.threadstudy;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * java 线程池学习
 * @author dingsj
 * @since 2013-12-23
 */
public class ThreadPoolTest {
    /**
    * 线程池的概念
    * jdk5提出了ThreadPool的概念
    * 之所以采用线程池的主要原因在于：
    * 线程时间=T1(创建时间)+T2(运行时间)+T3(销毁时间)
    * 线程池则可以一个线程空闲下来后为另一个服务，这样子，就达到了整体的效果，
    * 嘿嘿（敏捷团队的每一个人一样）
    *
    * 四种种静态工厂线程池
    *
    * 固定线程池
    * 缓存线程池
    * 单一线程池 （采用替补的方式）
    * 另外，还有定时器的线程池 （使用可以参考Timer的使用）
    */

    public static void main(String[] args) {
        /**
        * 测试实例说明
        * 有100个任务，一次提交给线程池10个，分析线程池，每次的处理情况
        */


        //固定的线程池
//        ExecutorService threadPool = Executors.newFixedThreadPool(3);            //Executor是一个工具类，声明了一个具有三个线程的线程池


        //缓存的线程池 ,根据任务量自动增加线程数量，和回收 ，就会自动增加到十个线程
//         ExecutorService threadPool = Executors.newCachedThreadPool();


        //线程池中只有一个线程，但是线程死了后可以对另一个线程调用重新启动，（单线程池）
        ExecutorService threadPool = Executors.newSingleThreadExecutor();
        for (int i = 0;i<10;i++){
            final int finalI = i;
            threadPool.execute(new Runnable() {            //任务执行
                @Override
                public void run() {
                    for(int j = 0;j<10;j++){
                        try {
                            Thread.sleep(20);                 //便于输出观看
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println(Thread.currentThread().getName()+" loop of " + finalI +
                        " for task of "+ j);     //某个线程正在进行第几次循环
                    }
                }
            });
        }
        System.out.println("all of 10 tasks have committed");
//        threadPool.shutdown();


        //采用定时任务，定时器任务类型
        Executors.newScheduledThreadPool(3).scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("bombing");
            }
        },5,2, TimeUnit.SECONDS);   //5秒之后炸，每个2秒炸一下  ，schedule（定时任务）或者schedule之后定时，最后一个是单位
    }
}

/**分析
* 真正的线程接口是ExecutorService，而Executor是一个顶级的接口。
* 线程池的编程模式下，任务是提交给整个池子，由池子来负责分配任务；相当于，先接手（接受任务防止长时间的等待），接手后进行细节分配。
* 任务是提交给线程池的，但是一个线程只能执行一个任务，但是可以同时向线程池提交多个任务。
* 但是对于高端的使用，需要自己定制线程池
*/
