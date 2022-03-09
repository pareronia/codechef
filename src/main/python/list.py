#! /usr/bin/env python3
#

import os
import re
import sys


def get_problem_info(file: str):
    with open(file, 'r') as f:
        lines = f.readlines()
        for i, line in enumerate(lines):
            if line.startswith("/**"):
                break
        if lines[i + 1].startswith(" * @see"):
            title = ""
            url = re.findall(r'>(.*)<\/a>', lines[i + 1].strip())
        else:
            title = lines[i + 1][3:].strip()
            url = re.findall(r'>(.*)<\/a>', lines[i + 2].strip())
        return {'title': title,
                'url': url[0],
                'code': url[0].split('/')[-1]
                }


def create_table(infos: dict) -> list[str]:
    lines = list[str]()
    lines.append("| Code | Problem | Solution |")
    lines.append("| --- | --- | --- |")
    infos.sort(key=lambda x: x['code'])
    for info in infos:
        lines.append(f"| {info['code']}"
                     f" | [{info['title']}]({info['url']})"
                     f" | [Java]({info['file']}) |")
    return lines


def add_table(table: list[str], file_name: str):
    with open(file_name, 'r') as f:
        tmp = f.read()
    with open(file_name, 'w') as f:
        in_table = False
        for line in tmp.splitlines():
            if line.startswith("<!-- @BEGIN:Problems"):
                print(line, file=f)
                in_table = True
                for row in table:
                    print(row, file=f)
            elif line.startswith("<!-- @END:Problems"):
                print(line, file=f)
                in_table = False
            else:
                if not in_table:
                    print(line, file=f)


def main():
    infos = list()
    for path, folders, files in os.walk("src/main/java"):
        for file in files:
            if not file.endswith(".java"):
                continue
            problem_file = os.path.join(path, file)
            info = get_problem_info(problem_file)
            info['file'] = problem_file.replace('\\', '/')
            infos.append(info)
    table = create_table(infos)
    add_table(table, sys.argv[1])


if __name__ == "__main__":
    main()
