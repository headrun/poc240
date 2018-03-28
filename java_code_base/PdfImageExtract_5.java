import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.asprise.ocr.Ocr;


public class PdfImageExtract_5 {

	public static void main(String... args) throws IOException {
		BufferedWriter writer = null;
		try
		{

        File input = new File("/home/headrun/workspace/PDFCrawling/src/Print_5-858x554.jpg");
        BufferedImage image = ImageIO.read(input);
        
        String Abs_name =null;

        BufferedImage resized = resize(image, 1200, 2000);

        File output = new File("/home/headrun/workspace/PDFCrawling/src/Print_5-1200x2000.jpg");
        ImageIO.write(resized, "jpg", output);
        Ocr.setUp(); // one time setup
    	Ocr ocr = new Ocr(); // create a new OCR engine
    	ocr.startEngine("eng", Ocr.SPEED_FASTEST); // English
      	String name = ocr.recognize("/home/headrun/workspace/PDFCrawling/src/Print_5-1200x2000.jpg",-1,0,0,0,0,Ocr.RECOGNIZE_TYPE_ALL,Ocr.OUTPUT_FORMAT_PLAINTEXT);
    	//System.out.println("Result: " + name);
    	//Cleans(name);
      	File out_txt = new File("Print_5.txt");
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
	  
	 String Cleansing_name = name.replace("**%","90%").replace("2*16-17", "2016-17").replace("T he Rsll .4**","The Rs11,400")
	 .replace("3*48*","30-180").replace("involvingdiaman»", "involving diaman-").replace("»","-").replace(";r","").
	 replace("atop ","a top ").replace("; "," ").replace("( )t hers","Others").replace("3*-18*","30-180").replace("AMI SHAH", "AMISHAH").
	 replace("feelthat","feel that").replace("tradingin","trading in").replace("olIices","offices").replace("Rsl","Rs1");
	 String Data_filter = Cleansing_name.replace("dealinguith","dealing with").replace(",r ",", ").replace("e*perienced","experienced").
	 replace("te*t","text").replace("*uick","quick").replace("2***","2009").replace("Rs4-b‘8t H )4- croretoa","Rs4,680.04 crore to a").
	 replace("oflenders","of lenders").replace("Iliad"," I had").replace("e*plain","explain").replace("\\V","W").replace("e*tremely","extremely").
	 replace("“in","Win").replace("[got","I got").replace("un‘i","um").replace("sinall"," small").replace("president *1","president of").
	 replace("e*ec-","exec-").replace("Aijav Dia-","Arjav Dia-").replace("issuevvith","issue with").replace("oliicial","offical").replace("vn't h","with");
	 //String Filter_UN = Cleansing_name.replaceAll("\\.(\\s.)", ",");
	 
	 String Filter_UN = Data_filter.replace("I Ie","He").replace("knowthecurrent","know the current").replace("hilt ","but ").
	 replace("mas-w ) «w w i r\n","").replace("I€1.d¢j\n","").replace("\nl\n","").replace("I.\n","\n").replace("Resen'e Bank ","Reserve Bank ").
	 replace("man/M: 17m fill?!” inf. mm","maulik.p@livemint.com");
	 
	// System.out.println(Filter_UN);
	 
	   
	return Filter_UN;
	   
   }
	
}
