/*
 * Copyright (C) 2014 Craig Thomas
 * This project uses an MIT style license - see LICENSE for details.
 */
package ca.craigthomas.visualclassifier.imageprocessing;

import java.awt.Color;
import java.awt.image.BufferedImage;

import org.jblas.DoubleMatrix;

import boofcv.core.image.ConvertBufferedImage;
import boofcv.core.image.ConvertImage;
import boofcv.io.image.UtilImageIO;
import boofcv.struct.image.ImageUInt8;
import boofcv.struct.image.MultiSpectral;

/**
 * The Image class stores image information. Contains useful functions to 
 * transform and manipulate images.
 * 
 * @author thomas
 */
public class Image {

    private MultiSpectral<ImageUInt8> mImage;
    private final int sBufferedImageType;
    
    /**
     * Generates a new Image from a BufferedImage.
     * 
     * @param image the BufferedImage to generate from
     */
    public Image(BufferedImage image) {
        if (image == null) {
            throw new IllegalArgumentException("image source cannot be null");
        }
        mImage = ConvertBufferedImage.convertFromMulti(image, null, true, ImageUInt8.class);
        sBufferedImageType = image.getType();
    }
    
    /**
     * Generates a new Image by loading in the image information from the 
     * specified filename.
     * 
     * @param filename the filename to load from
     */
    public Image(String filename) {
        this(UtilImageIO.loadImage(filename));
    }
    
    /**
     * Gets the width of the image in pixels.
     * 
     * @return the width of the image in pixels
     */
    public int getWidth() {
        return mImage.getWidth();
    }
    
    /**
     * Gets the height of the image in pixels.
     * 
     * @return the height of the image in pixels
     */
    public int getHeight() {
        return mImage.getHeight();
    }
    
    /**
     * Converts the image to grayscale. Returns a new copy of the image in
     * grayscale format.
     * 
     * @return a grayscale copy of the image
     */
    public Image convertToGrayscale() {
        ImageUInt8 grayscale = ConvertImage.average(mImage, null);
        return new Image(ConvertBufferedImage.convertTo(grayscale, null));
    }
    
    /**
     * Converts the image pixel intensities into a single column vector. 
     * First converts the image into a grayscale picture.
     * 
     * @return a column vector of the pixel intensities
     */
    public DoubleMatrix convertGrayscaleToMatrix() {
        ImageUInt8 grayscale = ConvertImage.average(mImage, null);
        byte [] data = grayscale.getData();
        DoubleMatrix result = new DoubleMatrix(1, data.length);
        for (int index = 0; index < data.length; index++) {
            result.put(0, index, (double)data[index]);
        }
        return result.divi(255.0);
    }
    
    /**
     * Generates a new image, which will be based upon the bounding box
     * of the top-left and bottom-right coordinates.
     * 
     * @param top the top y coordinate
     * @param left the left x coordinate
     * @param bottom the bottom y coordinate
     * @param right the right x coordinate
     * @return a new sub-image of the original
     */
    public Image getSubImage(int left, int top, int right, int bottom) {
        int height = bottom - top;
        int width = right - left;
        BufferedImage newImage = new BufferedImage(width, height, sBufferedImageType);
        BufferedImage oldImage = this.getBufferedImage();
        for (int x = left; x < right; x++) {
            for (int y = top; y < bottom; y++) {
                newImage.setRGB(x-left, y-top, oldImage.getRGB(x, y));
            }
        }
        return new Image(newImage);
    }
    
    /**
     * Converts an image into a BufferedImage.
     * 
     * @param image the image data to convert
     * @return a new BufferedImage
     */
    private BufferedImage convertToBufferedImage(MultiSpectral<ImageUInt8> image) {
        BufferedImage result = new BufferedImage(mImage.getWidth(), mImage.getHeight(), sBufferedImageType);
        ConvertBufferedImage.convertTo(mImage, result, true);
        return result;
    }
    
    /**
     * Returns the BufferedImage that backs the actual Image.
     * 
     * @return the BufferedImage backing the Image
     */
    public BufferedImage getBufferedImage() {
        return convertToBufferedImage(mImage);
    }
    
    /**
     * Draws a bounding box around the specified coordinates in the specified
     * color. Returns a new copy of the image with the bounding box placed
     * on the image.
     * 
     * @param top the top y position of the box
     * @param left the left x position of the box
     * @param bottom the bottom y position of the box
     * @param right the right x position of the box
     * @param color the color to draw the bounding box in
     * @return a new copy of the image with the bounding box
     */
    public Image drawBoundingBox(int top, int left, int bottom, int right, Color color) {
        return null;
    }
}