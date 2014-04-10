var cordova = require('cordova');
var exec = require('cordova/exec');

 /**
         * Constructor.
 *
 * @returns {BarcodeScanner}
 */
function BarcodeScanner() {

};

/**
 * Turn on barcode scanner listeners.  Listens for hardward button events.
 * 
 * Success function should expect a barcode to be passed in
 */
BarcodeScanner.prototype.register = function (successCallback, errorCallback) {
    if (errorCallback == null) {
        errorCallback = function () {
        };
    }

    if (typeof errorCallback != "function") {
        console.log("BarcodeScanner.register failure: failure parameter not a function");
        return;
    }

    if (typeof successCallback != "function") {
        console.log("BarcodeScanner.register failure: success callback parameter must be a function");
        return;
    }

    exec(successCallback, errorCallback, 'BarcodeScanner', 'register', []);
};
/**
 * Turn off barcode scanner
 */
BarcodeScanner.prototype.unregister = function (successCallback, errorCallback) {
    if (errorCallback == null) {
        errorCallback = function () {
        };
    }

    if (typeof errorCallback != "function") {
        console.log("BarcodeScanner.unregister failure: failure parameter not a function");
        return;
    }

    if (typeof successCallback != "function") {
        console.log("BarcodeScanner.unregister failure: success callback parameter must be a function");
        return;
    }

    exec(successCallback, errorCallback, 'BarcodeScanner', 'unregister', []);
};
/**
 * Manually turn on barcode scanner
 */
BarcodeScanner.prototype.softScanOn = function (successCallback, errorCallback) {
    if (errorCallback == null) {
        errorCallback = function () {
        };
    }

    if (typeof errorCallback != "function") {
        console.log("BarcodeScanner.softScanOn failure: failure parameter not a function");
        return;
    }

    if (typeof successCallback != "function") {
        console.log("BarcodeScanner.softScanOn failure: success callback parameter must be a function");
        return;
    }

    exec(successCallback, errorCallback, 'BarcodeScanner', 'softScanOn', []);
};

/**
 * Manually turn off barcode scanner
 */
BarcodeScanner.prototype.softScanOff = function (successCallback, errorCallback) {
    if (errorCallback == null) {
        errorCallback = function () {
        };
    }

    if (typeof errorCallback != "function") {
        console.log("BarcodeScanner.softScanOff failure: failure parameter not a function");
        return;
    }

    if (typeof successCallback != "function") {
        console.log("BarcodeScanner.softScanOff failure: success callback parameter must be a function");
        return;
    }

    exec(successCallback, errorCallback, 'BarcodeScanner', 'softScanOff', []);
};

var barcodeScanner = new BarcodeScanner();
module.exports = barcodeScanner;