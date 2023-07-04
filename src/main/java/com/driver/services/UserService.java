package com.driver.services;


import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.model.WebSeries;
import com.driver.repository.UserRepository;
import com.driver.repository.WebSeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    WebSeriesRepository webSeriesRepository;

    public UserService() {
        this.userRepository = userRepository;
        this.webSeriesRepository = webSeriesRepository;
    }

    public Integer addUser(User user){

        //Jut simply add the user to the Db and return the userId returned by the repository
        User savedUser=userRepository.save(user);
        int id=savedUser.getId();
        return id;
    }

    public Integer getAvailableCountOfWebSeriesViewable(Integer userId){

        //Return the count of all webSeries that a user can watch based on his ageLimit and subscriptionType
        //Hint: Take out all the Webseries from the WebRepository
        int count=0;
        List<WebSeries> webSeriesList=webSeriesRepository.findAll();
        User user=userRepository.findById(userId).get();
        for(WebSeries webSeries:webSeriesList) {
            if (user.getAge() >= webSeries.getAgeLimit()) {
                if (user.getSubscription().getSubscriptionType().equals(SubscriptionType.BASIC)
                        && webSeries.getSubscriptionType().equals(SubscriptionType.BASIC)) {
                    count++;
                } else if (user.getSubscription().getSubscriptionType().equals(SubscriptionType.PRO)
                        && (webSeries.getSubscriptionType().equals(SubscriptionType.BASIC) ||
                        webSeries.getSubscriptionType().equals(SubscriptionType.PRO))) {
                    count++;
                } else {
                    count++;
                }
            }
        }
        return count;
    }

}
