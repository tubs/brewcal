package tubs.brewing;

import java.time.LocalDate;

import javax.swing.JFrame;
import javax.swing.JScrollPane;


public class Entry {
	
	public static void main(String[] args) {
		final JFrame frame = new JFrame("Brewing");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.setSize(1900, 1024);
		
		LocalDate date = LocalDate.of(2016, 7, 16);
		
		BrewingCalendar.brew(date, new int[] { 1, 3, 6, 12, 24 });
		date = date.plusMonths(1);
		BrewingCalendar.brew(date, new int[] { 1, 3, 6, 12, 24 });
		date = date.plusMonths(1);
		BrewingCalendar.brew(date, new int[] { 3, 6, 12, 24, 25 });
		date = date.plusMonths(1);
		BrewingCalendar.brew(date, new int[] { 3, 6, 12, 25, 26 });
		date = date.plusMonths(1);
		BrewingCalendar.brew(date, new int[] { 6, 12, 13, 26, 27 });
		date = date.plusMonths(1);
		BrewingCalendar.brew(date, new int[] { 6, 13, 14, 27, 28 });
		date = date.plusMonths(2);
		BrewingCalendar.brew(date, new int[] { 12, 13, 14, 15, 16 });

		date = date.plusMonths(3);
		BrewingCalendar.brew(date, new int[] { 24, 25, 26, 27, 28 });

		date = date.plusMonths(5);
		BrewingCalendar.brew(date, new int[] { 24, 25, 26, 27, 28 });

		
		final JScrollPane scroll = new JScrollPane(new CalendarView());
		scroll.getVerticalScrollBar().setUnitIncrement(128);
		frame.getContentPane().add(scroll);

		frame.setVisible(true);
		
	}
}
