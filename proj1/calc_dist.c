/*
 * PROJ1-1: YOUR TASK A CODE HERE
 *
 * You MUST implement the calc_min_dist() function in this file.
 *
 * You do not need to implement/use the swap(), flip_horizontal(), transpose(), or rotate_ccw_90()
 * functions, but you may find them useful. Feel free to define additional helper functions.
 */

#include <stdlib.h>
#include <stdio.h>
#include "digit_rec.h"
#include "utils.h"
#include "limits.h"

/* Swaps the values pointed to by the pointers X and Y. */
void swap(unsigned char *x, unsigned char *y) {
    int temp = *x;
    *x = *y;
    *y = temp;
}

/* Flips the elements of a square array ARR across the y-axis. */
void flip_horizontal(unsigned char *arr, int width) {
	int x, y;
  	for (y = 0; y < width; y += 1) {
  		for (x = 0; x < width/2; x += 1) {
			swap(&arr[y * width + x], &arr[y * width + (width - x - 1)]);
		}
	}
}

/* Transposes the square array ARR. */
void transpose(unsigned char *arr, int width) {
    int x, y;
    for (y = 0; y < width; y += 1) {
        for (x = y; x < width; x += 1) {
            swap(&arr[y * width + x], &arr[x * width + y]);
        }
    }
}

/* Rotates the square array ARR by 90 degrees counterclockwise. */
void rotate_ccw_90(unsigned char *arr, int width) {
    flip_horizontal(arr, width);
    transpose(arr, width);
}

/*Prints a 2D ARR with DIM dimensions. */
void print_2D(unsigned char *arr, int dim) {
  char *format = "%02x ";
  char *format_nl = "%02x\n";
  int i = 0;
  for (i = 0; i < dim*dim; i++) {
    printf( ((i + 1) % dim) ? format : format_nl, arr[i]);
  }
  printf("\n");
}

unsigned int special_dist(unsigned char *image, int i_width, int i_height, 
        unsigned char *template, int t_width, int spec) {
	unsigned int temp = 0;
    unsigned int min_dist = UINT_MAX;
    if (spec == 0) {
        rotate_ccw_90(template, t_width);
    }
    if (spec == 1) {
        flip_horizontal(template, t_width);
    }
    int x, y;
    for (x = t_width - 1; x >= 0; x -= 1) {
        for (y = t_width - 1; y >= 0; y -= 1) {
            int n = image[x + y * t_width] - template[x + y * t_width];
            temp += n * n; 
        }
    }
    if (temp < min_dist) {
    	min_dist = temp;
    }
    return min_dist;
}

unsigned int translated_dist(unsigned char *image, int i_width, int i_height,
    unsigned char *template, int t_width, int xoffset, int yoffset) {
    unsigned int temp = 0;
    unsigned int min_dist = UINT_MAX;
    int x, y;
    if (xoffset == 0 && yoffset == 0) {
        return min_dist;
    }
    for (y = 0; y < t_width; y++) {
        for (x = 0; x < t_width; x++) {
            int n = image[x + xoffset + (y + yoffset) * i_width] - template[x + y * t_width];
            temp += n * n;
        }
    }
    return min_dist = (min_dist > temp) ? temp : min_dist;
}


/* Returns the squared Euclidean distance between TEMPLATE and IMAGE. The size of IMAGE
 * is I_WIDTH * I_HEIGHT, while TEMPLATE is square with side length T_WIDTH. The template
 * image should be flipped, rotated, and translated across IMAGE.
 */
unsigned int calc_min_dist(unsigned char *image, int i_width, int i_height, 
        unsigned char *template, int t_width) {
	unsigned int temp = 0;
    unsigned int min_dist = UINT_MAX;
    unsigned int hflip, rot, trans;
    int x, y, z, xoffset, yoffset;
    for (x = t_width - 1; x >= 0; x -= 1) {
    	for (y = t_width - 1; y >= 0; y -= 1) {
    		int n = image[x + y * t_width] - template[x + y * t_width];
    		temp += n * n; 
    	}
    }
    for (yoffset = 0; yoffset <= i_height - t_width; yoffset++) {
        for (xoffset = 0; xoffset <= i_width - t_width; xoffset++) {
            for (z = 0; z < 4; z++) {
                trans = translated_dist(image, i_width, i_height, template, t_width, xoffset, yoffset);
                hflip = special_dist(image, i_width, i_height, template, t_width, 1);
                flip_horizontal(template, t_width);
                hflip = (trans < hflip) ? trans : hflip;
                rot = special_dist(image, i_width, i_height, template, t_width, 0);
                rot = (rot < hflip) ? rot : hflip;
                temp = (temp < rot) ? temp : rot; 
            }
        }
    }
    return min_dist = (min_dist > temp) ? temp : min_dist;
}
