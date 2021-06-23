package com.test;

import java.util.Stack;

public class BalanceParenTheses {
	public static void main(String[] args) {
		// ((()))(((())))
		System.out.println(balanceBrackets(")))(((()"));
		
		// ((()))((()()))
		System.out.println(balanceBrackets("()))((()("));
	}
	
	public static String balanceBrackets(String input) {
		int remainOpenCount = 0;
		int remainCloseCount = 0;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < input.length(); i++) {
			Character c = input.charAt(i);
			if (c == ')') {
				// if there is any open ( remain, close it
				if (remainOpenCount > 0) {
					remainOpenCount--;
				} else {
					// if not
					remainCloseCount++;
				}
				sb.append(c);
			} else {
				sb.append(c);
				remainOpenCount++;
			}
		}
		while (remainOpenCount > 0) {
			sb.append(")");
			remainOpenCount--;
		}
		while (remainCloseCount > 0) {
			sb.insert(0, "(");
			remainCloseCount--;
		}
		return sb.toString();
	}
}
