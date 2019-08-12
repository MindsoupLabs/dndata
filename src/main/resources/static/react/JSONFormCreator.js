const Section = JSONSchemaForm.default;

const log = (type) => console.log.bind(console, type);
const onSubmit = ({formData}, e) => $("#jsonInput").val(JSON.stringify(formData));

const previousFormData = JSON.parse($("#jsonInput").val());

ReactDOM.render(
	(<Section schema={schema} formData={previousFormData} uiSchema={uiSchema} onChange={log("changed")} onSubmit={onSubmit} onError={log("errors")} />),
	document.getElementById("app")
);

// https://reactjs.org/docs/add-react-to-a-website.html#optional-try-react-with-jsx
// ...\static> npx babel --watch react --out-dir js --presets react-app/prod