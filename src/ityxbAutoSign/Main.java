package ityxbAutoSign;

import java.util.ArrayList;
import java.util.List;

public class Main {
	public static List<AccountUtil> users = new ArrayList<>();

	public static void main(String[] args){
		// TODO Auto-generated method stub
		
		AccountUtil a1 = new AccountUtil();
		a1.login("your username1 here", "password for username1");
		users.add(a1);
		
		AccountUtil a2 = new AccountUtil();
		a2.login("your username2 here", "password for username2");
		users.add(a2);
		
		while (true){
			for (int i=0;i<users.size();i++) {
				TaskUtil t = new TaskUtil();
				System.out.println("=============================================================================================");
				t.process(users.get(i));
			}
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
