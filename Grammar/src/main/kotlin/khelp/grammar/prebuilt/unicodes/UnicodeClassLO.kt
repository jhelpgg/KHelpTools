package khelp.grammar.prebuilt.unicodes

import khelp.utilities.extensions.interval
import khelp.utilities.extensions.plus
import khelp.utilities.text.interval

// Split in several groups, to help IDE compilation

private val group1 =
    '\u00AA'.interval + '\u00BA' + '\u01BB' + interval('\u01C0', '\u01C3') + '\u0294' +
    interval('\u05D0', '\u05EA') + interval('\u05F0', '\u05F2') + interval('\u0620', '\u063F') +
    interval('\u0641', '\u064A') + '\u066E' + '\u066F' + interval('\u0671', '\u06D3') +
    '\u06D5' + '\u06EE' + '\u06EF' + interval('\u06FA', '\u06FC') +
    '\u06FF' + '\u0710' + interval('\u0712', '\u072F') + interval('\u074D', '\u07A5') +
    '\u07B1' + interval('\u07CA', '\u07EA') + interval('\u0800', '\u0815') +
    interval('\u0840', '\u0858') + '\u08A0' + interval('\u08A2', '\u08AC') +
    interval('\u0904', '\u0939') + '\u093D' + '\u0950' + interval('\u0958', '\u0961') +
    interval('\u0972', '\u0977') + interval('\u0979', '\u097F') + interval('\u0985', '\u098C') +
    '\u098F' + '\u0990' + interval('\u0993', '\u09A8') + interval('\u09AA', '\u09B0')

private val group2 =
    '\u09B2'.interval + interval('\u09B6', '\u09B9') + '\u09BD' + '\u09CE' + '\u09DC' + '\u09DD' +
    interval('\u09DF', '\u09E1') + '\u09F0' + '\u09F1' + interval('\u0A05', '\u0A0A') +
    '\u0A0F' + '\u0A10' + interval('\u0A13', '\u0A28') + interval('\u0A2A', '\u0A30') +
    '\u0A32' + '\u0A33' + '\u0A35' + '\u0A36' + '\u0A38' + '\u0A39' +
    interval('\u0A59', '\u0A5C') + '\u0A5E' + interval('\u0A72', '\u0A74') +
    interval('\u0A85', '\u0A8D') + interval('\u0A8F', '\u0A91') +
    interval('\u0A93', '\u0AA8') + interval('\u0AAA', '\u0AB0') + '\u0AB2' + '\u0AB3' +
    interval('\u0AB5', '\u0AB9') + '\u0ABD' + '\u0AD0' + '\u0AE0' + '\u0AE1' +
    interval('\u0B05', '\u0B0C') + '\u0B0F' + '\u0B10' + interval('\u0B13', '\u0B28') +
    interval('\u0B2A', '\u0B30') + '\u0B32' + '\u0B33' + interval('\u0B35', '\u0B39')

private val group3 =
    '\u0B3D'.interval + '\u0B5C' + '\u0B5D' + interval('\u0B5F', '\u0B61') + '\u0B71' + '\u0B83' +
    interval('\u0B85', '\u0B8A') + interval('\u0B8E', '\u0B90') + interval('\u0B92', '\u0B95') +
    '\u0B99' + '\u0B9A' + '\u0B9C' + '\u0B9E' + '\u0B9F' + '\u0BA3' + '\u0BA4' +
    interval('\u0BA8', '\u0BAA') + interval('\u0BAE', '\u0BB9') + '\u0BD0' +
    interval('\u0C05', '\u0C0C') +
    interval('\u0C0E', '\u0C10') + interval('\u0C12', '\u0C28') + interval('\u0C2A', '\u0C33') +
    interval('\u0C35', '\u0C39') + '\u0C3D' + '\u0C58' + '\u0C59' + '\u0C60' + '\u0C61' +
    interval('\u0C85', '\u0C8C') + interval('\u0C8E', '\u0C90') + interval('\u0C92', '\u0CA8') +
    interval('\u0CAA', '\u0CB3') + interval('\u0CB5', '\u0CB9') + '\u0CBD' +
    '\u0CDE' + '\u0CE0' + '\u0CE1' + '\u0CF1' + '\u0CF2' + interval('\u0D05', '\u0D0C')

