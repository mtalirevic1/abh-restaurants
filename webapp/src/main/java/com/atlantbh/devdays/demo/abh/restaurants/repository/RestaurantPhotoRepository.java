package com.atlantbh.devdays.demo.abh.restaurants.repository;

import com.atlantbh.devdays.demo.abh.restaurants.domain.Restaurant;
import com.atlantbh.devdays.demo.abh.restaurants.domain.RestaurantPhoto;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface RestaurantPhotoRepository extends CrudRepository<RestaurantPhoto, Long> {
  List<RestaurantPhoto> findByRestaurant(Restaurant restaurant);
}
