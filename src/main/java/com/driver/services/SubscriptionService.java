package com.driver.services;


import com.driver.EntryDto.SubscriptionEntryDto;
import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.repository.SubscriptionRepository;
import com.driver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SubscriptionService {

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    UserRepository userRepository;

    public Integer buySubscription(SubscriptionEntryDto subscriptionEntryDto){

        //Save The subscription Object into the Db and return the total Amount that user has to pay
        int totalAmount=0;
        User user=userRepository.findById(subscriptionEntryDto.getUserId()).get();
        Subscription subscription=new Subscription();
        subscription.setSubscriptionType(subscriptionEntryDto.getSubscriptionType());
        subscription.setNoOfScreensSubscribed(subscriptionEntryDto.getNoOfScreensRequired());
        if(subscription.getSubscriptionType().equals(SubscriptionType.BASIC)){
        totalAmount=500+200*subscriptionEntryDto.getNoOfScreensRequired();
        }
        else if (subscription.getSubscriptionType().equals(SubscriptionType.PRO)){
            totalAmount=800+250*subscriptionEntryDto.getNoOfScreensRequired();
        }
        else if (subscription.getSubscriptionType().equals(SubscriptionType.ELITE)) {
            totalAmount=1000+350*subscriptionEntryDto.getNoOfScreensRequired();
        }
        subscription.setTotalAmountPaid(totalAmount);
        subscription.setUser(user);
        user.setSubscription(subscription);
        userRepository.save(user);
        return totalAmount;
    }

    public Integer upgradeSubscription(Integer userId)throws Exception{

        //If you are already at an ElITE subscription : then throw Exception ("Already the best Subscription")
        //In all other cases just try to upgrade the subscription and tell the difference of price that user has to pay
        //update the subscription in the repository

        //In this function you need to upgrade the subscription to  its next level
        //ie if You are A BASIC subscriber update to PRO and if You are a PRO upgrade to ELITE.
        //Incase you are already an ELITE member throw an Exception
        //and at the end return the difference in fare that you need to pay to get this subscription done.
        int amount=0;
        User user=userRepository.findById(userId).get();
        if(user.getSubscription().getSubscriptionType().equals(SubscriptionType.ELITE)){
            throw new Exception("Already the best Subscription");
        } else if (user.getSubscription().getSubscriptionType().equals(SubscriptionType.BASIC)) {
            user.getSubscription().setSubscriptionType(SubscriptionType.PRO);
            user.getSubscription().setTotalAmountPaid(800+250*user.getSubscription().getNoOfScreensSubscribed());
            amount=user.getSubscription().getTotalAmountPaid();
        }
        else if (user.getSubscription().getSubscriptionType().equals(SubscriptionType.PRO)){
            user.getSubscription().setSubscriptionType(SubscriptionType.ELITE);
            user.getSubscription().setTotalAmountPaid(1000+350*user.getSubscription().getNoOfScreensSubscribed());
            amount=user.getSubscription().getTotalAmountPaid();
        }
        userRepository.save(user);
        return amount;
    }

    public Integer calculateTotalRevenueOfHotstar(){

        //We need to find out total Revenue of hotstar : from all the subscriptions combined
        //Hint is to use findAll function from the SubscriptionDb

        int totalRevenue=0;
        List<Subscription> subscriptions=subscriptionRepository.findAll();
        for(Subscription subscription:subscriptions){
            totalRevenue+=subscription.getTotalAmountPaid();
        }
        return totalRevenue;
    }

}
