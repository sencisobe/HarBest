sap.ui.define(function () {
	"use strict";

	return {
		name: "QUnit test suite for the UI5 Application: harbest",
		defaults: {
			page: "ui5://test-resources/harbest/Test.qunit.html?testsuite={suite}&test={name}",
			qunit: {
				version: 2
			},
			sinon: {
				version: 1
			},
			ui5: {
				language: "EN",
				theme: "sap_horizon"
			},
			coverage: {
				only: "harbest/",
				never: "test-resources/harbest/"
			},
			loader: {
				paths: {
					"harbest": "../"
				}
			}
		},
		tests: {
			"unit/unitTests": {
				title: "Unit tests for harbest"
			},
			"integration/opaTests": {
				title: "Integration tests for harbest"
			}
		}
	};
});
