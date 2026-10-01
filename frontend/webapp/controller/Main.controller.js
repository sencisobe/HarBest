sap.ui.define(["./BaseController", "sap/m/MessageBox", "sap/ui/core/Fragment"], function (BaseController, MessageBox, Fragment) {
	"use strict";

	return BaseController.extend("harbest.controller.Main", {
		sayHello: function () {
			MessageBox.show("Hello World!");
		},

		onDeleteHabit: function (oEvent) {
			var oCtx = oEvent.getSource().getBindingContext();
			var sHabitId = oCtx.getProperty("id");

			fetch("http://localhost:8081/habits/" + sHabitId, { method: "DELETE" })
				.then(() => {
					this.getView().getModel().loadData("http://localhost:8081/habits");
				});
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

			fetch("http://localhost:8081/habits", {
				method: "POST",
				headers: { "Content-Type": "application/json" },
				body: JSON.stringify({ name: sName, dailyObjectiveTime: iTime })
			})
				.then(() => {
					this._oCreateDialog.close();
					this.getView().getModel().loadData("http://localhost:8081/habits");
				});
}
		
	});
	
});