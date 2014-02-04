package com.example.zcoins;

import java.util.ArrayList;
import java.util.Random;

public class SelectionSorter {

	private static int walkCounter = 0;
	private static int difficulty = 1;
	private static int listSlice = 0;
	private static double epsilon = 0;
	private static Random random = new Random();
	private static ArrayList<Integer> unsortedList = new ArrayList<Integer>();
	public static boolean isZombieThere = false;
	public static int speed = 0;
	public static int zombieSpeed = 0;
	public static long zombieTime = 0;

	public static void selectionSort() {
		int i = listSlice;
		for (int j = listSlice; j < unsortedList.size(); j++) {
			if (unsortedList.get(j) < unsortedList.get(i)) {
				i = j;
			}
		}
		if (i >= unsortedList.size()) {
			i = unsortedList.size() - 1;
			listSlice = i;
		}
		int lowestN = unsortedList.get(i);
		int firstN = unsortedList.get(listSlice);
		unsortedList.set(listSlice, lowestN);
		unsortedList.set(i, firstN);
		listSlice += 1;
	}

	public static void createZombie() {
		isZombieThere = true;
		zombieSpeed = (int) 1.5 * speed;
		zombieTime = System.currentTimeMillis();
	}

	public static void escapeZombie() {
		isZombieThere = false;
		zombieSpeed = 0;
		zombieTime = 0;
	}

	public static void checkZombie() {
		if (speed > zombieSpeed) {
			escapeZombie();
		} else if (System.currentTimeMillis() - zombieTime > 10000) {
			Mine.addCoins(-1);
		}
	}

	public static boolean checkSort() {
		boolean sorted = true;
		for (int i = 0; i < unsortedList.size() - 1; i++) {
			if (unsortedList.get(i) > unsortedList.get(i + 1)) {
				sorted = false;
			}
		}
		if (sorted) {
			difficulty += 1;
			listSlice = 0;
			Mine.addCoins(1);
			return true;
		}
		return false;
	}

	public static void makeList() {
		unsortedList = new ArrayList<Integer>();
		int length = 10 * difficulty;
		for (int i = 0; i < length; i++) {
			unsortedList.add(random.nextInt(10));
		}
	}

	public static void iterate() {
		walkCounter += 1;
		if (epsilon < 0.6) {
			epsilon = 100.0002 * walkCounter;
		} else {
			epsilon = 0.6;
		}
		if (random.nextDouble() < 0.2 + epsilon) {
			selectionSort();
		}
		if (checkSort()) {
			makeList();
		}
	}

}
