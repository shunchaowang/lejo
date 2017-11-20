(function ($) {
    $(function () {

        $("#locales").change(function () {
            var selectedOption = $('#locales').val();
            if (selectedOption) {

                window.location.replace(window.location.href + '?lang=' + selectedOption);
                var uri = URI(window.location.search);
                if (uri.hasSearch('lang')) {
                    uri.setSearch('lang', selectedOption);
                } else {
                    uri.addSearch('lang', selectedOption);
                }
                window.location.search = uri;
            }
        });
    });
})(jQuery);