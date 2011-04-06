package com.st1.bartapp;

public class EtdEstimate {
	public static final int North = 0;
	public static final int South = 1;
	public static final int East = 2;
	public static final int West = 3;

	public String Destination;
	public int ETDTime; //in minutes
	public int Platform; //platform number
	public int Direction; //based on constants defined above
	public int TrainLength; //number of cars
}
