function bindEvents() {
	var jsonForm = $("form.rjsf");
	var formButton = jsonForm.find("button[type=submit]");
	var submitButton = $("#submitButton");
	var jsonInput = $("#jsonInput");
	var realForm = $("#realForm");
	formButton.hide();
	submitButton.click(function(e) {
		e.preventDefault();
		var oldValue = jsonInput.val();
		jsonForm[0].reportValidity();
		formButton.click(e);
		var newValue = jsonInput.val();

		if(oldValue != newValue) {
			realForm.submit();
		}
	});
};

bindEvents();