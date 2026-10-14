sap.ui.define([
	"./BaseController",
	"sap/m/MessageToast",
	"harbest/service/AuthService"
], function (BaseController, MessageToast, AuthService) {
	"use strict";

	return BaseController.extend("harbest.controller.Login", {
		_credentials: function () {
			return [this.byId("email").getValue(), this.byId("password").getValue()];
		},

		onLogin: function () {
			AuthService.login(...this._credentials())
				.then(() => this.getOwnerComponent().getRouter().navTo("main"))
				.catch(() => MessageToast.show("Incorrect credentials"));
		},

		onRegister: function () {
			AuthService.register(...this._credentials())
				.then(() => this.getOwnerComponent().getRouter().navTo("main"))
				.catch(() => MessageToast.show("Couldn´t register"));
		}
	});
});