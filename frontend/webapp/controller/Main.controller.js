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


		onConfirmCreateHabit: function () {
			var oName = this.byId("habitNameInput");
			var oTime = this.byId("habitTimeInput");
			var sName = oName.getValue().trim();
			var iTime = parseInt(oTime.getValue(), 10);

			var bNameOk = !!sName;
			var bTimeOk = !isNaN(iTime) && iTime > 0;

			oName.setValueState(bNameOk ? "None" : "Error");
			oName.setValueStateText(this.getText("createNameRequired"));
			oTime.setValueState(bTimeOk ? "None" : "Error");
			oTime.setValueStateText(this.getText("createTimeInvalid"));

			if (!bNameOk || !bTimeOk) {
				return;
			}

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
		},
		onCancelCreateHabit: function () {
			this.byId("habitNameInput").setValueState("None");
			this.byId("habitTimeInput").setValueState("None");
			this._oCreateDialog.close();
		},
		onLogout: function () {
			AuthService.logout();
			this.getView().getModel().setData([]);
			this.getRouter().navTo("login");
		}
		
	});
	
});