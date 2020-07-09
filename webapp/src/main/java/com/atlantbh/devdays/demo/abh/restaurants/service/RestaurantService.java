package com.atlantbh.devdays.demo.abh.restaurants.service;

import com.atlantbh.devdays.demo.abh.restaurants.domain.*;
import com.atlantbh.devdays.demo.abh.restaurants.repository.RestaurantRepository;
import com.atlantbh.devdays.demo.abh.restaurants.repository.RestaurantReviewRepository;
import com.atlantbh.devdays.demo.abh.restaurants.repository.RestaurantTableRepository;
import com.atlantbh.devdays.demo.abh.restaurants.repository.specification.RestaurantSpecification;
import com.atlantbh.devdays.demo.abh.restaurants.service.exceptions.EntityNotFoundServiceException;
import com.atlantbh.devdays.demo.abh.restaurants.service.requests.RestaurantFilter;
import com.atlantbh.devdays.demo.abh.restaurants.service.requests.RestaurantRequest;
import com.atlantbh.devdays.demo.abh.restaurants.service.requests.ReviewRequest;
import com.atlantbh.devdays.demo.abh.restaurants.service.responses.PopularLocation;
import java.util.*;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Restaurant service. */
@Service
public class RestaurantService extends BaseCrudService<Restaurant, Long, RestaurantRepository> {
  private static final String AWS_BASE_PATH = "https://abhrestaurants.s3.amazonaws.com/";

  private CityService cityService;
  private RestaurantReviewRepository restaurantReviewRepository;
  private CuisineService cuisineService;
  private RestaurantTableRepository restaurantTableRepository;

  @Autowired
  public void setRestaurantTableRepository(RestaurantTableRepository restaurantTableRepository) {
    this.restaurantTableRepository = restaurantTableRepository;
  }

  @Autowired
  public void setCuisineService(CuisineService cuisineService) {
    this.cuisineService = cuisineService;
  }

  /**
   * Instantiates a new Base crud service.
   *
   * @param repository the repository
   */
  @Autowired
  public RestaurantService(RestaurantRepository repository) {
    super(repository);
  }

  @Autowired
  public void setCityService(CityService cityService) {
    this.cityService = cityService;
  }

  @Autowired
  public void setRestaurantReviewRepository(RestaurantReviewRepository restaurantReviewRepository) {
    this.restaurantReviewRepository = restaurantReviewRepository;
  }

  @Override
  public Restaurant get(Long aLong) throws EntityNotFoundServiceException {
    return populateItem(super.get(aLong));
  }

  /**
   * Creates a restaurant.
   *
   * @param request Create request.
   * @return Created restaurant.
   * @throws EntityNotFoundServiceException If dependent entity found.
   */
  public Restaurant create(RestaurantRequest request) throws EntityNotFoundServiceException {
    Restaurant restaurant = new Restaurant();
    updateRestaurant(restaurant, request);
    return repository.save(restaurant);
  }

  /**
   * Updates a restaurant.
   *
   * @param id Id of a restaurant to updated.
   * @param request Update request.
   * @return Updated restaurant.
   * @throws EntityNotFoundServiceException If no restaurant or any other dependent entity found.
   */
  public Restaurant update(Long id, RestaurantRequest request)
      throws EntityNotFoundServiceException {
    Restaurant restaurant = get(id);
    updateRestaurant(restaurant, request);
    return repository.save(restaurant);
  }

  public Page<Restaurant> find(RestaurantFilter filter) {
    final Pageable pageRequest = RestaurantSpecification.createPage(filter);
    final Page<Restaurant> restaurants =
        repository.findAll(new RestaurantSpecification(filter), pageRequest);
    return transformPage(restaurants, pageRequest);
  }

  /**
   * Fetches near-by restaurants with center specified by latitude and longitude.
   *
   * @return List of near-by restaurants.
   */
  public List<Restaurant> findNearBy(float latitude, float longitude) {
    return populateRestaurants(repository.findNearBy(latitude, longitude));
  }

  /**
   * Fetches popular restaurants.
   *
   * @return List of popular restaurants.
   */
  public List<Restaurant> findPopular() {
    return populateRestaurants(repository.findPopular());
  }

  /**
   * Fetches the popular locations for all restaurants.
   *
   * @return List of popular locations.
   * @throws EntityNotFoundServiceException If any entity is not found.
   */
  public List<PopularLocation> findPopularLocations() throws EntityNotFoundServiceException {
    final List<PopularLocation> popularLocations = repository.findPopularLocations();

    for (PopularLocation popularLocation : popularLocations) {
      popularLocation.setCity(cityService.get(popularLocation.getCityId()));
    }

    return popularLocations;
  }

