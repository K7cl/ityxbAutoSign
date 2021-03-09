package ityxbAutoSign;


public class Main {

	public static void main(String[] args){
		// TODO Auto-generated method stub
		AccountUtil a1 = new AccountUtil();
		a1.login("your username here", "your password here");
		TaskUtil t1 = new TaskUtil();
		while (true){
			System.out.println("=============================================================================================");
			t1.process(a1);
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
