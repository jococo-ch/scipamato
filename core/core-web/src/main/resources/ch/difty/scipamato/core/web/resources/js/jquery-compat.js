/*
 * Restores the jQuery 3 utility functions that jQuery 4 removed and that
 * bootstrap-datepicker and bootstrap-fileinput (wicket-bootstrap-extensions)
 * still call. Definitions as in jQuery 3.7.1 (src/deprecated.js, src/core.js).
 * Each one is only added if missing.
 */
(function ($) {
    'use strict';
    var rtrim = /^[\s﻿\xA0]+|([^\s﻿\xA0])[\s﻿\xA0]+$/g;

    if (typeof $.isArray !== 'function') {
        $.isArray = Array.isArray;
    }
    if (typeof $.isFunction !== 'function') {
        $.isFunction = function (obj) {
            return typeof obj === 'function' && typeof obj.nodeType !== 'number' && typeof obj.item !== 'function';
        };
    }
    if (typeof $.isNumeric !== 'function') {
        $.isNumeric = function (obj) {
            var type = Object.prototype.toString.call(obj);
            return (type === '[object Number]' || type === '[object String]') && !isNaN(obj - parseFloat(obj));
        };
    }
    if (typeof $.trim !== 'function') {
        $.trim = function (text) {
            return text == null ? '' : (text + '').replace(rtrim, '$1');
        };
    }
    if (typeof $.unique !== 'function') {
        $.unique = $.uniqueSort;
    }
})(jQuery);
