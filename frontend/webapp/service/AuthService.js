sap.ui.define([], function () {
	"use strict";

	const KEY = "harbest.token";
	const USER_KEY = "harbest.username";
	const BASE_URL = "";

	async function post(path, payload) {
		const res = await fetch(BASE_URL + path, {
			method: "POST",
			headers: { "Content-Type": "application/json" },
			body: JSON.stringify(payload)
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
		getUsername: () => sessionStorage.getItem(USER_KEY),
		logout: () => {
			sessionStorage.removeItem(KEY);
			sessionStorage.removeItem(USER_KEY);
		},

		async login(identifier, password) {
			const body = await post("/auth/login", { identifier, password });
			sessionStorage.setItem(KEY, body.token);
			sessionStorage.setItem(USER_KEY, body.username);
		},

		async register(username, email, password) {
			await post("/auth/register", { username, email, password });
			await this.login(username, password);
		}
	};
});