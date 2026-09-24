#!/usr/bin/env python3
"""从 GWT Messages 接口提取 (key, 英文默认值, 行号, 是否需人工) → TSV 底稿

处理 Java 源码中的字符串跨行拼接与缺省 @Key（此时 GWT 用方法名作 key）。
用法: python3 extract_messages.py Messages.java FormsMessages.java > messages-template.tsv
输出列: domain, key, english, lineno, manual
"""
import re
import sys

_STR = r'"(?:[^"\\]|\\.)*"'

# 形如 "..." + "..." + "..." 的 Java 字符串拼接 → 折叠为单个字面量
CONCAT_RE = re.compile(_STR + r'(?:\s*\+\s*' + _STR + r')+')

# @DefaultMessage(...) 到方法声明之间的完整条目
ENTRY_RE = re.compile(
    r'@DefaultMessage\(\s*"(?P<msg>(?:[^"\\]|\\.)*)"\)'
    r'(?P<extra>(?:(?!@DefaultMessage).)*?)'
    r'(?:String|SafeHtml)\s+(?P<method>\w+)\s*\(',
    re.S)

KEY_RE = re.compile(r'@Key\("([^"]+)"\)')

_ESCAPES = {'n': '\n', 't': '\t', '"': '"', '\\': '\\', "'": "'"}


def collapse_concat(src):
    def repl(m):
        parts = re.findall(_STR, m.group(0))
        return '"' + ''.join(p[1:-1] for p in parts) + '"'
    return CONCAT_RE.sub(repl, src)


def unescape(s):
    """只解 Java 标准转义，保留非 ASCII 字符原样"""
    out, i = [], 0
    while i < len(s):
        c = s[i]
        if c == '\\' and i + 1 < len(s) and s[i + 1] in _ESCAPES:
            out.append(_ESCAPES[s[i + 1]])
            i += 2
        else:
            out.append(c)
            i += 1
    return ''.join(out)


def extract(path):
    src = collapse_concat(open(path, encoding='utf-8').read())
    out = []
    for m in ENTRY_RE.finditer(src):
        key_m = KEY_RE.search(m.group('extra'))
        # GWT 约定：无 @Key 时以方法名作为 properties 查找键
        key = key_m.group(1) if key_m else m.group('method')
        manual = 'MANUAL' if '@AlternateMessage' in m.group('extra') else ''
        lineno = src[:m.start()].count('\n') + 1
        out.append((key, unescape(m.group('msg')), lineno, manual))
    return out


def tsv_safe(s):
    return s.replace('\\', '\\\\').replace('\t', '\\t').replace('\n', '\\n')


if __name__ == '__main__':
    for path in sys.argv[1:]:
        domain = path.rsplit('/', 1)[-1].replace('.java', '')
        for key, en, lineno, manual in extract(path):
            print(f'{domain}\t{key}\t{tsv_safe(en)}\t{lineno}\t{manual}')
