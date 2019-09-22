window.onload = function() {
	$('.js-preview-container').each(function() {
		$(this).change(function() {
			initialisePreview($(this));
		});
		initialisePreview($(this));
	});
};

function initialisePreview(element) {
	try {
		var jsonData = $(element).data('preview-json');

		if(!jsonData) {
			return;
		}

		if(typeof jsonData == 'string') {
			jsonData = JSON.parse(jsonData);
		}

		var data = [];
		data.push(jsonData);

		renderPreview(element, data, null);

	} catch (error) {
		console.log(error);
	}
}

function renderPreview(parent, data, parentListElement) {

	// for each preview element
	// simply verify we're not finding one too deep in the hierarchy
	// then fetch the correct data and append it
	$(parent).find(".js-preview").each(function(index, element) {
		var parentElement = $(element).closest('.js-preview-list');
		if(parentElement.length != 0 && parentElement[0] != parentListElement) {
			return;
		}

		$(element).html(getDataForElement(element, data));
	});


	// for each list element
	$(parent).find(".js-preview-list").each(function(index, element) {
		// first verify we're not finding an element too deep in the hierarchy
		var parentElement = $(element).parent().closest('.js-preview-list');
		if(parentElement.length != 0 && parentListElement != null && parentElement[0] != parentListElement) {
			return;
		}

		// get the list data for this element
		var listData = getDataForElement(element, data);
		if(listData == null) {
			return;
		}

		// store its current child (it should only ever have 1) and then empty it
		var childElement = $(element).children()[0];
		$(element).empty();

		// for each item in the list, append a child clone
		// then render the child with the correct data from the list
		for(listItem of listData) {
			var newData = shallowCloneArray(data);
			var childClone = $(childElement).clone();
			newData.push(listItem);
			$(element).append(childClone);
			renderPreview(childClone, newData, element);
		}
	});

}

function shallowCloneArray(array) {
	var newArray = [];

	for(item of array) {
		newArray.push(item);
	}

	return newArray;
}

function getDataByPath(path, data) {
	var paths = path.split('.');

	if(paths.length > 1) {
		return (getDataByPath(paths.slice(1).join('.'), data[paths[0]]));
	}

	if(paths[0] == "") {
		return data;
	}

	return data[paths[0]];
}

function getDataForElement(element, data) {
	var previewKey = $(element).data('preview-key');
	var parentLevel = $(element).data('preview-parentLevel') || 0;

	// our array is actually most senior data level first, even though we're treating it as most junior data level first
	// so we must invert the index
	parentLevel = data.length - 1 - parentLevel;

	if(parentLevel < 0) {
		console.warn('Warning! Parent level too high for element:');
		console.warn(element);
		return {};
	}

	return getDataByPath(previewKey, data[parentLevel]);
}
