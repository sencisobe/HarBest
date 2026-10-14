sap.ui.define(["./BaseController", "sap/m/MessageBox", "sap/ui/core/Fragment",
	"harbest/service/api", "harbest/service/AuthService"],
function (BaseController, MessageBox, Fragment, api, AuthService) {	"use strict";

	return BaseController.extend("harbest.controller.Main", {
		
		_loadHabits: function () {
			return api("/habits")
				.then(h => this.getView().getModel().setData(h))
				.catch(() => {});
		},

		onInit: function () {
			this.getOwnerComponent().getRouter().getRoute("main")
				.attachPatternMatched(() => {
					if (AuthService.getToken()) this._loadHabits();
				});
		},

		onDeleteHabit: function (oEvent) {
			var sHabitId = oEvent.getSource().getBindingContext().getProperty("id");
			api("/habits/" + sHabitId, { method: "DELETE" })
				.then(() => this._loadHabits())
				.catch(oError => MessageBox.error(oError.message));
		},
		onOpenCreateDialog: function () {
			if (!this._oCreateDialog) {
				Fragment.load({
					id: this.getView().getId(),
					name: "harbest.view.CreateHabitDialog",
					controller: this
				}).then(function (oDialog) {
					this._oCreateDialog = oDialog;
					this.getView().addDependent(oDialog);
					oDialog.open();
				}.bind(this));
			} else {
				this._oCreateDialog.open();
			}
		},

		onCancelCreateHabit: function () {
			this._oCreateDialog.close();
		},

		onConfirmCreateHabit: function () {
			var sName = this.byId("habitNameInput").getValue();
			var iTime = parseInt(this.byId("habitTimeInput").getValue(), 10);

			api("/habits", {
				method: "POST",
				body: JSON.stringify({ name: sName, dailyObjectiveTime: iTime })
			})
				.then(() => {
					this._oCreateDialog.close();
					return this._loadHabits();
				})
				.catch(oError => MessageBox.error(oError.message));
	},
		onHabitPress: function (oEvent) {
			var oCtx = oEvent.getSource().getBindingContext();
			var sHabitId = oCtx.getProperty("id");
			this.getRouter().navTo("habitDetail", { habitId: sHabitId });
		}
		
	});
	
});