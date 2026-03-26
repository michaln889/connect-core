package com.connectcore.service;

import com.connectcore.model.entity.Subscription;
import com.connectcore.model.entity.User;
import com.connectcore.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    @Transactional
    public Subscription create(Subscription subscription) {
        return subscriptionRepository.save(subscription);
    }

    public List<Subscription> getUserSubscriptions(User user) {
        return subscriptionRepository.findByUser(user);
    }

    public Subscription getActiveSubscription(User user) {
        return subscriptionRepository
                .findFirstByUserOrderByEndDateDesc(user)
                .orElse(null);
    }

    @Transactional
    public Subscription changeSubscription(User user, Subscription newSub) {
        return subscriptionRepository.save(newSub);
    }
}