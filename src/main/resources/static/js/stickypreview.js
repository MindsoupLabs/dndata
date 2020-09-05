window.addEventListener("load", function() {
    $(window).on('scroll', function() {
        $('.js-preview-container').each(function(index, element) {
            if(elementShouldSticky()) {
                sticky(element);
            } else {
                unsticky(element);
            }
        });
    });

    $('.js-preview-container').on("change", function(event) {
    	if(!$(this).hasClass('sticky')) {
    		return;
    	}
    	unsticky(this);
    	sticky(this);
    });
}, false);

function elementShouldSticky() {
    var triggerElement = $("#js-preview-sticky-trigger");

    // do not sticky on mobile
    if($(window).width() < 992) {
    	return false;
    }

    if(triggerElement.length != 1) {
        return true;
    }

    return $(triggerElement).offset().top - $(window).scrollTop() < 0;
}

function sticky(element) {
    element = $(element);

    if(element.hasClass('sticky')) {
    	return;
    }

    element.css('width', element.width());
    element.css('height', Math.min(element.height() + 25, $(window).height() - 20));
    element.css('top', 10);
    element.css('z-index', 9001);
    element.addClass('sticky');
}

function unsticky(element) {
    element = $(element);

    if(!element.hasClass('sticky')) {
		return;
	}

    element.css('width', '');
    element.css('height', '');
    element.css('top', '');
    element.css('z-index', '');
    element.removeClass('sticky');
}