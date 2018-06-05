package Entities;
import java.util.ArrayList;

import Ciphers.Paillier;


public abstract class Participant {
	ArrayList<Integer> set;
	Paillier cipher;

	public Participant(ArrayList<Integer> set) {
		this.set = set;
	}
	

	public ArrayList<Integer> getSet() {
		return set;
	}

	public void setSet(ArrayList<Integer> set) {
		this.set = set;
	}


}
