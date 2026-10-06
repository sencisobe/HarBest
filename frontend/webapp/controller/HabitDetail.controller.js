sap.ui.define(["./BaseController", "sap/ui/model/json/JSONModel", "sap/ui/core/Fragment" ,"sap/m/MessageBox"], function (BaseController, JSONModel,  Fragment, MessageBox) {
    "use strict";

    return BaseController.extend("harbest.controller.HabitDetail", {
        onInit: function () {
            this.getRouter().getRoute("habitDetail").attachPatternMatched(this._onRouteMatched, this);
        },

        _onRouteMatched: function (oEvent) {
            var sHabitId = oEvent.getParameter("arguments").habitId;
            this._sHabitId = sHabitId;

            var oModel = new JSONModel();
            oModel.loadData("http://localhost:8081/habits/" + sHabitId);
            this.getView().setModel(oModel);

            var oWeeklyModel = new JSONModel();
            oWeeklyModel.loadData("http://localhost:8081/habits/" + sHabitId + "/weekly-progress");
            this.getView().setModel(oWeeklyModel, "weekly");
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
    var sHabitId = this._sHabitId;
    var that = this;

    fetch("http://localhost:8081/habits/" + sHabitId + "/water", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ duration: iDuration })
    })
        .then(function (response) {
            if (!response.ok) {
                return response.json().then(function (oProblem) {
                    throw new Error(oProblem.detail || "Error watering the habit");
                });
            }
            return response.json();
        })
        .then(function (oResult) {
            that._oWaterDialog.close();

            that.getView().getModel().setData(oResult.habit);
            that.getView().getModel("weekly").loadData("http://localhost:8081/habits/" + sHabitId + "/weekly-progress");

            if (oResult.objectiveMetToday) {
                MessageBox.success("Daily objective completed!");
            } else {
                MessageBox.information("You have " + oResult.remainingMinutesToday + " minutes left to reach today's objective.");
            }
        })
        .catch(function (oError) {
            MessageBox.error(oError.message);
        });
        },

        onNavBack: function () {
            this.getRouter().navTo("main");
        }
    });
});