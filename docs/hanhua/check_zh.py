#!/usr/bin/env python3
"""校验 zh properties 与 Messages 接口的 parity、占位符、编码、空值（CI 门禁）

用法: python3 check_zh.py Messages.java Messages_zh.properties FormsMessages.java FormsMessages_zh.properties
退出码: 0=通过, 1=存在失败项
"""
import re
import sys
import os

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
from extract_messages import extract

# 占位符只比对参数编号集合：{0} {1,number} {1,plural,one#...{1,number}...} 均归一为 0/1
ARG_RE = re.compile(r'\{(\d+)')


def props_keys(path):
    d = {}
    for lineno, raw in enumerate(open(path, encoding='utf-8'), 1):
        line = raw.strip()
        if not line or line.startswith('#'):
            continue
        if '=' not in line:
            print(f'[格式] {path}:{lineno} 缺少 "="')
            continue
        k, v = line.split('=', 1)
        d[k.strip()] = v
    return d


def main(pairs):
    fails = []
    for jpath, ppath in pairs:
        jkeys = {key: en for key, en, _, _ in extract(jpath)}
        pkeys = props_keys(ppath)
        for k, en in jkeys.items():
            if k not in pkeys:
                fails.append(f'[缺译] {ppath}: {k}')
            elif not pkeys[k].strip():
                fails.append(f'[空译] {ppath}: {k}')
            elif set(ARG_RE.findall(en)) != set(ARG_RE.findall(pkeys[k])):
                fails.append(f'[占位符不一致] {k}: 英文[{en}] 中文[{pkeys[k]}]')
        for k in pkeys:
            if k not in jkeys:
                print(f'[告警] 多余 key（不影响构建，请确认）: {ppath}: {k}')
    try:
        for _, ppath in pairs:
            open(ppath, encoding='utf-8').read()
    except UnicodeDecodeError as e:
        fails.append(f'[非 UTF-8] {e}')
    print('\n'.join(fails) if fails else 'CHECK OK')
    return 1 if fails else 0


if __name__ == '__main__':
    args = sys.argv[1:]
    assert len(args) % 2 == 0, '参数应为 (java, properties) 成对出现'
    sys.exit(main(list(zip(args[0::2], args[1::2]))))
