import Ember from 'ember';

export function filterRestaurants([filter, ...restaurants]) {

    return restaurants;
}

export default Ember.Helper.helper(filterRestaurants);