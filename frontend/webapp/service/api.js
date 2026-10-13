sap.ui.define(["harbest/service/AuthService"], function (AuthService) {
  "use strict";

  return async function api(path, options = {}) {
    const res = await fetch(path, {
      ...options,
      headers: {
        "Content-Type": "application/json",
        ...options.headers,
        Authorization: `Bearer ${AuthService.getToken()}`
      }
    });

    if (res.status === 401 || res.status === 403) {
      AuthService.logout();
      sap.ui.getCore().getEventBus().publish("auth", "sessionExpired");
      throw new Error("Sesión caducada");
    }
    if (!res.ok) throw new Error(res.status);
    return res.status === 204 ? null : res.json();
  };
});