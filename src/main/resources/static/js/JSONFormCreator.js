// compile JSX:
// https://reactjs.org/docs/add-react-to-a-website.html#optional-try-react-with-jsx
// ...\static> npx babel --watch react --out-dir js --presets react-app/prod

var Section = JSONSchemaForm.default;

var log = function log(type) {
	return console.log.bind(console, type);
};
var onSubmit = function onSubmit(_ref, e) {
	var formData = _ref.formData;
	return $("#jsonInput").val(JSON.stringify(formData));
};
var onChange = function onChange(_ref2, e) {
	var formData = _ref2.formData;

	$("#js-preview-container").data('changedData', JSON.stringify(formData));
	$("#js-preview-container").trigger('change');
};

function initReactForm() {
	ReactDOM.render(React.createElement(Section, { schema: schema, formData: previousFormData, uiSchema: uiSchema, onChange: onChange, onSubmit: onSubmit, onError: log("errors") }), document.getElementById("app"));
}

// regular JS continues here
function bindEvents() {
	var jsonForm = $("form.rjsf");
	var formButton = jsonForm.find("button[type=submit]");
	var submitButton = $("#submitButton");
	var jsonInput = $("#jsonInput");
	var realForm = $("#realForm");
	formButton.hide();
	submitButton.click(function (e) {
		e.preventDefault();
		if (!jsonForm[0].reportValidity()) {
			return;
		}
		formButton[0].click();
		if (!realForm[0].reportValidity()) {
			return;
		}
		realForm.submit();
	});
};

function mergeFormData() {
	var jsonInput = $("#jsonInput");

	previousFormData = $.extend(true, presetValuesObject, previousFormData, overwriteValuesObject);
	jsonInput.val(JSON.stringify(previousFormData));
};

function init() {
	mergeFormData();
	initReactForm();
	bindEvents();
};

// script entry
var previousFormData = JSON.parse($("#jsonInput").val());
$(document).ready(function () {
	init();
});