  /**
   * Creates a review for this restaurant.
   *
   * @param restaurantId Restaurant id.
   * @param request Review request.
   * @param user User.
   * @throws EntityNotFoundServiceException If no restaurant found.
   */
  @Transactional
  public void createReview(Long restaurantId, ReviewRequest request, User user)
      throws EntityNotFoundServiceException {
    final Restaurant restaurant = get(restaurantId);
    final Optional<RestaurantReview> existingReview =
        restaurantReviewRepository.findByRestaurantAndUser(restaurant, user);

    final RestaurantReview review =
        existingReview.orElseGet(
            () -> {
              RestaurantReview result = new RestaurantReview();
              result.setUser(user);
              result.setRestaurant(restaurant);
              return result;
            });

    review.setRating(request.getReviewScore());
    review.setReview(request.getReviewText());

    restaurantReviewRepository.save(review);
  }

  /**
   * Updates a restaurant rating.
   *
   * @param restaurantId Id of a restaurant.
   * @throws EntityNotFoundServiceException If no restaurant found.
   */
  @Transactional
  public void updateRestaurantRating(Long restaurantId) throws EntityNotFoundServiceException {
    final Restaurant restaurant = get(restaurantId);

    final List<RestaurantReview> reviews = restaurantReviewRepository.findByRestaurant(restaurant);
    if (CollectionUtils.isNotEmpty(reviews)) {
      restaurant.setNumberOfRatings(reviews.size());
      final OptionalDouble averageRating =
          reviews.stream().mapToInt(RestaurantReview::getRating).average();
      restaurant.setAverageRating((float) averageRating.orElse(0d));
      restaurant.setNumberOfRatings(reviews.size());
      repository.save(restaurant);
    }
  }

  public long getNumberOfRestaurants() {
    return repository.count();
  }

  private void updateRestaurant(Restaurant restaurant, RestaurantRequest request)
      throws EntityNotFoundServiceException {
    restaurant.setName(request.getName());
    restaurant.setDescription(request.getDescription());
    restaurant.setAddress(request.getAddress());
    restaurant.setCity(cityService.get(request.getCityId()));
    restaurant.setCoverImagePath(request.getCoverImagePath());
    restaurant.setLatitude(request.getLatitude());
    restaurant.setLongitude(request.getLongitude());
    restaurant.setMenu(request.getMenu());
    restaurant.setPhone(request.getPhone());
    restaurant.setPriceRange(request.getPriceRange());
    restaurant.setProfileImagePath(request.getProfileImagePath());
    restaurant.setCloseTime(request.getCloseTime());
    restaurant.setOpenTime(request.getOpenTime());

    final List<Cuisine> ourCuisines = new ArrayList<>();
    final List<Cuisine> cuisines = request.getCuisines();
    if (CollectionUtils.isNotEmpty(cuisines)) {
      for (Cuisine cuisine : cuisines) {
        Cuisine ourCuisine = cuisineService.get(cuisine.getId());
        ourCuisines.add(ourCuisine);
      }
    }
    restaurant.setCuisines(ourCuisines);

    final List<RestaurantTable> ourTables = new ArrayList<>();
    final List<RestaurantTable> tables = request.getTables();
    if (CollectionUtils.isNotEmpty(tables)) {
      for (RestaurantTable table : tables) {
        if (table.getId() == null) {
          table.setRestaurant(restaurant);
          final RestaurantTable ourTable = restaurantTableRepository.save(table);
          ourTables.add(ourTable);
        } else {
          final Optional<RestaurantTable> ourTableOptional =
              restaurantTableRepository.findById(table.getId());
          if (ourTableOptional.isPresent()) {
            final RestaurantTable ourTable = ourTableOptional.get();

            ourTable.setRestaurant(restaurant);
            ourTable.setNumberOfChairs(table.getNumberOfChairs());
            restaurantTableRepository.save(ourTable);

            ourTables.add(ourTable);
          }
        }
      }
    }
    restaurant.setTables(ourTables);
  }

  /**
   * Populates a ratings on a list of restaurants.
   *
   * @param restaurants List of restaurants.
   * @return List of restaurants with its ratings populated.
   */
  private List<Restaurant> populateRestaurants(Collection<Restaurant> restaurants) {
    return restaurants.stream().map(this::populateItem).collect(Collectors.toList());
  }

  @Override
  protected Restaurant populateItem(Restaurant item) {
    populateTables(item);
    return item;
  }

  /**
   * Populates a ratings on a restaurant.
   *
   * @param restaurant Restaurant to populate its rating.
   */
  private void populateTables(Restaurant restaurant) {
    final List<RestaurantTable> tables = restaurantTableRepository.findByRestaurant(restaurant);
    restaurant.setTables(tables);
  }
}
