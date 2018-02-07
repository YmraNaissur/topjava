package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;

/**
 * GKislin
 * 31.05.2015.
 */
public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,10,0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,13,0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,20,0), "Ужин", 520),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,10,0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,13,0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,20,0), "Ужин", 500)
        );
        for (UserMealWithExceed m: getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(22,0), 2000)) {
            System.out.println(m);
        }
//        .toLocalDate();
//        .toLocalTime();
    }

    public static List<UserMealWithExceed> getFilteredWithExceeded
            (List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        List<UserMealWithExceed> userMealWithExceeds = new ArrayList<>();
        Map<LocalDate, Integer> userMealCaloriesSumsByDates = new HashMap<>();

        // Form map of calories sums by dates
        for (UserMeal meal: mealList) {
            LocalDate date = meal.getDateTime().toLocalDate();
            int currentCalories = meal.getCalories();
            int sumOfCaloriesOfTheDate = userMealCaloriesSumsByDates.getOrDefault(date, 0);
            userMealCaloriesSumsByDates.put(date, sumOfCaloriesOfTheDate + currentCalories);
        }

        // Fill the UserMealWithExceed list only with those records,
        // which sum of calories by date exceeds daily limit;
        // and which time is between start and end time.
        for (UserMeal meal: mealList) {
            LocalDateTime dateTime = meal.getDateTime();
            String description = meal.getDescription();
            int calories = meal.getCalories();

            if (userMealCaloriesSumsByDates.get(dateTime.toLocalDate()) > caloriesPerDay
                    && TimeUtil.isBetween(dateTime.toLocalTime(), startTime, endTime)) {

                UserMealWithExceed mealWithExceed = new UserMealWithExceed
                        (dateTime, description, calories, true);
                userMealWithExceeds.add(mealWithExceed);
            }
        }

        return userMealWithExceeds;
    }
}
