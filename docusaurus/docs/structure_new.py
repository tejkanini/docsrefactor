import os

docs = os.getcwd()  # assumes you're running inside docusaurus/docs

def walk(d, depth=0):
    rel = os.path.relpath(d, docs)
    print("  " * depth + "- " + (rel if rel != "." else "docs"))
    entries = sorted(os.listdir(d))
    for name in entries:
        fp = os.path.join(d, name)
        if os.path.isdir(fp):
            walk(fp, depth + 1)
        else:
            print("  " * (depth + 1) + "• " + name)

walk(docs)

