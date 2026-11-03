sap.ui.define(["./BaseController", "sap/m/MessageToast", "harbest/service/AuthService"],
function (BaseController, MessageToast, AuthService) {
	"use strict";

	return BaseController.extend("harbest.controller.Register", {
		onRegister: function () {
			var oUsername = this.byId("username");
			var oEmail = this.byId("email");
			var oPassword = this.byId("password");
			var oConfirm = this.byId("confirm");

			var bValid = [
				[oUsername, "Username is required"],
				[oEmail, "Email is required"],
				[oPassword, "Password is required"],
				[oConfirm, "Please confirm your password"]
			].map(([oInput, sText]) => {
				var bEmpty = !oInput.getValue().trim();
				oInput.setValueState(bEmpty ? "Error" : "None");
				oInput.setValueStateText(sText);
				return !bEmpty;
			}).every(Boolean);

			if (bValid && oPassword.getValue() !== oConfirm.getValue()) {
				oConfirm.setValueState("Error");
				oConfirm.setValueStateText("Passwords do not match");
				bValid = false;
			}

			if (!bValid) {
				return;
			}

			AuthService.register(oUsername.getValue(), oEmail.getValue(), oPassword.getValue())
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
		onGoLogin: function () {
			this.getRouter().navTo("login");
		}
	});
});