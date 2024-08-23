package com.example.trade_hub.repositories;

import com.example.trade_hub.entities.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating,Integer> {
    Optional<Rating> findRatingByAdvertisement_IdAndAppUser_Id(int adId,int userId);

    List<Rating> findRatingByAdvertisement_AppUser_EmailAndSatisfiedIsTrue(String email);
    List<Rating> findRatingByAdvertisement_AppUser_EmailAndSatisfiedIsFalse(String email);
    Integer countRatingByAdvertisement_AppUser_EmailAndSatisfiedIsFalse(String email);
    Integer countRatingByAdvertisement_AppUser_EmailAndSatisfiedIsTrue(String email);
}
