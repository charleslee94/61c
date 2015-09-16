#include "matcher.h"
#include "stdio.h"
#include "string.h"
#include <stdlib.h>
#include <ctype.h>

/**
 * Implementation of your matcher function, which
 * will be called by the main program.
 *
 * You may assume that both line and pattern point
 * to reasonably short, null-terminated strings.
 */
 int rgrep_helper1(char *line, char *pattern) {
	if (pattern[0] == 0) {
		return 1;
	}
	if (line[0] == 0) {
		return 0;
	}
	if (pattern[0] == line[0]) {
		return rgrep_helper1(&line[1], &pattern[1]);
	} else {
		return rgrep_helper1(&line[1], &pattern[0]);
	}
	return 0;
}
int rgrep_helper2(char *line, char *pattern, char c) {
	char *token;
	int a;
	token = strtok(pattern, "}");
	if (strlen(token) == 3) {
		int x = atoi(&token[0]);
		int y = atoi(&token[2]);
		for (int t = 0; t < x; t++) {
			if (line[t] == 0 || line[t] != c) {
				return 0;
			}
			a = t;
		}
		if (line[y] == c) {
			return 0;
		}
		return rgrep_matches(&line[a], &pattern[4]);
	} else {
		int i = atoi(token);
		for (int t = 0; t < i; t++) {
			if (line[t] == 0 || line[t] != c) {
				return 0;
				}
		a = t;
		}
		if (pattern[0] == 0) {
			return 1;
		}
		return rgrep_matches(&line[a], &pattern[4]);
	}
}
int rgrep_matches(char *line, char *pattern) {
	char c;
	if (pattern[0] == 0) {
		return 1;
	}
	if (line[0] == 0) {
		return 0;
	}
	if (pattern[0] == '.') {
		return rgrep_matches(&line[1], &pattern[1]);
	}
	if (pattern[0] == '\\') {
		return rgrep_helper1(&line[0], &pattern[1]);
	}
	if (pattern[1] == '{') {
		c = pattern[0];
		return rgrep_helper2(&line[0], &pattern[2], c);
	}
	if (pattern[0] == line[0]) {
		return rgrep_matches(&line[1], &pattern[1]);
	} else {
		return rgrep_matches(&line[1], &pattern[0]);
	}
	return 0;
}