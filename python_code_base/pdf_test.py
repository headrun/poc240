import os
import re
import csv
import datetime
import MySQLdb
import PyPDF2

def xcode(text, encoding='utf8', mode='strict'):
    return text.encode(encoding, mode) if isinstance(text, unicode) else text

def compact(text, level=0):
    if text is None: return ''

    if level == 0:
        text = text.replace("\n", " ")
        text = text.replace("\r", " ")

    compacted = re.sub("\s\s(?m)", " ", text)
    if compacted != text:
        compacted = compact(compacted, level+1)

    return compacted.strip()

def clean(text):
    if not text: return text

    value = text
    value = re.sub("&amp;", "&", value)
    value = re.sub("&lt;", "<", value)
    value = re.sub("&gt;", ">", value)
    value = re.sub("&quot;", '"', value)
    value = re.sub("&apos;", "'", value)

    return value

def normalize(text):
    return clean(compact(xcode(text)))

class PdfToTextConverter():

    def __init__(self):
        self.current = os.getcwd()
        self.new_pdf = os.path.join(self.current, "new_pdf")
        self.img_file = os.path.join(self.current, "IMAGE_FILES")
        self.path = os.path.join(self.current, "PDF_FILES")
	self.txt_files = os.path.join(self.current, "TEXT_FILES")
        self.date = datetime.date.today()
	self.filename = "Img_pdf_data.csv"
        self.csv_file = self.is_path_file_name(self.filename)
        self.fields = ["Title", "Date", "Description", "Filename"]
        self.csv_file.writerow(self.fields)


    def is_path_file_name(self, excel_file_name):
	if os.path.isfile(excel_file_name):
		os.system('rm %s' % excel_file_name)
	oupf = open(excel_file_name, 'ab+')
	todays_excel_file = csv.writer(oupf)
	return todays_excel_file


    def get_pdf_files(self):
        PDF = os.listdir(self.path)
        return PDF

    def main(self):
	""" need to fetch data from images which are present in database before doing this, this can be done by running java"""
	""" specifying file names and static values as per the patterns observed """
        pdf_list = self.get_pdf_files()
	image_dict = {"Print_1.pdf": "Print_1.txt" ,  "Print_2.jpg":"Print_2.txt" , "Print_3.jpg":"Print_3.txt", "Print_4.pdf": "Print_4.txt", "Print_5.pdf": "Print_5.txt", "Print_6.pdf": "Print_6.txt"}
	created_date_dict = {"Print_1.pdf":"", "Print_4.pdf": "", "Print_5.pdf": "", "Print_6.pdf": "", "Print_3.jpg":"2018-02-23", "Print_2.jpg":"2018-02-23"}
        if pdf_list:
		for pdl in pdf_list:		
			if pdl.endswith('.pdf'):
				pdfFileObj = open("%s%s%s" % (self.path , '/' , pdl),'rb') 
				pdfReader = PyPDF2.PdfFileReader(pdfFileObj, strict=False)
				doc_info = pdfReader.documentInfo
				created_date = doc_info.get('/CreationDate', '')
				for i in range(0, pdfReader.getNumPages()):
				    page = pdfReader.getPage(i)
				    xObject = page['/Resources']['/XObject'].getObject()
				    for obj in xObject:
					if xObject[obj]['/Subtype'] == '/Image':
						if xObject[obj]['/Filter'] == '/DCTDecode':
							img = open("%s%s%s" % (self.img_file, '/',str(pdl.replace('.pdf', '')))+ ".jpg", "wb")
							data = xObject[obj]._data
							img.write(data)
							img.close()
				try:
					f_created_date = str(datetime.datetime.strptime(''.join(re.findall("\d{8}", created_date)), "%Y%m%d").date())
					created_date_dict[pdl] = f_created_date
				except:
					f_created_date = ''
	for images, textfile in image_dict.iteritems():
		txt_files = open("%s%s%s" % (self.txt_files , '/' , textfile ),'rb')
		csvs = txt_files.readlines()
		title = ' '.join(csvs[0:2]).replace('\n', '')
		if images == 'Print_6.pdf' or images == "Print_2.jpg":
			title = ' '.join(csvs[0:1]).replace('\n', '')
		whole_desc = ''.join(csvs[5:-1])
		if ": " in whole_desc:
			whole_desc = ''.join(''.join(csvs).split(': ')[1:])
		if 'Print_3.jpg' in images:
			whole_desc = ''.join(csvs[4:-1])
		if 'Print_6.pdf' in images:
			whole_desc = "%s%s" % ('CBI: ', whole_desc)
		if 'Print_2.jpg' in images:
			whole_desc = ''.join(csvs[4:-4])
		if 'Print_4.pdf' in images:
			whole_desc = ''.join(csvs[6:-3])
		whole_desc = whole_desc.replace('\n', ' ').strip()
		values = [title, created_date_dict[images], whole_desc, images]
		self.csv_file.writerow(values)
				
					
if __name__ == '__main__':
    PdfToTextConverter().main()
