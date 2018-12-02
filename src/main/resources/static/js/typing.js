(function() {
  'use strict';

  var globals = typeof global === 'undefined' ? self : global;
  if (typeof globals.require === 'function') return;

  var modules = {};
  var cache = {};
  var aliases = {};
  var has = {}.hasOwnProperty;

  var expRe = /^\.\.?(\/|$)/;
  var expand = function(root, name) {
    var results = [], part;
    var parts = (expRe.test(name) ? root + '/' + name : name).split('/');
    for (var i = 0, length = parts.length; i < length; i++) {
      part = parts[i];
      if (part === '..') {
        results.pop();
      } else if (part !== '.' && part !== '') {
        results.push(part);
      }
    }
    return results.join('/');
  };

  var dirname = function(path) {
    return path.split('/').slice(0, -1).join('/');
  };

  var localRequire = function(path) {
    return function expanded(name) {
      var absolute = expand(dirname(path), name);
      return globals.require(absolute, path);
    };
  };

  var initModule = function(name, definition) {
    var hot = hmr && hmr.createHot(name);
    var module = {id: name, exports: {}, hot: hot};
    cache[name] = module;
    definition(module.exports, localRequire(name), module);
    return module.exports;
  };

  var expandAlias = function(name) {
    return aliases[name] ? expandAlias(aliases[name]) : name;
  };

  var _resolve = function(name, dep) {
    return expandAlias(expand(dirname(name), dep));
  };

  var require = function(name, loaderPath) {
    if (loaderPath == null) loaderPath = '/';
    var path = expandAlias(name);

    if (has.call(cache, path)) return cache[path].exports;
    if (has.call(modules, path)) return initModule(path, modules[path]);

    throw new Error("Cannot find module '" + name + "' from '" + loaderPath + "'");
  };

  require.alias = function(from, to) {
    aliases[to] = from;
  };

  var extRe = /\.[^.\/]+$/;
  var indexRe = /\/index(\.[^\/]+)?$/;
  var addExtensions = function(bundle) {
    if (extRe.test(bundle)) {
      var alias = bundle.replace(extRe, '');
      if (!has.call(aliases, alias) || aliases[alias].replace(extRe, '') === alias + '/index') {
        aliases[alias] = bundle;
      }
    }

    if (indexRe.test(bundle)) {
      var iAlias = bundle.replace(indexRe, '');
      if (!has.call(aliases, iAlias)) {
        aliases[iAlias] = bundle;
      }
    }
  };

  require.register = require.define = function(bundle, fn) {
    if (bundle && typeof bundle === 'object') {
      for (var key in bundle) {
        if (has.call(bundle, key)) {
          require.register(key, bundle[key]);
        }
      }
    } else {
      modules[bundle] = fn;
      delete cache[bundle];
      addExtensions(bundle);
    }
  };

  require.list = function() {
    var list = [];
    for (var item in modules) {
      if (has.call(modules, item)) {
        list.push(item);
      }
    }
    return list;
  };

  var hmr = globals._hmr && new globals._hmr(_resolve, require, modules, cache);
  require._cache = cache;
  require.hmr = hmr && hmr.wrap;
  require.brunch = true;
  globals.require = require;
})();

