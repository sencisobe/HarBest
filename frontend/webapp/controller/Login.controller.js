sap.ui.define(["./BaseController", "sap/m/MessageToast", "harbest/service/AuthService"],
function (BaseController, MessageToast, AuthService) {
	"use strict";

	return BaseController.extend("harbest.controller.Login", {
		onLogin: function () {
			AuthService.login(this.byId("email").getValue(), this.byId("password").getValue())
				.then(() => this.getRouter().navTo("main"))
				.catch(oError => MessageToast.show(oError.message));
		},
		onGoRegister: function () {
			this.getRouter().navTo("register");
		}
	});
});