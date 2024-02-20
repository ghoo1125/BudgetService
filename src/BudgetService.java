import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.stream.Collectors;

public class BudgetService {
    private BudgetRepo budgetRepo;
    public double query(LocalDate start, LocalDate end) {
        if (start.isAfter(end)) {
            return 0.00;
        }

        Map<YearMonth, Double> budgetMap = budgetRepo.getAll().stream()
                                                     .collect(Collectors.toMap(b -> b.yearMonth, b -> b.amount));

        Double result = 0.0;
        LocalDate current = LocalDate.of(start.getYear(), start.getMonth(), 1);
        for (; current.isBefore(end); current.plusMonths(1)) {
            YearMonth currentYearMonth = YearMonth.from(current);

            if (budgetMap.containsKey(currentYearMonth)) {
                Double amonut = budgetMap.get(YearMonth.from(current));

                if (currentYearMonth.equals(YearMonth.from(start))) {
                    int daysOfMonth = start.lengthOfMonth() - start.getDayOfMonth() + 1;
                    result += amonut / daysOfMonth;
                } else if (currentYearMonth.equals(YearMonth.from(end))) {
                    int daysOfMonth = end.getDayOfMonth();
                    result += amonut / daysOfMonth;
                } else {
                    result += amonut;
                }
            }
        }

        return result;
    }


}
