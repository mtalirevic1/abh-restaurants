import AjaxService from "ember-ajax/services/ajax";

export default AjaxService.extend({
  post(path, data) {

    return this.request(path, {
      method: "POST",
      contentType: "application/json",
      data: JSON.stringify(data)
    });
  },

  put(path, data) {
    console.log(data);
    return this.request(path, {
      method: "PUT",
      contentType: "application/json",
      data: JSON.stringify(data)
    });
  },

  get(path, queryParams) {
    return this.request(path, {
      method: "GET",
      data: queryParams
    });
  },

  delete(path, queryParams) {
    return this.request(path, {
      method: "DELETE",
      data: queryParams
    });
  }
});
