package com.example;

import org.bytedeco.javacpp.indexer.UByteIndexer;
import org.bytedeco.opencv.opencv_core.Mat;

//Input is 2 JavaCV Mat objects 
//I couldn't find any information on how to actually acheive this so hopefully this helps someone

public class HaldLutOpenCV {
    static public int[] pixelcords(int[] pixel, int level){
        int[] out = new int[2];
        int cube_size = level*level;

        int r = pixel[2] * (cube_size -1) / 255;
        int g = pixel[1] * (cube_size -1) / 255;
        int b = pixel[0] * (cube_size -1) / 255;

    
        int x = (r % cube_size) + (g % level) * cube_size;
        int y = (b * level) + (g / level);

        out[0] = y;
        out[1] = x;

        return out;
    }

    static public Mat applyLut(Mat img, Mat lut){
        Mat newMat = new Mat(img.arrayHeight(), img.arrayWidth(), img.type());
        UByteIndexer iInd = img.createIndexer();
        UByteIndexer lInd = lut.createIndexer();
        UByteIndexer mInd = newMat.createIndexer();
        for(int x = 0; x < iInd.height(); x++){
            for(int y = 0; y < iInd.width(); y++){
                int[] values = new int[3];
                iInd.get(x,y,values);
                int[] newCords = pixelcords(values, 12);
                int[] pixel = new int[3];
                lInd.get(newCords[0], newCords[1], pixel);
                mInd.put(x,y,pixel);
            }   
        }
        return newMat;
    }
}
