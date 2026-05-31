package com.it.exalt.belair.domain;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DomainModelTest {

    @Test
    void shouldPlaceOrderAndCalculateEstimate() {
        FestivalGoer festivalGoer = new FestivalGoer("Alice", new TokenBalance(6, 9));
        OrderLine drink1 = new DrinkOrderLine("Coke", DrinkType.NON_ALCOHOLIC, 2);
        OrderLine drink2 = new DrinkOrderLine("Biere", DrinkType.NORMAL_ALCOHOLIC, 1);
        OrderLine snack = new FoodOrderLine("Chips", FoodType.SNACK, 1);
        OrderLine meal = new FoodOrderLine("Burger", FoodType.MEAL, 1);

        Order order = festivalGoer.placeOrder(List.of(drink1, drink2, snack, meal));

        assertEquals(new TokenBalance(5, 5), festivalGoer.tokenBalance());
        order.acknowledge();
        assertEquals(OrderStatus.ACKNOWLEDGED, order.status());
        assertEquals(14, order.estimatedPreparationMinutes());
    }

    @Test
    void shouldCancelOrderAndRefundTokens() {
        FestivalGoer festivalGoer = new FestivalGoer("Bob", new TokenBalance(3, 4));
        OrderLine snack = new FoodOrderLine("Popcorn", FoodType.SNACK, 1);
        Order order = festivalGoer.placeOrder(List.of(snack));

        assertEquals(new TokenBalance(3, 3), festivalGoer.tokenBalance());
        order.cancel();
        assertEquals(OrderStatus.CANCELED, order.status());
        assertEquals(new TokenBalance(3, 4), festivalGoer.tokenBalance());
    }

    @Test
    void shouldValidateFestivalGoerAndTokenBalances() {
        FestivalGoer festivalGoer = new FestivalGoer(" Alice ", new TokenBalance(6, 9));

        assertEquals("Alice", festivalGoer.name());
        assertEquals(new TokenBalance(6, 9), festivalGoer.tokenBalance());
        assertEquals(new TokenBalance(7, 11), festivalGoer.tokenBalance().add(new TokenBalance(1, 2)));
        assertEquals(new TokenBalance(5, 7), festivalGoer.tokenBalance().subtract(new TokenBalance(1, 2)));
        assertTrue(festivalGoer.tokenBalance().canCover(new TokenBalance(6, 9)));
        assertFalse(festivalGoer.tokenBalance().canCover(new TokenBalance(7, 9)));
        assertTrue(festivalGoer.tokenBalance().toString().contains("drinkTokens=6"));

        assertThrows(DomainValidationException.class, () -> new FestivalGoer(" ", new TokenBalance(0, 0)));
        assertThrows(DomainValidationException.class, () -> new TokenBalance(-1, 0));
        assertThrows(DomainValidationException.class, () -> festivalGoer.deductTokens(new TokenBalance(7, 0)));
    }

    @Test
    void shouldExposeDrinkAndFoodLineCostsAndTypes() {
        DrinkOrderLine water = new DrinkOrderLine("Water", DrinkType.NON_ALCOHOLIC, 3);
        DrinkOrderLine beer = new DrinkOrderLine("Beer", DrinkType.NORMAL_ALCOHOLIC, 2);
        DrinkOrderLine cocktail = new DrinkOrderLine("Cocktail", DrinkType.PREMIUM_ALCOHOLIC, 1);
        FoodOrderLine chips = new FoodOrderLine("Chips", FoodType.SNACK, 2);
        FoodOrderLine burger = new FoodOrderLine("Burger", FoodType.MEAL, 1);

        assertEquals(new TokenBalance(0, 0), water.cost());
        assertEquals(new TokenBalance(2, 0), beer.cost());
        assertEquals(new TokenBalance(2, 0), cocktail.cost());
        assertEquals(new TokenBalance(0, 2), chips.cost());
        assertEquals(new TokenBalance(0, 3), burger.cost());
        assertFalse(DrinkType.NON_ALCOHOLIC.isAlcoholic());
        assertTrue(DrinkType.NORMAL_ALCOHOLIC.isAlcoholic());
        assertEquals(10, FoodType.MEAL.preparationTime());
        assertTrue(new DrinkOrderLine("Beer", DrinkType.NORMAL_ALCOHOLIC, 1).isSameType(beer));
        assertFalse(chips.isSameType(beer));
        assertEquals(new DrinkOrderLine("Beer", DrinkType.NORMAL_ALCOHOLIC, 1), beer);

        assertThrows(DomainValidationException.class, () -> new DrinkOrderLine("", DrinkType.NORMAL_ALCOHOLIC, 1));
        assertThrows(DomainValidationException.class, () -> new FoodOrderLine("Chips", FoodType.SNACK, 0));
    }

    @Test
    void shouldProtectOrderLifecycleRules() {
        FestivalGoer festivalGoer = new FestivalGoer("Dana", new TokenBalance(4, 4));
        OrderLine beer = new DrinkOrderLine("Beer", DrinkType.NORMAL_ALCOHOLIC, 1);
        Order order = festivalGoer.placeOrder(List.of(beer));

        assertEquals(OrderStatus.CREATED, order.status());
        assertEquals(festivalGoer, order.owner());
        assertThrows(UnsupportedOperationException.class, () -> order.lines().add(beer));

        order.changeItems(List.of(new DrinkOrderLine("Water", DrinkType.NON_ALCOHOLIC, 1)));
        assertEquals(new TokenBalance(0, 0), order.totalCost());
        order.acknowledge();

        assertThrows(DomainValidationException.class, order::acknowledge);
        assertThrows(DomainValidationException.class, () -> order.changeItems(List.of(beer)));
        assertThrows(DomainValidationException.class, order::cancel);
        assertThrows(DomainValidationException.class, () -> order.prepareItem(beer, 1));
        assertFalse(order.canMarkReady());

        OrderLine water = new DrinkOrderLine("Water", DrinkType.NON_ALCOHOLIC, 1);
        order.prepareItem(water, 5);
        assertTrue(order.canMarkReady());
        order.markReady();
        assertEquals(OrderStatus.READY, order.status());
    }

    @Test
    void shouldValidateOrderCreationAndPreparation() {
        FestivalGoer festivalGoer = new FestivalGoer("Eli", new TokenBalance(4, 4));

        assertThrows(DomainValidationException.class, () -> festivalGoer.placeOrder(List.of()));

        Order order = festivalGoer.placeOrder(List.of(new DrinkOrderLine("Water", DrinkType.NON_ALCOHOLIC, 1)));
        assertThrows(DomainValidationException.class, () -> order.prepareItem(order.lines().getFirst(), 1));
        assertThrows(DomainValidationException.class, order::markReady);

        order.acknowledge();
        assertThrows(DomainValidationException.class, () -> order.prepareItem(order.lines().getFirst(), 0));
    }

    @Test
    void shouldApproveChangeRequestOnlyWhenPreparedItemCanBeTransferred() {
        FestivalGoer festivalGoer = new FestivalGoer("Carla", new TokenBalance(2, 2));
        OrderLine beer = new DrinkOrderLine("Biere normale", DrinkType.NORMAL_ALCOHOLIC, 1);
        Order order = festivalGoer.placeOrder(List.of(beer));
        order.acknowledge();
        order.prepareItem(beer, 1);

        OrderLine newBeer = new DrinkOrderLine("Biere normale", DrinkType.NORMAL_ALCOHOLIC, 1);
        OrderChangeRequest request = order.requestChange(List.of(newBeer));
        request.approve();

        assertTrue(request.isApproved());
        assertEquals(OrderStatus.ACKNOWLEDGED, order.status());
    }

    @Test
    void shouldRejectInvalidOrderChangeRequests() {
        FestivalGoer festivalGoer = new FestivalGoer("Carla", new TokenBalance(3, 3));
        OrderLine beer = new DrinkOrderLine("Biere normale", DrinkType.NORMAL_ALCOHOLIC, 1);
        Order order = festivalGoer.placeOrder(List.of(beer));

        assertThrows(DomainValidationException.class, () -> order.requestChange(List.of(beer)));

        order.acknowledge();
        OrderChangeRequest request = order.requestChange(List.of(new DrinkOrderLine("Another beer", DrinkType.NORMAL_ALCOHOLIC, 1)));
        assertThrows(DomainValidationException.class, request::approve);
        request.reject();
        assertTrue(request.isRejected());
        assertThrows(DomainValidationException.class, request::reject);
        assertThrows(DomainValidationException.class, () -> new OrderChangeRequest(order, List.of()));
    }

    @Test
    void shouldCreateGroupOrderWithContributions() {
        FestivalGoer alice = new FestivalGoer("Alice", new TokenBalance(4, 4));
        FestivalGoer bob = new FestivalGoer("Bob", new TokenBalance(2, 2));
        OrderLine meal = new FoodOrderLine("Pizza", FoodType.MEAL, 1);
        OrderLine drink = new DrinkOrderLine("Cocktail", DrinkType.PREMIUM_ALCOHOLIC, 1);

        GroupOrder groupOrder = alice.placeGroupOrder(
                List.of(meal, drink),
                Map.of(alice, new TokenBalance(1, 2), bob, new TokenBalance(1, 1))
        );

        assertEquals(new TokenBalance(3, 2), alice.tokenBalance());
        assertEquals(new TokenBalance(1, 1), bob.tokenBalance());
        assertEquals(OrderStatus.CREATED, groupOrder.status());
    }

    @Test
    void shouldValidateAndCancelGroupOrders() {
        FestivalGoer alice = new FestivalGoer("Alice", new TokenBalance(4, 4));
        FestivalGoer bob = new FestivalGoer("Bob", new TokenBalance(1, 1));
        OrderLine meal = new FoodOrderLine("Pizza", FoodType.MEAL, 1);

        assertThrows(DomainValidationException.class, () -> alice.placeGroupOrder(List.of(meal), Map.of()));
        assertThrows(
                DomainValidationException.class,
                () -> alice.placeGroupOrder(List.of(meal), Map.of(bob, new TokenBalance(0, 3)))
        );

        GroupOrder groupOrder = alice.placeGroupOrder(List.of(meal), Map.of(alice, new TokenBalance(0, 3)));
        assertThrows(UnsupportedOperationException.class, () -> groupOrder.contributions().clear());
        assertEquals(new TokenBalance(4, 1), alice.tokenBalance());

        groupOrder.cancel();
        assertEquals(OrderStatus.CANCELED, groupOrder.status());
        assertEquals(new TokenBalance(4, 4), alice.tokenBalance());
    }

    @Test
    void shouldConfirmTokenTransfer() {
        FestivalGoer sender = new FestivalGoer("Sender", new TokenBalance(3, 3));
        FestivalGoer recipient = new FestivalGoer("Recipient", new TokenBalance(1, 1));

        TokenTransferRequest request = sender.requestTokenTransfer(recipient, 2, 1);
        request.confirm();

        assertEquals(TokenTransferRequest.Status.CONFIRMED, request.status());
        assertEquals(new TokenBalance(1, 2), sender.tokenBalance());
        assertEquals(new TokenBalance(3, 2), recipient.tokenBalance());
    }

    @Test
    void shouldRejectOrValidateTokenTransfers() {
        FestivalGoer sender = new FestivalGoer("Sender", new TokenBalance(3, 3));
        FestivalGoer recipient = new FestivalGoer("Recipient", new TokenBalance(1, 1));

        assertThrows(DomainValidationException.class, () -> sender.requestTokenTransfer(recipient, 4, 0));
        assertThrows(DomainValidationException.class, () -> sender.requestTokenTransfer(recipient, 0, 4));
        assertThrows(DomainValidationException.class, () -> sender.requestTokenTransfer(recipient, 3, 4));

        TokenTransferRequest request = sender.requestTokenTransfer(recipient, 1, 1);
        request.reject();
        assertEquals(TokenTransferRequest.Status.REJECTED, request.status());
        assertThrows(DomainValidationException.class, request::confirm);
    }

    @Test
    void shouldComputeHydrationReminderIntervals() {
        assertTrue(HydrationReminderPolicy.isWithinReminderWindow(LocalTime.of(11, 0)));
        assertTrue(HydrationReminderPolicy.isWithinReminderWindow(LocalTime.of(15, 30)));
        assertFalse(HydrationReminderPolicy.isWithinReminderWindow(LocalTime.of(10, 59)));
        assertFalse(HydrationReminderPolicy.isWithinReminderWindow(LocalTime.of(19, 0)));

        assertEquals(Duration.ofHours(1), HydrationReminderPolicy.nextReminderInterval(LocalTime.of(13, 0), 2));
        assertEquals(Duration.ofMinutes(30), HydrationReminderPolicy.nextReminderInterval(LocalTime.of(13, 0), 4));
        assertEquals(Duration.between(LocalTime.of(10, 0), LocalTime.of(11, 0)), HydrationReminderPolicy.nextReminderInterval(LocalTime.of(10, 0), 0));
        assertEquals(Duration.ofHours(15), HydrationReminderPolicy.nextReminderInterval(LocalTime.of(20, 0), 0));
        assertThrows(DomainValidationException.class, () -> HydrationReminderPolicy.nextReminderInterval(null, 0));
        assertEquals(0, OrderPreparationEstimator.estimate(new ArrayList<>()));
        assertEquals(0, OrderPreparationEstimator.estimate(null));
    }

    @Test
    void shouldCreateDomainValidationExceptionWithCause() {
        RuntimeException cause = new RuntimeException("cause");
        DomainValidationException exception = new DomainValidationException("message", cause);

        assertEquals("message", exception.getMessage());
        assertSame(cause, exception.getCause());
    }
}
