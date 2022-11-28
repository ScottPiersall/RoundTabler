package RoundTabler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.InputMismatchException;

import static RoundTabler.RoundTable.WriteResultNameToJSFile;

public class HTMLErrorOut {


    public HTMLErrorOut(String error){
        addToErrorLog(error);
    }

    private void addToErrorLog(String error) {



        try{
            //Create file at directory specified. Problem with directory resorts to simply printing to command line

            //if(filePath.length() == 0)throw(new IOException());

            File file = new File("html/errorLog.html");

            file.createNewFile();

            FileWriter htmlWrite = new FileWriter(file);

            htmlWrite.write("<html><body style=\"margin:0;\">");
            htmlWrite.write(
                    "<div style=\"display: flex; flex: 1; flex-direction: row; width: 100%; background: linear-gradient(to left, #f2c413, #FFFFFF); align-items: center; justify-content: space-between; padding-inline: 15px; box-sizing: border-box;\">\n" +
                            "\t\t\t<div style=\"display: flex; flex: 1; flex-direction: row; align-items: center;\">\n" +
                            "\t\t\t\t<h1 style=\"font-family: 'monospace', sans-serif; font-size: 2.5vw;\">RoundTabler</h1>\n" +
                            "\t\t\t\t<svg id=\"Layer_1\" style=\"width: 5%;\" data-name=\"Layer 1\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" viewBox=\"0 0 185.52 157.33\">\n" +
                            "\t\t\t\t  <defs>\n" +
                            "\t\t\t\t\t<linearGradient id=\"linear-gradient\" x1=\"100.3\" y1=\"49.34\" x2=\"161.56\" y2=\"62.51\" gradientTransform=\"translate(-1.53 2.19) rotate(-1.16)\" gradientUnits=\"userSpaceOnUse\">\n" +
                            "\t\t\t\t\t  <stop offset=\"0\" stop-color=\"#fff\" stop-opacity=\".6\"/>\n" +
                            "\t\t\t\t\t  <stop offset=\".43\" stop-color=\"#fff\" stop-opacity=\".3\"/>\n" +
                            "\t\t\t\t\t  <stop offset=\"1\" stop-color=\"#fff\" stop-opacity=\".25\"/>\n" +
                            "\t\t\t\t\t</linearGradient>\n" +
                            "\t\t\t\t  </defs>\n" +
                            "\t\t\t\t  <g>\n" +
                            "\t\t\t\t\t<circle cx=\"107\" cy=\"78.5\" r=\"78.5\" fill=\"#a67c52\"/>\n" +
                            "\t\t\t\t\t<path d=\"M147.94,11.51c22.53,13.8,37.56,38.64,37.56,66.99,0,43.35-35.15,78.5-78.5,78.5-18.17,0-34.9-6.17-48.2-16.54\" fill=\"#a67c52\"/>\n" +
                            "\t\t\t\t  </g>\n" +
                            "\t\t\t\t  <g>\n" +
                            "\t\t\t\t\t<g>\n" +
                            "\t\t\t\t\t  <rect x=\"120.09\" y=\"107.52\" width=\"8\" height=\"20\" transform=\"translate(-45.38 95.38) rotate(-36)\" fill=\"#333\"/>\n" +
                            "\t\t\t\t\t  <g>\n" +
                            "\t\t\t\t\t\t<polyline points=\"116.45 107 111.59 110.53 58.69 37.71 63.55 34.19\" fill=\"#b3b3b3\"/>\n" +
                            "\t\t\t\t\t\t<polyline points=\"63.55 34.19 68.4 30.66 121.3 103.47 116.45 107 63.55 34.19 58.69 37.71 58.75 27.58\" fill=\"#b3b3b3\"/>\n" +
                            "\t\t\t\t\t\t<polygon points=\"58.75 27.58 68.4 30.66 63.55 34.19 58.75 27.58\" fill=\"#b3b3b3\"/>\n" +
                            "\t\t\t\t\t\t<g>\n" +
                            "\t\t\t\t\t\t  <line x1=\"58.89\" y1=\"27.77\" x2=\"116.45\" y2=\"107\" fill=\"none\" stroke=\"#999\" stroke-linejoin=\"bevel\" stroke-width=\".5\"/>\n" +
                            "\t\t\t\t\t\t  <polygon points=\"59.13 27.64 58.69 27.96 58.75 27.58 59.13 27.64\" fill=\"#999\"/>\n" +
                            "\t\t\t\t\t\t</g>\n" +
                            "\t\t\t\t\t  </g>\n" +
                            "\t\t\t\t\t  <g>\n" +
                            "\t\t\t\t\t\t<g>\n" +
                            "\t\t\t\t\t\t  <circle cx=\"103.11\" cy=\"118.77\" r=\"2.29\" fill=\"#f2c413\"/>\n" +
                            "\t\t\t\t\t\t  <path d=\"M118.78,110.21s-6.68,3.44-7.61,4.11-5.94,5.27-5.94,5.27l-2.25-3.1s3.82-.41,6.84-4.02,5.59-6.89,5.59-6.89\" fill=\"#f2c413\"/>\n" +
                            "\t\t\t\t\t\t</g>\n" +
                            "\t\t\t\t\t\t<g>\n" +
                            "\t\t\t\t\t\t  <ellipse cx=\"133.2\" cy=\"96.91\" rx=\"2.51\" ry=\"2.29\" transform=\"translate(-31.52 96.8) rotate(-36)\" fill=\"#f2c413\"/>\n" +
                            "\t\t\t\t\t\t  <path d=\"M118.78,110.21s5.94-5.73,6.96-6.47,7.48-4.48,7.48-4.48l-2.25-3.1s-1.84,3.7-6.58,5.73-8.97,3.69-8.97,3.69\" fill=\"#f2c413\"/>\n" +
                            "\t\t\t\t\t\t</g>\n" +
                            "\t\t\t\t\t  </g>\n" +
                            "\t\t\t\t\t  <circle cx=\"131.14\" cy=\"127.22\" r=\"5\" fill=\"#b3b3b3\"/>\n" +
                            "\t\t\t\t\t</g>\n" +
                            "\t\t\t\t\t<g opacity=\".1\">\n" +
                            "\t\t\t\t\t  <path d=\"M147.95,11.84c22.53,13.8,37.56,38.64,37.56,66.99,0,43.35-35.15,78.5-78.5,78.5-18.17,0-34.9-6.17-48.2-16.54\"/>\n" +
                            "\t\t\t\t\t</g>\n" +
                            "\t\t\t\t\t<g>\n" +
                            "\t\t\t\t\t  <circle cx=\"129.81\" cy=\"55.32\" r=\"31.32\" fill=\"url(#linear-gradient)\" stroke=\"#333\" stroke-miterlimit=\"10\" stroke-width=\"3\"/>\n" +
                            "\t\t\t\t\t  <rect x=\"62.51\" y=\"99.42\" width=\"62.02\" height=\"9.4\" rx=\"3\" ry=\"3\" transform=\"translate(-45.65 118.9) rotate(-54.13)\" fill=\"#333\"/>\n" +
                            "\t\t\t\t\t</g>\n" +
                            "\t\t\t\t  </g>\n" +
                            "\t\t\t\t</svg>\n" +
                            "\t\t\t</div>\n" +
                            "\t\t\t<a href=\"https://github.com/ScottPiersall/RoundTabler/blob/main/README.md\" target=\"_blank\" rel=\"noreferrer\" style=\"justify-self: flex-end;\">" +
                            "<svg style=\"width: 100%;\" aria-hidden=\"true\" height=\"24\" viewBox=\"0 0 16 16\" version=\"1.1\" width=\"24\" data-view-component=\"true\" class=\"octicon octicon-mark-github\">\n" +
                            "    <path fill-rule=\"evenodd\" d=\"M8 0C3.58 0 0 3.58 0 8c0 3.54 2.29 6.53 5.47 7.59.4.07.55-.17.55-.38 0-.19-.01-.82-.01-1.49-2.01.37-2.53-.49-2.69-.94-.09-.23-.48-.94-.82-1.13-.28-.15-.68-.52-.01-.53.63-.01 1.08.58 1.23.82.72 1.21 1.87.87 2.33.66.07-.52.28-.87.51-1.07-1.78-.2-3.64-.89-3.64-3.95 0-.87.31-1.59.82-2.15-.08-.2-.36-1.02.08-2.12 0 0 .67-.21 2.2.82.64-.18 1.32-.27 2-.27.68 0 1.36.09 2 .27 1.53-1.04 2.2-.82 2.2-.82.44 1.1.16 1.92.08 2.12.51.56.82 1.27.82 2.15 0 3.07-1.87 3.75-3.65 3.95.29.25.54.73.54 1.48 0 1.07-.01 1.93-.01 2.2 0 .21.15.46.55.38A8.013 8.013 0 0016 8c0-4.42-3.58-8-8-8z\"></path>\n" +
                            "</svg>" +
                            "</a>\n" +
                            "\t\t</div>\n" +
                            "\t\t<div>\n" +
                            "\t\t\t<p style=\"color: red; font-size: 1.5vw; font-family: 'monospace', sans-serif; padding-left: 20px\">"
            );

            htmlWrite.write(error + "</p>");
            htmlWrite.write("<p style=\"color: black; font-size: 1vw; font-family: 'monospace', sans-serif; padding-left: 20px\">");
            htmlWrite.write("Please see the documentation to help resolve any issues you may be having.");
            htmlWrite.write("</p>");
            htmlWrite.write(
                    "</div>\n" +
                            "\t</body>\n" +
                            "</html>"
            );

            htmlWrite.close();

            WriteResultNameToJSFile("errorLog.html");

            System.out.println();
            System.out.println("RoundTabler has encountered an error.");
            System.out.println("Error information can be found in the error log file at localhost8000/errorLog.html");

        }catch(IOException e){

            System.out.println(e);
            System.out.println(error);
            System.out.println("Please refer to https://github.com/ScottPiersall/RoundTabler/blob/main/README.md to help resolve any confusion.");

        }
    }


}
