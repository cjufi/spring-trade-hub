package com.example.trade_hub.services;

import com.example.trade_hub.dto.advertisements.AdvertisementAddDTO;
import com.example.trade_hub.dto.advertisements.AdvertisementPatchDTO;
import com.example.trade_hub.dto.advertisements.AdvertisementResponseDTO;
import com.example.trade_hub.dto.mappers.AdvertisementResponseMapper;
import com.example.trade_hub.entities.*;
import com.example.trade_hub.exceptions.AdvertisementNotFoundException;
import com.example.trade_hub.exceptions.AdvertisementPromotionNotFoundException;
import com.example.trade_hub.exceptions.UserNotEnoughCreditException;
import com.example.trade_hub.exceptions.UserNotFoundException;
import com.example.trade_hub.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.trade_hub.config.Constants.*;

@Service
@RequiredArgsConstructor
public class AdvertisementService {

    private final AdvertisementRepository advertisementRepository;
    private final UserRepository userRepository;

    private final UserService userService;

    private final AdvertisementResponseMapper advertisementResponseMapper;

    private final AdvertisementPromotionRepository advertisementPromotionRepository;
    private final AdvertisementCategoryRepository advertisementCategoryRepository;

    private final AdvertisementStatusRepository advertisementStatusRepository;


    public AdvertisementResponseDTO addAdvertisement(AdvertisementAddDTO dto,String email) throws UserNotFoundException, UserNotEnoughCreditException, AdvertisementNotFoundException, AdvertisementPromotionNotFoundException {
        String base64 = dto.getPicture();
        byte[] pic = Base64.getDecoder().decode(base64);

      AdvertisementCategory category = advertisementCategoryRepository.findById(dto.getAdvertisementCategory()).get();
      AdvertisementPromotion promotion = advertisementPromotionRepository.findById(dto.getAdvertisementPromotion()).get();

        Advertisement ad = Advertisement.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .picture(pic)
                .price(dto.getPrice())
                .creationDate(new Date())
                .appUser(userRepository.findByEmail(email).get())
                .advertisementCategory(AdvertisementCategory.builder().id(dto.getAdvertisementCategory()).categoryName(category.getCategoryName()).build())
                .advertisementStatus(AdvertisementStatus.builder().id(ADVERTISEMENT_STATUS_ACTIVE_).statusName(ADVERTISEMENT_STATUS_ACTIVE).build())
                .advertisementPromotion(AdvertisementPromotion.builder().id(dto.getAdvertisementPromotion()).title(promotion.getTitle()).build())
                .build();

        Advertisement advertisement =  advertisementRepository.save(ad);
        if(advertisement.getAdvertisementPromotion().getId() != ADVERTISEMENT_PROMOTION_STANDARD){
            userService.activatePromotion(dto.getAdvertisementPromotion(),advertisement.getId(),email);
        }
       return advertisementResponseMapper.apply(advertisement);
    }

    public List<AdvertisementResponseDTO> getAllAdvertisements(String category,int page) {
        List<Advertisement> advertisementList = advertisementRepository.findAdvertisementsFromCategoryAndOrderByPromotion(category, PageRequest.of(page-1,PAGE_SIZE)).getContent();
        return   advertisementList.stream()
                .map(ad -> advertisementResponseMapper.apply(ad))
                .collect(Collectors.toList());
    }

    public byte[] getAdPic(int id) {
      Advertisement advertisement = advertisementRepository.getById(id);
        return advertisement.getPicture();
    }

    public List<AdvertisementResponseDTO> getAdsByStatus(String status) {
        List<Advertisement> ads;

        if(status.equals(ADVERTISEMENT_STATUS_ACTIVE)){
          ads =  advertisementRepository.findAdvertisementsByAdvertisementStatus_StatusNameOrAdvertisementStatus_StatusName(status,ADVERTISEMENT_STATUS_EXPIRES);
        }else{
           ads = advertisementRepository.findAdvertisementsByAdvertisementStatus_StatusName(status);
        }

        return ads.stream().map(advertisementResponseMapper::apply).collect(Collectors.toList());
    }


