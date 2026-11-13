sap.ui.define(["./BaseController", "sap/m/MessageToast", "harbest/service/AuthService"],
function (BaseController, MessageToast, AuthService) {
	"use strict";

	return BaseController.extend("harbest.controller.ForgotPassword", {
		onSend: function () {
			var oInput = this.byId("identifier");
			if (!oInput.getValue().trim()) {
				oInput.setValueState("Error");
				oInput.setValueStateText(this.getText("forgotRequired"));
				return;
			}
			AuthService.forgotPassword(oInput.getValue())
				.then(() => {
					MessageToast.show(this.getText("forgotSent"));
					this.getRouter().navTo("resetPassword");
				})
				.catch(oError => MessageToast.show(oError.message));
		},
		onGoLogin: function () {
			this.getRouter().navTo("login");
		}
	});
});