import os, json, sys

docs = os.getcwd()  # now assumes you are already inside docusaurus/docs

def read_cat(d):
    p = os.path.join(d, 'category.json')
    if os.path.exists(p):
        try:
            return json.load(open(p, 'r', encoding='utf-8'))
        except:
            return None
    return None

def walk(d, depth=0):
    rel = os.path.relpath(d, docs)
    cat = read_cat(d)
    label = cat.get('label') if cat else rel
    print('  '*depth + '- ' + label)
    for name in sorted(os.listdir(d)):
        if name == 'category.json':
            continue
        fp = os.path.join(d, name)
        if os.path.isdir(fp):
            walk(fp, depth + 1)
        elif name.endswith('.md'):
            print('  '*(depth+1) + '• ' + name)

walk(docs)

