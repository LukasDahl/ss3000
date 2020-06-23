package com.cdio.ss3000.Vision;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.dnn.Dnn;
import org.opencv.dnn.Net;
import org.opencv.imgproc.Imgproc;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.widget.TextView;

import com.cdio.ss3000.CameraActivity;
import com.cdio.ss3000.DataLayer.Card;
import com.cdio.ss3000.DataLayer.GameControl;
import com.cdio.ss3000.DataLayer.Status;
import com.cdio.ss3000.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// AUTHORS: Lukas Amtoft Dahl & August Gammon Macholm
// Inspiration from: https://docs.opencv.org/3.4/d0/d6c/tutorial_dnn_android.html
//                   https://github.com/EdjeElectronics/OpenCV-Playing-Card-Detector
//                   https://github.com/ivangrov/Android-Deep-Learning-with-OpenCV

public class ComputerVision {

    // Lists for contour
    List<MatOfPoint> contours;
    Mat hierarchy;
    ArrayList<Integer> contourIsCard;

    // Yolo files
    File weightsFile;
    File cfgFile;

    // Scaling the input
    double SCALE;
    final static double SCALE_WIDTH = 32 * 40;

    // Bitmap to be analyzed
    Bitmap inputPic;

    int tuning = -25;

    // Colors for boxes and text
    final static int TEXT_R = 138;
    final static int TEXT_G = 43;
    final static int TEXT_B = 226;

    // Put the card in the center of the image?
    final static boolean CENTER = true;

    double CONFIDENCE = 0.2;

    int BOXSIZE = 430;

    int MIN_DISTANCE = 15;

    int CARD_MAX_AREA = 200000;
    int CARD_MIN_AREA = 10000;

    int l = 0;
    int t = 0;
    int moves = 0;

    GameControl gc;

    private static String[] classNames = {
            "Ah", "Kh", "Qh", "Jh", "10h", "9h", "8h", "7h", "6h", "5h", "4h", "3h", "2h",
            "Ad", "Kd", "Qd", "Jd", "10d", "9d", "8d", "7d", "6d", "5d", "4d", "3d", "2d",
            "Ac", "Kc", "Qc", "Jc", "10c", "9c", "8c", "7c", "6c", "5c", "4c", "3c", "2c",
            "As", "Ks", "Qs", "Js", "10s", "9s", "8s", "7s", "6s", "5s", "4s", "3s", "2s"
    };

    // Singleton
    private static ComputerVision instance = null;

    public static ComputerVision getInstance(Context context) {
        if (instance == null) {
            instance = new ComputerVision();
        }
        instance.setContext(context);
        return instance;
    }

    private ComputerVision() {
        OpenCVLoader.initDebug();
        gc = new GameControl(null);
    }

    public void runVision(Bitmap bitmap) {
        inputPic = bitmap;
        Handler handler = new Handler();
        // Delay and offload to other thread
        handler.postDelayed(runnable, 50);
    }

    private Context context;

    public void setContext(Context context) {
        this.context = context;
    }


