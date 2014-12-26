function clear() {
	var ctx = document.getElementById("surrounding_fill").getContext("2d");
	ctx.fillStyle = "#ffffff";
	ctx.fillRect(0, 0, 620, 620);
	var ctx = document.getElementById("dots").getContext("2d");
	ctx.fillStyle = "#ffffff";
	ctx.fillRect(0, 0, 620, 620);
	var ctx = document.getElementById("surrounding_perimeter").getContext("2d");
	ctx.fillStyle = "#ffffff";
	ctx.fillRect(0, 0, 620, 620);
}

function dots(isRed, x, y) {
	var c = document.getElementById("dots");
	var ctx = c.getContext("2d");

	var img = document.getElementById("redDotImg");

	for ( var i = 1; i < arguments.length; i += 2) {
		var x = -17 + 20 * arguments[i];
		var y = -17 + 20 * arguments[i + 1];
		ctx.drawImage(img, x, y);
	}
}

function surr(isRed) {
	var c = document.getElementById("surrounding_fill");
	var ctx = c.getContext("2d");

	ctx.beginPath();
	ctx.fillStyle = isRed ? "#FF0000" : "#0000FF";

	for ( var i = 1; i < arguments.length; i += 2) {
		var x = -10 + 20 * arguments[i];
		var y = -10 + 20 * arguments[i + 1];
		ctx.lineTo(x, y);
	}
	ctx.closePath();
	ctx.fill();

	var c = document.getElementById("surrounding_perimeter");
	var ctx = c.getContext("2d");
	ctx.beginPath();
	ctx.strokeStyle = "#000000";

	for ( var i = 1; i < arguments.length; i += 2) {
		var x = -10 + 20 * arguments[i];
		var y = -10 + 20 * arguments[i + 1];
		ctx.lineTo(x, y);
	}
	ctx.closePath();
	ctx.stroke();
}

jQuery(document).ready(function() {
	$("#mainField").mousemove(function(e) {
		var x = Math.floor((e.pageX - this.offsetLeft - 4) / 20);
		var y = Math.floor((e.pageY - this.offsetTop - 4) / 20);
		// if (sizeX != hiDotX && y != hiDotY) {
		// hiDotX = sizeX;
		// hiDotY = y;
		$('#coordinates').html(x + '-' + y);
		// }
	});
});

var hiDotX = 1;
var hiDotY = 1;
