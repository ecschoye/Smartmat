package ntnu.idatt2106.backend.service;

import lombok.RequiredArgsConstructor;
import ntnu.idatt2106.backend.model.GroceryHistory;
import ntnu.idatt2106.backend.model.dto.GroceryStatisticDTO;
import ntnu.idatt2106.backend.repository.GroceryHistoryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class GroceryHistoryService {

    private final GroceryHistoryRepository groceryHistoryRepository;

    public List<GroceryHistory> findAllGroceryHistoryForLastYear() {
        LocalDate lastYear = LocalDate.now().minusYears(1);
        return groceryHistoryRepository.findAllByDateConsumedAfter(lastYear);
    }

    private List<GroceryHistory> findGroceriesForMonth(int x) {
        LocalDate dateXMonthsBack = LocalDate.now().minusMonths(x);
        LocalDate startDate = dateXMonthsBack.withDayOfMonth(1); // Get first day of month x months back
        LocalDate endDate = dateXMonthsBack.withDayOfMonth(dateXMonthsBack.lengthOfMonth()); // Get last day of month x months back
        return groceryHistoryRepository.findByDateConsumedBetween(startDate, endDate);
    }
    public List<GroceryStatisticDTO> getStatsforLastYear(){
        List<GroceryStatisticDTO> stats = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM, yyyy");
        for(int i = 0; i < 12; i++){
            List<GroceryHistory> iMonth = findGroceriesForMonth(i);
            LocalDate dateXMonthsBack = LocalDate.now().minusMonths(i);
            String formattedDate = dateXMonthsBack.format(formatter);


            stats.add(GroceryStatisticDTO.builder()
                    .foodWaste(sumTrash(iMonth))
                    .foodEaten(sumEaten(iMonth))
                    .monthName(formattedDate)
                    .build());
        }
        return stats;
    }

    private Integer sumTrash(List<GroceryHistory> list) {
        List<GroceryHistory> trash = list.stream()
                .filter(groceryHistory -> groceryHistory.isWasTrashed())
                .collect(Collectors.toList());

        Integer totalWeight = trash.stream()
                .mapToInt(GroceryHistory::getWeightInGrams)
                .sum();

        return totalWeight;
    }

    private Integer sumEaten(List<GroceryHistory> list) {
        List<GroceryHistory> eaten = list.stream()
                .filter(groceryHistory -> !groceryHistory.isWasTrashed())
                .collect(Collectors.toList());

        Integer totalWeight = eaten.stream()
                .mapToInt(GroceryHistory::getWeightInGrams)
                .sum();

        return totalWeight;
    }



}
