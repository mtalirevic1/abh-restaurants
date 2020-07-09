package com.atlantbh.devdays.demo.abh.restaurants.repository.specification;

import com.atlantbh.devdays.demo.abh.restaurants.domain.Restaurant;
import com.atlantbh.devdays.demo.abh.restaurants.domain.Restaurant_;
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
      /**
       * builder.like( builder.lower( root.get( type.getDeclaredSingularAttribute("firstname",
       * String.class) ) ), "%" + keyword.toLowerCase() + "%" )
       */
      Root<Restaurant> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
    Predicate defaultQuery = criteriaBuilder.like(root.get(Restaurant_.NAME), "%" + "%");
    Predicate predicate = null;
    if (filter != null && filter.getName() != null) {
      predicate =
          criteriaBuilder.and(
              (!filter.getName().toString().isEmpty())
                  ? criteriaBuilder.like(
                      criteriaBuilder.lower(root.get(Restaurant_.NAME)),
                      filter.getName().toLowerCase() + "%")
                  : defaultQuery,
              (filter.getPrice() != null)
                  ? criteriaBuilder.equal(root.get(Restaurant_.PRICE_RANGE), filter.getPrice())
                  : defaultQuery,
              (filter.getRating() != null)
                  ? criteriaBuilder.equal(
                      criteriaBuilder.toLong(root.get(Restaurant_.AVERAGE_RATING)),
                      filter.getRating())
                  : defaultQuery);
    }
    return predicate;
    //    return criteriaBuilder.like(root.get(Restaurant_.NAME), filter.getName() + "%");
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