private val group4 =
    interval('\u0D0E', '\u0D10') + interval('\u0D12', '\u0D3A') + '\u0D3D' + '\u0D4E' +
    '\u0D60' + '\u0D61' + interval('\u0D7A', '\u0D7F') + interval('\u0D85', '\u0D96') +
    interval('\u0D9A', '\u0DB1') + interval('\u0DB3', '\u0DBB') + '\u0DBD' +
    interval('\u0DC0', '\u0DC6') + interval('\u0E01', '\u0E30') + '\u0E32' + '\u0E33' +
    interval('\u0E40', '\u0E45') + '\u0E81' + '\u0E82' + '\u0E84' + '\u0E87' + '\u0E88' +
    '\u0E8A' + '\u0E8D' + interval('\u0E94', '\u0E97') + interval('\u0E99', '\u0E9F') +
    interval('\u0EA1', '\u0EA3') + '\u0EA5' + '\u0EA7' + '\u0EAA' + '\u0EAB' +
    interval('\u0EAD', '\u0EB0') + '\u0EB2' + '\u0EB3' + '\u0EBD' + interval('\u0EC0', '\u0EC4') +
    interval('\u0EDC', '\u0EDF') + '\u0F00' + interval('\u0F40', '\u0F47') +
    interval('\u0F49', '\u0F6C') + interval('\u0F88', '\u0F8C') + interval('\u1000', '\u102A')

private val group5 =
    '\u103F'.interval + interval('\u1050', '\u1055') + interval('\u105A', '\u105D') + '\u1061' +
    '\u1065' + '\u1066' + interval('\u106E', '\u1070') + interval('\u1075', '\u1081') +
    '\u108E' + interval('\u10D0', '\u10FA') + interval('\u10FD', '\u1248') +
    interval('\u124A', '\u124D') + interval('\u1250', '\u1256') + '\u1258' +
    interval('\u125A', '\u125D') + interval('\u1260', '\u1288') + interval('\u128A', '\u128D') +
    interval('\u1290', '\u12B0') + interval('\u12B2', '\u12B5') + interval('\u12B8', '\u12BE') +
    '\u12C0' + interval('\u12C2', '\u12C5') + interval('\u12C8', '\u12D6') +
    interval('\u12D8', '\u1310') + interval('\u1312', '\u1315') + interval('\u1318', '\u135A') +
    interval('\u1380', '\u138F') + interval('\u13A0', '\u13F4') + interval('\u1401', '\u166C') +
    interval('\u166F', '\u167F') + interval('\u1681', '\u169A') + interval('\u16A0', '\u16EA')

private val group6 =
    interval('\u1700', '\u170C') + interval('\u170E', '\u1711') + interval('\u1720', '\u1731') +
    interval('\u1740', '\u1751') + interval('\u1760', '\u176C') + interval('\u176E', '\u1770') +
    interval('\u1780', '\u17B3') + '\u17DC' + interval('\u1820', '\u1842') +
    interval('\u1844', '\u1877') + interval('\u1880', '\u18A8') + '\u18AA' +
    interval('\u18B0', '\u18F5') + interval('\u1900', '\u191C') + interval('\u1950', '\u196D') +
    interval('\u1970', '\u1974') + interval('\u1980', '\u19AB') + interval('\u19C1', '\u19C7') +
    interval('\u1A00', '\u1A16') + interval('\u1A20', '\u1A54') + interval('\u1B05', '\u1B33') +
    interval('\u1B45', '\u1B4B') + interval('\u1B83', '\u1BA0') + '\u1BAE' + '\u1BAF' +
    interval('\u1BBA', '\u1BE5') + interval('\u1C00', '\u1C23') + interval('\u1C4D', '\u1C4F') +
    interval('\u1C5A', '\u1C77') + interval('\u1CE9', '\u1CEC') + interval('\u1CEE', '\u1CF1')

