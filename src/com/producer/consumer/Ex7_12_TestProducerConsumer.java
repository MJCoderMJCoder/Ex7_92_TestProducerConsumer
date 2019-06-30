package com.producer.consumer;

//������
public class Ex7_12_TestProducerConsumer {

	public static void main(String[] args) {
		ShareData s = new ShareData();
		new Consumer(s).start();
		new Producer(s).start();

	}

}

//����������
class MyData{
	//������չ����︴�ӵ�����
	public int data;	//��Ա����
}

//�������ݿ�����
class ShareData{
	//��������
	private MyData data;
	//֪ͨ����
	private boolean writeable = true;
	
	/*
	 * ��Ҫע����ǣ��ڵ���wait()����ʱ����Ҫ�����ŵ�һ��ͬ��������򽫻�����쳣��
	 * ��java.lagn.IllegalMonitorStateException:current thread not owner.
	 */
	public synchronized void setShareData(MyData data){
		if(!writeable) {
			try {
				//��δ������ȴ�
				wait();	//���ϲ��ԣ��ȴ��źţ�
			} catch (InterruptedException e) {
			}
		}
		this.data = data;
		
		//����Ѿ�����
		writeable = false;
		
		//֪ͨ�������Ѿ���������������
		notify();
	}
	public synchronized MyData getShareData() {
		if(writeable) {
			try{
				//��δ������ȴ�
				wait();
			}catch (InterruptedException e){
			}
		}
		//����Ѿ�����
		writeable = true;
		//֪ͨ��Ҫ����
		notify();
		return this.data;
	}
}

//�������߳���
class Producer extends Thread {
	private ShareData s;
	
	Producer (ShareData s) {
		this.s=s;
		
	}
	
	public void run() {
		for (int i=1; i<=10; i++){
			try{
				Thread.sleep((int) Math.random() * 100);
			}catch (InterruptedException e){
				
			}
			MyData mydata = new MyData();
			mydata.data = i;	//�����Լ��ĳ�Ա����data
			s.setShareData(mydata);
			System.out.println("�����߲���һ�����ݣ�" + mydata.data + "��");
		}
	}
}

//�������߳���
class Consumer extends Thread {
	private ShareData s;
	
	Consumer (ShareData s) {
		this.s=s;
	}
	
	public void run() {
		MyData mydata;
		do{
			try{
				Thread.sleep((int) Math.random() * 100);
			}catch(InterruptedException e) {
				
			}
			mydata = s.getShareData();
			System.out.println("����������һ�����ݣ�" + mydata.data + "��");
		}while (mydata.data<=10);
	}
}