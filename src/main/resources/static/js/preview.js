window.onload = function() {
	$("#js-preview-container").change(function() {
		try {
			var jsonData = JSON.parse($("#js-preview-container").data('changedData'));
			$(this).find(".js-preview").each(function(index, element) {
				var previewKey = $(element).data('preview-key');
				$(element).html(getDataByPath(previewKey, jsonData));
			});
		} catch (error) {
			// do nothing
		}
	});
};

function getDataByPath(path, data) {
	var paths = path.split('.');

	if(paths.length > 1) {
		return (getDataByPath(paths.slice(1).join('.'), data[paths[0]]));
	}

	return data[paths[0]];
}

