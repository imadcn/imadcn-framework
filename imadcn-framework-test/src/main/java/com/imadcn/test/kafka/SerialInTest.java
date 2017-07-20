package com.imadcn.test.kafka;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class SerialInTest {

	public static void main(String[] args) throws Exception {
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File("d:\\save_20170626.data")));
		User user = (User) ois.readObject();
		ois.close();
		System.out.println(user);
	}
}
