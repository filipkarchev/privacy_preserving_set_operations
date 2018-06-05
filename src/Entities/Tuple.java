package Entities;

import java.math.BigInteger;

public class Tuple {
	private BigInteger first;
	private BigInteger second;

	public Tuple(BigInteger first, BigInteger second) {
		super();
		this.first = first;
		this.second = second;
	}

	public Tuple() {
	}

	public BigInteger getFirst() {
		return first;
	}

	public void setFirst(BigInteger first) {
		this.first = first;
	}

	public BigInteger getSecond() {
		return second;
	}

	public void setSecond(BigInteger second) {
		this.second = second;
	}

}
