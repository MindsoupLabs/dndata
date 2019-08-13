// compile JSX:
// https://reactjs.org/docs/add-react-to-a-website.html#optional-try-react-with-jsx
// ...\static> npx babel --watch react --out-dir js --presets react-app/prod

const Section = JSONSchemaForm.default;

const log = (type) => console.log.bind(console, type);
const onSubmit = ({formData}, e) => $("#jsonInput").val(JSON.stringify(formData));

function initReactForm() {
	ReactDOM.render(
		(<Section schema={schema} formData={previousFormData} uiSchema={uiSchema} onChange={log("changed")} onSubmit={onSubmit} onError={log("errors")} />),
		document.getElementById("app")
	);
}

// regular JS continues here
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
$(document).ready(function(){
    init();
});