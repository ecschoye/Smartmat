package ntnu.idatt2106.backend.service;


import ntnu.idatt2106.backend.model.*;
import ntnu.idatt2106.backend.repository.GroceryNotificationRepository;
import ntnu.idatt2106.backend.repository.RefrigeratorGroceryRepository;
import ntnu.idatt2106.backend.repository.RefrigeratorUserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;

public class NotificationServiceTest {

    @Mock
    private GroceryNotificationRepository groceryNotificationRepository;
    @Mock
    private RefrigeratorUserRepository refrigeratorUserRepository;
    @Mock
    private RefrigeratorGroceryRepository refrigeratorGroceryRepository;
    @InjectMocks
    private NotificationService notificationService;


    private Grocery grocery;
    private User user;
    private Refrigerator refrigerator;
    private RefrigeratorGrocery refrigeratorGrocery;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        grocery = Grocery.builder()
                .name("Milk")
                .groceryExpiryDays(14)
                .description("Milk from cow")
                .subCategory(SubCategory
                        .builder().build())
                .build();

        user = new User();
        user.setId("testUserId");
        user.setEmail("testuser@test.com");

        refrigerator = new Refrigerator();
        refrigerator.setId(1L);
        refrigerator.setName("Test Refrigerator");

        refrigeratorGrocery = RefrigeratorGrocery.builder()
                .grocery(grocery)
                .refrigerator(refrigerator)
                .build();


    }

    @Test
    public void testGenerateNotificationsNoPrevious(){
        List<RefrigeratorGrocery> refrigeratorGroceries = new ArrayList<>();
        refrigeratorGrocery.setPhysicalExpireDate(new Date());
        refrigeratorGroceries.add(refrigeratorGrocery);

        when(groceryNotificationRepository.findAllByGroceryEntity(refrigeratorGroceries.get(0))).thenReturn(new ArrayList<>());
        notificationService.generateNotifications(refrigeratorGroceries, user);

        verify(groceryNotificationRepository, times(1)).save(Mockito.any(GroceryNotification.class));
    }

    @Test
    public void testGenerateNotificationsOnePrevious(){
        List<RefrigeratorGrocery> refrigeratorGroceries = new ArrayList<>();
        refrigeratorGrocery.setPhysicalExpireDate(new Date());
        refrigeratorGroceries.add(refrigeratorGrocery);

        ArrayList<GroceryNotification> notifications = new ArrayList<>();
        notifications.add(GroceryNotification.builder().build());

        when(groceryNotificationRepository.findAllByGroceryEntity(refrigeratorGroceries.get(0))).thenReturn(notifications);
        notificationService.generateNotifications(refrigeratorGroceries, user);

        verify(groceryNotificationRepository, times(1)).save(Mockito.any(GroceryNotification.class));
    }

    @Test
    public void testGenerateNotificationsTwoPrevious(){
        List<RefrigeratorGrocery> refrigeratorGroceries = new ArrayList<>();
        refrigeratorGrocery.setPhysicalExpireDate(new Date());
        refrigeratorGroceries.add(refrigeratorGrocery);

        ArrayList<GroceryNotification> notifications = new ArrayList<>();
        notifications.add(GroceryNotification.builder().build());
        notifications.add(GroceryNotification.builder().build());

        when(groceryNotificationRepository.findAllByGroceryEntity(refrigeratorGroceries.get(0))).thenReturn(notifications);
        notificationService.generateNotifications(refrigeratorGroceries, user);

        verify(groceryNotificationRepository, times(0)).save(Mockito.any(GroceryNotification.class));
    }

    @Test
    public void testGenerateNotificationsItemDoesNotNeed() throws ParseException {
        List<RefrigeratorGrocery> refrigeratorGroceries = new ArrayList<>();

        String date_string = "26-04-2300";
        //Instantiating the SimpleDateFormat class
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        //Parsing the given String to Date object
        Date date = formatter.parse(date_string);

        refrigeratorGrocery.setPhysicalExpireDate(date);
        refrigeratorGroceries.add(refrigeratorGrocery);
        ArrayList<GroceryNotification> notifications = new ArrayList<>();

        when(groceryNotificationRepository.findAllByGroceryEntity(refrigeratorGroceries.get(0))).thenReturn(notifications);
        notificationService.generateNotifications(refrigeratorGroceries, user);

        verify(groceryNotificationRepository, times(0)).save(Mockito.any(GroceryNotification.class));
    }

    @Test
    public void testGenerateNotificationsItemDoesNotNeedAlreadyExists() throws ParseException {
        List<RefrigeratorGrocery> refrigeratorGroceries = new ArrayList<>();

        String date_string = "26-04-2023";
        //Instantiating the SimpleDateFormat class
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        //Parsing the given String to Date object
        Date date = formatter.parse(date_string);

        refrigeratorGrocery.setPhysicalExpireDate(date);
        refrigeratorGroceries.add(refrigeratorGrocery);

        ArrayList<GroceryNotification> notifications = new ArrayList<>();
        notifications.add(GroceryNotification.builder().build());

        when(groceryNotificationRepository.findAllByGroceryEntity(refrigeratorGroceries.get(0))).thenReturn(notifications);
        notificationService.generateNotifications(refrigeratorGroceries, user);

        verify(groceryNotificationRepository, times(0)).save(Mockito.any(GroceryNotification.class));
    }

    @Test
    public void testGenerateNotificationsThreeDayWarning() throws ParseException {
        List<RefrigeratorGrocery> refrigeratorGroceries = new ArrayList<>();
        // Create a new Date object
        Date currentDate = new Date();

// Create a Calendar object and set it to the current date
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);

// Add three days to the calendar object
        calendar.add(Calendar.DATE, 3);

// Get the new date from the calendar object
        Date newDate = calendar.getTime();
        refrigeratorGrocery.setPhysicalExpireDate(newDate);
        refrigeratorGroceries.add(refrigeratorGrocery);

        ArrayList<GroceryNotification> notifications = new ArrayList<>();
        when(groceryNotificationRepository.findAllByGroceryEntity(refrigeratorGroceries.get(0))).thenReturn(notifications);
        notificationService.generateNotifications(refrigeratorGroceries, user);

        verify(groceryNotificationRepository, times(1)).save(Mockito.any(GroceryNotification.class));
    }
}
