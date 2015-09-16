#include <stdio.h>
#include <stdlib.h>
#include <sys/time.h>
#include <time.h>

float special_dist(float *image, int i_width, int i_height, float *template, int t_width, int spec) {
    float temp = 0;
    float min_dist = UINT_MAX;
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

float translated_dist(float *image, int i_width, int i_height, float *template, int t_width, int xoffset, int yoffset) {
    float temp = 0;
    float min_dist = UINT_MAX;
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
float calc_min_dist(float *image, int i_width, int i_height, float *template, int t_width) {
    float temp = 0;
    float min_dist = UINT_MAX;
    float hflip, rot, trans;
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