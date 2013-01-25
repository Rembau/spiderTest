package spider_;

class Father {
	int a = 1;
	
	public int getA() {
		return a;
	}

	public void setA(int a) {
		this.a = a;
	}
}

class Child extends Father {
	int a = 2;
	}

public class TestExtend {
	public static void main(String[] args) {
		System.out.println(new Child().getA());
	}
}
