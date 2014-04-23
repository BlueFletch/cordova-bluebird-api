var cordova = require('cordova');
var exec = require('cordova/exec');

 /**
         * Constructor.
 *
 * @returns {MagstripeReader}
 */
function MagstripeReader() {

};

/**
 * Turn on magstripe reader listener.  Listens for hardward scan
 * 
 * Success function should expect a list of tracks to be passed in
 */
MagstripeReader.prototype.register = function (successCallback, errorCallback) {
    errorCallback = errorCallback || function () { };
    
    if (typeof errorCallback !== "function") {
        console.log("MagstripeReader.register failure: failure parameter not a function");
        return;
    }

    if (typeof successCallback !== "function") {
        console.log("MagstripeReader.register failure: success callback parameter must be a function");
        return;
    }

    exec(successCallback, errorCallback, 'BluebirdMagStripeReader', 'register', []);
};
/**
 * Turn off scanner
 */
MagstripeReader.prototype.unregister = function (successCallback, errorCallback) {
    errorCallback = errorCallback || function () { };
    

    if (typeof errorCallback !== "function") {
        console.log("MagstripeReader.unregister failure: failure parameter not a function");
        return;
    }

    if (typeof successCallback !== "function") {
        console.log("MagstripeReader.unregister failure: success callback parameter must be a function");
        return;
    }

    exec(successCallback, errorCallback, 'BluebirdMagStripeReader', 'unregister', []);
};
/**
 * Manually turn on magstripe reader
 */
MagstripeReader.prototype.read = function () {
    var successCallback = function(result) {
        console.log("Magstripe reader begin read.  Result= " + result);
    }
    var errorCallback = function(result) {
        console.log("FAIL: Magstripe reader begin read.  Result=" + result);   
    }

    exec(successCallback, errorCallback, 'BluebirdMagStripeReader', 'read', []);
};


var magstripeReader = new MagstripeReader();

module.exports = magstripeReader;