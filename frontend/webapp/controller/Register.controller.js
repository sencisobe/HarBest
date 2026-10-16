sap.ui.define(["./BaseController", "sap/m/MessageToast", "harbest/service/AuthService"],
function (BaseController, MessageToast, AuthService) {
	"use strict";

	return BaseController.extend("harbest.controller.Register", {
		onRegister: function () {
			var sPassword = this.byId("password").getValue();
			if (sPassword !== this.byId("confirm").getValue()) {
				MessageToast.show("Passwords do not match");
				return;
			}
			AuthService.register( this.byId("username").getValue(), this.byId("email").getValue(),sPassword)
				.then(() => this.getRouter().navTo("main"))
				.catch(oError => MessageToast.show(oError.message));
		},
		onGoLogin: function () {
			this.getRouter().navTo("login");
		}
	});
});