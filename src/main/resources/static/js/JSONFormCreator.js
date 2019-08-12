var Section = JSONSchemaForm.default;

var log = function log(type) {
	return console.log.bind(console, type);
};
var onSubmit = function onSubmit(_ref, e) {
	var formData = _ref.formData;
	return $("#jsonInput").val(JSON.stringify(formData));
};

var previousFormData = JSON.parse($("#jsonInput").val());

ReactDOM.render(React.createElement(Section, { schema: schema, formData: previousFormData, uiSchema: uiSchema, onChange: log("changed"), onSubmit: onSubmit, onError: log("errors") }), document.getElementById("app"));

// https://reactjs.org/docs/add-react-to-a-website.html#optional-try-react-with-jsx
// ...\static> npx babel --watch react --out-dir js --presets react-app/prod