package cn.hd.event;

import java.io.IOException;
import java.util.Timer;

//class MyThread extends Thread{
//    public void run(){
//    	Timer timer = new Timer();
//        timer.schedule(new TimerTest(), 1000, 2000);
//        
//    	 while(true){//这个是用来停止此任务的,否则就一直循环执行此任务了
//             try { 
//                 int ch = System.in.read();
//                 if(ch-'c'==0){ 
//                     timer.cancel();//使用这个方法退出任务
//                     
//                 }
//             } catch (IOException e) { 
//                 // TODO Auto-generated catch block
//                 e.printStackTrace();
//             }
//         } 
//}
//}

public class TimerTest extends java.util.TimerTask{
    @Override
    public void run() { 
        // TODO Auto-generated method stub
        System.out.println("timer test here");
    }
}
