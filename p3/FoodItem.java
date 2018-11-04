package p3;

public class FoodItem {

	private String name;
	private double weight;
	private double volume;

	public FoodItem(double volume, double weight, String name) {
		this.name = name;
		this.weight = weight;
		this.volume = volume;

	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	public double getVolume() {
		return volume;
	}
	public void setVolume(double volume) {
		this.volume = volume;
	}

}