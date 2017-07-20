package com.imadcn.test.kafka;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class SerialOutTest {

	public static void main(String[] args) throws Exception {
		/*User u1 = new User();
		u1.setName("萌小羊");
		u1.setAge(27);
		
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("d:\\save_20170626.data")));
		oos.writeObject(u1);
		oos.flush();
		oos.close();*/
		String s1 = new String("hello");
		String s2 = s1.intern();
		System.out.println(s1 == s2);
	}
}
