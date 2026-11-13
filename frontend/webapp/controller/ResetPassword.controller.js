sap.ui.define(["./BaseController", "sap/m/MessageToast", "harbest/service/AuthService"],
function (BaseController, MessageToast, AuthService) {
	"use strict";

	return BaseController.extend("harbest.controller.ResetPassword", {
		onReset: function () {
			var oToken = this.byId("token");
			var oPassword = this.byId("password");
			var oConfirm = this.byId("confirm");

			var bValid = [
				[oToken, this.getText("resetTokenRequired")],
				[oPassword, this.getText("registerPasswordRequired")],
				[oConfirm, this.getText("registerConfirmRequired")]
			].map(([oInput, sText]) => {
				var bEmpty = !oInput.getValue().trim();
				oInput.setValueState(bEmpty ? "Error" : "None");
				oInput.setValueStateText(sText);
				return !bEmpty;
			}).every(Boolean);

			if (bValid && oPassword.getValue() !== oConfirm.getValue()) {
				oConfirm.setValueState("Error");
				oConfirm.setValueStateText(this.getText("registerNoMatch"));
				bValid = false;
			}
			if (!bValid) {
				return;
			}

			AuthService.resetPassword(oToken.getValue().trim(), oPassword.getValue())
				.then(() => {
					MessageToast.show(this.getText("resetDone"));
					this.getRouter().navTo("login");
				})
				.catch(oError => MessageToast.show(oError.message));
		},
		onGoLogin: function () {
			this.getRouter().navTo("login");
		}
	});
});