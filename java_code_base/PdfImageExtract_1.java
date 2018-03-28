import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.asprise.ocr.Ocr;


public class PdfImageExtract_1{

	public static void main(String... args) throws IOException {
		BufferedWriter writer = null;
		try
		{

        File input = new File("/home/headrun/workspace/PDFCrawling/src/Print 1.jpg");
        BufferedImage image = ImageIO.read(input);
        
        String Abs_name =null;

        BufferedImage resized = resize(image, 1800, 2400);

        File output = new File("/home/headrun/workspace/PDFCrawling/src/Print_1-1800x2400.jpg");
        ImageIO.write(resized, "jpg", output);
        Ocr.setUp(); // one time setup
    	Ocr ocr = new Ocr(); // create a new OCR engine
    	ocr.startEngine("eng", Ocr.SPEED_FASTEST); // English
      	String name = ocr.recognize("/home/headrun/workspace/PDFCrawling/src/Print_1-1800x2400.jpg",-1,0,0,0,0,Ocr.RECOGNIZE_TYPE_ALL,Ocr.OUTPUT_FORMAT_PLAINTEXT);
    	//System.out.println("Result: " + name);
    	//Cleans(name);
      	File out_txt = new File("Print_1.txt");
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
	  
	 String Cleansing_name = name.replace("@iiiianfla ii","Gitanjali").replace("Me i stares", "Modi stores").replace("(ELL JELE‘ unlit","desolate look")
	 .replace("(dines","dues").replace("e*ecu", "execu").replace("- sapna.a@|iveminl.cnm","sapna.a@livemint.com").replace("MUMBAl/","MUMBAI/").
	 replace("andablue","and a blue").replace("brassnameplate","brass nameplate").
	 replace("s*uat","squat").replace("Income-Ta*","Income-Tax").replace("1*61", "1961").
	 replace("weara","wear a").replace("by?11,4**-crore fl‘aud ","by $11,400-crore fraud ").
	 replace("mainsuspects","main suspects").replace("*u","qu");
	 	 
	 String Data_filter = Cleansing_name.replace("fl‘","fr").replace("GitanjaliGemsLtd’s","Gitanjali Gems Ltd’s ").
	 replace("1*1112*15","10 in 2015").replace("E*e","Exe").replace("severalGitanj all","several Gitanjali").
	 replace("as pera","as per a").replace("?8* ","$80").replace("2*","20").
	 replace("ofii-","offi-").replace(" notto "," not to ").replace("(Gitanj ali Gems)","(Gitanjali Gems)").replace("ofdoing","of doing").
	 replace("e*p","exp").replace("afterseven","after seven").
	 replace("businessi because","business- because").replace("16*1akh","$60 lakh").
	 replace("1** ","100 ").replace("E*change","Exchange").
	 replace("butthesehavefallenondeafears","but these have fallen on deafears").replace("Therepercussionsthoughare","The repercussions though are").
	 replace(":USINESS","BUSINESS").replace(":IAMOND","DIAMOND").replace(":_IJING","BEIJING\nSutirotho Patranobis\nfeedback@livemint.com").
	 replace("BEUINGzlt","BEIJING:It").replace("Rsll,4**»","Rs11,400-").replace("Beijing,\" saf","Beijing,\" said").replace("most c","most of");
	 
	 String Filter_UN = Data_filter.replace("been shu","been shut").replace("located ii","located in").replace("district, the first on the Ch","district, the first on the Chi").
	 replace("May 20]","May 2017").replace("ofhis","of his");
	 String End_filter = Filter_UN.substring(0, Filter_UN.length()-149)+"";
	//System.out.println(End_filter);
	 
	   
	return End_filter;
	   
   }
	
}
