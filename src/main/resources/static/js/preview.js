window.addEventListener("load", function() {
	$(".js-preview-container").data('preview-json', $("#jsonInput").val());
	$('.js-preview-container').each(function() {
		$(this).change(function() {
			initialisePreview($(this));
		});
		initialisePreview($(this));
	});
}, false);

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
	$(parent).find(".js-preview").each(function(index, element) {
		// clear each preview element
		$(element).empty();

		// verify we're not finding one too deep in the hierarchy
		var parentElement = $(element).closest('.js-preview-list');
		if(parentElement.length != 0 && parentElement[0] != parentListElement) {
			return;
		}

		// fetch the correct data and append it
		$(element).html(convertNewlinesToBr(getDataForElement(element, data)));
	});


	// for each list element
	$(parent).find(".js-preview-list").each(function(index, element) {
		// first verify we're not finding an element too deep in the hierarchy
		var parentElement = $(element).parent().closest('.js-preview-list');
		if(parentElement.length != 0 && parentListElement != null && parentElement[0] != parentListElement) {
			return;
		}

		// store its current child (it should only ever have 1) and then empty it
		var childElement = getListChild($(element));
		$(element).empty();

        // get the list data for this element
        var listData = filterNullValuesFromArray(getDataForElement(element, data));
        if(listData == null || listData.length == 0) {
            return;
        }

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

function convertNewlinesToBr(string) {
	if(typeof string !== 'string') {
		return string;
	}

	return string.replace(/\n/g,'<br>');
}

function getDataByPath(path, data) {
    // early out
    if(!data) {
        return '';
    }

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

function filterNullValuesFromArray(array) {
    var newArray = [];

    if(array == null) {
        return newArray;
    }

    for(item of array) {
        if(item != null) {
            newArray.push(item);
        }
    }

    return newArray;
}

function getListChild(parentElement) {
    var listChildDataAttribute = "list-child";
    var listChild = parentElement.data(listChildDataAttribute);

    if(listChild == null) {
        listChild = parentElement.children()[0];

        if(listChild != null) {
        	parentElement.data(listChildDataAttribute, listChild.outerHTML);
        }
    } else {
        listChild == $(listChild)[0];
    }

    return listChild;
}
