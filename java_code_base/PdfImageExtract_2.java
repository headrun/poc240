import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.asprise.ocr.Ocr;


public class PdfImageExtract_2{

	public static void main(String... args) throws IOException {
		BufferedWriter writer = null;
		try
		{

        File input = new File("/home/headrun/workspace/PDFCrawling/src/Print 2.jpg");
        BufferedImage image = ImageIO.read(input);
        
        String Abs_name =null;

        BufferedImage resized = resize(image, 2400, 2400);

        File output = new File("/home/headrun/workspace/PDFCrawling/src/Print_2-2400x2400.jpg");
        ImageIO.write(resized, "jpg", output);
        Ocr.setUp(); // one time setup
    	Ocr ocr = new Ocr(); // create a new OCR engine
    	ocr.startEngine("eng", Ocr.SPEED_FASTEST); // English
      	String name = ocr.recognize("/home/headrun/workspace/PDFCrawling/src/Print_2-2400x2400.jpg",-1,0,0,0,0,Ocr.RECOGNIZE_TYPE_ALL,Ocr.OUTPUT_FORMAT_PLAINTEXT);
    	//System.out.println("Result: " + name);
    	//Cleans(name);
      	File out_txt = new File("Print_2.txt");
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
	  
	 String Cleansing_name = name.replace("tides?","rides?").replace("EVER SINCETHE", "EVER SINCE THE").replace("(PN B)","(PNB)")
	 .replace("brokewe","broke, we").replace(" toa ", " to a ").replace("ShetIy","Shetty").replace("othercrooked","other crooked").
	 replace("allegedlyoperatingby","allegedly operating by").replace("todefraud thebank ofRs 11,3*7","to defraud the bank of Rs 11,397").
	 replace("{LoU}","(LoU)").replace("fin'ns","firms").replace("lndianbank", "Indian bank").
	 replace("prauices","practices").replace("notall'","not all").replace("theseloU","these LoU").replace("tracIG","tracks");
	 String Data_filter = Cleansing_name.replace("globeand","globe and").replace(" of fi"," offi").replace("honouringAnd","honouring. And").
	 replace("thesedoc","these doc").replace("Si*th neitherPNBnor thecor","Sixth neither PNB nor the cor").replace("adicey","a dicey").
	 replace("earIier","earlier").replace(",]atin",". Jatin").replace("St Kids","St Kitts").replace("ofall","of all").replace("lightaf","light af").
	 replace("Sheuy's","Shetty's").replace("claimingthatitwas neverneeded","claiming that it was never needed").replace("thisscandal","this scandal").replace("*uestions","questions").
	 replace("agood","a good").replace("fectl y","fectly").replace("[PNB, Brady House}","(PNB, Brady House)").replace("behalfofacus","behalf of a cus").
	 replace("honoura","honour a").replace("(a Modi finn)","(a Modi firm)").replace("corresponding","\n").replace("PNB.","PNB,");
	 
	 String Filter_UN = Data_filter.replace("Hotts e's SXVIFT","House's SWIFT").replace("\\V","W").replace("client Subject","client.Subject").
	 replace("overs eas","overseas").replace("3* toQD "," 30 to 90 ").replace("Forany boU","For any LoU").
	 replace("bank eams","bank earns").replace("LoUsentto anoverseasbank","LoU sent to an overseas bank").replace(" re*uires "," requires ").replace("stch ons forfinancial","structions for financial")
	 .replace("throughSWIFf","through SWIFT").replace("clearsetofcodes","clear setof codes").replace("threedilferent","three different").replace("Amaker","A maker").
	 replace("theseSWIFTmessages","these SWIFT messages").replace(" Io-"," lo-").replace("volvingincreasingly Iargera mounts were e*-","volving increasingly larger amounts were ex-").
	 replace("thefirst","the first").replace("impl ement full-ll edged","implement full-fledged").replace("trans actions","trans actions").replace("automaticallyaclmowledged","automatically acknowledged").
	 replace("senda ","send a ").replace("sageconfin-ning theamountinthe","sage confirming the amount in the").replace("nos nos","nostros").replace("olfered","offered").
	 replace("cred it","credit").replace("supplierabroad","supplier abroad").replace("acco untand","account, and");
	 
	 String More_filter = Filter_UN.replace("LoU. ","LoU, ").replace("e*panding","expanding").replace("mo re gems","more gems").replace("Shet1y","Shetty").
	 replace("evenlarger loUs","even larger LoUs").replace("circu m","circum").replace("ne*t","next").replace("Iargerand Ia rgerPNB","larger and larger PNB").replace("Ne*t","Next").
	 replace("toUs","LoUs").replace("creditwithoutany","credit without any").replace("Whatabout “abundant cautio n\".","What about “abundant caution ,").
	 replace("Rs 11,4**","Rs 11,400").replace("concurrentaudits","concurrent audits").replace("auditcommitteesleepingon","audit committee sleeping on").
	 replace("s*uared","squared").replace("theseaccounts","these accounts").replace("givenits pastrecord","given its past record").replace("e*pect","expect").
	 replace("Costs-urn tfourrderond","Goswami,founder and").replace("ofCERG","of CERG").replace("Advisory Private Limited. is authorof","Advisory Private Limited, is author of").
	 replace("Indium","India:A").replace("comparative perspective","comparative perspective‘");
	 String End_filter = More_filter.substring(0, More_filter.length()-78)+"";
	//System.out.println(End_filter);
	 
	   
	return End_filter;
	   
   }
	
}