    public AdvertisementResponseDTO advertisementPatch(int id,AdvertisementPatchDTO dto) throws AdvertisementNotFoundException {
        Advertisement advertisement = advertisementRepository.findById(id).orElseThrow(()-> new AdvertisementNotFoundException("No active advertisement with given id"));
        String title = dto.getTitle();
        String description = dto.getDescription();
        Double price = dto.getPrice();
        Integer status = dto.getAdvertisementStatus();

        if(title != null) advertisement.setTitle(title);
        if(description != null) advertisement.setDescription(description);
        if(price != null && dto.getPrice() > 100) advertisement.setPrice(price);
        if(status != null) advertisement.setAdvertisementStatus(AdvertisementStatus.builder().id(status).build());

        Advertisement patched = advertisementRepository.save(advertisement);
        return advertisementResponseMapper.apply(patched);
    }

    public void followAdvertisement(int id, String userEmail) throws UserNotFoundException, AdvertisementNotFoundException {

        Optional<AppUser> optUser = userRepository.findByEmail(userEmail);
        Optional<Advertisement> optAd = advertisementRepository.findByIdAndStatus(id,ADVERTISEMENT_STATUS_SOLD);

        if(optUser.isEmpty()) throw new UserNotFoundException("No user with given email");
        if(optAd.isEmpty()) throw new AdvertisementNotFoundException("Cant follow expired advertisement");

        AppUser appUser = optUser.get();
        Advertisement ad = optAd.get();
        ad.addFollowers(appUser);
        advertisementRepository.save(ad);
    }

    public void unfollowAdvertisement(int id, String email) throws UserNotFoundException, AdvertisementNotFoundException {
        Optional<AppUser> optUser = userRepository.findByEmail(email);
        Optional<Advertisement> optAd = advertisementRepository.findById(id);

        if(optUser.isEmpty()) throw new UserNotFoundException("No user with given email");
        if(optAd.isEmpty()) throw new AdvertisementNotFoundException("Bad advertisement id");

        AppUser appUser = optUser.get();
        Advertisement ad = optAd.get();
        ad.removeFollower(appUser.getId());
        advertisementRepository.save(ad);

    }

    public long getAdFollowers(int id) throws AdvertisementNotFoundException {
        Optional<Advertisement> optAd = advertisementRepository.findById(id);
        if(optAd.isEmpty()) throw new AdvertisementNotFoundException("Bad advertisement id");
        Advertisement ad = optAd.get();
        long followers = ad.getFollowers().stream().count();
        return followers;
    }

    public List<AdvertisementResponseDTO> searchAdvertisements(String keywords) {
        return advertisementRepository.findAdvertisementsByTitleContaining(keywords).stream().map(advertisementResponseMapper::apply).collect(Collectors.toList());
    }

    public List<AdvertisementStatus> getAdvertisementStatuses() {
        var statuses = advertisementStatusRepository.findAll();
        System.out.println("Statusi: " + statuses);
        return statuses;
    }

    public List<AdvertisementResponseDTO> sortAdvertisements(String category, String[] sort, int page) {
        List<Sort.Order> orders = new ArrayList<>();
        if(sort[0].contains(",")){
            for (String s : sort) {
                String[] fields = s.split(",");
                orders.add(new Sort.Order(getSortOrder(fields[1]),fields[0]));
            }
        }else{
            orders.add(new Sort.Order(getSortOrder(sort[1]),sort[0]));
        }

        Pageable pageable = PageRequest.of(page-1,PAGE_SIZE,Sort.by(orders));
        Date dateOfActiveAd = Date.from(LocalDate.now().minusDays(30).atStartOfDay(ZoneId.systemDefault()).toInstant());
        AdvertisementStatus activeStatus = new AdvertisementStatus();
        activeStatus.setId(1);
        activeStatus.setStatusName("ACTIVE");
        List<Advertisement> advertisements = advertisementRepository.findAdvertisementsByAdvertisementCategory_CategoryName(category,dateOfActiveAd ,pageable, activeStatus).getContent();
        return advertisements.stream().map(advertisementResponseMapper::apply).collect(Collectors.toList());
    }

    private Sort.Direction getSortOrder(String direction) {
        if(direction.equals("desc")) {
            return Sort.Direction.DESC;
        }
        return Sort.Direction.ASC;
    }

    public void deleteAdvertisement(int id) throws AdvertisementNotFoundException {
        Advertisement advertisement = advertisementRepository.findById(id).orElseThrow(()-> new AdvertisementNotFoundException("Advertisement not found"));
        advertisementRepository.delete(advertisement);
    }
}
