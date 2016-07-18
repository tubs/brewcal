package tubs.brewing;

import java.security.InvalidParameterException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class BrewingCalendar {

	public static enum EventType {
		Brew, Rack, Bottle
	}

	private static int nextBrewID = 0;

	public static class Event {
		public EventType type;
		public String demijon;
		public int brewID;
		public int age;

		public static Event brew() {
			Event e = new Event();
			e.type = EventType.Brew;
			e.brewID = nextBrewID++;
			return e;
		}

		public static Event[] rack(Event brew, int maturing[]) {
			Event list[] = new Event[maturing.length];
			for (int i = 0; i < maturing.length; i++) {
				Event e = new Event();
				e.type = EventType.Rack;
				e.brewID = brew.brewID;
				e.demijon = availableDemijons.first();
				availableDemijons.remove(e.demijon);
				e.age = maturing[i];
				list[i] = e;
			}
			return list;
		}

		public static Event bottle(Event rack) {
			Event e = new Event();
			e.type = EventType.Bottle;
			e.brewID = rack.brewID;
			e.demijon = rack.demijon;
			e.age = rack.age;
			return e;
		}

		@Override
		public String toString() {
			StringBuffer buffer = new StringBuffer();
			buffer.append("[").append(brewID).append("] ").append(type);
			if (type == EventType.Rack || type == EventType.Bottle) {
				buffer.append(" (").append(demijon).append(")");
			}
			return buffer.toString();
		}
	}

	private static final Map<LocalDate, List<Event>> events = new HashMap<>();

	private final static TemporalField dayOfweek = WeekFields.of(Locale.UK).dayOfWeek();

	private final static SortedSet<String> availableDemijons = new TreeSet<>(Arrays.asList("Caana", "Choquequirao",
			"Conjunto", "Coricancha", "Crossroads Temple", "El Castillo", "El Duende", "El Tigre", "High Temple",
			"Inca Pisac", "Isla del Sol", "Jaguar Temple", "LD-49", "La Danta", "La Gran Piramide", "La Iglesia",
			"Llactapata", "Machu Picchu", "Mask Temple", "Moray", "Ollantaytambo", "Pyramid of the Magician",
			"Pyramid of the Moon", "Pyramid of the Niches", "Pyramid of the Sun", "Sacsayhuaman", "Temple",
			"Temple of the Cross", "Temple of the Feather Serpent", "Temple of the Inscriptions",
			"Temple of the Wooden Lintel", "Templo Mayor", "The Great Pyramid", "The Great Pyramid of Cholula",
			"The Nohoch Mul pyramid", "The Pyramid of Flowers", "The Spiral Building", "The Temple of the Murals",
			"Tikal Temple IV", "The Bell of San Andrés", "Winay wayna"

	));

	public static LocalDate findNextWeekend(LocalDate d) {
		if (d.getDayOfWeek() == DayOfWeek.SATURDAY)
			return d;
		else
			return d.with(dayOfweek, 6);
	}

	private static LocalDate lastRack = null;

	public static void brew(LocalDate when, int maturing[]) {
		when = findNextWeekend(when);
		Event brew = Event.brew();

		LocalDate rackDate = when.plusWeeks(2);

		if (lastRack != null) {
			LocalDate date = lastRack;

			while (date.isBefore(rackDate)) {
				List<Event> events = eventsFor(date);
				if (events != null) {
					System.out.println("Simulating: " + date);
					for (Event e : events) {
						if (e.type == EventType.Bottle) {
							System.out.println("returning: " + e.demijon);
							availableDemijons.add(e.demijon);
						}
					}
				}

				date = date.plusWeeks(1);
			}
		}
		lastRack = rackDate;

		Event[] rack = Event.rack(brew, maturing);
		BrewingCalendar.register(when, brew);

		for (int i = 0; i < maturing.length; i++) {
			BrewingCalendar.register(rackDate, rack[i]);
			Event bottle = Event.bottle(rack[i]);
			BrewingCalendar.register(when.plusMonths(maturing[i]), bottle);
		}
	}

	public static void register(LocalDate when, Event what) {

		LocalDate nextWeekend = findNextWeekend(when);

		events.computeIfAbsent(nextWeekend, x -> {
			return new ArrayList<>();
		}).add(what);
	}

	public static List<Event> eventsFor(LocalDate when) {
		if (when.getDayOfWeek() != DayOfWeek.SATURDAY) {
			throw new InvalidParameterException("Can only get events for Saturday...");
		}

		return events.get(when);

	}

}
