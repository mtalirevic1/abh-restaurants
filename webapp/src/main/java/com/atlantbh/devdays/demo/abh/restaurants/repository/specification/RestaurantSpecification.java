package com.atlantbh.devdays.demo.abh.restaurants.repository.specification;

import com.atlantbh.devdays.demo.abh.restaurants.domain.Restaurant;
import com.atlantbh.devdays.demo.abh.restaurants.service.requests.RestaurantFilter;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

/** Restaurant filter specification. */
public class RestaurantSpecification implements Specification<Restaurant> {
  private final RestaurantFilter filter;

  /**
   * Instantiates a new restaurant specification.
   *
   * @param filter Filter.
   */
  public RestaurantSpecification(RestaurantFilter filter) {
    this.filter = filter;
  }

  @Override
  public Predicate toPredicate(
      Root<Restaurant> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
    return null;
  }

  /**
   * Create a page object from filter.
   *
   * @param filter Filter.
   * @return Page object.
   */
  public static Pageable createPage(RestaurantFilter filter) {
    RestaurantFilter.Sort sortProperty = filter.getSortBy();
    return PageRequest.of(
        filter.getPage(),
        filter.getPageSize(),
        sortProperty.getDirection(),
        sortProperty.getPropertyName());
  }
}