(function() {
var global = typeof window === 'undefined' ? this : window;
var __makeRelativeRequire = function(require, mappings, pref) {
  var none = {};
  var tryReq = function(name, pref) {
    var val;
    try {
      val = require(pref + '/node_modules/' + name);
      return val;
    } catch (e) {
      if (e.toString().indexOf('Cannot find module') === -1) {
        throw e;
      }

      if (pref.indexOf('node_modules') !== -1) {
        var s = pref.split('/');
        var i = s.lastIndexOf('node_modules');
        var newPref = s.slice(0, i).join('/');
        return tryReq(name, newPref);
      }
    }
    return none;
  };
  return function(name) {
    if (name in mappings) name = mappings[name];
    if (!name) return;
    if (name[0] !== '.' && pref) {
      var val = tryReq(name, pref);
      if (val !== none) return val;
    }
    return require(name);
  }
};
require.register("src/typing.js", function(exports, require, module) {
'use strict';

Object.defineProperty(exports, "__esModule", {
	value: true
});

var _util = require('./util');

var DEFAULT_SETTINGS = {
	sentences: ['Hello typing.js'],
	caretChar: '_',
	caretClass: 'typingjs__caret',

	ignoreContent: false,
	ignorePrefix: false,
	typeDelay: 50,
	sentenceDelay: 750,
	humanize: true,

	onType: undefined,
	onBackspace: undefined,
	onFinish: undefined,
	onSentenceFinish: undefined
};

var Typing = {
	new: function _new(selector, options) {
		var elements = document.querySelectorAll(selector);
		this.withElements(elements, options);
	},

	withElements: function withElements(elements, options) {
		// Settings.
		var settings = (0, _util.merge)(DEFAULT_SETTINGS, options);

		Array.prototype.map.call(elements, function (el) {
			// Creates initial elements.
			var initialText = settings.ignoreContent ? '' : el.textContent;

			var content = document.createElement('span');
			content.className = 'typingjs__content';
			content.textContent = initialText;

			var caret = document.createElement('caret');
			caret.className = settings.caretClass;
			caret.textContent = settings.caretChar;

			el.innerHTML = '';
			el.appendChild(content);
			el.appendChild(caret);

			// Starts progress here.
			var sentencesLeft = settings.sentences.slice();

			function typeSentence(typer) {
				// Reads next iteration of the typing animation.
				var _typer = typer(),
				    current = _typer.current,
				    isType = _typer.isType,
				    isBackspace = _typer.isBackspace,
				    isDone = _typer.isDone;

				content.textContent = current;

				if (isDone) {
					if ((0, _util.isFunction)(settings.onSentenceFinish)) settings.onSentenceFinish.call(this_);
					typeArray();
				} else {
					// Callbacks.
					if (isType && (0, _util.isFunction)(settings.onType)) settings.onType.call(this_);
					if (isBackspace && (0, _util.isFunction)(settings.onBackspace)) settings.onBackspace.call(this_);

					// Next step
					var humanTimeout = settings.typeDelay;
					if (settings.humanize) humanTimeout = (0, _util.noise)(settings.typeDelay, settings.typeDelay);
					setTimeout(typeSentence, humanTimeout, typer);
				}
			}

			function typeArray() {
				var targetStr = (0, _util.head)(sentencesLeft);
				sentencesLeft = (0, _util.tail)(sentencesLeft);
				if (targetStr !== undefined) {
					var typer = (0, _util.makePrefixTyper)(content.textContent, targetStr);
					if (settings.ignorePrefix) {
						typer = (0, _util.makeTyper)(content.textContent, targetStr, function (curr) {
							return curr.length == 0;
						});
					}
					setTimeout(typeSentence, settings.sentenceDelay, typer);
				} else if ((0, _util.isFunction)(settings.onFinish)) {
					settings.onFinish.call(this_);
				}
			}

			typeArray();
		});
	}
};

if (typeof jQuery != 'undefined') {
	(function ($) {
		$.fn.typing = function (options) {
			Typing.withElements(this.get(), options);
		};
	})(jQuery);
}

window.Typing = Typing;
exports.default = Typing;

});

require.register("src/util.js", function(exports, require, module) {
"use strict";

Object.defineProperty(exports, "__esModule", {
	value: true
});
exports.merge = merge;
exports.isFunction = isFunction;
exports.isArray = isArray;
exports.head = head;
exports.tail = tail;
exports.strTail = strTail;
exports.strHead = strHead;
exports.strLast = strLast;
exports.strDrop = strDrop;
exports.strDropTail = strDropTail;
exports.strIntersect = strIntersect;
exports.noise = noise;
exports.isPrefix = isPrefix;
exports.isEmpty = isEmpty;
exports.makeTyper = makeTyper;
exports.makePrefixTyper = makePrefixTyper;
// Merges two objects.
function merge() {
	var merged = {};
	for (var i = 0; i < arguments.length; i++) {
		var obj = arguments[i];
		for (var attr in obj) {
			merged[attr] = obj[attr];
		}
	}
	return merged;
}

// Checks if the given object is a function. Taken from underscorejs source code.
function isFunction(obj) {
	return !!(obj && obj.constructor && obj.call && obj.apply);
}

// Checks if the given object is an array.
function isArray(obj) {
	return toString(obj) === "[object Array]";
}

// Returns the first element of the array.
function head(array) {
	return array[0];
}

// Returns the same array skipping the first element.
function tail(array) {
	return array.slice(1);
}

// Drops the first character of the string and returns the rest.
function strTail(str) {
	return str.substring(1, str.length);
}

// Returns the first character of the string.
function strHead(str) {
	return str[0];
}

// Return the last character of the string.
function strLast(str) {
	return str[str.length - 1];
}

// Drops the given number of characters from the start of the string.
function strDrop(string, n) {
	return string.substr(n, string.length);
}

// Drops the given number of characters from the end of the string
function strDropTail(string, n) {
	return string.substr(0, string.length - n);
}

// Intersects the start of two strings.
function strIntersect(a, b) {
	var i;
	for (i = 0; i < Math.min(a.length, b.length); i++) {
		if (a[i] != b[i]) break;
	}
	return a.substr(0, i);
}

// Returns the original value with the given noise applied.
// E.g. noise(x, 2) = x - 2 <= y <= x + 2
function noise(x, delta) {
	return Math.round(Math.random() * delta * 2 - delta) + x;
}

// Checks if the given prefix is prefix of target.
function isPrefix(prefix, target) {
	return target.substr(0, prefix.length) == prefix;
}

// Checks if the given string has length zero.
function isEmpty(string) {
	return string.length == 0;
}

// Creates a typer that deletes characters each time it
// is called until predicate is true. After that, it appends
// the characters of the given string one by one each time
// it is called.
function makeTyper(current, pending, predicate) {
	var forward = current.length == 0 || predicate(current, pending);
	var prevLength = current.length;
	return function () {
		var step = {
			current: current,
			pending: pending,
			isType: current.length > prevLength,
			isBackspace: current.length < prevLength,
			isDone: forward && pending.length <= 0
		};

		prevLength = current.length;
		if (forward && pending.length > 0) {
			current = current + head(pending);
			pending = tail(pending);
		} else if (!forward && current.length > 0) {
			current = strDropTail(current, 1);
		}
		forward = forward || current.length == 0 || predicate(current, pending);

		return step;
	};
}

// Creates a typer that deletes characters until current is a prefix
// of target.
function makePrefixTyper(current, target) {
	var commonPrefix = strIntersect(current, target);
	var pending = target.substr(commonPrefix.length, target.length);
	return makeTyper(current, pending, function (curr) {
		return curr == commonPrefix;
	});
}

});

;require.register("___globals___", function(exports, require, module) {
  
});})();require('___globals___');

require('src/typing');
//# sourceMappingURL=typing.js.map