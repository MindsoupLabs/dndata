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
}, false);

function elementShouldSticky() {
    var triggerElement = $("#js-preview-sticky-trigger");

    if(triggerElement.length != 1) {
        return true;
    }

    return $(triggerElement).offset().top - $(window).scrollTop() < 0;
}

function sticky(element) {
    element = $(element);
    element.css('width', element.width());
    element.css('top', 10);
    element.css('z-index', 9001);
    element.addClass('sticky');
}

function unsticky(element) {
    element = $(element);
    element.removeClass('sticky');
    element.css('width', '');
    element.css('top', '');
    element.css('z-index', '');
}