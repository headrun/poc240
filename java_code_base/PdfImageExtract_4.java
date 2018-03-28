import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.asprise.ocr.Ocr;


public class PdfImageExtract_4{

	public static void main(String... args) throws IOException {
		BufferedWriter writer = null;
		try
		{

        File input = new File("/home/headrun/workspace/PDFCrawling/src/Print 4.jpg");
        BufferedImage image = ImageIO.read(input);
        
        String Abs_name =null;

        BufferedImage resized = resize(image, 2500, 2500);

        File output = new File("/home/headrun/workspace/PDFCrawling/src/Print_4-2500x2500.jpg");
        ImageIO.write(resized, "jpg", output);
        Ocr.setUp(); // one time setup
    	Ocr ocr = new Ocr(); // create a new OCR engine
    	ocr.startEngine("eng", Ocr.SPEED_FASTEST); // English
      	String name = ocr.recognize("/home/headrun/workspace/PDFCrawling/src/Print_4-2500x2500.jpg",-1,0,0,0,0,Ocr.RECOGNIZE_TYPE_ALL,Ocr.OUTPUT_FORMAT_PLAINTEXT);
    	//System.out.println("Result: " + name);
    	//Cleans(name);
      	File out_txt = new File("Print_4.txt");
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
	  
	 String Cleansing_name = name.replace("ggressive PM accrus","Aggressive PNB accuses").replace("f mney Hemmerimg", "of money laundering").replace("n@|ivemint.com","n@livemint.com")
	 .replace("\nunjab","\nPunjab").replace("e*", "ex").replace("*u","qu").replace("R55.***","Rs5,000").
	 replace("an*i","anxi").replace("Rs6,5**","Rs6,500").replace("E*","Ex").replace("R5*45","Rs94.5").replace("lu*ury", "luxury").
	 replace("launderingand","laundering and").replace("Modi. accused","Modi, accused").replace("case. ","case, ").replace("time. ","time, ");
	 String Data_filter = Cleansing_name.replace("(LoUs).","(LoUs),").replace("\n( : “a.\n","\n").replace("Sun” Mehta","Sunil Mehta").
	 replace("\\‘ats","Vats").replace("rexuired","required").replace("letteradded","letter added").
	 replace("Modi. saying ","Modi, saying ").replace("Modi. in","Modi, in").replace("Girceslz Chandra Pmsad 3i:","Gireesh Chandra Prasad &").replace("Elisabet/1","Elizabeth")
	 .replace("II: is stoma","this Story.").replace("law. the","law, the");
	 String End_filter = Data_filter.substring(0, Data_filter.length()-72)+"";
	//System.out.println(End_filter);
	 
	   
	return End_filter;
	   
   }
	
}
