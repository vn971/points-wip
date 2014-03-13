
$(document).ready(function() {
	alert('base: ' + base);
	alert('mod: ' + mod);
	alert('accept: ' + accept);
	var power = 1;
	var remainder = base;
	while (remainder >= accept) {
		remainder = (((remainder * base) >>> 0) % mod) >>> 0;
		power = power + 1;
		alert('power: ' + power + ', remainder: ' + remainder);
	}
	alert('power: ' + power);
});
