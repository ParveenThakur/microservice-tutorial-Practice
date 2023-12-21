package com.icwd.user.service.impl;

import com.icwd.user.service.entities.Hotel;
import com.icwd.user.service.entities.Rating;
import com.icwd.user.service.entities.User;
import com.icwd.user.service.exceptions.ResourceNotFoundException;
import com.icwd.user.service.external.services.HotelService;
import com.icwd.user.service.repositories.UserRepository;
import com.icwd.user.service.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private HotelService hotelService;

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public User saveUser(User user) {
        //generate unique user id everytime
        String randomUserId = UUID.randomUUID().toString();
        user.setUserId(randomUserId);
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUser() {
        //implement
        return userRepository.findAll();
    }

    @Override
    public User getUser(String userId) {

        //get user from database with the help of user repository
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User with given id is not found on server !!"+userId));

        /*
        // Below code we got error :
        java.lang.ClassCastException: class java.util.LinkedHashMap cannot be cast to class com.icwd.user.service.entities.Rating (java.util.LinkedHashMap is in module java.base of loader 'bootstrap'; com.icwd.user.service.entities.Rating is in unnamed module of loader 'app')
	at java.base/java.util.stream.ReferencePipeline$3$1.accept(ReferencePipeline.java:197) ~[na:na]
	at java.base/java.util.ArrayList$ArrayListSpliterator.forEachRemaining(ArrayList.java:1625) ~[na:na]
	at java.base/java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:509) ~[na:na]
	at java.base/java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:499) ~[na:na]
	at java.base/java.util.stream.ReduceOps$ReduceOp.evaluateSequential(ReduceOps.java:921) ~[na:na]
	at java.base/java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:234) ~[na:na]
	at java.base/java.util.stream.ReferencePipeline.collect(ReferencePipeline.java:682) ~[na:na]

        //fetch the rating from above user from Rating Service
        // http://localhost:8083/ratings/users/ddee3d37-6a9e-4855-9aa4-cc8efc3f9191
        ArrayList<Rating> ratingsForObject = restTemplate.getForObject("http://localhost:8083/ratings/users/"+user.getUserId(), ArrayList.class);
        logger.info("{} ", ratingsForObject);
        List<Rating> ratingList = ratingsForObject.stream().map(rating -> {
            // Api call to hetel service to get the hotel
            // http://localhost:8082/hotels/a1d20aa8-c27a-4bb0-919a-3a29e72656eb
            ResponseEntity<Hotel> forEntity = restTemplate.getForEntity("// http://localhost:8082/hotels/"+rating.getHotelId(), Hotel.class);
            Hotel hotel = forEntity.getBody();
            logger.info("response status code : {} ", forEntity.getStatusCode());
            //set the hotel to rating
            rating.setHotel(hotel);
            //return the rating
            return rating;
        }).collect(Collectors.toList());*/

        // Solution of above problem

        Rating[] ratingsForObject = restTemplate.getForObject("http://RATING-SERVICE/ratings/users/"+user.getUserId(), Rating[].class);
        logger.info("{} ", ratingsForObject);
        List<Rating> ratings = Arrays.stream(ratingsForObject).toList();

        List<Rating> ratingList = ratings.stream().map(rating -> {
            // Api call to hetel service to get the hotel
            // http://localhost:8082/hotels/a1d20aa8-c27a-4bb0-919a-3a29e72656eb
           /*
           This code works with rest template
            ResponseEntity<Hotel> forEntity = restTemplate.getForEntity("http://HOTEL-SERVICE/hotels/"+rating.getHotelId(), Hotel.class);
            Hotel hotel = forEntity.getBody();
            logger.info("response status code : {} ", forEntity.getStatusCode());
            */

            //THis method is used with figen client
            Hotel hotel = hotelService.getHotel(rating.getHotelId());

            //set the hotel to rating
            rating.setHotel(hotel);

            //return the rating
            return rating;
        }).collect(Collectors.toList());

        user.setRatings(ratingList);
        return user;
    }
}
