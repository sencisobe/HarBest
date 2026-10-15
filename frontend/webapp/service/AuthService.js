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
		const text = await res.text();
		const body = text ? JSON.parse(text) : null;
		if (!res.ok) {
			throw new Error((body && body.detail) || "Error " + res.status);
		}
		return body;
	}

	return {
		BASE_URL,
		getToken: () => sessionStorage.getItem(KEY),
		logout: () => sessionStorage.removeItem(KEY),

		async login(email, password) {
			const body = await post("/auth/login", email, password);
			sessionStorage.setItem(KEY, body.token);
		},

		async register(email, password) {
			await post("/auth/register", email, password);
			await this.login(email, password);
		}
	};
});