import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.asprise.ocr.Ocr;


public class PdfImageExtract_6 {

	public static void main(String... args) throws IOException {
		BufferedWriter writer = null;
		try
		{

        File input = new File("/home/headrun/workspace/PDFCrawling/src/Print6-858x554.jpg");
        BufferedImage image = ImageIO.read(input);
        
        String Abs_name =null;

        BufferedImage resized = resize(image, 1150, 2050);

        File output = new File("/home/headrun/workspace/PDFCrawling/src/Print_6-1150x2050.jpg");
        ImageIO.write(resized, "jpg", output);
        Ocr.setUp(); // one time setup
    	Ocr ocr = new Ocr(); // create a new OCR engine
    	ocr.startEngine("eng", Ocr.SPEED_FASTEST); // English
      	String name = ocr.recognize("/home/headrun/workspace/PDFCrawling/src/Print_6-1150x2050.jpg",-1,0,0,0,0,Ocr.RECOGNIZE_TYPE_ALL,Ocr.OUTPUT_FORMAT_PLAINTEXT);
    	//System.out.println("Result: " + name);
      	File out_txt = new File("Print_6.txt");
    	Abs_name = Cleans(name);
    	writer = new BufferedWriter(new FileWriter(out_txt));
        writer.write(Abs_name);
		}
        catch(Exception e)
        {
        	e.printStackTrace();
        }
        finally
    	{ 
    	   try{
    	      if(writer!=null)
    		 writer.close();
    	   }catch(Exception ex){
    	       System.out.println("Error in closing the BufferedWriter"+ex);
    	    }
    	}
       }
        
        
        
    

    private static BufferedImage resize(BufferedImage img, int height, int width) {
        Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resized;
    }
    
    
   private static String Cleans(String name)
   
   {
	  
	 String Cleansing_name = name.replace("15*","150").replace("ape*", "apex").replace("*uest","quest").replace("$6,4*8","$6,498")
	 .replace("<' .", "").replace("EVERYCJNE “WANTS NDRAV MCJIDII","EVERYONE WANTS NIRAV MODI").replace("Ta*","Tax");
	 String Filter_UN = Cleansing_name.substring(0, Cleansing_name.length()-19)+"";
	 //System.out.println(Filter_UN);
	 
	   
	return Filter_UN;
	   
   }
	
}
