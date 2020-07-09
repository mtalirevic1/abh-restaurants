import BaseService from "./base-service";

export default BaseService.extend({
  getAllCities(params) {
    return this.get("/api/v1/city", params);
  },

  getCity(id) {
    return this.get(`/api/v1/city/${id}`);
  },

  createCity(request) {
    return this.post(`/api/v1/city`, request);
  },

  updateCity(id, request) {
    return this.put(`/api/v1/city/${id}`, request);
  },

  delete(id) {
    return this.delete(`/api/v1/city/${id}`);
  }
});
