import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ChefScheduler {
	private static final String MON = "mon";
	private static final String TUE = "tue";
	private static final String WED = "wed";
	private static final String THU = "thu";
	private static final String FRI = "fri";
	private static final String SAT = "sat";
	private static final String SUN = "sun";

	public static boolean schedulable(HashMap<String, ArrayList<String>> requests) {
		List<String> weekdays = Arrays.asList(MON, TUE, WED, THU, FRI, SAT, SUN);
		Map<String, Integer> employeeWorkCount = new HashMap<>();
		HashMap<String, ArrayList<String>> availableByWeekday = new HashMap<>();
		for (String weekday : weekdays) {
			availableByWeekday.put(weekday, new ArrayList<>());
		}
		for (Entry<String, ArrayList<String>> entry : requests.entrySet()) {
			String name = entry.getKey();
			employeeWorkCount.put(name, 0);
			ArrayList<String> offDays = entry.getValue();
			ArrayList<String> availableDays = new ArrayList<>(weekdays);
			availableDays.removeAll(offDays);

			for (String day : availableDays) {
				availableByWeekday.get(day).add(name);
			}
		}

		HashMap<String, List<List<String>>> combinationPlans = new HashMap<>();
		HashMap<String, List<String>> currentAssignment = new HashMap<>();

		for (String weekday : weekdays) {
			List<List<String>> combinations = new ArrayList<>();
			if (availableByWeekday.get(weekday).size() > 2) {
				combinations = combinations(availableByWeekday.get(weekday), 2, 0, new String[2], combinations);
			} else {
				combinations.add(availableByWeekday.get(weekday));
			}

			combinationPlans.put(weekday, combinations);
		}
		return isScheduleable(combinationPlans, new LinkedList<String>(weekdays), currentAssignment);
	}

	static List<List<String>> combinations(List<String> arr, int len, int startIndex, String[] result,
			List<List<String>> combinations) {
		if (len == 0) {
			combinations.add(Arrays.asList(Arrays.copyOf(result, result.length)));
			return combinations;
		}
		for (int i = startIndex; i <= arr.size() - len; i++) {
			result[result.length - len] = arr.get(i);
			combinations(arr, len - 1, i + 1, result, combinations);
		}
		return combinations;
	}

	public static boolean isScheduleable(HashMap<String, List<List<String>>> combinationPlans,
			LinkedList<String> remainingWeekdays, HashMap<String, List<String>> currentAssignment) {
		if (remainingWeekdays.isEmpty()) {
			// check if they have work enough
			List<String> allAsignments = new ArrayList<>();
			for (Entry<String, List<String>> entry : currentAssignment.entrySet()) {
				if (entry.getValue().size() < 2) {
					System.out.println(entry.getKey() + " has not enough staff " + entry.getValue().size());
					return false;
				}
				allAsignments.addAll(entry.getValue());
			}

			Map<String, Long> counts = allAsignments.stream()
					.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
			for (Entry<String, Long> entry : counts.entrySet()) {
				if (entry.getValue() > 5) {
					System.out.println(entry.getKey() + " has overload " + entry.getValue());
					return false;
				}
			}

			return true;
		}

		String currentWeekday = remainingWeekdays.get(0);
		remainingWeekdays.remove(0);

		List<List<String>> possiblePlanForCurrentDay = combinationPlans.get(currentWeekday);
		if (possiblePlanForCurrentDay.isEmpty()) {
			return false;
		}
		for (List<String> plan : possiblePlanForCurrentDay) {
			currentAssignment.put(currentWeekday, plan);
			if (isScheduleable(combinationPlans, new LinkedList<String>(remainingWeekdays), currentAssignment)) {
				return true;
			}
		}

		return false;
	}
}