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
    if (errorCallback == null) {
        errorCallback = function () {
        };
    }

    if (typeof errorCallback != "function") {
        console.log("MagstripeReader.register failure: failure parameter not a function");
        return;
    }

    if (typeof successCallback != "function") {
        console.log("MagstripeReader.register failure: success callback parameter must be a function");
        return;
    }

    exec(successCallback, errorCallback, 'MagStripeReader', 'register', []);
};
/**
 * Turn off scanner
 */
MagstripeReader.prototype.unregister = function (successCallback, errorCallback) {
    if (errorCallback == null) {
        errorCallback = function () {
        };
    }

    if (typeof errorCallback != "function") {
        console.log("MagstripeReader.unregister failure: failure parameter not a function");
        return;
    }

    if (typeof successCallback != "function") {
        console.log("MagstripeReader.unregister failure: success callback parameter must be a function");
        return;
    }

    exec(successCallback, errorCallback, 'MagStripeReader', 'unregister', []);
};
/**
 * Manually turn on magstripe reader
 */
MagstripeReader.prototype.read = function (successCallback, errorCallback) {
    if (errorCallback == null) {
        errorCallback = function () {
        };
    }

    if (typeof errorCallback != "function") {
        console.log("MagstripeReader.read failure: failure parameter not a function");
        return;
    }

    if (typeof successCallback != "function") {
        console.log("MagstripeReader.read failure: success callback parameter must be a function");
        return;
    }

    exec(successCallback, errorCallback, 'MagStripeReader', 'read', []);
};


var MagstripeReader = new MagstripeReader();
module.exports = MagstripeReader;