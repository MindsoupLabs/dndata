// compile JSX:
// https://reactjs.org/docs/add-react-to-a-website.html#optional-try-react-with-jsx
// ...\static> npx babel --watch react --out-dir js --presets react-app/prod

const Section = JSONSchemaForm.default;

const log = (type) => console.log.bind(console, type);
const onSubmit = ({formData}, e) => $("#jsonInput").val(JSON.stringify(formData));
const onChange = ({formData}, e) => {
	$("#js-preview-container").data('changedData', JSON.stringify(formData))
	$("#js-preview-container").trigger('change');
};

function initReactForm() {
	ReactDOM.render(
		(<Section schema={schema} formData={previousFormData} uiSchema={uiSchema} onChange={onChange} onSubmit={onSubmit} onError={log("errors")} />),
		document.getElementById("app")
	);
}

// regular JS continues here
function bindEvents() {
	var jsonForm = $("form.rjsf");
	var formButton = jsonForm.find("button[type=submit]");
	// initialise data in form format in jsonInput
	formButton[0].click();

	var submitButton = $("#submitButton");
	var jsonInput = $("#jsonInput");
	var originalJsonData = jsonInput.val();
	var realForm = $("#realForm");
	formButton.hide();
	submitButton.click(function(e) {
		e.preventDefault();
		if(!jsonForm[0].reportValidity()) {
			return;
		}
		formButton[0].click();
		if(!realForm[0].reportValidity()) {
			return;
		}

		if(jsonInput.val() != originalJsonData) {
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