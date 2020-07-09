import Controller from "@ember/controller";
import { inject as service } from "@ember/service";

export default Controller.extend({
  locationService: service("location-service"),

  actions: {
    submitCity() {
      const id = this.get("model.city.id");
      const isCreating = typeof id === "undefined";

      let promise;

      if (isCreating) {
        promise = this.get("locationService").createCity(this.get("model.city"));
      } else {
        promise = this.get("locationService").update(
          id,
          this.get("model.city")
        );
      }

      promise.then(() => this.transitionToRoute("admin.location"));
    }
  }
});
