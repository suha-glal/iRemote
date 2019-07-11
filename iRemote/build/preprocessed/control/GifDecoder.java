/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package control;

import java.util.Vector;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class GifDecoder {

    /**
     * File read status: No errors.
     */
    public static final int STATUS_OK = 0;

    /**
     * File read status: Error decoding file (may be partially decoded)
     */
    public static final int STATUS_FORMAT_ERROR = 1;

    /**
     * File read status: Unable to open source.
     */
    public static final int STATUS_OPEN_ERROR = 2;

    protected byte[] in;
    protected int status;

    protected int width; // full image width
    protected int height; // full image height
    protected boolean gctFlag; // global color table used
    protected int gctSize; // size of global color table
    protected int loopCount = 1; // iterations; 0 = repeat forever

    protected int[] gct; // global color table
    protected int[] lct; // local color table
    protected int[] act; // active color table

    protected int bgIndex; // background color index
    protected int bgColor; // background color
    protected int lastBgColor; // previous bg color
    protected int pixelAspect; // pixel aspect ratio

    protected boolean lctFlag; // local color table flag
    protected boolean interlace; // interlace flag
    protected int lctSize; // local color table size

    protected int ix, iy, iw, ih; // current image rectangle
    protected int[] lastRect; // last image rect
    protected Image image; // current frame
    protected Image lastImage; // previous frame

    protected byte[] block = new byte[256]; // current data block
    protected int blockSize = 0; // block size

    // last graphic control extension info
    protected int dispose = 0;
    // 0=no action; 1=leave in place; 2=restore to bg; 3=restore to prev
    protected int lastDispose = 0;
    protected boolean transparency = false; // use transparent color
    protected int delay = 0; // delay in milliseconds
    protected int transIndex; // transparent color index

    protected static final int MaxStackSize = 4096;
    // max decoder pixel stack size

    // LZW decoder working arrays
    protected short[] prefix;
    protected byte[] suffix;
    protected byte[] pixelStack;
    protected byte[] pixels;

    protected Vector frames; // frames read from current file
    protected int frameCount;

    static class GifFrame {
        public GifFrame(Image im, int del) {
            image = im;
            delay = del;
        }
        public Image image;
        public int delay;
    }

    /**
     * Gets display duration for specified frame.
     *
     * @param n int index of frame
     * @return delay in milliseconds
     */
    public int getDelay(int n) {
        //
        delay = -1;
        if ((n >= 0) && (n < frameCount)) {
            delay = ((GifFrame) frames.elementAt(n)).delay;
        }
        return delay;

    }

    /**
     * Gets the number of frames read from file.
     * @return frame count
     */
    public int getFrameCount() {
        return frameCount;
    }

    /**
     * Gets the first (or only) image read.
     *
     * @return BufferedImage containing first frame, or null if none.
     */
    public Image getImage() {
        return getFrame(0);
    }

    /**
     * Gets the "Netscape" iteration count, if any.
     * A count of 0 means repeat indefinitiely.
     *
     * @return iteration count if one was specified, else 1.
     */
    public int getLoopCount() {
        return loopCount;
    }

    /**
     * Creates new frame image from current data (and previous
     * frames as specified by their disposition codes).
     */
    protected void setPixels() {
        // expose destination image's pixels as int array
                int[] dest =new int[image.getWidth()*image.getHeight()];
                image.getRGB(dest, 0,0,0,0,image.getWidth(), image.getHeight());


        // fill in starting image contents based on last image's dispose code
        if (lastDispose > 0) {
            if (lastDispose == 3) {
                // use image before last
                int n = frameCount - 2;
                if (n > 0) {
                    lastImage = getFrame(n - 1);
                } else {
                    lastImage = null;
                }
            }

            if (lastImage != null) {
                int[] prev = new int[lastImage.getHeight()*lastImage.getWidth()];
                                 lastImage.getRGB(prev, 0,0,0,0,lastImage.getWidth(), lastImage.getHeight());
                System.arraycopy(prev, 0, dest, 0, width * height);
                // copy pixels

                if (lastDispose == 2) {
                    // fill last image rect area with background color
                    Graphics g = image.getGraphics();

                    if (transparency) {
                        g.setColor(0x00000000);     // assume background is transparent
                    } else {
                        g.setColor(lastBgColor); // use given background color
                    }

                    g.fillRect(lastRect[0],lastRect[1],lastRect[2],lastRect[3]);

                }
            }
        }

        // copy each source line to the appropriate place in the destination
        int pass = 1;
        int inc = 8;
        int iline = 0;
        for (int i = 0; i < ih; i++) {
            int line = i;
            if (interlace) {
                if (iline >= ih) {
                    pass++;
                    switch (pass) {
                        case 2 :
                            iline = 4;
                            break;
                        case 3 :
                            iline = 2;
                            inc = 4;
                            break;
                        case 4 :
                            iline = 1;
                            inc = 2;
                    }
                }
                line = iline;
                iline += inc;
            }
            line += iy;
            if (line < height) {
                int k = line * width;
                int dx = k + ix; // start of line in dest
                int dlim = dx + iw; // end of dest line
                if ((k + width) < dlim) {
                    dlim = k + width; // past dest edge
                }
                int sx = i * iw; // start of line in source
                while (dx < dlim) {
                    // map color and insert in destination
                    int index = ((int) pixels[sx++]) & 0xff;
                    int c = act[index];
                    if (c != 0) {
                        dest[dx] = c;
                    }
                    dx++;
                }
            }
        }
    }

    /**
     * Gets the image contents of frame n.
     *
     * @return BufferedImage representation of frame, or null if n is invalid.
     */
    public Image getFrame(int n) {
        Image im = null;
        if ((n >= 0) && (n < frameCount)) {
            im = ((GifFrame) frames.elementAt(n)).image;
        }
        return im;
    }

    /**
     * Gets image size.
     *
     * @return GIF image dimensions
     */
    public int[] getFrameSize() {

            int[] dimension = new int[2];
            dimension[0] = width;
            dimension[1] = height;

        return dimension;
    }

}
        /**
         *
         * This method loads the GifDecoder with a GIF image byte array
         *
         * This is a modification from the original GifDecoder, which accepts a InputStream and is geared towards direct loading from the connection.
         * The Helix_Parser takes care of this in this case and therefore no streaming is required.
         *
         * CHANGES:
         *
         * Remove any DataInputStream functionality
         * Replace with byte[] reading functionality.
         *
         * Strip away unwanted methods.
         * Better understand LZW compression
         *
         * Consider legal issues.
         *
         *
         **/
/*
        public int read(byte[] raw) {
            init();

            if (raw != null) {

                in = raw;
                readHeader();
                if (!err()) {
                    readContents();
                    if (frameCount < 0) {
                        status = STATUS_FORMAT_ERROR;
                    }

                }

            } else {
                   status = STATUS_OPEN_ERROR;
            }

            return status;

        }
 */