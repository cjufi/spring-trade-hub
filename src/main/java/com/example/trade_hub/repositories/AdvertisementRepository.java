package com.example.trade_hub.repositories;

import com.example.trade_hub.entities.Advertisement;
import com.example.trade_hub.entities.AdvertisementStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface AdvertisementRepository extends JpaRepository<Advertisement,Integer> {
    @Transactional
    @Modifying
    @Query(value = "update advertisement a set a.status_id = " +
            "(CASE WHEN datediff(curdate(),a.creation_date) BETWEEN 25 AND 30 THEN 3 " +
            "WHEN datediff(curdate(),a.creation_date) > 30 THEN 2 " +
            "ELSE 1 " +
            "END) " +
            "WHERE a.status_id IN (1,3);",nativeQuery = true)
    void updateStatuses();


    List<Advertisement> findAdvertisementsByAdvertisementStatus_StatusName(String name);


    List<Advertisement> findAdvertisementsByAdvertisementStatus_StatusNameOrAdvertisementStatus_StatusName(String status1,String status2);
    Optional<Advertisement> findById(int id);
    @Query("SELECT a from Advertisement a where a.id = :id and a.advertisementStatus.statusName <> :status")
    Optional<Advertisement> findByIdAndStatus(int id,String status);

    @Transactional
    @Modifying
    @Query(value ="UPDATE Advertisement a " +
            "SET a.title = :title, " +
            "a.description = :description, " +
            "a.picture = :picture, " +
            "a.price = :price, " +
            "a.advertisementCategory.id= :advertisementCategory " +
            "WHERE a.id = :id")
    void patchAdvertisement(@Param("id")int id, @Param("title")String title,@Param("description") String description,@Param("picture") byte[] picture,@Param("price") double price,@Param("advertisementCategory") int advertisementCategory);
    @Query("select a " +
            "from Advertisement a join AdvertisementCategory ac on a.advertisementCategory.id = ac.id " +
            "where ac.categoryName = :categoryName " +
            "order by a.advertisementPromotion.id desc,a.creationDate desc")
    Page<Advertisement> findAdvertisementsFromCategoryAndOrderByPromotion(String categoryName, Pageable pageable);

    List<Advertisement> findAdvertisementsByTitleContaining(String keywords);

    @Query("select a " +
            "from Advertisement a join AdvertisementCategory ac on a.advertisementCategory.id = ac.id " +
            "where ac.categoryName = :categoryName and a.creationDate >= :dateOfActiveAd and a.advertisementStatus = :status")
    Page<Advertisement> findAdvertisementsByAdvertisementCategory_CategoryName(String categoryName, Date dateOfActiveAd, Pageable pageable, AdvertisementStatus status);

    Optional<Advertisement> findByIdAndAppUser_Id(int adId,int userId);

    @Transactional
    @Modifying
    @Query(value = "update Advertisement a " +
            "set a.promotionExpiration=null, a.advertisementPromotion.id = 1 " +
            "where CURRENT_DATE > a.promotionExpiration")
    void updateAdvertisementsPromotions();

    Page<Advertisement> findByAppUser_EmailAndAdvertisementStatus_StatusName(String name, String status, Pageable pageable);
    Page<Advertisement> findByAppUser_IdAndAdvertisementCategory_Id(int userId,int categoryId,Pageable pageable);
    Page<Advertisement> findByAppUser_Id(int userId,Pageable pageable);

    Page<Advertisement> findAdvertisementsByFollowersEmail(String email,Pageable pageable);
    Page<Advertisement> findAdvertisementsByAdvertisementCategory_IdAndFollowersEmail(Integer category,String email,Pageable pageable);

}