    Runnable runnable = new Runnable() {
        @Override
        public void run() {

            // Load yolo net
            try {
                pushNet(context);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Net net = Dnn.readNet(weightsFile.getAbsolutePath(), cfgFile.getAbsolutePath());


            // Get image and make it a Mat
            Mat imgorig = new Mat();
            Utils.bitmapToMat(inputPic, imgorig);


            for (int xx2 = 0; xx2 < 10; xx2++) {
                double newScale = SCALE_WIDTH - (32*xx2);
                double niceniceconf = 0.0;



                // Scale the image for the network
                SCALE = newScale / imgorig.width();
                Imgproc.resize(imgorig, imgorig, new Size(imgorig.width() * SCALE, imgorig.height() * SCALE));

                // Convert the color for openCV as that wants BGR not RGB
                Imgproc.cvtColor(imgorig, imgorig, Imgproc.COLOR_RGB2BGR);
                Mat img;
                Mat imgOriginal = imgorig.clone();
                // Convert back to display on screen or in debugger
                Imgproc.cvtColor(imgorig, imgorig, Imgproc.COLOR_BGR2RGB);

                // Find bounding boxes of piles
                detectPiles(prepareBoard(imgOriginal));

                if (contours.size() == 0)
                    return;

                List<Pile> piles = new ArrayList<>();
                List<Pile> pilesTop = new ArrayList<>();
                List<Pile> pilesBottom = new ArrayList<>();

                Rect rect;
                MatOfPoint contour;

                // For each contour detected:
                for (int index = 0; index < contours.size(); index++) {

                    contour = contours.get(index);
                    rect = Imgproc.boundingRect(contour);


                    // Don't show too small contours. There would be errors all over the picture.
                    if (Imgproc.contourArea(contour) < CARD_MIN_AREA)
                        continue;


                    // --------- START DEBUG ---------
                    // Show a red rectangle around bad contours
                    if (contourIsCard.get(index) != 1) {
                        System.out.println(rect.width);
                        Imgproc.rectangle(
                                imgorig,
                                new Point(rect.x, rect.y),
                                new Point(rect.x + rect.width, rect.y + rect.height),
                                new Scalar(255, 0, 0));
                        Imgproc.putText(
                                imgorig,
                                contourIsCard.get(index).toString(),
                                new Point(rect.x + rect.width, rect.y),
                                Core.FONT_HERSHEY_TRIPLEX,
                                2,
                                new Scalar(255, 0, 0)
                        );
                    }
                    // Show a green rectangle around good contours
                    else {
                        Imgproc.rectangle(
                                imgorig,
                                new Point(rect.x, rect.y),
                                new Point(rect.x + rect.width, rect.y + rect.height),
                                new Scalar(0, 255, 0));
                    }

                    // ---------- END DEBUG ----------

                    // Skip detection if contour is not a card
                    if (contourIsCard.get(index) != 1 || rect.height < rect.width || Imgproc.contourArea(contour) < CARD_MIN_AREA)
                        continue;

                    // Process pile to detect on it
                    img = convertToSquare(imgOriginal, rect);

                    Imgproc.cvtColor(img, img, Imgproc.COLOR_BGR2RGB);

                    // Get blob from image
                    Mat blob = Dnn.blobFromImage(img,
                            1.0 / 255.0,
                            new Size(416, 416),
                            new Scalar(0, 0, 0),
                            false,
                            false);

                    // Run detection
                    net.setInput(blob);
                    List<Mat> outs = new ArrayList<>();
                    net.forward(outs);

                    int cols = img.cols();
                    int rows = img.rows();
                    List<Rect> detectedRects = new ArrayList<>();
                    List<Integer> classIds = new ArrayList<>();
                    List<Double> confidences = new ArrayList<>();
                    //System.out.println("BLOBS: " + outs.size());

                    // Loop through submats
                    for (Mat out : outs) {

                        //System.out.println("DETECTIONS: " + out.rows());
                        // Loop through rows of submat (detections)
                        for (int i = 0; i < out.rows(); i++) {
                            Mat row = out.row(i);

                            // Find most confident item
                            Mat scores = row.colRange(5, out.cols());
                            Core.MinMaxLocResult mm = Core.minMaxLoc(scores);
                            double confidence = mm.maxVal;
                            Point classIdPoint = mm.maxLoc;

                            // Thresholding confidence
                            if (confidence > CONFIDENCE) {

                                // Find location of detection
                                int centerX = (int) (row.get(0, 0)[0] * cols);
                                int centerY = (int) (row.get(0, 1)[0] * rows);
                                int width = (int) (row.get(0, 2)[0] * cols);
                                int height = (int) (row.get(0, 3)[0] * rows);
                                int left = centerX - (width / 2);
                                int top = centerY - (height / 2);
                                int right = left + width;
                                int bottom = top + height;

                                Rect detectionRect = new Rect(left, top, width, height);
                                detectedRects.add(detectionRect);
                                classIds.add((int) classIdPoint.x);
                                confidences.add(confidence);
                                System.out.println(classNames[(int) classIdPoint.x] + " " + confidence);

                                // Draw rectangle on sub-image for debugging
//                            Imgproc.rectangle(
//                                    img,
//                                    new Point(left, top),
//                                    new Point(right, bottom),
//                                    new Scalar(TEXT_R, TEXT_G, TEXT_B)
//                            );

                                // Draw rectangle on original image
                                int origX1 = (int) ((double) (left - l)) + rect.x;
                                int origX2 = (int) ((double) (right - l)) + rect.x;
                                int origY1 = (int) ((double) (top - t)) + rect.y;
                                int origY2 = (int) ((double) (bottom - t)) + rect.y;

                                Imgproc.rectangle(
                                        imgorig,
                                        new Point(origX1, origY1),
                                        new Point(origX2, origY2),
                                        new Scalar(TEXT_R, TEXT_G, TEXT_B)
                                );

                                String conf = (confidence + "").substring(0, 5);
                                String label = classNames[(int) classIdPoint.x] + " " + conf;

                                // Draw text on sub-image for debugging
//                            Imgproc.putText(
//                                    img,
//                                    label,
//                                    new Point(right, bottom),
//                                    Core.FONT_HERSHEY_TRIPLEX,
//                                    1,
//                                    new Scalar(TEXT_R, TEXT_G, TEXT_B)
//
//                            );

                                // Draw text on original image
                                Imgproc.putText(
                                        imgorig,
                                        label,
                                        new Point(origX2, origY2),
                                        Core.FONT_HERSHEY_TRIPLEX,
                                        0.5,
                                        new Scalar(TEXT_R, TEXT_G, TEXT_B)

                                );

                            }
                        }
                    }

                    // Remove detections that are too close to each other
                    // Prefers the most confident
                    for (int i = 0; i < detectedRects.size(); i++) {
                        Rect r = detectedRects.get(i);
                        for (int j = 0; j < detectedRects.size(); j++) {
                            Rect r2 = detectedRects.get(j);
                            if (r == r2) {
                                continue;
                            }

                            int distX = Math.abs(r.x - r2.x);
                            int distY = Math.abs(r.y - r2.y);
                            if (MIN_DISTANCE > distX && MIN_DISTANCE > distY) {
                                if (confidences.get(i) > confidences.get(j)) {
                                    classIds.set(j, null);
                                } else classIds.set(i, null);
                            }
                        }
                    }
                    for (int i = 0; i < detectedRects.size(); i++) {
                        if (classIds.get(i) == null) {
                            classIds.remove(i);
                            confidences.remove(i);
                            detectedRects.remove(i);
                            i--;
                        }
                    }


                    //System.out.println("CLASSIDS: " + classIds);

                    // Add newly found cards to a pile
                    piles.add(new Pile(rect.x, rect.y, classIds));

                    // For debugging
                    //showMat(img);

                    for(double conf: confidences) {
                        niceniceconf = niceniceconf + conf;
                    }

                }

                // For debugging
                //showMat(imgorig);


                System.out.println(niceniceconf + " " + newScale);



                // Find top and bottom piles
                Pile.splitPiles(piles, pilesTop, pilesBottom);

                // Sort the piles by x coordinate
                Collections.sort(pilesBottom);
                Collections.sort(pilesTop);

                // Convert piles to state object
                Status status = gc.updateState(Pile.pileListToState(pilesTop, pilesBottom));
                if (status != Status.INPROGRESS) {
                    if (status == Status.INVALID) {
                        ((TextView) ((CameraActivity) context).findViewById(R.id.move_text)).setText("Wrong move or bad picture. Try again.");
                        return;
                    } else {
                        ((TextView) ((CameraActivity) context).findViewById(R.id.move_text)).setText("GAME OVER");
                        ((CameraActivity) context).gameOver(status == Status.WON, moves);
                        return;
                    }
                }
                Card bestMove = gc.run();
                String bestMoveString = bestMove.toMovesString();
                System.out.println("----------\nACTUAL MOVE\n----------");
                System.out.println(bestMoveString);
                ((TextView) ((CameraActivity) context).findViewById(R.id.move_text)).setText(bestMoveString);
                moves++;


            }
        }
    };

    // Converts a pile to a square image so detections can be made in it
    private Mat convertToSquare(Mat imgOriginal, Rect rect) {

        // Crop and resize
        Mat img = imgOriginal.submat(rect);
        img = img.clone();

        //System.out.println("RECT: " + rect.width + " " + rect.height);

        // Create white bars
        int t, b, l, r;
        if (CENTER) {
            if (img.height() <= BOXSIZE) {

                t = (BOXSIZE - img.height()) / 2;
                b = (BOXSIZE - img.height()) / 2;

                if (img.height() % 2 != 0 && BOXSIZE % 2 == 0)
                    t++;

                l = (BOXSIZE - img.width()) / 2;
                r = (BOXSIZE - img.width()) / 2;

                if (img.width() % 2 != 0 && BOXSIZE % 2 == 0)
                    l++;
            } else {
                t = 0;
                b = 0;

                l = (img.height() - img.width()) / 2;
                r = (img.height() - img.width()) / 2;

                if (img.width() % 2 != 0 && img.height() % 2 == 0)
                    l++;
            }
        } else {
            if (img.height() <= BOXSIZE) {

                t = 50;
                b = (BOXSIZE - img.height());

                l = 50;
                r = (BOXSIZE - img.width());

            } else {
                t = 50;
                b = 0;

                l = 50;
                r = (img.height() - img.width());
            }
        }

        this.t = t;
        this.l = l;

        // Add the calculated white border
        Core.copyMakeBorder(img, img, t, b, l, r, Core.BORDER_CONSTANT, new Scalar(255, 255, 255));

        return img;
    }

    // Inspiration from: https://github.com/EdjeElectronics/OpenCV-Playing-Card-Detector
    public void detectPiles(Mat img) {

        // Find contours in the image
        contours = new ArrayList<>();
        hierarchy = new Mat();
        Imgproc.findContours(img, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);

        if (contours.size() == 0)
            return;

        // Find all contours that fit the spec of a pile. And give error values if they don't.
        contourIsCard = new ArrayList<>();
        double size;
        for (int i = 0; i < contours.size(); i++) {

            MatOfPoint contour = contours.get(i);
            size = Imgproc.contourArea(contour);

            if (!(size < CARD_MAX_AREA))
                contourIsCard.add(-1);  // Too big
            else if (!(size > CARD_MIN_AREA))
                contourIsCard.add(-2);  // Too small
            else if (!(hierarchy.get(0, i)[3] == -1))
                contourIsCard.add(-3);  // Nested contour
            else if (contour.width() > (contour.height() * 0.8))
                contourIsCard.add(-4);  // Wrong proportions
            else
                contourIsCard.add(1);   // Actual pile

        }
    }

    // Inspiration from: https://github.com/EdjeElectronics/OpenCV-Playing-Card-Detector
    // Process image to find bounding boxes of cards
    public Mat prepareBoard(Mat image) {

        int meanVal;

        // Convert to grayscale
        Mat gray = new Mat();
        Imgproc.cvtColor(image, gray, Imgproc.COLOR_BGR2GRAY);

        // Blur the image
        Mat blur = new Mat();
        Imgproc.GaussianBlur(gray, blur, new Size(5, 5), 0);
        Mat threshold = new Mat();

        // Calculate a threshold and threshold the image
        for (int i = 0; i < 15; i++) {
            Imgproc.adaptiveThreshold(blur, threshold, 200, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY, 101, tuning);
            System.out.println("THRESHOLD: " + threshold.get(0, 0)[0] + " " + Core.mean(threshold));
            meanVal = (int) Core.mean(threshold).val[0];
            if (meanVal < 25) {
                tuning = tuning + 5;
            } else if (meanVal > 40) {
                tuning = tuning - 1;
            } else {
                break;
            }
        }
        showMat(threshold);

        return threshold;
    }


    // Move file to storage.
    private void pushNet(Context context) throws IOException {
        String weights = "yolov.weights";
        String cfg = "yolov3_tinynew.cfg";

        // Finds document dir
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);

        // Make files
        weightsFile = new File(storageDir, weights);
        cfgFile = new File(storageDir, cfg);

        InputStream in;
        FileOutputStream out;
        byte[] buff = new byte[1024];
        int read;

        // Check if file exists. If not, create it.
        if (!weightsFile.exists()) {
            in = context.getResources().openRawResource(R.raw.yolov);
            out = new FileOutputStream(weightsFile);
            try {
                while ((read = in.read(buff)) > 0) {
                    out.write(buff, 0, read);
                }
            } finally {
                in.close();
                out.close();
            }
        }

        // Check if file exists. If not, create it.
        if (!cfgFile.exists()) {
            in = context.getResources().openRawResource(R.raw.yolov3_tinynew);
            out = new FileOutputStream(cfgFile);
            try {
                while ((read = in.read(buff)) > 0) {
                    out.write(buff, 0, read);
                }
            } finally {
                in.close();
                out.close();
            }
        }


    }

    private void showMat(Mat img) {
        //Imgproc.cvtColor(img, img, Imgproc.COLOR_BGR2RGB);

        //// SHOW ON SCREEN - FOR DEBUGGING
        Bitmap bm = Bitmap.createBitmap(img.cols(), img.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(img, bm);
        int x = 0;
//        ImageView im = ((CameraActivity) context).findViewById(R.id.mats);
//        im.setImageBitmap(bm);
//        im.setVisibility(View.VISIBLE);
    }


}
