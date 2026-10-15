sap.ui.define(["harbest/service/AuthService"], function (AuthService) {
  "use strict";

  return async function api(path, options = {}) {
    const res = await fetch(AuthService.BASE_URL + path, {
      ...options,
      headers: {
        "Content-Type": "application/json",
        ...options.headers,
        Authorization: `Bearer ${AuthService.getToken()}`
      }
    });

  if (res.status === 401) {
    AuthService.logout();
    sap.ui.getCore().getEventBus().publish("auth", "sessionExpired");
    throw new Error("Session expired");
  }
  if (!res.ok) {
    const problem = await res.json().catch(() => null);
    throw new Error(problem?.detail || "Error " + res.status);
  }
    const text = await res.text();
    return text ? JSON.parse(text) : null;
  };
});