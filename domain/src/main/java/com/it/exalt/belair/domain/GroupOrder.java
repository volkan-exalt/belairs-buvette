package com.it.exalt.belair.domain;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GroupOrder extends Order {

    private final Map<FestivalGoer, TokenBalance> contributions;

    public GroupOrder(FestivalGoer representative, List<OrderLine> lines, Map<FestivalGoer, TokenBalance> contributions) {
        super(representative, lines);
        if (contributions == null || contributions.isEmpty()) {
            throw new DomainValidationException("Group order requires at least one contributor");
        }
        this.contributions = new HashMap<>(contributions);
        validateContributions();
        applyContributions();
    }

    public Map<FestivalGoer, TokenBalance> contributions() {
        return Collections.unmodifiableMap(contributions);
    }

    @Override
    public void cancel() {
        if (status() == OrderStatus.ACKNOWLEDGED || status() == OrderStatus.READY) {
            throw new DomainValidationException("Only orders that are not yet acknowledged can be canceled");
        }
        contributions.forEach(FestivalGoer::addTokens);
        cancelWithoutRefund();
    }

    private void validateContributions() {
        TokenBalance totalContribution = contributions.values().stream()
                .reduce(new TokenBalance(0, 0), TokenBalance::add);
        if (!totalContribution.canCover(totalCost())) {
            throw new DomainValidationException("Group contributions do not cover the total order cost");
        }
        contributions.forEach((participant, contributedBalance) -> {
            if (!participant.tokenBalance().canCover(contributedBalance)) {
                throw new DomainValidationException("Contributor does not have enough tokens to support their contribution");
            }
        });
    }

    private void applyContributions() {
        contributions.forEach((participant, contributedBalance) -> participant.deductTokens(contributedBalance));
    }
}
