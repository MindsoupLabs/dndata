function bindEvents() {
	var formButton = $("form.rjsf").find("button[type=submit]");
	var submitButton = $("#submitButton");
	formButton.hide();
	submitButton.click(function() {
		formButton.click();
	});
};

bindEvents();