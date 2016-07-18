package tubs.brewing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.time.LocalDate;
import java.util.EnumMap;
import java.util.List;

import javax.swing.JComponent;

import tubs.brewing.BrewingCalendar.Event;
import tubs.brewing.BrewingCalendar.EventType;

public class CalendarView extends JComponent {
	
	private final static LocalDate start = LocalDate.of(2016, 7, 16);
	
	private final static int weeksPerRow = 4;
	private int itemHeight = 128;
	
	private static EnumMap<EventType, Color> eventColors = new EnumMap<>(EventType.class);
	
	static {
		eventColors.put(EventType.Bottle, Color.green.darker().darker());
		eventColors.put(EventType.Brew, Color.red);
		eventColors.put(EventType.Rack, Color.blue);
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(1800, itemHeight * 12 * 8);
	}
	
	@Override
	protected void paintComponent(Graphics gBase) {
		
		final Graphics2D g = (Graphics2D) gBase.create();
		
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		final Font ageFont = getFont().deriveFont(Font.ITALIC, 10f);
		final Font eventFont = getFont().deriveFont(12f);
		final Font brewFont = eventFont.deriveFont(Font.BOLD, 14f);
		final Font heading = getFont().deriveFont(Font.BOLD);
		
		int localWidth = getWidth() / weeksPerRow;
		
		LocalDate date = start;
		
		LocalDate nextWeekend = BrewingCalendar.findNextWeekend(LocalDate.now());
		
		for (int y = 0; y < getHeight(); y += itemHeight)
		{
			for (int i =0 ; i < weeksPerRow; i++) {
				
				Color bg = i % 2 == 0 ? Color.lightGray : Color.white;
				int month = y / itemHeight;
				if (month % 2 == 1) {
					bg = new Color(bg.getRed(), bg.getGreen(), bg.getBlue() - 16);
				}
				g.setColor(bg);
				g.fillRect(0, 0, localWidth, itemHeight);
				
				if (nextWeekend.equals(date)) {
					g.setColor(Color.blue);
					g.drawRect(1, 1, localWidth - 2, itemHeight - 2);
					
				}

				g.setFont(heading);
				g.setColor(Color.gray);
				g.drawString(date.toString(), 16, 14);
				
				List<Event> events = BrewingCalendar.eventsFor(date);
				
				if (events != null) {
					int row = 0;
					for (Event event : events) {
						if (event.type == EventType.Brew)
							g.setFont(brewFont);
						else
							g.setFont(eventFont);
						g.setColor(eventColors.get(event.type));
						g.drawString(event.toString(), 12, 32 + row * 14);
						if (event.age != 0) {
							g.setFont(ageFont);
							g.drawString("" + event.age, localWidth - 32, 32 + row * 14);
						}
						row++;
					}
				}
				
				date = date.plusWeeks(1);
				g.translate(localWidth, 0);
			}
			g.translate(-weeksPerRow * localWidth, itemHeight);
		}
		
		// TODO Auto-generated method stub
		super.paintComponent(g);
	}

}
