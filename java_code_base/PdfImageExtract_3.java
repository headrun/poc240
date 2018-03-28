import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.asprise.ocr.Ocr;


public class PdfImageExtract_3 {

	public static void main(String... args) throws IOException {
		BufferedWriter writer = null;
		try
		{

        File input = new File("/home/headrun/workspace/PDFCrawling/src/Print 3.jpg");
        BufferedImage image = ImageIO.read(input);
        
        String Abs_name =null;

        BufferedImage resized = resize(image, 1500, 2200);

        File output = new File("/home/headrun/workspace/PDFCrawling/src/Print_3-1500x2200.jpg");
        ImageIO.write(resized, "jpg", output);
        Ocr.setUp(); // one time setup
    	Ocr ocr = new Ocr(); // create a new OCR engine
    	ocr.startEngine("eng", Ocr.SPEED_FASTEST); // English
      	String name = ocr.recognize("/home/headrun/workspace/PDFCrawling/src/Print_3-1500x2200.jpg",-1,0,0,0,0,Ocr.RECOGNIZE_TYPE_ALL,Ocr.OUTPUT_FORMAT_PLAINTEXT);
    	//System.out.println("Result: " + name);
    	//Cleans(name);
      	File out_txt = new File("Print_3.txt");
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
	  
	 String Cleansing_name = name.replace("* firms","9 firms").replace("2*1*", "2010").replace("butdid","but did")
	 .replace("w ere","were").replace("KHUSHBDD NARAYAN", "KHUSHBOO NARAYAN").replace("2*12","2012").replace("E*press","Express").
	 replace("MUMBAI.","MUMBAI,").replace("wakeofalleged","wake of alleged").replace("MarchandAugustZDlD","March and August 2010").replace("Morlir","Modi,").replace("CBlas", "CBI as").
	 replace("Supasva nath","Supasvanath").replace("1v'","V").replace("Vas hupujva","Vashupujya").replace("andarenot","and are not");
	 String Data_filter = Cleansing_name.replace("forclos ureon","for closure on").replace("theire*istenceand","their existence and").replace("an}.'","any").
	 replace("ofacc ounts","of accounts").replace("firmsaccordingto","firms,according to").replace("inthealleged","in the alleged").replace("Rs 11,4**-","Rs 11,400-").
	 replace("ID NIRAVMODI ","NIRAV MODI ").replace(" TD "," TO ").replace(" AVEN LIES."," AVENUES,").replace("O PURSUED","PURSUED").replace("iThe hidianEiPfiESS\n","").
	 replace("ski, 23 February 2618\n","").replace("can.” mum, epaper‘ . ind iane*p ress . comffcf265*4642\n","").replace("[lbelliE PAGES *, 1*\n","\n");
	 
	 String Filter_UN = Data_filter.replace("PvtLtd","Pvt Ltd").replace("GO MTINUED","CONTINUED").replace("\no\n","");
	 
	//System.out.println(Filter_UN);
	 
	   
	return Filter_UN;
	   
   }
	
}
