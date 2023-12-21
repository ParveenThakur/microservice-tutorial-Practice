package com.icwd.hotel.services;

import com.icwd.hotel.entites.Hotel;

import java.util.List;

public interface HotelService {

    //create
    Hotel create(Hotel hotel);

    // getAll
    List<Hotel> getAll();

    //get Single Hotel
    Hotel get(String id);
}
