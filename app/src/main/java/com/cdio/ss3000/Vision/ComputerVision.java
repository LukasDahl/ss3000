package com.cdio.ss3000.Vision;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.dnn.Dnn;
import org.opencv.dnn.Net;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.widget.ImageView;

import com.cdio.ss3000.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ComputerVision {

    private static ComputerVision instance = null;

    public static ComputerVision getInstance(Context context, ImageView imageView){
        if (instance == null){
            instance = new ComputerVision();
        }
        instance.setContext(context);
        instance.setImageView(imageView);
        return instance;
    }

    private ComputerVision(){
        Handler handler = new Handler();
        handler.postDelayed(runnable,500);
    }

    private Context context;
    private ImageView imageView;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {

            // Load Yolo



            //File fileDir = context.getFilesDir();
            //weights = new File(fileDir, "yolov.weights").getAbsolutePath();
            //cfg = new File(fileDir, "yolov3_tinynew.cfg").getAbsolutePath();

            // String weights = Environment.getDataDirectory() + "/" + "yolov.weights";
            String weights = Environment.getExternalStorageDirectory() + "/Android/data/com.cdio.ss3000/dnns/" + "yolov.weights";
            // String cfg = Environment.getDataDirectory() + "/" + "yolov3_tinynew.cfg";
            String cfg = Environment.getExternalStorageDirectory() + "/Android/data/com.cdio.ss3000/dnns/" + "yolov3_tinynew.cfg";
            Net net = Dnn.readNet(weights, cfg);


//            classes = []
//            frame_array = []
//            with open("cards.names", "r") as f:
//            classes = [line.strip() for line in f.readlines()]
//            layer_names = net.getLayerNames()
            List<String> outputLayers = getOutputNames(net);
//            colors = np.random.uniform(0, 255, size=(len(classes), 3))

            // Loading image
            String image = Environment.getExternalStorageDirectory() + "/Android/data/com.cdio.ss3000/dnns/" + "testcrop.jpg";
            Mat imgorig = Imgcodecs.imread(image);
            Mat img = new Mat();
          //  Imgproc.resize(imgorig, img, new Size(1050,675));
            Mat imgOriginal = imgorig.clone();

            find_cards(preprocess_image(imgOriginal));

            System.out.println("Depth: " + imgOriginal.depth());

            if (contours.size() == 0)
                return;
            System.out.println(contours.size() + " contours");
            // For each contour detected:
            Rect rect;
            MatOfPoint contour;

            for (int index = 0; index < contours.size(); index ++){

                contour = contours.get(index);
                rect = Imgproc.boundingRect(contour);

                if (contourIsCard.get(index) != 1 || rect.height < rect.width || Imgproc.contourArea(contour) < 10000)
                    continue;

                img = box_thing(imgOriginal, rect);
                System.out.println(img.channels());
                Bitmap bm = Bitmap.createBitmap(img.cols(), img.rows(),Bitmap.Config.ARGB_8888);
                if (true)
                    continue;
                Utils.matToBitmap(img, bm);

                imageView.setImageBitmap(bm);


                if (true)
                    continue;
                Imgproc.cvtColor(img, img, Imgproc.COLOR_RGBA2BGR);

                Mat blob = Dnn.blobFromImage(img, 1.0/255.0, new Size(416, 416), new Scalar(0, 0, 0), true, false);
                net.setInput(blob);
                List<Mat> outs = new ArrayList<>();
                net.forward(outs, outputLayers);
                int cols = img.cols();
                int rows = img.rows();
                List<Rect> detectedRects = new ArrayList<>();
                List<Integer> classIds = new ArrayList<>();
                List<Double> confidences = new ArrayList<>();
                for (Mat out: outs){
                    for (int i = 0; i < out.rows(); i++){
                        double confidence = out.get(i, 2)[0];
                        if (confidence > CONFIDENCE){
                            int classId = (int) out.get(i, 1)[0];
                            int left = (int) (out.get(i, 3)[0] * cols);
                            int top = (int) (out.get(i, 4)[0] * rows);
                            int right = (int) (out.get(i, 5)[0] * cols);
                            int bottom = (int) (out.get(i, 6)[0] * rows);
                            Rect detectionRect = new Rect(left, top, right-left, bottom-top);
                            detectedRects.add(detectionRect);
                            classIds.add(classId);
                            confidences.add(confidence);
                        }
                    }
                }
                System.out.println(classIds);




            }
//
//            indexes = cv2.dnn.NMSBoxes(boxes, confidences, 0.3, 0.4)
//            print(indexes)
//            font = cv2.FONT_HERSHEY_PLAIN
//            for i in range(len(boxes)):
//            if i in indexes:
//            x, y, w, h = boxes[i]
//            label = str(classes[class_ids[i]])
//            color = colors[i]
//            cv2.rectangle(img, (x, y), (x + w, y + h), color, 2)
//            cv2.putText(img, label, (x, y + 30), font, 3, color, 3)
//            print(f"{x} {y} {label} {scalar / 100}")
//
//            frame_array.append(img)
//            cv2.imshow("Image", img)
//            while (True):
//            if cv2.waitKey(1) & 0xFF == ord('q'):
//            break
//                    cv2.destroyAllWindows()







            // frame_width = int(cap.get(3))
            // frame_height = int(cap.get(4))
            // out = cv2.VideoWriter('outpy.avi', cv2.VideoWriter_fourcc('M', 'J', 'P', 'G'), 10, (frame_width,frame_height))
            //
            // for i in range(len(frame_array)):
            //     // writing to a image array
//     out.write(frame_array[i])
            //
            // cap.release()
//            cv2.imshow("Image", imgNotSoOrig)
//            cv2.imwrite('out.jpg', imgNotSoOrig)
//            while (True):
//            if cv2.waitKey(1) & 0xFF == ord('q'):
//            break
//                    cv2.destroyAllWindows()
        }
    };

    List<MatOfPoint> contours;
    Mat hierarchy;
    ArrayList<Integer> contourIsCard;

    double CONFIDENCE = 0.5;
    int BKG_THRESH = 200;
    int CARD_THRESH = 30;

    //Width and height of card corner, where rank and suit are
    int CORNER_WIDTH = 32;
    int CORNER_HEIGHT = 84;

    // Dimensions of rank train images
    int RANK_WIDTH = 70;
    int RANK_HEIGHT = 125;

    // Dimensions of suit train images
    int SUIT_WIDTH = 70;
    int SUIT_HEIGHT = 100;

    int RANK_DIFF_MAX = 2000;
    int SUIT_DIFF_MAX = 700;

    int CARD_MAX_AREA = 120000;
    int CARD_MIN_AREA = 40000;

    private Mat box_thing(Mat imgOriginal, Rect rect) {
        //crop
        Mat img = imgOriginal.submat(rect);

        Bitmap bm = Bitmap.createBitmap(img.cols(), img.rows(),Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(img, bm);

        imageView.setImageBitmap(bm);



        Mat blank_image = Mat.zeros(1000, 1000, 3);


        img.copyTo(blank_image.submat(new Rect(0, 0, rect.width, rect.height)));

        //find thing


        //draw
        return blank_image;
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
            if ((size < CARD_MAX_AREA) && (size > CARD_MIN_AREA) && (hierarchy.get(0, i)[3] == -1)) {
                contourIsCard.add(1);
            } else {
                contourIsCard.add(0);
            }
        }
        return;
    }

    public Mat preprocess_image(Mat image) {

        Mat gray = new Mat();
        Imgproc.cvtColor(image, gray, Imgproc.COLOR_BGR2GRAY);
        Mat blur = new Mat();
        Imgproc.GaussianBlur(gray, blur,  new Size(5,5), 0);
        Mat threshold = new Mat();
        Imgproc.threshold(blur, threshold, BKG_THRESH, 255, Imgproc.THRESH_BINARY);

        Bitmap bm = Bitmap.createBitmap(threshold.cols(), threshold.rows(),Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(threshold, bm);

        imageView.setImageBitmap(bm);

        return threshold;
    }

    private List<String> getOutputNames(Net net){
        List<String> names = new ArrayList<>();
        List<Integer> outLayers = net.getUnconnectedOutLayers().toList();
        List<String> layerNames = net.getLayerNames();
        for (int i = 0; i < outLayers.size(); i++){
            names.add(layerNames.get(outLayers.get(i) - 1));
        }
        return names;
    }

    // Upload file to storage and return a path.
    private static void pushNet(Context context) throws IOException {
       // String weights = Environment.getDataDirectory() + "/" + "yolov.weights";
        String weights = Environment.getExternalStorageDirectory() + "/Android/data/com.cdio.ss3000/dnns/"  + "yolov.weights";
       // String cfg = Environment.getDataDirectory() + "/" + "yolov3_tinynew.cfg";
        String cfg = Environment.getExternalStorageDirectory() + "/Android/data/com.cdio.ss3000/dnns/"  + "yolov3_tinynew.cfg";
        File fileDir = context.getFilesDir();
       // File weights = new File(fileDir, "yolov.weights");

        InputStream in;
        FileOutputStream out;
            System.out.println("HERE!");
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



    //    File cfg = new File(fileDir, "yolov3_tinynew.cfg");
            System.out.println("THERE!");
            in = context.getResources().openRawResource(R.raw.yolov3_tinynew);
            out = new FileOutputStream(cfg);
            buff = new byte[1024];
            read = 0;

            try {
                while ((read = in.read(buff)) > 0) {
                    System.out.println(read);
                    out.write(buff, 0, read);
                }
            } finally {
                in.close();
                out.close();
            }


    }

    private static void pushPic(Context context) throws IOException {
        // String weights = Environment.getDataDirectory() + "/" + "yolov.weights";
        String jpg = Environment.getExternalStorageDirectory() + "/Android/data/com.cdio.ss3000/dnns/"  + "testcrop.jpg";


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


}
