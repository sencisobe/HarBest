sap.ui.define(["./BaseController", "sap/m/MessageToast", "harbest/service/AuthService"],
function (BaseController, MessageToast, AuthService) {
	"use strict";

	return BaseController.extend("harbest.controller.Login", {
		onLogin: function () {
			var oIdentifier = this.byId("identifier");
			var oPassword = this.byId("password");

			var bValid = [
				[oIdentifier, "Username or email is required"],
				[oPassword, "Password is required"]
			].map(([oInput, sText]) => {
				var bEmpty = !oInput.getValue().trim();
				oInput.setValueState(bEmpty ? "Error" : "None");
				oInput.setValueStateText(sText);
				return !bEmpty;
			}).every(Boolean);

			if (!bValid) {
				return;
			}

			AuthService.login(oIdentifier.getValue(), oPassword.getValue())
				.then(() => {
					this.getOwnerComponent().getModel("user")
						.setProperty("/username", AuthService.getUsername());
					this.getRouter().navTo("main");
				})
				.catch(oError => MessageToast.show(oError.message));
	},
		onInputChange: function (oEvent) {
		oEvent.getSource().setValueState("None");
	},
		onGoRegister: function () {
			this.getRouter().navTo("register");
		},
		onGoForgot: function () {
			this.getRouter().navTo("forgotPassword");
		}
	});
});