private val group7 =
    '\u1CF5'.interval + '\u1CF6' + interval('\u2135', '\u2138') + interval('\u2D30', '\u2D67') +
    interval('\u2D80', '\u2D96') + interval('\u2DA0', '\u2DA6') + interval('\u2DA8', '\u2DAE') +
    interval('\u2DB0', '\u2DB6') + interval('\u2DB8', '\u2DBE') + interval('\u2DC0', '\u2DC6') +
    interval('\u2DC8', '\u2DCE') + interval('\u2DD0', '\u2DD6') + interval('\u2DD8', '\u2DDE') +
    '\u3006' + '\u303C' + interval('\u3041', '\u3096') + '\u309F' +
    interval('\u30A1', '\u30FA') + '\u30FF' + interval('\u3105', '\u312D') +
    interval('\u3131', '\u318E') + interval('\u31A0', '\u31BA') + interval('\u31F0', '\u31FF') +
    '\u3400' + '\u4DB5' + '\u4E00' + '\u9FCC' + interval('\uA000', '\uA014') +
    interval('\uA016', '\uA48C') + interval('\uA4D0', '\uA4F7') + interval('\uA500', '\uA60B') +
    interval('\uA610', '\uA61F') + '\uA62A' + '\uA62B' + '\uA66E'

private val group8 =
    interval('\uA6A0', '\uA6E5') + interval('\uA7FB', '\uA801') + interval('\uA803', '\uA805') +
    interval('\uA807', '\uA80A') + interval('\uA80C', '\uA822') + interval('\uA840', '\uA873') +
    interval('\uA882', '\uA8B3') + interval('\uA8F2', '\uA8F7') + '\uA8FB' +
    interval('\uA90A', '\uA925') + interval('\uA930', '\uA946') + interval('\uA960', '\uA97C') +
    interval('\uA984', '\uA9B2') + interval('\uAA00', '\uAA28') + interval('\uAA40', '\uAA42') +
    interval('\uAA44', '\uAA4B') + interval('\uAA60', '\uAA6F') + interval('\uAA71', '\uAA76') +
    '\uAA7A' + interval('\uAA80', '\uAAAF') + '\uAAB1' + '\uAAB5' + '\uAAB6' +
    interval('\uAAB9', '\uAABD') + '\uAAC0' + '\uAAC2' + '\uAADB' + '\uAADC' +
    interval('\uAAE0', '\uAAEA') + '\uAAF2' + interval('\uAB01', '\uAB06') +
    interval('\uAB09', '\uAB0E') + interval('\uAB11', '\uAB16') + interval('\uAB20', '\uAB26')

private val group9 =
    interval('\uAB28', '\uAB2E') + interval('\uABC0', '\uABE2') + '\uAC00' + '\uD7A3' +
    interval('\uD7B0', '\uD7C6') + interval('\uD7CB', '\uD7FB') + interval('\uF900', '\uFA6D') +
    interval('\uFA70', '\uFAD9') + '\uFB1D' + interval('\uFB1F', '\uFB28') +
    interval('\uFB2A', '\uFB36') + interval('\uFB38', '\uFB3C') + '\uFB3E' +
    '\uFB40' + '\uFB41' + '\uFB43' + '\uFB44' + interval('\uFB46', '\uFBB1') +
    interval('\uFBD3', '\uFD3D') + interval('\uFD50', '\uFD8F') + interval('\uFD92', '\uFDC7') +
    interval('\uFDF0', '\uFDFB') + interval('\uFE70', '\uFE74') + interval('\uFE76', '\uFEFC') +
    interval('\uFF66', '\uFF6F') + interval('\uFF71', '\uFF9D') + interval('\uFFA0', '\uFFBE') +
    interval('\uFFC2', '\uFFC7') + interval('\uFFCA', '\uFFCF') + interval('\uFFD2', '\uFFD7') +
    interval('\uFFDA', '\uFFDC')

val unicodeClassLO =
    group1 + group2 + group3 + group4 + group5 + group6 + group7 + group8 + group9
