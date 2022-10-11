package ch.firealarmtv.handlers;

public final class Console {

	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";

	public static void write(String txt, String color) {
		System.out.print(color + txt + ANSI_RESET);
	}
	
	public static void write(String txt) {
		System.out.print(txt);
	}
	
	public static void writeln(String txt, String color) {
		System.out.println(color + txt + ANSI_RESET);
	}
	
	public static void writeln(String txt) {
		System.out.println(txt);
	}
	
	public static void writelnError(String txt) {
		System.out.println(ANSI_RED + txt + ANSI_RESET);
	}

	public static void writeError(String txt) {
		System.out.print(ANSI_RED + txt + ANSI_RESET);
	}
}
