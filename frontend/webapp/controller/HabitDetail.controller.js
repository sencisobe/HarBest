sap.ui.define(["./BaseController", "sap/ui/model/json/JSONModel"], function (BaseController, JSONModel) {
    "use strict";

    return BaseController.extend("harbest.controller.HabitDetail", {
        onInit: function () {
            this.getRouter().getRoute("habitDetail").attachPatternMatched(this._onRouteMatched, this);
        },

        _onRouteMatched: function (oEvent) {
            var sHabitId = oEvent.getParameter("arguments").habitId;

            var oModel = new JSONModel();
            oModel.loadData("http://localhost:8081/habits/" + sHabitId);
            this.getView().setModel(oModel);

            var oWeeklyModel = new JSONModel();
            oWeeklyModel.loadData("http://localhost:8081/habits/" + sHabitId + "/weekly-progress");
            this.getView().setModel(oWeeklyModel, "weekly");
        },

        onNavBack: function () {
            this.getRouter().navTo("main");
        }
    });
});