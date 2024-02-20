import java.time.Instant;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.stream.Collectors;

public class BudgetService {
    private final BudgetRepo budgetRepo;
//    private BudgetRepo budgetRepo;

    public BudgetService(BudgetRepo budgetRepo) {

        this.budgetRepo = budgetRepo;
    }

    public double query(LocalDate start, LocalDate end) {
        if (start.isAfter(end)) {
            return 0.00;
        }

        Map<YearMonth, Double> budgetMap = budgetRepo.getAll().stream()
                                                     .collect(Collectors.toMap(b -> YearMonth.parse(b.yearMonth, DateTimeFormatter.ofPattern("yyyyMM")), b -> b.amount));

        if (YearMonth.from(start).equals(YearMonth.from(end))) {
            Double localAmount = budgetMap.get(YearMonth.from(end));
            localAmount = localAmount == null ? 0 : localAmount;
            int days = end.getDayOfMonth() - start.getDayOfMonth() + 1;
            return (localAmount/ start.lengthOfMonth()) * days;
        }
        Double result = 0.0;
        LocalDate current = LocalDate.of(start.getYear(), start.getMonth(), 1);
        while (true) {
            if (current.isAfter(end)) {
                break;
            }
            YearMonth currentYearMonth = YearMonth.from(current);

            if (budgetMap.containsKey(currentYearMonth)) {
                Double amonut = budgetMap.get(YearMonth.from(current));

                if (currentYearMonth.equals(YearMonth.from(start))) {
                    int daysOfMonth =  start.lengthOfMonth() - start.getDayOfMonth() + 1;
                    result += (amonut / start.lengthOfMonth())* daysOfMonth;
                } else if (currentYearMonth.equals(YearMonth.from(end))) {
                    int daysOfMonth = end.getDayOfMonth();
                    result += (amonut / end.lengthOfMonth())*daysOfMonth;
                } else {
                    result += amonut;
                }
            }
            current = current.plusMonths(1);

        }

        return result;
    }


}
