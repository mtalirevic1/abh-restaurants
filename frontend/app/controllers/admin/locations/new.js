import Controller from "@ember/controller";
import { inject as service } from "@ember/service";

export default Controller.extend({
  locationService: service("location-service"),

  actions: {
    submitCity() {
      const id = this.get("model.location.id");
      const isCreating = typeof id === "undefined";
      console.log(this.get("model.location"));
      let promise;

      if (isCreating) {
        promise = this.get("locationService").createCity(this.get("model.location"));
      } else {
        promise = this.get("locationService").updateCity(
          id,
          this.get("model.location")
        );
      }

      promise.then(() => this.transitionToRoute("admin.locations")).catch(error => console.log(error));
    }
  }
});
