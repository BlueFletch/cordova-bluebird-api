Cordova Bluebird Plugin
============

This is a Cordova/Phonegap plugin to interact with the BlueBird ruggedized devices' Barcode Scanners and Magnetic Stripe Readers.  It has been tested on a  BP30, with soon to be testing on a BP70.

=============

This plugin is compatible with plugman.  To install, run the following from your project command line: 
```$ cordova plugin add https://github.com/BlueFletch/cordova-bluebird-api.git```


==============

<h3>To Use:</h3>
You'll register a callback which will be called when a successful "read" event occurs on the scanner or magstripe reader.  

```
function loadBarcode(barcode) {
	console.log("barcode read : " + barcode);
	//TODO: handle barcode read
}
function handleMagstripeTracks(tracks) {
	console.log("read magstripe value : " + JSON.stringify(tracks));
   
	//read track 1
	var track1 = tracks[0].split('^');
	if (track1.length == 1)  {
		window.magstripe.read(function(){});
		return;
	}
	
	var cc = {
		number : track1[0].substr(1),
		name : track1[1].trim(),
		expr : '20' + track1[2].substr(0,2) + '-' + track1[2].substr(2,2)
	};
	console.log("read credit card " + JSON.stringify(cc));
	//TODO: handle magstripe
}
document.addEventListener("deviceready", function(){ 
	if (window.barcodeScanner) {
		console.log("initializing barcode scanner")
		window.barcodeScanner.register(loadBarcode, function (argument) {
			console.log("failed to register barcode scanner");
		});
	}
	 
	if (window.magstripe) {
		console.log("initializing magstripe")
		window.magstripe.register(handleMagstripeTracks, function (argument) {
				console.log("failed to register for mag stripe reader");
		});	
	}
}, false);
```

=============
<h3>More API options:</h3>

<h5>Magstripe:</h5>
* You need to "activate" the reader, by calling `window.magstripe.read()` when you need to read a credit card (for instance, on a checkout page).  A succcessful read will call your registration callback.

<h5>Scanner:</h5>
* You can wire a soft button to the barcode scanner by calling `window.barcodeScanner.softScanOn()`
* Turn off the scanner manually using: `window.barcodeScanner.softScanOff()`

==============
Copyright 2014 BlueFletch Mobile

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

