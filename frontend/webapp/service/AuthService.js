sap.ui.define([], function () {
  "use strict";
  const KEY = "harbest.token";
  return {
  
  getToken: () => sessionStorage.getItem(KEY),
  logout: () => sessionStorage.removeItem(KEY),

  async login(email, password) {
    const res = await fetch("/auth/login", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ email, password })
    });
    if (!res.ok) throw new Error(res.status);
    const { token } = await res.json();
    sessionStorage.setItem(KEY, token);
  },

  async register(email, password) {
    const res = await fetch("/auth/register", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ email, password })
    });
    if (!res.ok) throw new Error(res.status);
    await this.login(email, password);
      await this.login(email, password);
  }
};
});