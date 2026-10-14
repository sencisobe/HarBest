sap.ui.define([], function () {
	"use strict";

	const KEY = "harbest.token";
	const BASE_URL = "http://localhost:8081";

	async function post(path, email, password) {
		const res = await fetch(BASE_URL + path, {
			method: "POST",
			headers: { "Content-Type": "application/json" },
			body: JSON.stringify({ email, password })
		});
		if (!res.ok) throw new Error(res.status);
		return res;
	}

	return {
		BASE_URL,
		getToken: () => sessionStorage.getItem(KEY),
		logout: () => sessionStorage.removeItem(KEY),

		async login(email, password) {
			const res = await post("/auth/login", email, password);
			const { token } = await res.json();
			sessionStorage.setItem(KEY, token);
		},

		async register(email, password) {
			await post("/auth/register", email, password);
			await this.login(email, password);
		}
	};
});