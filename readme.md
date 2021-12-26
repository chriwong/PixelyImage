# PixelyImage
A small library to for novice students to practice working on small but slightly more substantial projects.

Includes a 4-byte image format to render rectangles on a grid.

Images are rendered with a 1-'pixel' white border.

## Image format
|Byte|meaning|
|-|-|
|0|Number of rows in the image|
|1|Number of columns in the image|
|2..(4 * total cells)|Pixels|

### Pixel format
|Byte|meaning|
|-|-|
|0|Red channel|
|1|Green channel|
|2|Blue channel|
|3|Alpha channel|
