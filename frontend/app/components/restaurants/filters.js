import Component from "@ember/component";
import { inject as service } from "@ember/service";
import { computed } from "@ember/object";

export default Component.extend({
  name: null,
  price: null,
  rating: null,
  sortBy: null,
  cuisine: null,
  filterBy: null,

  cuisineService: service("cuisine-service"),
  cuisines: null,

  isSortDropdownShown: false,
  isFiltersDropdownShown: false,

  init() {
    this._super(...arguments);

    this.get("cuisineService")
      .fetchAllCuisines()
      .then(({ content }) =>
        this.set("cuisines", content.map(item => item.name))
      );
  },

  cuisineList: computed("cuisine", {
    get() {
      const cuisine = this.get("cuisine") || "";
      if (!cuisine) {
        return [];
      }

      return cuisine.split(",");
    },
    set(_, value) {
      this.set("cuisine", (value || []).join(","));
      return value;
    }
  }),

  actions: {
    onShowSortDropdown() {
      this.set("isFiltersDropdownShown", false);
      this.toggleProperty("isSortDropdownShown");
    },

    onShowFiltersDropdown() {
      this.set("isSortDropdownShown", false);
      this.toggleProperty("isFiltersDropdownShown");
    },

    onRemoveCuisine() {
      this.set("cuisine", []);
    },

    onRemoveName() {
      this.set("name", "");
    },

    onRemovePrice() {
      this.set("price", null);
    },

    onRemoveRating() {
      this.set("rating", null);
    },

    onUpdateCuisineList(cuisineList) {
      this.set("cuisine", cuisineList.join(","));
    },

    onPriceChange(price) {
      this.set("price", price);
    },

    onRatingChange(rating) {
      this.set("rating", rating);
    }
  }
});
