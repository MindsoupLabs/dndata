window.onload = function() {
	$('.js-preview-container').each(function() {
		$(this).change(function() {
			renderPreview($(this));
		});
		renderPreview($(this));
	});
};

function renderPreview(element) {
	try {
		var jsonData = $(element).data('preview-json');

		if(!jsonData) {
			return;
		}

		if(typeof jsonData == 'string') {
			jsonData = JSON.parse(jsonData);
		}

		$(element).find(".js-preview").each(function(index, element) {
			var previewKey = $(element).data('preview-key');
			$(element).html(getDataByPath(previewKey, jsonData));
		});
	} catch (error) {
		console.log(error);
	}
}

function getDataByPath(path, data) {
	var paths = path.split('.');

	if(paths.length > 1) {
		return (getDataByPath(paths.slice(1).join('.'), data[paths[0]]));
	}

	return data[paths[0]];
}

