import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Random;
import javax.imageio.ImageIO;
public class ImageProcessing {
	public static void main(String[] args) {
    // The provided images are apple.jpg, flower.jpg, and kitten.jpg
		int[][] imageData = imgToTwoD("./apple.jpg");
    // Or load your own image using a URL!
		//int[][] imageData = imgToTwoD("https://content.codecademy.com/projects/project_thumbnails/phaser/bug-dodger.png");
	  //viewImageData(imageData);

    // trimming image
		int[][] trimmed = trimBorders(imageData, 60);
		twoDToImage(trimmed, "./trimmed_apple.jpg");

    // calling inverted method
    int[][] inverted = negativeColor(imgToTwoD("./kitten.jpg"));
    twoDToImage(inverted, "./inverted_kitten.jpg");

    // calling stretching horizontally method
    int[][] stretchedHoriz = stretchHorizontally(imgToTwoD("./flower.jpg"));
    twoDToImage(stretchedHoriz, "./stretched_horiz_flower.jpg");

    // calling shrinking vertically method
    int[][] shrinkVert = shrinkVertically(imgToTwoD("./apple.jpg"));
    twoDToImage(shrinkVert, "./shrink_apple.jpg");

    // calling inverted image method
    int[][] upsideDown = invertImage(imgToTwoD("./kitten.jpg"));
    twoDToImage(upsideDown, "./upside_down_kitten.jpg");

    // calling filter method
    int[][] filtered = colorFilter(imgToTwoD("./flower.jpg"), -75, 80, -45);
    twoDToImage(filtered, "./filtered_flower.jpg");

    // calling painting a random image method
    int[][] blankImg = new int[500][500];
    int[][] randColors = paintRandomImage(blankImg);
    twoDToImage(randColors, "./random_image.jpg");

    // calling painting a rectangle method
    int[] rgba = {255, 255, 0, 255};
    int[][] img = imgToTwoD("./apple.jpg");
    int[][] paintRect = paintRectangle(img, 200, 200, 100, 100, getColorIntValFromRGBA(rgba));
    twoDToImage(paintRect, "./rect_image.jpg");

    // calling generate # of rectangles method
    int [][] generateRect = generateRectangles(imgToTwoD("./kitten.jpg"), 1000);
    twoDToImage(generateRect, "./rectangle_kitten.jpg");

		// int[][] allFilters = stretchHorizontally(shrinkVertically(colorFilter(negativeColor(trimBorders(invertImage(imageData), 50)), 200, 20, 40)));
		// Painting with pixels
	}
	// Image Processing Methods
	public static int[][] trimBorders(int[][] imageTwoD, int pixelCount) {
		// Example Method
		if (imageTwoD.length > pixelCount * 2 && imageTwoD[0].length > pixelCount * 2) {
			int[][] trimmedImg = new int[imageTwoD.length - pixelCount * 2][imageTwoD[0].length - pixelCount * 2];
			for (int i = 0; i < trimmedImg.length; i++) {
				for (int j = 0; j < trimmedImg[i].length; j++) {
					trimmedImg[i][j] = imageTwoD[i + pixelCount][j + pixelCount];
				}
			}
			return trimmedImg;
		} else {
			System.out.println("Cannot trim that many pixels from the given image.");
			return imageTwoD;
		}
	}
	public static int[][] negativeColor(int[][] imageTwoD) {
    int[][] new2DImage = new int[imageTwoD.length][imageTwoD[0].length];
    for (int i = 0; i < imageTwoD.length; i++) {
      for (int j = 0; j < imageTwoD[i].length; j++) {
        // get RGBA value
        int[] rgba = getRGBAFromPixel(imageTwoD[i][j]);
        // invert colours
        rgba[0] = 255 - rgba[0];
        rgba[1] = 255 - rgba[1];
        rgba[2] = 255 - rgba[2];
        // set new 2DImage to that inverted colour
        new2DImage[i][j] = getColorIntValFromRGBA(rgba);
      }
    }
    // return inverted image
		return new2DImage;
	}
	public static int[][] stretchHorizontally(int[][] imageTwoD) {
    int[][] stretchedImage = new int[imageTwoD.length][imageTwoD[0].length*2];
    // for width
    int doubleCol = 0;
    for (int i = 0; i < imageTwoD.length; i++) {
      for (int j = 0; j < imageTwoD[i].length; j++) {
        doubleCol = j * 2;
        // copy current pixel over
        stretchedImage[i][doubleCol] = imageTwoD[i][j];
        // copy current pixel over again
        stretchedImage[i][doubleCol+1] = imageTwoD[i][j];
      }
    }
		return stretchedImage;
	}
	public static int[][] shrinkVertically(int[][] imageTwoD) {
    int[][] shrinkedImage = new int[imageTwoD.length/2][imageTwoD[0].length];
    for (int i = 0; i < imageTwoD[0].length; i++) {
      for (int j = 0; j < imageTwoD.length-1; j+=2) {
        // shrinks the y-axis or columns
        shrinkedImage[j/2][i] = imageTwoD[j][i];
      }
    }
		return shrinkedImage;
	}
	public static int[][] invertImage(int[][] imageTwoD) {
		int[][] invertedImage = new int[imageTwoD.length][imageTwoD[0].length];
    for (int i = 0; i < imageTwoD.length; i++) {
      for (int j = 0; j < imageTwoD[i].length; j++) {
        // changes the pixels at i and j to be the opposite (symmetrical on the diagonal)
        invertedImage[i][j] = imageTwoD[(imageTwoD.length-1)-i][(imageTwoD[i].length-1)-j];
      }
    }
		return invertedImage;
	}
	public static int[][] colorFilter(int[][] imageTwoD, int redChangeValue, int greenChangeValue, int blueChangeValue) {
		int[][] filteredImage = new int[imageTwoD.length][imageTwoD[0].length];
    for (int i = 0; i < imageTwoD.length; i++) {
      for (int j = 0; j < imageTwoD[i].length; j++) {
        // get pixel data in int array form
        int[] rgba = getRGBAFromPixel(imageTwoD[i][j]);
        // stored changes in array for convenience
        int[] change = {redChangeValue, greenChangeValue, blueChangeValue};
        // change each color in rgba and make sure they're in range
        for (int k = 0; k < 3; k++) {
          rgba[k] += change[k];
          if (rgba[k] > 255) rgba[k] = 255;
          else if (rgba[k] < 0) rgba[k] = 0;
        }
        // storing new pixel data in new array
        filteredImage[i][j] = getColorIntValFromRGBA(rgba);
      }
    }
		return filteredImage;
	}
	// Painting Methods
	public static int[][] paintRandomImage(int[][] canvas) {
    // Random object class
    Random rand = new Random();
    for (int i = 0; i < canvas.length; i++) {
      for (int j = 0; j < canvas[i].length; j++) {
        // create 3 random numbers
        int rand1 = rand.nextInt(256);
        int rand2 = rand.nextInt(256);
        int rand3 = rand.nextInt(256);
        // put these new rgb values in an int array
        int[] rgbaValues = {rand1, rand2, rand3, 255};
        // store it in the original image
        canvas[i][j] = getColorIntValFromRGBA(rgbaValues);
      }
    }
		return canvas;
	}
	public static int[][] paintRectangle(int[][] canvas, int width, int height, int rowPosition, int colPosition, int color) {
    for (int i = 0; i < canvas.length; i++) {
      for (int j = 0; j < canvas[i].length; j++) {
        // check if the pixel is painted in this area with boundaries of width
        if (i >= rowPosition && i <= rowPosition + width) {
          // check if the pixel is painted in this area with boundaries of height
          if (j >= colPosition && j <= colPosition + height) {
            canvas[i][j] = color;
          }
        }
      }
    }
		return canvas;
	}
	public static int[][] generateRectangles(int[][] canvas, int numRectangles) {
    Random rand = new Random();
    for (int i = 0; i < numRectangles; i++) {
      // randomize width and height
      int width = rand.nextInt(canvas[0].length);
      int height = rand.nextInt(canvas.length);
      // randomize position
      int rowPos = rand.nextInt(canvas.length - height);
      int colPos = rand.nextInt(canvas[0].length - width);
      // create a random color
      int rand1 = rand.nextInt(256);
      int rand2 = rand.nextInt(256);
      int rand3 = rand.nextInt(256);
      // put these new rgb values in an int array
      int[] rgbaValues = {rand1, rand2, rand3, 255};
      // gets color in int form
      int randColor = getColorIntValFromRGBA(rgbaValues);
      // overwrite the input image
      canvas = paintRectangle(canvas, width, height, rowPos, colPos, randColor);
    }
    return canvas;
	}
	// Utility Methods
	public static int[][] imgToTwoD(String inputFileOrLink) {
		try {
			BufferedImage image = null;
			if (inputFileOrLink.substring(0, 4).toLowerCase().equals("http")) {
				URL imageUrl = new URL(inputFileOrLink);
				image = ImageIO.read(imageUrl);
				if (image == null) {
					System.out.println("Failed to get image from provided URL.");
				}
			} else {
				image = ImageIO.read(new File(inputFileOrLink));
			}
			int imgRows = image.getHeight();
			int imgCols = image.getWidth();
			int[][] pixelData = new int[imgRows][imgCols];
			for (int i = 0; i < imgRows; i++) {
				for (int j = 0; j < imgCols; j++) {
					pixelData[i][j] = image.getRGB(j, i);
				}
			}
			return pixelData;
		} catch (Exception e) {
			System.out.println("Failed to load image: " + e.getLocalizedMessage());
			return null;
		}
	}
	public static void twoDToImage(int[][] imgData, String fileName) {
		try {
			int imgRows = imgData.length;
			int imgCols = imgData[0].length;
			BufferedImage result = new BufferedImage(imgCols, imgRows, BufferedImage.TYPE_INT_RGB);
			for (int i = 0; i < imgRows; i++) {
				for (int j = 0; j < imgCols; j++) {
					result.setRGB(j, i, imgData[i][j]);
				}
			}
			File output = new File(fileName);
			ImageIO.write(result, "jpg", output);
		} catch (Exception e) {
			System.out.println("Failed to save image: " + e.getLocalizedMessage());
		}
	}
	public static int[] getRGBAFromPixel(int pixelColorValue) {
		Color pixelColor = new Color(pixelColorValue);
		return new int[] { pixelColor.getRed(), pixelColor.getGreen(), pixelColor.getBlue(), pixelColor.getAlpha() };
	}
	public static int getColorIntValFromRGBA(int[] colorData) {
		if (colorData.length == 4) {
			Color color = new Color(colorData[0], colorData[1], colorData[2], colorData[3]);
			return color.getRGB();
		} else {
			System.out.println("Incorrect number of elements in RGBA array.");
			return -1;
		}
	}
	public static void viewImageData(int[][] imageTwoD) {
		if (imageTwoD.length > 3 && imageTwoD[0].length > 3) {
			int[][] rawPixels = new int[3][3];
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					rawPixels[i][j] = imageTwoD[i][j];
				}
			}
			System.out.println("Raw pixel data from the top left corner.");
			System.out.print(Arrays.deepToString(rawPixels).replace("],", "],\n") + "\n");
			int[][][] rgbPixels = new int[3][3][4];
			for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					rgbPixels[i][j] = getRGBAFromPixel(imageTwoD[i][j]);
				}
			}
			System.out.println();
			System.out.println("Extracted RGBA pixel data from top the left corner.");
			for (int[][] row : rgbPixels) {
				System.out.print(Arrays.deepToString(row) + System.lineSeparator());
			}
		} else {
			System.out.println("The image is not large enough to extract 9 pixels from the top left corner");
		}
	}
}
