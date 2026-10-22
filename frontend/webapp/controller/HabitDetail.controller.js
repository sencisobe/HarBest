sap.ui.define(["./BaseController", "sap/ui/model/json/JSONModel", "sap/ui/core/Fragment", "sap/m/MessageBox", "harbest/service/api", "harbest/service/AuthService"],
function (BaseController, JSONModel, Fragment, MessageBox, api, AuthService) {    "use strict";

    return BaseController.extend("harbest.controller.HabitDetail", {
        onInit: function () {
            this.getRouter().getRoute("habitDetail").attachPatternMatched(this._onRouteMatched, this);
        },

        _onRouteMatched: function (oEvent) {
                var sHabitId = oEvent.getParameter("arguments").habitId;
                this._sHabitId = sHabitId;

                var oModel = new JSONModel();
                var oWeeklyModel = new JSONModel();
                this.getView().setModel(oModel);
                this.getView().setModel(oWeeklyModel, "weekly");

                api("/habits/" + sHabitId).then(h => oModel.setData(h)).catch(() => {});
                this._loadWeekly();
            },

            _loadWeekly: function () {
                return api("/habits/" + this._sHabitId + "/weekly-progress")
                    .then(w => this.getView().getModel("weekly").setData(w))
                    .catch(() => {});
            },
            onOpenWaterDialog: function () {
            if (!this._oWaterDialog) {
                Fragment.load({
                    id: this.getView().getId(),
                    name: "harbest.view.WaterHabitDialog",
                    controller: this
                }).then(function (oDialog) {
                    this._oWaterDialog = oDialog;
                    this.getView().addDependent(oDialog);
                    oDialog.open();
                }.bind(this));
            } else {
                this._oWaterDialog.open();
            }
        },

        onCancelWaterHabit: function () {
            this._oWaterDialog.close();
        },

        onConfirmWaterHabit: function () {
        var iDuration = parseInt(this.byId("waterDurationInput").getValue(), 10);
        var oModel = this.getView().getModel();
        var bWasMetBefore = oModel.getProperty("/minutesDoneToday") >= oModel.getProperty("/dailyObjectiveTime");

        api("/habits/" + this._sHabitId + "/water", {
            method: "POST",
            body: JSON.stringify({ duration: iDuration })
        })
            .then(oResult => {
                this._oWaterDialog.close();
                oModel.setData(oResult.habit);
                this._loadWeekly();

                if (oResult.objectiveMetToday) {
                    if (!bWasMetBefore) {
                        MessageBox.success("Daily objective completed!");
                    }
                } else {
                    MessageBox.information("You have " + oResult.remainingMinutesToday + " minutes left to reach today's objective.");
                }
            })
            .catch(oError => MessageBox.error(oError.message));
    },

        onNavBack: function () {
            this.getRouter().navTo("main");
        },
        onLogout: function () {
			AuthService.logout();
			this.getView().getModel().setData([]);
			this.getRouter().navTo("login");
		}
    });
});