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
    errorCallback = errorCallback || function () { };

    if (typeof errorCallback !== "function") {
        console.log("BarcodeScanner.register failure: failure callback is not a function");
        return;
    }

    if (typeof successCallback !== "function") {
        console.log("BarcodeScanner.register failure: success callback parameter must be a function");
        return;
    }

    exec(successCallback, errorCallback, 'BluebirdBarcodeScanner', 'register', []);
};
/**
 * Turn off barcode scanner
 */
BarcodeScanner.prototype.unregister = function (successCallback, errorCallback) {
    errorCallback = errorCallback || function () { };
    
    if (typeof errorCallback !== "function") {
        console.log("BarcodeScanner.unregister failure: failure parameter not a function");
        return;
    }

    if (typeof successCallback !== "function") {
        console.log("BarcodeScanner.unregister failure: success callback parameter must be a function");
        return;
    }

    exec(successCallback, errorCallback, 'BluebirdBarcodeScanner', 'unregister', []);
};
/**
 * Manually turn on barcode scanner
 */
BarcodeScanner.prototype.softScanOn = function () {
    
    var successCallback = function(result) {
        console.log("Barcode scanner scanner on result= " + result);
    }
    var errorCallback = function(result) {
        console.log("Barcode scanner scanner on failed " + result);   
    }

    exec(successCallback, errorCallback, 'BluebirdBarcodeScanner', 'softScanOn', []);
};

/**
 * Manually turn off barcode scanner
 */
BarcodeScanner.prototype.softScanOff = function () {

    var successCallback = function(result) {
        console.log("Barcode scanner scanner off result= " + result);
    }
    var errorCallback = function(result) {
        console.log("Barcode scanner scanner off failed " + result);   
    }

    exec(successCallback, errorCallback, 'BluebirdBarcodeScanner', 'softScanOff', []);
};

//create instance
var barcodeScanner = new BarcodeScanner();

module.exports = barcodeScanner;