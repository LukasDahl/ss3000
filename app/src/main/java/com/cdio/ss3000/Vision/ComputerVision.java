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
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.cdio.ss3000.Camera1Activity;
import com.cdio.ss3000.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ComputerVision {

    List<MatOfPoint> contours;
    Mat hierarchy;
    ArrayList<Integer> contourIsCard;

    double SCALEFACTOR = 0;
    double NOT_GLOBAL_SCALEFACTOR = 3.4;
    double PRE_SCALE;
    double PRE_SCALE_WITH = 1280;

    Bitmap bitmap, inputPic;

    int TEXT_R = 138;
    int TEXT_G = 43;
    int TEXT_B = 226;

    boolean CENTER = true;

    double CONFIDENCE = 0.2;
    int BKG_THRESH = 150;

    int BOXSIZE = 430;

    int MIN_DISTANCE = 5;

    int CARD_MAX_AREA = 200000;
    int CARD_MIN_AREA = 10000;

    int l = 0;
    int t = 0;

    private static ComputerVision instance = null;

    private static String[] classNames = {
            "Ah", "Kh", "Qh", "Jh", "10h", "9h", "8h", "7h", "6h", "5h", "4h", "3h", "2h",
            "Ad", "Kd", "Qd", "Jd", "10d", "9d", "8d", "7d", "6d", "5d", "4d", "3d", "2d",
            "Ac", "Kc", "Qc", "Jc", "10c", "9c", "8c", "7c", "6c", "5c", "4c", "3c", "2c",
            "As", "Ks", "Qs", "Js", "10s", "9s", "8s", "7s", "6s", "5s", "4s", "3s", "2s"
    };

    public static ComputerVision getInstance(Context context) {
        if (instance == null) {
            instance = new ComputerVision();
        }
        instance.setContext(context);
        return instance;
    }

    private ComputerVision() {
        OpenCVLoader.initDebug();
    }

    public void runVision(Bitmap bitmap){
        inputPic = bitmap;
        Handler handler = new Handler();
        handler.postDelayed(runnable, 50);
    }

    private Context context;

    public void setContext(Context context) {
        this.context = context;
    }


    Runnable runnable = new Runnable() {
        @Override
        public void run() {

            // Load Yolo
            String weights = Environment.getExternalStorageDirectory() + "/Android/data/com.cdio.ss3000/dnns/" + "yolov.weights";
            String cfg = Environment.getExternalStorageDirectory() + "/Android/data/com.cdio.ss3000/dnns/" + "yolov3_tinynew.cfg";
            Net net = Dnn.readNet(weights, cfg);


            // Loading image
            //String image = Environment.getExternalStorageDirectory() + "/Android/data/com.cdio.ss3000/dnns/" + "testcrop.jpg";
            //Mat imgorig = Imgcodecs.imread(image);
            Mat imgorig = new Mat();
            Utils.bitmapToMat(inputPic, imgorig);

            PRE_SCALE = PRE_SCALE_WITH/imgorig.width();
            Imgproc.resize(imgorig, imgorig, new Size(imgorig.width() * PRE_SCALE, imgorig.height() * PRE_SCALE));

            Imgproc.cvtColor(imgorig, imgorig, Imgproc.COLOR_RGB2BGR);
            Mat img;
            Mat imgOriginal = imgorig.clone();
            Imgproc.cvtColor(imgorig, imgorig, Imgproc.COLOR_BGR2RGB);

            find_cards(preprocess_image(imgOriginal));

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


                // debug start
                if (Imgproc.contourArea(contour) < CARD_MIN_AREA)
                    continue;

                if (contourIsCard.get(index) != 1){
                    System.out.println(rect.width);
                    Imgproc.rectangle(
                            imgorig,
                            new Point(rect.x,rect.y),
                            new Point(rect.x + rect.width, rect.y + rect.height),
                            new Scalar(255,0,0));
                    Imgproc.putText(
                            imgorig,
                            contourIsCard.get(index).toString(),
                            new Point(rect.x + rect.width, rect.y),
                            Core.FONT_HERSHEY_TRIPLEX,
                            2,
                            new Scalar(TEXT_R, TEXT_G, TEXT_B)
                    );
                } else {
                    Imgproc.rectangle(
                            imgorig,
                            new Point(rect.x,rect.y),
                            new Point(rect.x + rect.width, rect.y + rect.height),
                            new Scalar(0,255,0));
                }


                // debug end

                if (contourIsCard.get(index) != 1 || rect.height < rect.width || Imgproc.contourArea(contour) < CARD_MIN_AREA)
                    continue;



                for (int f = 0; f < 1; f++) {

                    img = convertToSquare(imgOriginal, rect);

                    Imgproc.cvtColor(img, img, Imgproc.COLOR_BGR2RGB);

                    Mat blob = Dnn.blobFromImage(img,
                            1.0 / 255.0,
                            new Size(416, 416),
                            new Scalar(0, 0, 0),
                            false,
                            false);
                    net.setInput(blob);
                    List<Mat> outs = new ArrayList<>();
                    net.forward(outs);
                    //net.forward(outs, outputLayers);
                    int cols = img.cols();
                    int rows = img.rows();
                    List<Rect> detectedRects = new ArrayList<>();
                    List<Integer> classIds = new ArrayList<>();
                    List<Double> confidences = new ArrayList<>();
                    System.out.println("BLOBS: " + outs.size());
                    for (Mat out : outs) {
                        System.out.println("DETECTIONS: " + out.rows());
                        for (int i = 0; i < out.rows(); i++) {

                            Mat row = out.row(i);
                            Mat scores = row.colRange(5, out.cols());
                            Core.MinMaxLocResult mm = Core.minMaxLoc(scores);

                            //double confidence = out.get(i, 2)[0];
                            double confidence = mm.maxVal;
                            Point classIdPoint = mm.maxLoc;


                            if (confidence > CONFIDENCE) {
                                System.out.println("IN HERE ( CONFIDENCE IF )");
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


                                Imgproc.rectangle(
                                        img,
                                        new Point(left, top),
                                        new Point(right, bottom),
                                        new Scalar(TEXT_R, TEXT_G, TEXT_B)
                                );

                                int origX1 = (int)((double)(left - l) / SCALEFACTOR) + rect.x;
                                int origX2 = (int)((double)(right - l) / SCALEFACTOR) + rect.x;
                                int origY1 = (int)((double)(top - t) / SCALEFACTOR) + rect.y;
                                int origY2 = (int)((double)(bottom - t) / SCALEFACTOR) + rect.y;

                                Imgproc.rectangle(
                                        imgorig,
                                        new Point(origX1, origY1),
                                        new Point(origX2, origY2),
                                        new Scalar(TEXT_R, TEXT_G, TEXT_B)
                                );

                                String conf = (confidence + "").substring(0, 5);
                                String label = classNames[(int) classIdPoint.x] + " " + conf;

                                Imgproc.putText(
                                        img,
                                        label,
                                        new Point(right, bottom),
                                        Core.FONT_HERSHEY_TRIPLEX,
                                        1,
                                        new Scalar(TEXT_R, TEXT_G, TEXT_B)

                                );
                                Imgproc.putText(
                                        imgorig,
                                        label,
                                        new Point(origX2, origY2),
                                        Core.FONT_HERSHEY_TRIPLEX,
                                        1,
                                        new Scalar(TEXT_R, TEXT_G, TEXT_B)

                                );

                            }
                        }
                    }

                    // remove to close detektions
                    for (int i = 0; i< detectedRects.size(); i++){
                        Rect r = detectedRects.get(i);
                        for ( int j = 0; j<detectedRects.size(); j++){
                            Rect r2 = detectedRects.get(j);
                            if (r == r2){
                                continue;
                            }

                            int distX = Math.abs(r.x - r2.x);
                            int distY = Math.abs(r.y - r2.y);
                            if ( MIN_DISTANCE > distX && MIN_DISTANCE > distY ) {
                                if (confidences.get(i) > confidences.get(j)){
                                    classIds.set(j, null);
                                }
                                else classIds.set(i, null);
                            }
                        }
                    }
                    for (int i = 0; i< detectedRects.size(); i++){
                        if (classIds.get(i) == null){
                            classIds.remove(i);
                            confidences.remove(i);
                            detectedRects.remove(i);
                            i--;
                        }
                    }


                    System.out.println("CLASSIDS: " + classIds);

                    piles.add(new Pile(rect.x, rect.y, classIds));

                    //showMat(img);
                }
            }

            showMat(imgorig);

            Pile.splitPiles(piles, pilesTop, pilesBottom);

            Collections.sort(pilesBottom);
            Collections.sort(pilesTop);

            Pile.pileListToState(pilesTop, pilesBottom);


        }
    };




    private Mat convertToSquare(Mat imgOriginal, Rect rect) {

        //double SCALEFACTOR = ((double)BOXSIZE)/(NOT_GLOBAL_SCALEFACTOR*(double)rect.width);

        //Crop and resize
        Mat img = imgOriginal.submat(rect);
        img = img.clone();
        //Imgproc.resize(img, img, new Size(img.width() * SCALEFACTOR, img.height() * SCALEFACTOR));

        System.out.println("RECT: " + rect.width + " " + rect.height);
        //Create black bars
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
        }
        else {
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
        this.SCALEFACTOR = SCALEFACTOR;

        Core.copyMakeBorder(img, img, t, b, l, r, Core.BORDER_CONSTANT, new Scalar(255, 255, 255));


        return img;
    }

    public void find_cards(Mat preprocessedImage) {

        // Find contours and sort their indices by contour size
        contours = new ArrayList<>();
        hierarchy = new Mat();
        Imgproc.findContours(preprocessedImage, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);


        // If there are no contours, do nothing
        if (contours.size() == 0)
            return;

        // Otherwise, initialize empty sorted contour and hierarchy lists
        contourIsCard = new ArrayList<>();
        double size;
        for (int i = 0; i < contours.size(); i++) {
            MatOfPoint contour = contours.get(i);
            size = Imgproc.contourArea(contour);
            if (!(size < CARD_MAX_AREA)) {
                contourIsCard.add(-1);
            } else if (!(size > CARD_MIN_AREA)) {
                contourIsCard.add(-2);
            } else if (!(hierarchy.get(0, i)[3] == -1)) {
                contourIsCard.add(-3);
            } else if (contour.width() > contour.height()) {
                contourIsCard.add(-4);
            } else {
                System.out.println("with:");
                System.out.println(contour.width());
                contourIsCard.add(1);
            }
        }
    }

    public Mat preprocess_image(Mat image) {

        int tuning = -25, meanVal;
        double tempMeanVal;

        Mat gray = new Mat();
        Imgproc.cvtColor(image, gray, Imgproc.COLOR_BGR2GRAY);
        Mat blur = new Mat();
        Imgproc.GaussianBlur(gray, blur, new Size(5, 5), 0);
        Mat threshold = new Mat();
        while (true) {
            Imgproc.adaptiveThreshold(blur, threshold, 200, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY, 101, tuning);


            // debug
            System.out.println("threshold val:");
            System.out.println(threshold.get(0, 0)[0]);
            System.out.println(Core.mean(threshold));

            meanVal = (int) Core.mean(threshold).val[0];
            if (meanVal < 30) {
                tuning = tuning + 5;
            }
            else if (meanVal > 50){
                tuning = tuning - 5;
            }
            else {
                break;
            }
        }
        showMat(threshold);
        return threshold;
    }

    private List<String> getOutputNames(Net net) {
        List<String> names = new ArrayList<>();
        List<Integer> outLayers = net.getUnconnectedOutLayers().toList();
        List<String> layerNames = net.getLayerNames();
        for (int i = 0; i < outLayers.size(); i++) {
            names.add(layerNames.get(outLayers.get(i) - 1));
        }
        return names;
    }

    // Upload file to storage and return a path.
    private static void pushNet(Context context) throws IOException {
        // String weights = Environment.getDataDirectory() + "/" + "yolov.weights";
        String weights = Environment.getExternalStorageDirectory() + "/Android/data/com.cdio.ss3000/dnns/" + "yolov.weights";
        // String cfg = Environment.getDataDirectory() + "/" + "yolov3_tinynew.cfg";
        String cfg = Environment.getExternalStorageDirectory() + "/Android/data/com.cdio.ss3000/dnns/" + "yolov3_tinynew.cfg";
        File fileDir = context.getFilesDir();
        // File weights = new File(fileDir, "yolov.weights");

        InputStream in;
        FileOutputStream out;
        in = context.getResources().openRawResource(R.raw.yolov);
        out = new FileOutputStream(weights);
        byte[] buff = new byte[1024];
        int read = 0;

        try {
            while ((read = in.read(buff)) > 0) {
                out.write(buff, 0, read);
            }
        } finally {
            in.close();
            out.close();
        }


        in = context.getResources().openRawResource(R.raw.yolov3_tinynew);
        out = new FileOutputStream(cfg);
        buff = new byte[1024];
        read = 0;

        try {
            while ((read = in.read(buff)) > 0) {
                out.write(buff, 0, read);
            }
        } finally {
            in.close();
            out.close();
        }


    }

    private static void pushPic(Context context) throws IOException {
        // String weights = Environment.getDataDirectory() + "/" + "yolov.weights";
        String jpg = Environment.getExternalStorageDirectory() + "/Android/data/com.cdio.ss3000/dnns/" + "testcrop.jpg";


        InputStream in;
        FileOutputStream out;
        in = context.getResources().openRawResource(R.raw.testcrop);
        out = new FileOutputStream(jpg);
        byte[] buff = new byte[1024];
        int read = 0;

        try {
            while ((read = in.read(buff)) > 0) {
                out.write(buff, 0, read);
            }
        } finally {
            in.close();
            out.close();
        }

    }

    private void showMat(Mat img) {
        //Imgproc.cvtColor(img, img, Imgproc.COLOR_BGR2RGB);

        ////SHOW ON SCREEN - FOR DEBUGGING
        Bitmap bm = Bitmap.createBitmap(img.cols(), img.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(img, bm);
        ImageView im = ((Camera1Activity) context).findViewById(R.id.mats);
        im.setImageBitmap(bm);
        im.setVisibility(View.VISIBLE);
    }


}
