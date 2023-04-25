package ntnu.idatt2106.backend.service;


import lombok.RequiredArgsConstructor;
import ntnu.idatt2106.backend.model.*;
import ntnu.idatt2106.backend.model.dto.GroceryNotificationDTO;
import ntnu.idatt2106.backend.repository.GroceryNotificationRepository;
import ntnu.idatt2106.backend.repository.RefrigeratorGroceryRepository;
import ntnu.idatt2106.backend.repository.RefrigeratorRepository;
import ntnu.idatt2106.backend.repository.RefrigeratorUserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Service for notifications
 */
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final GroceryNotificationRepository groceryNotificationRepository;
    private final RefrigeratorUserRepository refrigeratorUserRepository;
    private final RefrigeratorGroceryRepository refrigeratorGroceryRepository;


    /**
     * Gets all the grocery notifications for a user.
     * @param user
     * @return
     */
    public List<GroceryNotificationDTO> getNotifications(User user){
        List<Refrigerator> refrigerators = refrigeratorUserRepository.findByUser(user)
                .stream().map(refrigeratorUser -> refrigeratorUser.getRefrigerator())
                .collect(Collectors.toList());
        List<RefrigeratorGrocery> refrigeratorGroceries = new ArrayList<>();

        refrigerators.stream().forEach(refrigerator -> {
            refrigeratorGroceries.addAll(refrigeratorGroceryRepository.findAllByRefrigeratorId(refrigerator.getId()));
        });

        generateNotifications(refrigeratorGroceries, user);

        List<GroceryNotificationDTO> notifications = groceryNotificationRepository
                .findAllByUserId(user.getId())
                .stream()
                .map(groceryNotification -> new GroceryNotificationDTO(groceryNotification))
                .collect(Collectors.toList());

        return notifications;
    }

    /**
     * Generates a new notification and saves it to database either if the
     * grocery entity has no existing notifications or has one existing notification but expires the current day.
     * @param refrigeratorGroceries
     * @param user
     */
    public void generateNotifications(List<RefrigeratorGrocery> refrigeratorGroceries, User user){
        Date date = new Date();

        //Get all groceries which have below 3 days left.
        List<RefrigeratorGrocery> mightNeedNotification = refrigeratorGroceries.stream()
                .filter(refrigeratorGrocery -> getDaysBetweenTodayAndDate(refrigeratorGrocery.getPhysicalExpireDate()) <= 3)
                .collect(Collectors.toList());

        List<GroceryNotification> notifications = new ArrayList<>();

        mightNeedNotification.stream()
                .forEach(refrigatorGrocery -> {
                    List<GroceryNotification> preExisting = groceryNotificationRepository.findAllByGroceryEntity(refrigatorGrocery);
                    if(preExisting.isEmpty()){
                        notifications.add(GroceryNotification.builder()
                                .groceryEntity(refrigatorGrocery)
                                .daysLeft(getDaysBetweenTodayAndDate(refrigatorGrocery.getPhysicalExpireDate()))
                                .user(user)
                                .build());
                    }
                    if(preExisting.size() == 1 && getDaysBetweenTodayAndDate(refrigatorGrocery.getPhysicalExpireDate()) == 0){
                        notifications.add(GroceryNotification.builder()
                                .groceryEntity(refrigatorGrocery)
                                .daysLeft(getDaysBetweenTodayAndDate(refrigatorGrocery.getPhysicalExpireDate()))
                                .user(user)
                                .build());
                    }
                });

        notifications.forEach(groceryNotification -> groceryNotificationRepository.save(groceryNotification));
    }

    private long getDaysBetweenTodayAndDate(Date date) {
        // Get today's date
        Calendar today = Calendar.getInstance();
        // Set the time to midnight
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        // Get the given date
        Calendar givenDate = Calendar.getInstance();
        givenDate.setTime(date);
        // Set the time to midnight
        givenDate.set(Calendar.HOUR_OF_DAY, 0);
        givenDate.set(Calendar.MINUTE, 0);
        givenDate.set(Calendar.SECOND, 0);
        givenDate.set(Calendar.MILLISECOND, 0);

        // Calculate the number of days between the two dates
        long diffInMillis = givenDate.getTimeInMillis() - today.getTimeInMillis();
        long daysDiff = (long) (diffInMillis / (24 * 60 * 60 * 1000));

        return daysDiff;
    }
}
