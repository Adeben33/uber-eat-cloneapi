package com.github.uber_eat_cloneapi1.service.restaurantsService;

import com.github.uber_eat_cloneapi1.dto.request.ResponseDTO;
import com.github.uber_eat_cloneapi1.dto.request.RestaurantDTO;
import com.github.uber_eat_cloneapi1.dto.request.RestaurantUpdateDTO;
import com.github.uber_eat_cloneapi1.models.RestaurantModel;
import com.github.uber_eat_cloneapi1.models.UserModel;
import com.github.uber_eat_cloneapi1.repository.RestaurantRepo;
import com.github.uber_eat_cloneapi1.repository.UserRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

@Service
public class AdminRestaurantService {

    private final RestaurantRepo restaurantRepo;
    private final UserRepo userRepo;

    public AdminRestaurantService(RestaurantRepo restaurantRepo, UserRepo userRepo) {
        this.restaurantRepo = restaurantRepo;
        this.userRepo = userRepo;
    }

    public ResponseEntity<?> createRestaurant(RestaurantDTO restaurantDTO, String userId) {
        // Get the authentication object
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Optional<UserModel> userById = userRepo.findById(Long.parseLong(userId));


        // Extract user details from the authentication object
        if (auth != null && auth.isAuthenticated() && userById.isPresent()) {
            Object principal = auth.getPrincipal();

            // If using Spring Security's UserDetails, cast and extract info
            if (principal instanceof UserDetails userDetails) {
                String email = userDetails.getUsername();

                if ((Objects.equals(email, userById.get().getEmail())) && (userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")))) {


                    RestaurantModel restaurant = RestaurantModel.builder()
                            .name(restaurantDTO.getName())
                            .address(restaurantDTO.getAddress())
                            .cuisineType(restaurantDTO.getCuisineType())
                            .phoneNumber(restaurantDTO.getPhone())
                            .build();

                    RestaurantModel savRestaurant = restaurantRepo.save(restaurant);

                    return new ResponseEntity<>(savRestaurant, HttpStatus.CREATED);

                } else {
                    return ResponseEntity.badRequest().body("user cannot create a restaurant because you are not an admin");
                }

            }
        }

        // If authentication is null or user is not authenticated, return an error response

        return new ResponseEntity<>(new ResponseDTO("User is not authenticated"), HttpStatus.FORBIDDEN);
    }


    public ResponseEntity<?> updateRestaurant(String restaurantId, String userId, RestaurantUpdateDTO restaurantUpdateDTO) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

//        Optional<UserModel> userById = userRepo.findById(Long.parseLong(userId));

        Optional<RestaurantModel> restuarant = restaurantRepo.findById(Long.parseLong(restaurantId));

        if ((auth != null && auth.isAuthenticated() && restuarant.isPresent()) && (auth.getPrincipal() instanceof UserDetails userDetails)) {

            String email = userDetails.getUsername();

            if (Objects.equals(email, restuarant.get().getUser().getEmail())) {

                RestaurantModel updatedRestaurant = restuarant.get();

                updatedRestaurant.setAddress(restaurantUpdateDTO.getAddress());
                updatedRestaurant.setAvailable(restaurantUpdateDTO.getAvailable());
                updatedRestaurant.setName(restaurantUpdateDTO.getName());
                updatedRestaurant.setDeliveryOption(restaurantUpdateDTO.getDeliveryOption());
                updatedRestaurant.setOperatingHours(restaurantUpdateDTO.getOperatingHours());

                restaurantRepo.save(updatedRestaurant);

                return ResponseEntity.ok().body("restaurant successufully created");


            }

        }

        return ResponseEntity.badRequest().body("User is not authenticated or you are not an admin or no restaurant with that id");

    }
}
