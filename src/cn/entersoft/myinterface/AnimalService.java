package cn.entersoft.myinterface;

public class AnimalService implements Animal{

	@Override
	public void setName(String animalName) {
		System.out.println(animalName);
	}
	
}
