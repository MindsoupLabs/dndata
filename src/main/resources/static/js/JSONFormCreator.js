var Form = JSONSchemaForm.default;

var schema = {
  title: "Todo",
  type: "object",
  required: ["title"],
  properties: {
    title: { type: "string", title: "Title", default: "A new task" },
    done: { type: "boolean", title: "Done?", default: false }
  }
};

var log = function log(type) {
  return console.log.bind(console, type);
};

ReactDOM.render(React.createElement(Form, { schema: schema, onChange: log("changed"), onSubmit: log("submitted"), onError: log("errors") }), document.getElementById("app"));