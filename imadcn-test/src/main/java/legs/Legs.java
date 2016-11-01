package legs;

public class Legs {
	
	public static void main(String[] args) {
		Legs legs = new Legs();
		int total = 1*2 + 1*4 + 5*2 + 5 * legs.legs(5);
		System.out.println(total);
		
		System.out.println(2+4+5*2+5*5*5*5*5*4); 
	}
	
	public int legs(int depth) {
		int legs = 0;
		for (int i = 0; i < depth; i++) {
			if (depth < 3) {
				legs += legs(depth-1);
			} else {
				legs += 4;
			}
		}
		return legs;
	}

}
