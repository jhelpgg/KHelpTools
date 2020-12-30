package khelp.grammar.prebuilt.unicodes

import khelp.utilities.extensions.plus
import khelp.utilities.text.interval

val unicodeClassNL =
    interval('\u16EE', '\u16F0') +
    interval('\u2160', '\u2182') +
    interval('\u2185', '\u2188') +
    '\u3007' +
    interval('\u3021', '\u3029') +
    interval('\u3038', '\u303A') +
    interval('\uA6E6', '\uA6EF')
