package com.it.exalt.belair.domain;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class OrderPreparationEstimator {
    private OrderPreparationEstimator() {
        // utility class
    }

    public static int estimate(List<OrderLine> lines) {
        if (lines == null || lines.isEmpty()) {
            return 0;
        }

        int snackTime = 0;
        int mealTypes = 0;
        Set<DrinkType> distinctDrinkTypes = new HashSet<>();
        Set<FoodType> distinctMealTypes = new HashSet<>();
        Set<FoodType> distinctSnackTypes = new HashSet<>();

        for (OrderLine line : lines) {
            if (line instanceof DrinkOrderLine drinkLine) {
                distinctDrinkTypes.add(drinkLine.drinkType());
            } else if (line instanceof FoodOrderLine foodLine) {
                if (foodLine.foodType() == FoodType.MEAL) {
                    distinctMealTypes.add(FoodType.MEAL);
                } else {
                    distinctSnackTypes.add(FoodType.SNACK);
                }
            }
        }

        int snackMinutes = distinctSnackTypes.size() * FoodType.SNACK.preparationTime();
        int mealMinutes = distinctMealTypes.size() * FoodType.MEAL.preparationTime();
        int drinkMinutes = distinctDrinkTypes.stream()
                .mapToInt(DrinkType::preparationTime)
                .sum();

        if (!distinctMealTypes.isEmpty() && !distinctDrinkTypes.isEmpty()) {
            int longestDrink = distinctDrinkTypes.stream()
                    .mapToInt(DrinkType::preparationTime)
                    .max()
                    .orElse(0);
            return mealMinutes + longestDrink + snackMinutes;
        }

        return drinkMinutes + snackMinutes + mealMinutes;
    }
}
